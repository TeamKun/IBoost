package net.kunmc.lab.iboost;

import net.kunmc.lab.iboost.boost.BoostManager;
import net.kunmc.lab.iboost.handler.ServerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class IBoost extends JavaPlugin {

    @Override
    public void onEnable() {
        ServerHandler.init(getServer().getPluginManager(), this);
    }

    @Override
    public void onDisable() {
        BoostManager.getInstance().clear();
    }
}
