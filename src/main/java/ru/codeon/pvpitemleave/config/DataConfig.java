package ru.codeon.pvpitemleave.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DataConfig {

    private final ConfigurationSection itemData;
    private final String successMessage;
    private final String incorrectSyntax;
    private final String playerIsNull;
    private final String successGived;
    private final int velocityValue;
    private final String successReloaded;

    public DataConfig(FileConfiguration config) {

        itemData = config.getConfigurationSection("item_data");
        successMessage = config.getString("success_message");
        incorrectSyntax = config.getString("incorrect_syntax");
        playerIsNull = config.getString("player_is_null");
        successGived = config.getString("success_gived");
        velocityValue = config.getInt("velocity_value");
        successReloaded = config.getString("success_reloaded");

    }

    public ConfigurationSection getItemData() {
        return this.itemData;
    }

    public String getSuccessMessage() { return this.successMessage; }

    public String getIncorrectSyntax() { return this.incorrectSyntax; }

    public String getPlayerIsNull() { return this.playerIsNull; }

    public String getSuccessGived() { return this.successGived; }

    public int getVelocityValue() { return this.velocityValue; }

    public String getSuccessReloaded() { return this.successReloaded; }

}
