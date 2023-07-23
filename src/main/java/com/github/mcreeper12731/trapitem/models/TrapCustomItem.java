package com.github.mcreeper12731.trapitem.models;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class TrapCustomItem {

    private final NamespacedKey key;
    @Getter
    private final ItemStack item;

    public TrapCustomItem(NamespacedKey key) {
        this.key = key;
        this.item = generateItem();
    }

    private ItemStack generateItem() {

        ItemStack item = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Trap Setter");
        meta.addEnchant(Enchantment.LUCK, 0, true);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(key, PersistentDataType.STRING, "trap-item");
        item.setItemMeta(meta);

        return item;

    }
}
