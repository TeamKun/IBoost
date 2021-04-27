package net.kunmc.lab.iboost.handler;

import net.kunmc.lab.iboost.Config;
import net.kunmc.lab.iboost.boost.BoostManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerHandler implements Listener {

    public static void init(PluginManager manager, JavaPlugin plugin) {
        manager.registerEvents(new ServerHandler(), plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, n -> onTick(), 1, 1);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        //e.isSneaking()
        if (!Config.isPressMode() && e.isSneaking()) {
            BoostManager.getInstance().boosted(e.getPlayer().getUniqueId(), 5f);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        BoostManager.getInstance().clear(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BoostManager.getInstance().clear(e.getPlayer().getUniqueId());
    }

    public static void onTick() {
        BoostManager manager = BoostManager.getInstance();

        Bukkit.getServer().getOnlinePlayers().forEach(n -> {

            if (Config.isPressMode() && n.isSneaking())
                manager.boosted(n.getUniqueId(), 0.5f);

            manager.tick(n.getUniqueId());
            n.spigot().sendMessage(ChatMessageType.ACTION_BAR, manager.getActionBar(n.getUniqueId()));
        });
    }
}
