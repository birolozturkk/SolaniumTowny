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
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        if(townOptional.isEmpty()) return;
        Town town = townOptional.get();

        Optional<TownRegion> townRegionOptional = town.getBaseRegion();
        if(townRegionOptional.isEmpty()) return;
        TownRegion townRegion = townRegionOptional.get();

        Optional<Chunk> chunkOptional = townRegion.getChunk();
        if(chunkOptional.isEmpty()) return;
        Chunk baseChunk = chunkOptional.get();

        List<Chunk> emptyRegionsItems = getEmptyRegions(baseChunk);
        int centerRegionSlot = plugin.getTownMapGUI().getInt("center-region-slot");

        System.out.println(emptyRegionsItems.size());

        for (Chunk emptyChunk : emptyRegionsItems) {
            int x = emptyChunk.getX() - baseChunk.getX();
            int y = emptyChunk.getZ() - baseChunk.getZ();
            GuiItem item = ItemUtils.makeItem(plugin.getTownMapGUI().getConfigurationSection("items.empty-region"),
                    Placeholder.builder().applyForChunk(emptyChunk).build()).asGuiItem();
            gui.setItem(centerRegionSlot + x + 9 * y, item);
        }


        gui.open(player);
    }

    private static List<Chunk> getEmptyRegions(Chunk baseChunk) {
        int radiusX = plugin.getTownMapGUI().getInt("radius-X");
        int radiusY = plugin.getTownMapGUI().getInt("radius-Y");
        return plugin.getTownManager().getChunksAround(baseChunk, radiusX, radiusY).stream()
                .filter(chunk -> plugin.getTownManager().getTownRegionByChunk(chunk).isEmpty())
                .toList();
    }
}
