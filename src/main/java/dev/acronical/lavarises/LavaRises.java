package dev.acronical.lavarises;

import dev.acronical.lavarises.commands.PluginCommands;
import org.bukkit.plugin.java.JavaPlugin;

public final class LavaRises extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginCommands commands= new PluginCommands();
        getCommand("lavarise").setExecutor(commands);
        getCommand("initlava").setExecutor(commands);
        getServer().getConsoleSender().sendMessage("[LavaRises] - Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[LavaRises] - Plugin Disabled");
    }
}
