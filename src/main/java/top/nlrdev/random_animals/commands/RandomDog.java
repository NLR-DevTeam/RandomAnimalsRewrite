package top.nlrdev.random_animals.commands;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.ConsoleCommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import top.nlrdev.random_animals.RandomAnimals;
import top.nlrdev.random_animals.config.PluginConfig;
import top.nlrdev.random_animals.utils.Cooler;
import top.nlrdev.random_animals.utils.DownloadUtil;

import java.io.InterruptedIOException;
import java.util.Objects;
import java.util.concurrent.Future;

public class RandomDog extends JRawCommand {
    public RandomDog() {
        super(RandomAnimals.INSTANCE, "random-dog", "来只狗", "来只狗狗", "来只狗勾", "来只修勾", "来只汪");
        setDescription("随机来张狗勾图");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        if (sender instanceof ConsoleCommandSender || sender.getSubject() == null) {
            sender.sendMessage("请勿在控制台中运行此命令!");
            return;
        }

        long uid = Objects.requireNonNull(sender.getUser()).getId();

        if (Cooler.isLocked(uid)) {
            sender.getSubject().sendMessage("修勾玩累了，请待会再来！");
            return;
        }
        Cooler.lock(uid, PluginConfig.INSTANCE.coolDownTime.get());

        sender.getSubject().sendMessage("请稍等，修勾正在跑步前进！");

        Future<?> task = RandomAnimals.downloadExecutorService.submit(() -> {
            try {
                ExternalResource externalResource = DownloadUtil.downloadAndParse("https://random.dog/woof.json", "url");
                Image image = sender.getSubject().uploadImage(externalResource);
                sender.getSubject().sendMessage(image);

                externalResource.close();
            } catch (Exception ex) {
                if (ex instanceof InterruptedIOException) return;

                sender.getSubject().sendMessage("修勾因为错误迷路了，请到控制台查看详细信息!\n" + ex);
                ex.printStackTrace();
            }
        });

        RandomAnimals.watchExecutorService.submit(() -> {
            try {
                Thread.sleep(PluginConfig.INSTANCE.fetchTimeout.get());
            } catch (InterruptedException ex) {
                return;
            }

            if (task.isDone() || task.isCancelled()) {
                return;
            }

            task.cancel(true);
            sender.getSubject().sendMessage("修勾图片获取超时，请稍后再试!");
        });
    }
}
