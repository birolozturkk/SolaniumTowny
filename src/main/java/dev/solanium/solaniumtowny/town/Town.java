package dev.solanium.solaniumtowny.town;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.solanium.solaniumtowny.DatabaseObject;
import dev.solanium.solaniumtowny.SolaniumTowny;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "towns")
public class Town extends DatabaseObject {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(columnName = "created_by")
    private UUID createdBy;

    @DatabaseField(columnName = "created_at", dataType = DataType.TIME_STAMP)
    private Timestamp createdAt;

    @DatabaseField
    private UUID owner;

    public Town(int id) {
        this.id = id;
    }

    public Town(String name, UUID createdBy) {
        this.name = name;
        this.owner = createdBy;
        this.createdBy = createdBy;
        this.createdAt = Timestamp.from(Instant.now());
    }

    public List<TownRegion> getTownRegions() {
        return SolaniumTowny.getInstance().getTownRegionRepository().getTownRegions(id);
    }
}
