package dev.solanium.solaniumtowny;

import com.j256.ormlite.support.ConnectionSource;
import dev.solanium.solaniumtowny.commands.SolaniumTownyCommand;
import dev.solanium.solaniumtowny.commands.TownCommand;
import dev.solanium.solaniumtowny.config.Configuration;
import dev.solanium.solaniumtowny.database.DatabaseManager;
import dev.solanium.solaniumtowny.database.impl.MysqlDatabaseManager;
import dev.solanium.solaniumtowny.database.impl.SQLiteDatabaseManager;
import dev.solanium.solaniumtowny.managers.TownManager;
import dev.solanium.solaniumtowny.managers.UserManager;
import dev.solanium.solaniumtowny.repository.impl.TownRegionRepository;
import dev.solanium.solaniumtowny.repository.impl.TownRepository;
import dev.solanium.solaniumtowny.repository.impl.UserRepository;
import dev.solanium.solaniumtowny.utils.StringUtils;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

@Getter
public final class SolaniumTowny extends JavaPlugin {

    private final Configuration config = new Configuration(this, "config.yml");
    private final Configuration sqlConfig = new Configuration(this, "sql.yml");
    private Configuration lang;

    private TownRepository townRepository;
    private TownRegionRepository townRegionRepository;
    private UserRepository userRepository;

    private TownManager townManager;
    private UserManager userManager;
    private DatabaseManager databaseManager;
    private BukkitCommandManager<CommandSender> commandManager;

    private BukkitAudiences adventure;

    private BukkitTask saveTask;

    @Getter
    private static SolaniumTowny instance;

    @Override
    public void onEnable() {
        instance = this;
        this.adventure = BukkitAudiences.create(this);

        config.create();
        sqlConfig.create();
        lang = new Configuration(this, "lang/" + config.getString("lang") + ".yml");
        lang.create();

        initDatabase();
        setupManagers();
        setupCommands();

        saveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::saveData, 0, 20 * 60 * 5);
    }
    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        saveTask.cancel();
        saveData();
    }

    private void saveData() {
        townRepository.save();
        townRegionRepository.save();
        userRepository.save();
    }

    private void initDatabase() {
        switch (sqlConfig.getString("driver")) {
            case "SQLITE":
                databaseManager = new SQLiteDatabaseManager(this, sqlConfig);
                break;
            case "MYSQL":
                databaseManager = new MysqlDatabaseManager(this, sqlConfig);
        }
        ConnectionSource connectionSource = databaseManager.init();
        townRepository = new TownRepository(connectionSource);
        townRegionRepository = new TownRegionRepository(connectionSource);
        userRepository = new UserRepository(connectionSource);
    }

    private void setupManagers() {
        townManager = new TownManager(this, townRepository, townRegionRepository);
        userManager = new UserManager(userRepository);
    }

    private void setupCommands() {
        this.commandManager = BukkitCommandManager.create(this);
        commandManager.registerCommand(new TownCommand(this));
        commandManager.registerCommand(new SolaniumTownyCommand(this));

        commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> adventure.sender(sender)
                .sendMessage(StringUtils.format(lang.getString("invalid-argument"), Placeholder.builder().build())));

        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> adventure.sender(sender)
                .sendMessage(StringUtils.format(lang.getString("unknown-command"), Placeholder.builder().build())));

        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> adventure.sender(sender)
                .sendMessage(StringUtils.format(lang.getString("not-enough-arguments"), Placeholder.builder().build())));

        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> adventure.sender(sender)
                .sendMessage(StringUtils.format(lang.getString("too-many-arguments"), Placeholder.builder().build())));

        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> adventure.sender(sender)
                .sendMessage(StringUtils.format(lang.getString("no-permission"), Placeholder.builder().build())));
    }

    public void reload() {
        this.config.reload();
        this.sqlConfig.reload();
        lang = new Configuration(this, "lang/" + config.getString("lang") + ".yml");
        lang.create();
    }
}
