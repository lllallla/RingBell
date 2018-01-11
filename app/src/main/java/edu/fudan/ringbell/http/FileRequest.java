package edu.fudan.ringbell.http;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fudan.ringbell.entity.MusicInfo;

/**
 *
 */

public class FileRequest {
    static final HttpUtil util = HttpUtil.getInstance();

    public static int upload(File file, String name) {
        Map<String, String> names = new HashMap<>();
        names.put("name", name);
        Map<String, File> files = new HashMap<>();
        files.put("file", file);
        try {
            String result = util.doFilePost(HttpURLs.UPLOAD.getUrl(),names,files);
            if (result != null) {
                System.out.println(result);
                return 0;
            } else {
                return 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static int download(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        try {
            boolean result = util.downLoadFromUrl(HttpURLs.DOWNLOAD.getUrl(),params, "UTF-8", name, "/audios");
            return result ? 0:1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int delete(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        try {
            String result = util.doPost(HttpURLs.DELETE.getUrl(),params);
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    return 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static List<MusicInfo> list() {
        try {
            String result = util.doGet(HttpURLs.LIST.getUrl());
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    List<MusicInfo> list = new ArrayList<>();
                    for (String name : response.getObject()) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setTitle(name);
                        list.add(musicInfo);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
