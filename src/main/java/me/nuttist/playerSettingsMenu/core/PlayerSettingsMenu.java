package me.nuttist.playerSettingsMenu.core;

import me.nuttist.playerSettingsMenu.commands.menu;
import me.nuttist.playerSettingsMenu.commands.unhide;
import me.nuttist.playerSettingsMenu.util.visibleBlackList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public final class PlayerSettingsMenu extends JavaPlugin implements Listener {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PlayerSettingsMenu.class);
    private Logger logger = getLogger();
    private List<Player> hidePlayers;
    private NamespacedKey hideBlackListKey;
    @Override
    public void onEnable() {

        loadCommands();
        getServer().getPluginManager().registerEvents(this,this);

        hidePlayers = new ArrayList<>();

        hideBlackListKey = new NamespacedKey(this,"hide_black_list");
        logger.info("PlayerSettingsMenu by Nuttist has successfully booted up");

    }
    public NamespacedKey getHideBlackListKey(){
        return hideBlackListKey;
    }
    private void loadCommands() {
        getCommand("menu").setExecutor(new menu(this));
        getCommand("unhide").setExecutor(new unhide(this, this));

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        String title = PlainTextComponentSerializer.plainText()
                .serialize(e.getView().title());
        if (title.equals("Preferences Menu")){
            NamespacedKey inventoryOpenKey = new NamespacedKey(this,"menu_open");
            e.getPlayer().getPersistentDataContainer().set(inventoryOpenKey, PersistentDataType.BYTE, (byte)0);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        String title = PlainTextComponentSerializer.plainText()
                .serialize(e.getView().title());
        if (title.equals("Preferences Menu")){
            if(e.getCurrentItem() != null){
                toggleSetting(e.getSlot(), (Player)e.getWhoClicked(), e.getInventory());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player p = e.getPlayer();

        //hide this player from all players with it enabled
        for (Player player : hidePlayers) {
            Set<UUID> ignoredPlayers = visibleBlackList.getVisibleExceptions(player,this);
            if(!ignoredPlayers.contains(p.getUniqueId())){
                player.hidePlayer(this, p);
            }
        }

        //check if player has invisible players turned on then add them to the list and hide all players
        if(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(this, "hide_players_enabled"),PersistentDataType.BYTE,(byte)0) == 1){
            hideAllPlayers(e.getPlayer());
        }


    }
    public void onPlayerLeave(PlayerQuitEvent e){
        //remove player from hiding list if they were on it
        hidePlayers.remove(e.getPlayer());

    }

    public void onPlayerKick(PlayerKickEvent e){
        //remove player from hiding list if they were on it
        hidePlayers.remove(e.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player victim && e.getDamager() instanceof Player attacker) {
            if( attacker.getPersistentDataContainer().getOrDefault(new NamespacedKey(this, "pvp_enabled"),PersistentDataType.BYTE,(byte)0) == 0){
                //attacker doesnt have permissions enabled
                attacker.sendMessage("§4You have PVP disabled");
                e.setCancelled(true);
                return;
            }
            if( victim.getPersistentDataContainer().getOrDefault(new NamespacedKey(this, "pvp_enabled"),PersistentDataType.BYTE,(byte)0) == 0){
                //victim doesnt have permissions enabled
                attacker.sendMessage("§4This player has PVP disabled");
                e.setCancelled(true);
                return;
            }

        }
    }

    //add sounds
    private void toggleSetting(int slot, Player player, Inventory inventory){
        switch (slot){
            case(12):
                //set pvp meta data
                NamespacedKey pvpKey = new NamespacedKey(this,"pvp_enabled");
                byte currentval = player.getPersistentDataContainer().getOrDefault(pvpKey, PersistentDataType.BYTE, (byte)0);
                currentval = (byte) (currentval ^ 1);
                player.getPersistentDataContainer().set(pvpKey,PersistentDataType.BYTE, currentval);


                //set visual item
                ItemStack item = inventory.getItem(12);
                ItemMeta metadata = item.getItemMeta();

                if (currentval == (byte) 1){
                    metadata.displayName(Component.text("§2PVP ENABLED"));
                }else{
                    metadata.displayName(Component.text("§4PVP DISABLED"));
                }
                item.setItemMeta(metadata);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f,1.0f);
                break;
            case(14):
                //set  player visibility meta data
                NamespacedKey hideplayersKey = new NamespacedKey(this,"hide_players_enabled");
                byte currentplayersvisible = player.getPersistentDataContainer().getOrDefault(hideplayersKey, PersistentDataType.BYTE, (byte)0);
                currentplayersvisible = (byte) (currentplayersvisible ^ 1);
                player.getPersistentDataContainer().set(hideplayersKey,PersistentDataType.BYTE, currentplayersvisible);


                //set visual item
                ItemStack hideplayersitem = inventory.getItem(14);
                ItemMeta hideplayersmetadata = hideplayersitem.getItemMeta();

                if (currentplayersvisible == (byte) 1){
                    hideAllPlayers(player);
                    hideplayersmetadata.displayName(Component.text("§4Players Hidden"));
                }else{
                    unhideAllPlayers(player);
                    hideplayersmetadata.displayName(Component.text("§2Players Visible"));

                }
                hideplayersitem.setItemMeta(hideplayersmetadata);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f,1.0f);
                break;
        }
    }

    private void hideAllPlayers(Player playerToHideFrom){
        hidePlayers.add(playerToHideFrom);
        logger.info(playerToHideFrom.getUniqueId().toString());
        Set<UUID> ignoredPlayers = visibleBlackList.getVisibleExceptions(playerToHideFrom,this);
        for(UUID uuid :ignoredPlayers){
            logger.info(uuid.toString());
        }
        for (Player player : Bukkit.getOnlinePlayers()){
            if(!ignoredPlayers.contains(player.getUniqueId())){
                playerToHideFrom.hidePlayer(this, player);
            }
        }
    }

    private void unhideAllPlayers(Player playerToUnhideFrom){
        hidePlayers.remove(playerToUnhideFrom);
        for(Player player : Bukkit.getOnlinePlayers()){
            playerToUnhideFrom.showPlayer(this, player);
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
