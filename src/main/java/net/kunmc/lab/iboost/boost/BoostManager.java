package net.kunmc.lab.iboost.boost;

import net.kunmc.lab.iboost.util.IkisugiUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoostManager {
    private static final BoostManager INSTANCE = new BoostManager();
    private final Map<UUID, BoostState> STATES = new HashMap<>();
    private boolean active;
    private boolean pressMode = false;

    public static BoostManager getInstance() {
        return INSTANCE;
    }

    public void boosted(UUID playerUUID, float parsent) {
        if (!STATES.containsKey(playerUUID))
            STATES.put(playerUUID, new BoostState(playerUUID));

        STATES.get(playerUUID).addChaged(parsent);
    }

    public BaseComponent getActionBar(UUID playerID) {

        float parsent = 0f;

        if (STATES.containsKey(playerID))
            parsent = STATES.get(playerID).getChaged() / STATES.get(playerID).getMaxCharge();

        float prePar = IkisugiUtil.clamp(parsent, 0f, 0.5f);
        float postPar = IkisugiUtil.clamp(parsent - 0.5f, 0f, 0.5f);
        String str = IkisugiUtil.getPercentageBar(prePar * 2f, 11) + (int) (parsent * 100) + "%" + IkisugiUtil.getPercentageBar(postPar * 2.3f, 11);
        TextComponent component = new TextComponent(str);
        if (STATES.containsKey(playerID))
            component.setColor(STATES.get(playerID).getColor());

        return component;
    }

    public void tick(UUID playerUUID) {
        if (!STATES.containsKey(playerUUID))
            STATES.put(playerUUID, new BoostState(playerUUID));

        STATES.get(playerUUID).tick();

    }

    public void clear(UUID playerUUID) {
        STATES.remove(playerUUID);
    }

    public void clear() {
        STATES.clear();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPressMode() {
        return pressMode;
    }

    public void setPressMode(boolean pressMode) {
        this.pressMode = pressMode;
    }

    public BoostState getState(UUID plid) {

        if (!STATES.containsKey(plid))
            STATES.put(plid, new BoostState(plid));

        return STATES.get(plid);
    }
}
