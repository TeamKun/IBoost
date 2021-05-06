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
    private float charged;
    private int reduceCoolDown;
    private int warningColorCoolDown;
    private boolean warningColor;
    private boolean ikisugiCoolDown;

    public BoostState(UUID playerID) {
        this.playerID = playerID;
    }

    public void setChaged(float chaged) {
        this.charged = chaged;
    }

    public void addChaged(float addChaged) {
        if (!ikisugiCoolDown) {
            setChaged(Math.min(this.charged + addChaged, maxCharge));
            reduceCoolDown = 20;
        }
    }

    public float getChaged() {
        return charged;
    }

    public float getMaxCharge() {
        return maxCharge;
    }

    public boolean isIkisugible() {
        return charged >= ikisugiCharge;
    }

    public void reduseChaged(float reduseChaged) {
        setChaged(Math.max(this.charged - reduseChaged, 0));
    }

    public ChatColor getColor() {

        if (ikisugiCoolDown) {
            return ChatColor.AQUA;
        }

        if (isIkisugible()) {
            if (warningColor)
                return ChatColor.DARK_RED;
            else
                return ChatColor.GOLD;
        }

        if (charged >= 50)
            return ChatColor.GOLD;

        if (charged >= 25)
            return ChatColor.YELLOW;

        return ChatColor.WHITE;
    }

    public void tick() {
        Player player = Bukkit.getServer().getPlayer(playerID);
        if (reduceCoolDown < 0) {
            reduseChaged(ikisugiCoolDown ? 0.5f : 0.1f);
        } else {
            reduceCoolDown--;
        }

        if (isIkisugible()) {
            if (warningColorCoolDown < 3) {
                warningColorCoolDown++;
            } else {
                warningColorCoolDown = 0;
                warningColor = !warningColor;
                if (!ikisugiCoolDown) {
                    Bukkit.getPlayer(playerID).playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_HARP, Sound.Source.PLAYER, 1, 1));
                }
            }

            if (charged >= maxCharge && !ikisugiCoolDown) {
                player.getWorld().createExplosion(player.getLocation(), 5);


                ikisugiCoolDown = true;
                reduceCoolDown = 0;
            }
        }

        if (charged <= 0) {
            ikisugiCoolDown = false;
        }

        if (!ikisugiCoolDown) {

            if (charged < 20) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, (int) ((20 - charged) / 10), true, false));
            }

            if(!BoostManager.getInstance().isPressMode() || !player.isSneaking()){
                player.setWalkSpeed(IkisugiUtil.clamp(charged / 100f, 0.07f, 1f));
                player.setFlySpeed(IkisugiUtil.clamp(charged / 100f, 0.1f, 1f));
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(IkisugiUtil.clamp(charged / 10f * 4, 1, 40));

                //   int hl = (int) IkisugiUtil.clamp(charged / 100 * 120 - 1, -1, 120);

                if (charged >= 30) {
                    int xx = (int) Math.pow(1.06d, charged);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10, Math.min(xx, 255), true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, Math.max((int) (10 * ((double) (charged - 30) / 70d)), 0), true, false));
                }
            }

        } else {
            player.setWalkSpeed(0.07f);
            player.setFlySpeed(0.1f);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1);

            if (!BoostManager.getInstance().isPressMode() || !player.isSneaking())
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5, 2, true, false));

        }

    }

    public boolean isIkisugiCoolDown() {
        return ikisugiCoolDown;
    }
}
