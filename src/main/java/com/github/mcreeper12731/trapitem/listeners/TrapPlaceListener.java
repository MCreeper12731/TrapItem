package com.github.mcreeper12731.trapitem.listeners;

import com.github.mcreeper12731.trapitem.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class TrapPlaceListener implements Listener {

    private final NamespacedKey key;

    public TrapPlaceListener(NamespacedKey key) {
        this.key = key;
    }

    @EventHandler
    public void onTrapPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (!isTrapItem(item)) return;
        event.setCancelled(true);
        if (player.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);

        //Summon an invisible armor stand to mark trap's location
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(
                event.getBlockPlaced().getLocation().clone().add(0.5, -2, 0.5),
                EntityType.ARMOR_STAND
        );
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setArms(true);
        armorStand.setCustomName(Constants.ARMOR_STAND_NAME);

        player.sendMessage(ChatColor.GREEN + "Trap armed!");

    }

    private boolean isTrapItem(ItemStack item) {

        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING) &&
                item.getItemMeta().hasEnchant(Enchantment.LUCK);

    }

}
