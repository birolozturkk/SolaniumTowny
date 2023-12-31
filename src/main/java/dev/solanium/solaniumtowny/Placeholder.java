package dev.solanium.solaniumtowny;

import dev.solanium.solaniumtowny.town.Town;
import dev.solanium.solaniumtowny.town.TownRegion;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Placeholder {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SolaniumTowny.getInstance().getConfig().getString("date-pattern"));

    private final String key;
    private final String value;

    public static PlaceholderBuilder builder() {
        return new PlaceholderBuilder();
    }

    public static class PlaceholderBuilder {

        private final List<Placeholder> placeholders = new ArrayList<>();

        private PlaceholderBuilder() {
            placeholders.add(new Placeholder("%prefix%", SolaniumTowny.getInstance().getConfig().getString("prefix")));
        }

        public PlaceholderBuilder apply(String key, String value) {
            placeholders.add(new Placeholder(key, value));
            return this;
        }

        public PlaceholderBuilder applyForTown(Town town) {
            placeholders.add(new Placeholder("%town_owner%", Bukkit.getOfflinePlayer(town.getOwner()).getName()));
            placeholders.add(new Placeholder("%town_created_by%", Bukkit.getOfflinePlayer(town.getCreatedBy()).getName()));
            placeholders.add(new Placeholder("%town_created_at%", formatter.format(town.getCreatedAt().toLocalDateTime())));
            placeholders.add(new Placeholder("%town_name%", town.getName()));
            return this;
        }

        public PlaceholderBuilder applyForTownRegion(TownRegion townRegion) {
            townRegion.getTown().ifPresent(this::applyForTown);
            placeholders.add(new Placeholder("%town_region_x%", String.valueOf(townRegion.getX())));
            placeholders.add(new Placeholder("%town_region_z%", String.valueOf(townRegion.getZ())));
            placeholders.add(new Placeholder("%town_region_claimed_by%", Bukkit.getOfflinePlayer(townRegion.getClaimedBy()).getName()));
            placeholders.add(new Placeholder("%town_region_claimed_at%", formatter.format(townRegion.getClaimedAt().toLocalDateTime())));
            return this;
        }

        public PlaceholderBuilder applyForChunk(Chunk chunk) {
            placeholders.add(new Placeholder("%chunk_x%", String.valueOf(chunk.getX())));
            placeholders.add(new Placeholder("%chunk_z%", String.valueOf(chunk.getZ())));
            Optional<TownRegion> townRegionOptional = SolaniumTowny.getInstance().getTownManager().getTownRegionByChunk(chunk);
            townRegionOptional.ifPresent(this::applyForTownRegion);
            return this;
        }

        public List<Placeholder> build() {
            return placeholders;
        }
    }
}
