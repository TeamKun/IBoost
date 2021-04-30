package net.kunmc.lab.iboost.command;

import net.kunmc.lab.iboost.boost.BoostManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModeChangeCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equals("boost")) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("/boost <enable/disable/mode> <mode場合,mash/press>");
        } else if (args[0].equals("enable")) {
            if (!BoostManager.getInstance().isActive()) {
                BoostManager.getInstance().setActive(true);
                sender.sendMessage("Boost Pluginを有効にしました");
            } else {
                sender.sendMessage("Boost Pluginはすでに有効です");
            }
        } else if (args[0].equals("disable")) {
            if (BoostManager.getInstance().isActive()) {
                BoostManager.getInstance().setActive(false);
                sender.sendMessage("Boost Pluginを無効にしました");
                BoostManager.getInstance().clear();
                Bukkit.getOnlinePlayers().forEach(n -> {
                    n.setWalkSpeed(0.2f);
                    n.setFlySpeed(0.1f);
                    n.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
                });
            } else {
                sender.sendMessage("Boost Pluginはすでに無効です");
            }
        } else if (args[0].equals("mode")) {
            if (args.length == 1) {
                sender.sendMessage("/boost mode <mash/press>");
            } else if (args[1].equals("press")) {
                if (!BoostManager.getInstance().isPressMode()) {
                    BoostManager.getInstance().setPressMode(true);
                    sender.sendMessage("Boost Pluginを長押しモードにしました");
                } else {
                    sender.sendMessage("Boost Pluginはすでに長押しモードです");
                }
            } else if (args[1].equals("mash")) {
                if (BoostManager.getInstance().isPressMode()) {
                    BoostManager.getInstance().setPressMode(false);
                    sender.sendMessage("Boost Pluginを連打モードにしました");
                } else {
                    sender.sendMessage("Boost Pluginはすでに連打モードです");
                }
            }
        }
        return true;
    }

    //https://github.com/TeamKun/FanFanFan/blob/master/src/main/java/net/kunmc/lab/FanPlugin.java#L66
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equals("boost")) {
            if (args.length == 1) {
                return Arrays.asList("enable", "disable", "mode");
            } else if (args.length == 2 && args[0].equals("mode")) {
                return Arrays.asList("press", "mash");
            }
        }
        return Collections.emptyList();
    }
}
