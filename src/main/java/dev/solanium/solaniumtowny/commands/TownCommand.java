package dev.solanium.solaniumtowny.commands;

import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;

@Command(value = "town")
public class TownCommand extends BaseCommand {

    private final SolaniumTowny plugin;

    public TownCommand(SolaniumTowny plugin) {
        this.plugin = plugin;
    }

}
