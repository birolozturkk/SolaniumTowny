package dev.solanium.solaniumtowny.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.solanium.solaniumtowny.DatabaseObject;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.town.Town;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@DatabaseTable(tableName = "users")
public class User extends DatabaseObject {

    @Getter
    @DatabaseField(generatedId = true)
    private UUID uuid;

    @DatabaseField(columnName = "town_id")
    private int townId;

    public Optional<Town> getTown() {
        return SolaniumTowny.getInstance().getTownManager().getTownById(townId);
    }
}