package info.pocka.pkcartplugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class PKCartPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PKCartExitListener(), this);
        pm.registerEvents(new PKPlayerInteractListener(), this);
    }

}
