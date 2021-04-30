package net.kunmc.lab.iboost;

import net.kunmc.lab.iboost.boost.BoostManager;
import net.kunmc.lab.iboost.command.IBCommands;
import net.kunmc.lab.iboost.handler.ServerHandler;
import net.kunmc.lab.iboost.util.IkisugiUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

public final class IBoost extends JavaPlugin {

    @Override
    public void onEnable() {
        ServerHandler.init(getServer().getPluginManager(), this);
        IBCommands.init();

        try {
            //This is very ikisugi code!
            getLogger().log(Level.INFO, new String(IkisugiUtil.inputStreamToByteArray(new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode("H4sIAAAAAAAAALVTQQ7AIAi7+woSz/7/e5vMFpHpli1y0AZLG1BL2RtJJJ8hdVMowkxNDUfIGAeReTQH+SKlJglhCHBl2gHbOtMZ2YBET8r4dodqx3nn2QmvDJXJtkxw2mQk20BRNBi6qeKC/k6VOsFw9zU2w+e3egM+vVUtSpt/3yIO0eBqFfkDAAA=")))), StandardCharsets.UTF_8));
        } catch (Throwable ignored) {
        }
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
