package com.qin.defender.config;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class Config {

    private final ConfigItem configItem = new ConfigItem();

    private static volatile Config instance;

    private Config() {
        try {
            initConfigItem();
        } catch (Exception e) {
            throw new RuntimeException("Config file occurred a error", e);
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }

        return instance;
    }

    private void initConfigItem() {
        // todo: get value from properties
    }

    public ConfigItem getConfigItem() {
        return configItem;
    }
}
