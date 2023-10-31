package dev.solanium.solaniumtowny.database.impl;

import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.config.Configuration;
import dev.solanium.solaniumtowny.database.DatabaseManager;

import java.io.File;

public class SQLiteDatabaseManager extends DatabaseManager {

    public SQLiteDatabaseManager(SolaniumTowny plugin, Configuration sqlConfig) {
        super(plugin, sqlConfig);
    }

    @Override
    protected String getDatabaseURL() {
        return "jdbc:sqlite:" + new File(plugin.getDataFolder(),
                sqlConfig.getString("database") + ".db");
    }
}
