package me.nuttist.playerSettingsMenu.commands;

import me.nuttist.playerSettingsMenu.core.PlayerSettingsMenu;
import net.kyori.adventure.text.Component;
import org.apache.maven.artifact.repository.metadata.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class settings implements CommandExecutor {
    JavaPlugin psmPlugin;

    public settings(JavaPlugin plugin) {
        psmPlugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings){


        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players have access to settings menu");
            return true;
        }
        Player player = (Player)commandSender;


        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey pvpkey = new NamespacedKey(psmPlugin,"pvp_enabled");
        NamespacedKey hideplayerkey = new NamespacedKey(psmPlugin,"hide_players_enabled");

        ItemStack PVPButton = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta pvpMeta = PVPButton.getItemMeta();
        ItemStack hidePlayersButton = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta hidePlayersMeta = hidePlayersButton.getItemMeta();

        if(pdc.getOrDefault(pvpkey, PersistentDataType.BYTE, (byte)0) == 1){
            //pvpEnabled
            pvpMeta.displayName(Component.text("§2PVP ENABLED"));
        }
        else{
            pvpMeta.displayName(Component.text("§4PVP DISABLED"));
        }
        PVPButton.setItemMeta(pvpMeta);

        if(pdc.getOrDefault(hideplayerkey, PersistentDataType.BYTE, (byte)0) == 1){
            hidePlayersMeta.displayName(Component.text("§2Players Hidden"));
        }
        else{
            hidePlayersMeta.displayName(Component.text("§4Players Visible"));
        }
        hidePlayersButton.setItemMeta(hidePlayersMeta);


        Inventory inventory = Bukkit.createInventory(player, 9*3, "Preferences Menu");

        inventory.setItem(12, PVPButton);//12 and 14 will be centered for 2 items
        inventory.setItem(14, hidePlayersButton);
        player.openInventory(inventory);


        NamespacedKey inventoryOpenKey = new NamespacedKey(psmPlugin,"menu_open");
        pdc.set(inventoryOpenKey,PersistentDataType.BYTE, (byte)1);
        return true;
    }

}
