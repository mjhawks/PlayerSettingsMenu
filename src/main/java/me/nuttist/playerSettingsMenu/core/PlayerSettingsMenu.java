package me.nuttist.playerSettingsMenu.core;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public final class PlayerSettingsMenu extends JavaPlugin implements Listener {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PlayerSettingsMenu.class);
    private Logger logger = getLogger();

    @Override
    public void onEnable() {

        loadCommands();
        getServer().getPluginManager().registerEvents(this,this);
        logger.info("Simple Commands by Nuttist has successfully booted up");
    }

    private void loadCommands() {
        //getCommand("commandname").setExecutor(new commandname(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
