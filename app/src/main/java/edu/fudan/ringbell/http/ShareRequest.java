package edu.fudan.ringbell.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fudan.ringbell.entity.MusicInfo;

/**
 * Created by pc on 2018/1/11.
 */

public class ShareRequest {
    private static final HttpUtil util = HttpUtil.getInstance();

    public static String create(String username, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("name", name);
        try {
            String result = util.doPost(HttpURLs.SHARE_CREATE.getUrl(),params);
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    return response.getObject().get(0);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int download(String uniqueId) {
        Map<String, String> params = new HashMap<>();
        params.put("uniqueId", uniqueId);
        try {
            boolean result = util.downLoadFromUrl(HttpURLs.SHARE_DOWNLOAD.getUrl(),params, "UTF-8", uniqueId+".mp3", "/audios");
            return result ? 0 : 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
