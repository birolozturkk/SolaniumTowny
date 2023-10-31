package dev.solanium.solaniumtowny.database.impl;

import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.config.Configuration;
import dev.solanium.solaniumtowny.database.DatabaseManager;

public class MysqlDatabaseManager extends DatabaseManager {

    public MysqlDatabaseManager(SolaniumTowny plugin, Configuration sqlConfig) {
        super(plugin, sqlConfig);
    }

    @Override
    protected String getDatabaseURL() {
        return "jdbc:mysql://" + sqlConfig.getString("host") + ":" + sqlConfig.getString("port") + "/" +
                sqlConfig.getString("database") + "?useSSL=" + sqlConfig.getBoolean("useSSL");
    }
}
