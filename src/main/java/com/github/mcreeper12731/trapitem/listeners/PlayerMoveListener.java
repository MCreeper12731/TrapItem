package com.github.mcreeper12731.trapitem.listeners;

import com.github.mcreeper12731.trapitem.TrapItem;
import com.github.mcreeper12731.trapitem.util.Constants;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerMoveListener implements Listener {

    private final TrapItem plugin;
    private final ParticleCooldown particleCooldown;

    public PlayerMoveListener(TrapItem plugin) {
        this.plugin = plugin;
        particleCooldown = new ParticleCooldown();
        particleCooldown.start();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        //Check if player is in trap activation range
        for (Entity entity : player.getNearbyEntities(0.5, 5, 0.5)) {
            if (!(entity instanceof ArmorStand armorStand)) continue;
            if (!isArmorStandValid(armorStand)) continue;
            armorStand.remove();

            //Send block updates to client in order to not mess with actual terrain
            List<Material> replaceWith = new ArrayList<>();
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    Location tempLocation = armorStand.getLocation().clone().add(x, 1, z);
                    player.sendBlockChange(tempLocation, Bukkit.createBlockData(Material.PACKED_ICE));
                    replaceWith.add(tempLocation.getBlock().getType());
                }
            }

            //Send block updates again after 3 seconds to restore client side view
            new RestoreTerrain(player, armorStand.getLocation().add(0, 1, 0), replaceWith).start();

            //Player effects
            player.sendMessage(ChatColor.YELLOW + "You stepped on a trap!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);

        }

        //Check if player is in particle display range
        for (Entity entity : player.getNearbyEntities(10, 14, 10)) {
            if (!(entity instanceof ArmorStand armorStand)) continue;
            if (!isArmorStandValid(armorStand)) continue;
            if (particleCooldown.cantSummonParticles.contains(armorStand.getUniqueId())) continue;

            //Spawn a simple redstone particle
            player.spawnParticle(Particle.REDSTONE, armorStand.getLocation().add(0, 2.5, 0), 5,
                    new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0F)
            );

            //Add armor stand to a set to prevent any more particles at that location
            particleCooldown.cantSummonParticles.add(armorStand.getUniqueId());
        }

    }

    private boolean isArmorStandValid(ArmorStand armorStand) {
        return armorStand.isInvisible() &&
                !armorStand.hasGravity() &&
                armorStand.hasArms() &&
                armorStand.getCustomName() != null &&
                armorStand.getCustomName().equals(Constants.ARMOR_STAND_NAME);
    }

    private class ParticleCooldown extends BukkitRunnable {

        private final Set<UUID> cantSummonParticles = new HashSet<>();
        private int cooldownRemaining = 0;

        @Override
        public void run() {
            if (cooldownRemaining <= 0) {
                //After 5 seconds clear the set to allow new particles at existing locations
                cantSummonParticles.clear();
                cooldownRemaining = 5;
            }

            cooldownRemaining--;
        }

        public void start() {
            this.runTaskTimer(plugin, 0L, 20L);
        }
    }

    private class RestoreTerrain extends BukkitRunnable {

        private final Player player;
        private final Location center;
        private final List<Material> replaceWith;

        public RestoreTerrain(Player player, Location center, List<Material> replaceWith) {
            this.player = player;
            this.center = center;
            this.replaceWith = replaceWith;
        }

        @Override
        public void run() {

            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    player.sendBlockChange(center.clone().add(x, 0, z), Bukkit.createBlockData(replaceWith.remove(0)));
                }
            }

        }

        public void start() {
            this.runTaskLater(plugin, 60L);
        }
    }

}
