package edu.fudan.ringbell.http;

import android.content.res.Resources;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */

public class UserRequest {
    static final HttpUtil util = HttpUtil.getInstance();
    public static int login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        try {
            String result = util.doPost(HttpURLs.LOGIN.getUrl(), params);
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    return 0;
                } else {
                    System.out.println(response.getMessage());
                    int j = register(username,password,username);
                    if (j == 0) {
                        return login(username,password);
                    } else {
                        return 1;
                    }
//                    return 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 1;
    }

    public static int register(String username, String password, String nickname) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        try {
            String result = util.doPost(HttpURLs.REGISTER.getUrl(), params);
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    return 0;
                } else {
                    System.out.println(response.getMessage());
                    return 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 1;
    }

    public static int modify(String oldpwd, String newpwd) {
        Map<String, String> params = new HashMap<>();
        params.put("old", oldpwd);
        params.put("new", newpwd);
        try {
            String result = util.doPost(HttpURLs.MODIFY.getUrl(), params);
            if (result != null) {
                System.out.println(result);
                Gson gson = new Gson();
                ResponseDTO response = gson.fromJson(result, ResponseDTO.class);
                if (response.isOk()) {
                    return 0;
                } else {
                    System.out.println(response.getMessage());
                    return 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 1;
    }
}
