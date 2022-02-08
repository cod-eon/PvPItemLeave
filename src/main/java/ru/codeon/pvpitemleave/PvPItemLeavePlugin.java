package ru.codeon.pvpitemleave;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.codeon.pvpitemleave.command.PvPItemLeaveCommand;
import ru.codeon.pvpitemleave.config.DataConfig;
import ru.codeon.pvpitemleave.handlers.PvPItemLeaveHandler;
import ru.codeon.pvpitemleave.utils.StringUtils;

import java.io.File;

public final class PvPItemLeavePlugin extends JavaPlugin {

    public static PvPItemLeavePlugin instance;
    public static DataConfig dataConfig;
    private File configFile;
    private FileConfiguration config;

    public void onEnable() {

        try {
			
            this.configFile = new File(getDataFolder() + "/config.yml");
			
            if (!this.configFile.exists())
                saveResource("config.yml", false);
				
            this.config = YamlConfiguration.loadConfiguration(this.configFile);
			
        } catch (Exception e) {
			
            Bukkit.getLogger().warning(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
			
        }

        instance = this;
        dataConfig = new DataConfig(config);

        getCommand("pvpitemleave").setExecutor(new PvPItemLeaveCommand());
        Bukkit.getPluginManager().registerEvents(new PvPItemLeaveHandler(), this);

    }

    public void reload() {
        try {
			
            if (!this.configFile.exists())
                saveResource("config.yml", false);
			
            this.config = YamlConfiguration.loadConfiguration(this.configFile);
			
        } catch (Exception e) {
			
            this.getLogger().severe(StringUtils.convertString("Не удалось перезагрузить конфиг"));
            this.getLogger().info(e.getClass().getSimpleName() + "-" + e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber());
			
        }
		
        dataConfig = new DataConfig(config);
    }

}
