package dev.solanium.solaniumtowny.managers;

import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.repository.impl.TownRegionRepository;
import dev.solanium.solaniumtowny.repository.impl.TownRepository;
import dev.solanium.solaniumtowny.town.Town;
import dev.solanium.solaniumtowny.town.TownRegion;
import dev.solanium.solaniumtowny.user.User;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TownManager {

    private final SolaniumTowny plugin;
    private final TownRepository townRepository;
    private final TownRegionRepository townRegionRepository;

    public TownManager(SolaniumTowny plugin, TownRepository townRepository, TownRegionRepository townRegionRepository) {
        this.plugin = plugin;
        this.townRepository = townRepository;
        this.townRegionRepository = townRegionRepository;
    }

    public CompletableFuture<Town> createTown(Player owner, String name) {
        return CompletableFuture.supplyAsync(() -> {
            User user = plugin.getUserManager().getUser(owner);

            Town town = new Town(name, owner.getUniqueId());
            town = townRepository.registerTown(town).join();

            TownRegion townRegion = claimTownRegion(owner, town).join();
            town.setBaseRegionId(townRegion.getId());

            user.setTown(town);
            plugin.getUserRepository().save();

            return town;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<TownRegion> claimTownRegion(Player player, Town town) {
        return CompletableFuture.supplyAsync(() -> {
            Chunk chunk = player.getLocation().getChunk();
            TownRegion townRegion = new TownRegion(town.getId(),
                    chunk.getWorld().getName(),
                    chunk.getX(),
                    chunk.getZ(),
                    player.getUniqueId());
            return townRegionRepository.registerTownRegion(townRegion).join();
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public Optional<TownRegion> getTownRegionByChunk(Chunk chunk) {
        return townRegionRepository.getEntries().stream()
                .filter(townRegion -> townRegion.isInRegion(chunk))
                .findFirst();
    }

    public Optional<Town> getTownById(int id) {
        return townRepository.findTownById(id);
    }

    public Collection<TownRegion> getTownRegionsAround(Chunk chunk, int radius) {
        Collection<Chunk> chunks = getChunksAround(chunk, radius, radius, true);
        return chunks.stream()
                .map(this::getTownRegionByChunk)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public boolean availableForClaiming(Chunk chunk, Town town) {
        return !plugin.getTownManager().getChunksAround(chunk, 1, 1, false).stream()
                .map(plugin.getTownManager()::getTownRegionByChunk)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(townRegion -> townRegion.getTownId() == town.getId())
                .toList().isEmpty();
    }

    public Collection<Chunk> getChunksAround(Chunk baseChunk, int offX, int offZ, boolean includeCross) {
        Collection<Chunk> chunksAround = new HashSet<>();
        for (int x = -offX; x <= offX; x++) {
            for (int z = -offZ; z <= offZ; z++) {
                if(x == 0 && z == 0) continue;
                if(!includeCross && x != 0 && z != 0) continue;
                Chunk chunk = baseChunk.getWorld().getChunkAt(baseChunk.getX() + x, baseChunk.getZ() + z);
                chunksAround.add(chunk);
            }
        }
        return chunksAround;
    }
}
