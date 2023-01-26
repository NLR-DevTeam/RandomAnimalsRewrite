package top.nlrdev.random_animals;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.internal.deps.okhttp3.OkHttpClient;
import top.nlrdev.random_animals.commands.RandomCat;
import top.nlrdev.random_animals.commands.RandomDog;
import top.nlrdev.random_animals.commands.RandomDuck;
import top.nlrdev.random_animals.commands.RandomFox;
import top.nlrdev.random_animals.config.PluginConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class RandomAnimals extends JavaPlugin {
    public static final RandomAnimals INSTANCE = new RandomAnimals();
    public static final OkHttpClient globalClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
    public static final ExecutorService downloadExecutorService = Executors.newFixedThreadPool(8);
    public static final ExecutorService watchExecutorService = Executors.newFixedThreadPool(8);

    private RandomAnimals() {
        super(new JvmPluginDescriptionBuilder("top.nlrdev.random-animals", "1.0.1")
                .name("Random Animals")
                .author("NLR DevTeam")
                .build());

        reloadPluginConfig(PluginConfig.INSTANCE);
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(new RandomCat(), true);
        CommandManager.INSTANCE.registerCommand(new RandomDog(), true);
        CommandManager.INSTANCE.registerCommand(new RandomFox(), true);
        CommandManager.INSTANCE.registerCommand(new RandomDuck(), true);
    }
}