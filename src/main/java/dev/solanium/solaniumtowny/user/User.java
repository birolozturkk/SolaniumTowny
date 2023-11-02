package dev.solanium.solaniumtowny.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.solanium.solaniumtowny.DatabaseObject;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.town.Town;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User extends DatabaseObject {

    @DatabaseField(id = true)
    private UUID uuid;

    @DatabaseField(columnName = "town_id")
    private int townId;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public Optional<Town> getTown() {
        return SolaniumTowny.getInstance().getTownManager().getTownById(townId);
    }

    public void setTown(Town town) {
        this.townId = town.getId();
        setChanged(true);
    }
}