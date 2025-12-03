package me.nuttist.playerSettingsMenu.commands;

import me.nuttist.playerSettingsMenu.core.PlayerSettingsMenu;
import me.nuttist.playerSettingsMenu.util.visibleBlackList;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class unhide implements CommandExecutor {
    JavaPlugin psmPlugin;
    PlayerSettingsMenu psm;
    public unhide(JavaPlugin plugin, PlayerSettingsMenu newPsm){
        psmPlugin = plugin;
        psm = newPsm;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players have access to menu");
            return true;
        }
        if(strings.length >= 1){
            Player player = (Player)commandSender;
            Set<UUID> uuids = visibleBlackList.getVisibleExceptions(player, psm);
            for(String arg : strings){
                Player playerToUnhide = Bukkit.getPlayer(strings[0]);
                UUID uuid = Bukkit.getOfflinePlayer(arg).getUniqueId();
                if(uuids.contains(uuid)){
                    uuids.remove(uuid);
                    if(playerToUnhide != null) {
                        player.hidePlayer(psm, playerToUnhide);
                    }
                }
                else{
                    uuids.add(uuid);
                    if(playerToUnhide != null) {
                        player.showPlayer(psm, playerToUnhide);
                    }
                }


            }
            visibleBlackList.saveVisibleExceptions(player,psm,uuids);
        }
        else{
            commandSender.sendMessage("Please include the name of the player you want to be visible");
            return false;
        }
        return true;
    }


}
