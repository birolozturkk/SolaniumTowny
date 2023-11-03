package dev.solanium.solaniumtowny.commands;

import dev.solanium.solaniumtowny.Placeholder;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.gui.TownMapGUI;
import dev.solanium.solaniumtowny.town.Town;
import dev.solanium.solaniumtowny.town.TownRegion;
import dev.solanium.solaniumtowny.user.User;
import dev.solanium.solaniumtowny.utils.StringUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Command(value = "town")
public class TownCommand extends BaseCommand {

    private final SolaniumTowny plugin;

    public TownCommand(SolaniumTowny plugin) {
        this.plugin = plugin;
    }

    @SubCommand(value = "create")
    @Permission(value = "solaniumtowny.town.create")
    public void createTown(Player player, String townName) {
        if (hasAnyTown(player)) {
            StringUtils.sendMessage(player, plugin.getLang().getString("you-already-res"));
            return;
        }

        if (!isValidRegion(player)) return;

        plugin.getTownManager().createTown(player, townName)
                .thenAccept(town -> {
                    Bukkit.getOnlinePlayers()
                            .forEach(loopPlayer ->
                                    StringUtils.sendMessage(loopPlayer, plugin.getLang().getString("new-town"),
                                            Placeholder.builder().applyForTown(town)));
                });
    }

    @SubCommand(value = "claim")
    @Permission(value = "solaniumtowny.town.claim")
    public void claimRegion(Player player) {
        if (!hasAnyTown(player)) {
            StringUtils.sendMessage(player, plugin.getLang().getString("has-no-town"));
            return;
        }

        if (!isValidRegion(player)) return;

        User user = plugin.getUserManager().getUser(player);
        plugin.getTownManager().claimTownRegion(player, user.getTown().get())
                .thenAccept(townRegion -> StringUtils.sendMessage(player, plugin.getLang().getString("claimed")));
    }

    @SubCommand(value = "map")
    @Permission(value = "solaniumtowny.town.map")
    public void open(Player player) {
        TownMapGUI.open(player);
    }

    private boolean isValidRegion(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        Optional<TownRegion> townRegionOptional = plugin.getTownManager().getTownRegionByChunk(chunk);
        if (townRegionOptional.isPresent()) {
            StringUtils.sendMessage(player, plugin.getLang().getString("already-claimed"),
                    Placeholder.builder().applyForTownRegion(townRegionOptional.get()));
            return false;
        }

        User user = plugin.getUserManager().getUser(player);
        int radius = plugin.getConfig().getInt("max-claim-radius-value");
        Collection<TownRegion> townRegions = plugin.getTownManager().getTownRegionsAround(chunk, radius).stream()
                    .map(TownRegion::getTown)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(town -> !town.equals(user.getTown().orElse(null)))
                    .map(Town::getTownRegions)
                    .flatMap(List::stream)
                    .toList();
        if (!townRegions.isEmpty()) {
            StringUtils.sendMessage(player, plugin.getLang().getString("too-close"));
            return false;
        }
        return true;
    }

    private boolean hasAnyTown(Player player) {
        User user = plugin.getUserManager().getUser(player);
        return user.getTown().isPresent();
    }

}
