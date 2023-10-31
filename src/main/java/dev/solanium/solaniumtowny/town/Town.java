package dev.solanium.solaniumtowny.town;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.solanium.solaniumtowny.DatabaseObject;
import lombok.Getter;

import java.util.UUID;

@Getter
@DatabaseTable(tableName = "towns")
public class Town extends DatabaseObject {

    @DatabaseField(generatedId = true)
    private final int id;

    private UUID createdBy;
    private UUID owner;

    public Town(int id) {
        this.id = id;
    }
}
