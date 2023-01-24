package top.nlrdev.random_animals.utils;

import net.mamoe.mirai.internal.deps.okhttp3.Request;
import net.mamoe.mirai.internal.deps.okhttp3.Response;
import net.mamoe.mirai.utils.ExternalResource;
import org.json.JSONObject;
import top.nlrdev.random_animals.RandomAnimals;

import java.io.IOException;
import java.util.function.Function;

public class DownloadUtil {
    public static ExternalResource downloadAndParse(String jsonURL, String jsonKey, Function<String, JSONObject> getObjectFunction) throws IOException {
        Request request = new Request.Builder().url(jsonURL).build();
        Response response = RandomAnimals.globalClient.newCall(request).execute();

        if (response.body() == null) throw new IOException("Response body is null.");
        JSONObject jsonObject = getObjectFunction.apply(response.body().string());

        Request imageRequest = new Request.Builder().url(jsonObject.getString(jsonKey)).build();
        Response imageResponse = RandomAnimals.globalClient.newCall(imageRequest).execute();

        if (imageResponse.body() == null) throw new IOException("Response body is null.");

        RandomAnimals.INSTANCE.getLogger().info("图片下载成功!");

        return ExternalResource.create(imageResponse.body().bytes());
    }

    public static ExternalResource downloadAndParse(String jsonURL, String jsonKey) throws IOException {
        return downloadAndParse(jsonURL, jsonKey, JSONObject::new);
    }
}
