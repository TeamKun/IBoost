package net.kunmc.lab.iboost.boost;


import net.kunmc.lab.iboost.util.IkisugiUtil;
import net.kyori.adventure.sound.Sound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class BoostState {
    private final float maxCharge = 100;
    private final float ikisugiCharge = 75;
    private final UUID playerID;
    private float chaged;
    private int reduseCoolDown;
    private int waringColorCoolDown;
    private boolean waningColor;
    private boolean ikisugiCoolDown;

    public BoostState(UUID playerID) {
        this.playerID = playerID;
    }

    public void setChaged(float chaged) {
        this.chaged = chaged;
    }

    public void addChaged(float addChaged) {
        if (!ikisugiCoolDown) {
            setChaged(Math.min(this.chaged + addChaged, maxCharge));
            reduseCoolDown = 20;
        }
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

        if (ikisugiCoolDown) {
            return ChatColor.AQUA;
        }

        if (isIkisugible()) {
            if (waningColor)
                return ChatColor.DARK_RED;
            else
                return ChatColor.GOLD;
        }

        if (chaged >= 50)
            return ChatColor.GOLD;

        return ChatColor.WHITE;
    }

    public void tick() {
        Player player = Bukkit.getServer().getPlayer(playerID);
        if (reduseCoolDown < 0) {
            reduseChaged(ikisugiCoolDown ? 0.5f : 0.1f);
        } else {
            reduseCoolDown--;
        }

        if (isIkisugible()) {
            if (waringColorCoolDown < 3) {
                waringColorCoolDown++;
            } else {
                waringColorCoolDown = 0;
                waningColor = !waningColor;
                if (!ikisugiCoolDown) {
                    Bukkit.getPlayer(playerID).playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_HARP, Sound.Source.PLAYER, 1, 1));
                }
            }

            if (chaged >= maxCharge && !ikisugiCoolDown) {
                player.getWorld().createExplosion(player, 10f);
                ikisugiCoolDown = true;
                reduseCoolDown = 0;
            }
        }

        if (chaged <= 0) {
            ikisugiCoolDown = false;
        }

        if (!ikisugiCoolDown) {
            player.setWalkSpeed(IkisugiUtil.clamp(chaged / 100f, 0.2f, 1f));
            player.setFlySpeed(IkisugiUtil.clamp(chaged / 100f, 0.2f, 1f));
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(IkisugiUtil.clamp(chaged / 10f * 4, 4, 40));

            int hl = (int) IkisugiUtil.clamp(chaged / 100 * 120 - 1, -1, 120);
            if (hl >= 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10, hl, true, false));
            }
        } else {
            player.setWalkSpeed(0.2f);
            player.setFlySpeed(0.1f);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
        }

    }
}
