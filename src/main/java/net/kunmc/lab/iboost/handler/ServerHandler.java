package net.kunmc.lab.iboost.handler;

import net.kunmc.lab.iboost.boost.BoostManager;
import net.kunmc.lab.iboost.boost.BoostState;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ServerHandler implements Listener {

    public static void init(PluginManager manager, JavaPlugin plugin) {
        manager.registerEvents(new ServerHandler(), plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, n -> onTick(), 1, 1);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        if (BoostManager.getInstance().isActive() && !BoostManager.getInstance().isPressMode() && e.isSneaking()) {
            BoostManager.getInstance().boosted(e.getPlayer().getUniqueId(), 1f);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        BoostManager.getInstance().clear(e.getEntity().getUniqueId());
        e.getEntity().setWalkSpeed(0.2f);
        e.getEntity().setFlySpeed(0.1f);
        e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BoostManager.getInstance().clear(e.getPlayer().getUniqueId());
        e.getPlayer().setWalkSpeed(0.2f);
        e.getPlayer().setFlySpeed(0.1f);
        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        BoostManager manager = BoostManager.getInstance();

        if (!manager.isActive())
            return;

        BoostState state = manager.getState(e.getPlayer().getUniqueId());

        if (e.getPlayer().isInWater()) {
            Location f = e.getFrom();
            Location t = e.getTo();
            double speed = e.getFrom().distance(e.getTo());
            //  Vector v = new Vector(t.getX() - f.getX(), t.getY() - f.getY(), t.getZ() - f.getZ());
            Vector v = e.getPlayer().getLocation().getDirection().normalize();

            if (state.getChaged() <= 20) {
                v.multiply(0.5f * (state.getChaged() / 20f));
                v.setY(e.getPlayer().getVelocity().getY());
                e.getPlayer().setVelocity(v);
            } else {
                float par = (state.getChaged() - 20f) / 80f;
                if (speed <= 1.3 + (0.3 * par)) {
                    if (!e.getPlayer().isSwimming())
                        v.setY(0);
                    e.getPlayer().setVelocity(v.multiply(par + 1.5));
                }
            }

        }
    }


    public static void onTick() {
        if (BoostManager.getInstance().isActive()) {
            BoostManager manager = BoostManager.getInstance();

            Bukkit.getServer().getOnlinePlayers().forEach(n -> {

                if (BoostManager.getInstance().isPressMode() && n.isSneaking()) {
                    manager.boosted(n.getUniqueId(), 0.3f);
                    n.setWalkSpeed(0.2f);
                    n.setFlySpeed(0.1f);
                    n.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
                }

                manager.tick(n.getUniqueId());
                n.spigot().sendMessage(ChatMessageType.ACTION_BAR, manager.getActionBar(n.getUniqueId()));
            });
        }
    }
}
