package dev.solanium.solaniumtowny.town;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.solanium.solaniumtowny.DatabaseObject;
import dev.solanium.solaniumtowny.SolaniumTowny;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "town_regions")
public class TownRegion extends DatabaseObject {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "town_id")
    private int townId;

    @DatabaseField(columnName = "world_name")
    private String worldName;

    @DatabaseField
    private int x;

    @DatabaseField
    private int z;

    @DatabaseField(columnName = "claimed_by")
    private UUID claimedBy;

    @DatabaseField(columnName = "claimed_at", dataType = DataType.TIME_STAMP)
    private Timestamp claimedAt;

    public TownRegion(int id, int townId) {
        this.id = id;
        this.townId = townId;
    }

    public TownRegion(int townId, String worldName, int x, int z, UUID claimedBy) {
        this.townId = townId;
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        this.claimedBy = claimedBy;
        this.claimedAt = Timestamp.from(Instant.now());
    }

    public boolean isInRegion(Location location) {
        return getChunk().filter(chunk -> location.getChunk().equals(chunk)).isPresent();
    }

    public boolean isInRegion(Chunk chunk) {
        return getChunk().filter(chunk::equals).isPresent();
    }

    public Optional<Town> getTown() {
        return SolaniumTowny.getInstance().getTownManager().getTownById(townId);
    }

    public Optional<Chunk> getChunk() {
        World world = Bukkit.getWorld(worldName);
        if(world == null) return Optional.empty();
        return Optional.of(world.getChunkAt(x, z));
    }
}
