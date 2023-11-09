package dev.solanium.solaniumtowny.gui;

import dev.solanium.solaniumtowny.Placeholder;
import dev.solanium.solaniumtowny.SolaniumTowny;
import dev.solanium.solaniumtowny.XMaterial;
import dev.solanium.solaniumtowny.town.Town;
import dev.solanium.solaniumtowny.town.TownRegion;
import dev.solanium.solaniumtowny.user.User;
import dev.solanium.solaniumtowny.utils.ItemUtils;
import dev.solanium.solaniumtowny.utils.StringUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TownMapGUI {

    private static final SolaniumTowny plugin = SolaniumTowny.getInstance();

    public static void open(Player player) {
        Gui gui = Gui.gui().title(StringUtils.format(plugin.getTownMapGUI().getString("title"),
                        Collections.emptyList()))
                .rows(plugin.getTownMapGUI().getInt("rows"))
                .disableAllInteractions()
                .create();

        List<Integer> fillerSlots = plugin.getTownMapGUI().getIntegerList("filler-slots");
        for (Integer slot : fillerSlots) {
            gui.setItem(slot, ItemBuilder.from(XMaterial.matchXMaterial(plugin.getTownMapGUI().getString("filler-material")).get().parseMaterial())
                    .name(Component.space()).asGuiItem());
        }

        User user = plugin.getUserManager().getUser(player);

        Optional<Town> townOptional = user.getTown();
        if (townOptional.isEmpty()) return;
        Town town = townOptional.get();

        Optional<TownRegion> baseRegionOptional = town.getBaseRegion();
        if (baseRegionOptional.isEmpty()) return;
        TownRegion baseRegion = baseRegionOptional.get();

        Optional<Chunk> chunkOptional = baseRegion.getChunk();
        if (chunkOptional.isEmpty()) return;
        Chunk baseChunk = chunkOptional.get();

        int radiusX = plugin.getTownMapGUI().getInt("radius-X");
        int radiusY = plugin.getTownMapGUI().getInt("radius-Y");

        List<Chunk> allChunksAround = plugin.getTownManager().getChunksAround(baseChunk, radiusX, radiusY, true).stream().toList();
        for (Chunk chunk : allChunksAround) {
            Optional<TownRegion> townRegionOptional = plugin.getTownManager().getTownRegionByChunk(chunk);
            if (townRegionOptional.isEmpty()) {
                if(plugin.getTownManager().availableForClaiming(chunk, town)) {
                    setRegionItem(gui, chunk, baseChunk, "items.available-claim-region");
                }else {
                    setRegionItem(gui, chunk, baseChunk, "items.empty-region");
                }
            } else {
                TownRegion townRegion = townRegionOptional.get();
                if (townRegion.getTownId() == user.getTownId() &&
                        town.getBaseRegionId() != townRegion.getId()) {
                    setRegionItem(gui, chunk, baseChunk, "items.safe-region");
                } else {
                    setRegionItem(gui, chunk, baseChunk, "items.enemy-region");
                }
            }
        }
        setRegionItem(gui, baseChunk, baseChunk, "items.base-region");

        gui.open(player);
    }

    private static void setRegionItem(Gui gui, Chunk chunk, Chunk baseChunk, String sectionPath) {
        int x = chunk.getX() - baseChunk.getX();
        int y = chunk.getZ() - baseChunk.getZ();

        int centerRegionSlot = plugin.getTownMapGUI().getInt("center-region-slot");
        GuiItem item = ItemUtils.makeItem(plugin.getTownMapGUI().getConfigurationSection(sectionPath),
                Placeholder.builder().applyForChunk(chunk).build()).asGuiItem();
        gui.setItem(centerRegionSlot + x + 9 * y, item);
    }
}
