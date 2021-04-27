package net.kunmc.lab.iboost.command;

import org.bukkit.Bukkit;

public class IBCommands {
    public static void init() {
        Bukkit.getPluginCommand("boost").setExecutor(new ModeChangeCommand());
    }
}
