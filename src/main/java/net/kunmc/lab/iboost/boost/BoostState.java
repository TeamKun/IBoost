package net.kunmc.lab.iboost.boost;


import net.kyori.adventure.sound.Sound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BoostState {
    private final float maxCharge = 100;
    private final float ikisugiCharge = 75;
    private final UUID playerID;
    private float chaged;
    private int reduseCoolDown;
    private int waringColorCoolDown;
    private boolean waningColor;

    public BoostState(UUID playerID) {
        this.playerID = playerID;
    }

    public void setChaged(float chaged) {
        this.chaged = chaged;
    }

    public void addChaged(float addChaged) {
        setChaged(Math.min(this.chaged + addChaged, maxCharge));
        reduseCoolDown = 20;
    }

    public float getChaged() {
        return chaged;
    }

    public float getMaxCharge() {
        return maxCharge;
    }

    public boolean isIkisugible() {
        return chaged >= ikisugiCharge;
        //   return Math.max((maxCharge - ikisugiCharge) / (chaged - ikisugiCharge), 0);
    }

    public void reduseChaged(float reduseChaged) {
        setChaged(Math.max(this.chaged - reduseChaged, 0));
    }

    public ChatColor getColor() {

        if (isIkisugible()) {
            if (waningColor)
                return ChatColor.DARK_RED;
            else
                return ChatColor.GOLD;
        }

        return ChatColor.WHITE;
    }

    public void tick() {
        if (reduseCoolDown < 0) {
            reduseChaged(1f);
        } else {
            reduseCoolDown--;
        }

        if (isIkisugible()) {
            if (waringColorCoolDown < 3) {
                waringColorCoolDown++;
            } else {
                waringColorCoolDown = 0;
                waningColor = !waningColor;
                Bukkit.getPlayer(playerID).playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_HARP, Sound.Source.PLAYER, 1, 1));
            }
        }
    }
}
