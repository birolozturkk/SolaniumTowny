package dev.solanium.solaniumtowny.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

public class ItemUtils {

/*    public static ItemBuilder makeItem(ItemConfig itemConfig, List<Placeholder> placeholders) {

        if (itemConfig == null) return ItemBuilder.from(Material.AIR);

        ItemBuilder itemBuilder = ItemBuilder.from(itemConfig.material.parseItem())
                .name(StringUtils.format(itemConfig.displayName, placeholders))
                .lore(StringUtils.format(itemConfig.lore, placeholders))
                .amount(itemConfig.amount)
                .glow(itemConfig.glowing);

        if(itemConfig.material == XMaterial.PLAYER_HEAD) {
            SkullBuilder skullBuilder = ItemBuilder.skull(itemBuilder.build());
            if (itemConfig.headData != null) {
                skullBuilder.texture(itemConfig.headData);
            } else if (itemConfig.headOwner != null) {
                skullBuilder.owner(Bukkit.getOfflinePlayer(StringUtils.processPlaceholders(itemConfig.headOwner, placeholders)));
            }
            itemBuilder = ItemBuilder.from(skullBuilder.build());
        }
        return itemBuilder;
    }*/



}
