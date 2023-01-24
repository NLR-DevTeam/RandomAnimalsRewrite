package top.nlrdev.random_animals.config;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;

public class PluginConfig extends JavaAutoSavePluginConfig {
    public static final PluginConfig INSTANCE = new PluginConfig();
    public Value<Long> coolDownTime = value("coolDownTime", 30000L);
    public Value<Long> fetchTimeout = value("fetchTimeout", 10000L);

    private PluginConfig() {
        super("Config");
    }
}
