package dev.solanium.solaniumtowny.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.logger.NullLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.config.Configuration;

import java.sql.SQLException;

public abstract class DatabaseManager {

    protected final SolaniumTowny plugin;
    protected final Configuration sqlConfig;

    protected DatabaseManager(SolaniumTowny plugin, Configuration sqlConfig) {
        this.plugin = plugin;
        this.sqlConfig = sqlConfig;
    }

    public ConnectionSource init() {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());
        try {
            String databaseURL = getDatabaseURL();
            return new JdbcConnectionSource(
                    databaseURL,
                    sqlConfig.getString("username"),
                    sqlConfig.getString("password"),
                    DatabaseTypeUtils.createDatabaseType(databaseURL));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getDatabaseURL();
}
