package net.kunmc.lab.iboost;

import net.kunmc.lab.iboost.boost.BoostManager;
import net.kunmc.lab.iboost.command.IBCommands;
import net.kunmc.lab.iboost.handler.ServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

public final class IBoost extends JavaPlugin {

    @Override
    public void onEnable() {
        ServerHandler.init(getServer().getPluginManager(), this);
        IBCommands.init();
    }

    @Override
    public void onDisable() {
        BoostManager.getInstance().clear();
        Bukkit.getOnlinePlayers().forEach(n -> {
            n.setWalkSpeed(0.2f);
            n.setFlySpeed(0.1f);
            n.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
        });
    }
}
