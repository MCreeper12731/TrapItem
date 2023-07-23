package com.github.mcreeper12731.trapitem;

import com.github.mcreeper12731.trapitem.commands.GetItemCommand;
import com.github.mcreeper12731.trapitem.listeners.PlayerMoveListener;
import com.github.mcreeper12731.trapitem.listeners.TrapPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class TrapItem extends JavaPlugin {

    private NamespacedKey key;

    @Override
    public void onEnable() {

        key = new NamespacedKey(this, "trap");

        registerCommands();
        registerListeners(
                new PlayerMoveListener(this),
                new TrapPlaceListener(key)
        );

    }

    private void registerListeners(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {

        getCommand("gettrapitem").setExecutor(new GetItemCommand(key));

    }

}
