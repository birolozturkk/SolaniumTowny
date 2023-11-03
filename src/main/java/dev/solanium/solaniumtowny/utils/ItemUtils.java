package dev.solanium.solaniumtowny.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import dev.solanium.solaniumtowny.Placeholder;
import dev.solanium.solaniumtowny.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ItemUtils {

    public static ItemBuilder makeItem(ConfigurationSection itemSection, List<Placeholder> placeholders) {
        if (itemSection == null) return ItemBuilder.from(Material.AIR);

        Component displayName = StringUtils.format(itemSection.getString("display-name"), placeholders);
        List<Component> lore = StringUtils.format(itemSection.getStringList("lore"), placeholders);

        ItemBuilder itemBuilder = ItemBuilder.from(XMaterial.matchXMaterial(itemSection.getString("material")).get().parseMaterial())
                .name(displayName)
                .lore(lore)
                .glow(itemSection.getBoolean("glowing"))
                .amount(itemSection.getInt("amount"));

        if (itemSection.getString("material").equals("PLAYER_HEAD")) {
            if (itemSection.getString("head-data") != null) {
                itemBuilder = setHeadData(itemBuilder, itemSection.getString("head-data"));
            }
            else if (itemSection.getString("head-owner") != null) {
                itemBuilder = setHeadData(itemBuilder, SkinUtils.getHeadData(SkinUtils.getUUID(StringUtils.processPlaceholders(itemSection.getString("head-owner"), placeholders))));
            }
        }
        return itemBuilder;
    }


    public static ItemBuilder setHeadData(ItemBuilder itemBuilder, String headData) {
        if (headData == null) return itemBuilder;

        ItemStack itemStack = itemBuilder.build();
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound skull = nbtItem.addCompound("SkullOwner");
        skull.setUUID("Id", UUID.randomUUID());

        NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
        texture.setString("Value", headData);
        itemStack = nbtItem.getItem();
        return ItemBuilder.from(itemStack);
    }

}
