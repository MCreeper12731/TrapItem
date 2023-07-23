package com.github.mcreeper12731.trapitem.commands;

import com.github.mcreeper12731.trapitem.models.TrapCustomItem;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetItemCommand implements CommandExecutor {

    private final NamespacedKey key;

    //Command to get the item
    public GetItemCommand(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        player.getInventory().addItem(new TrapCustomItem(key).getItem());
        player.sendMessage("Trap item received!");

        return true;
    }
}
