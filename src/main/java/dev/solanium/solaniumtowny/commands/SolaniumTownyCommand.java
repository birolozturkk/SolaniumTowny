package dev.solanium.solaniumtowny.commands;

import dev.solanium.solaniumtowny.Placeholder;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.user.User;
import dev.solanium.solaniumtowny.utils.StringUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "towny")
public class SolaniumTownyCommand extends BaseCommand {

    private final SolaniumTowny plugin;

    public SolaniumTownyCommand(SolaniumTowny plugin) {
        this.plugin = plugin;
    }

    @SubCommand(value = "reload")
    @Permission(value = "solaniumtowny.towny.reload")
    public void reload(CommandSender sender) {
        plugin.reload();
    }
}
