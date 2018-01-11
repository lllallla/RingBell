package edu.fudan.ringbell.http;

/**
 * Created by pc on 2018/1/10.
 */

public enum HttpURLs {
    LOGIN("/user/login","user login"),
    REGISTER("/user/register", "user register"),
    MODIFY("/user/modify", "user modify password"),
    UPLOAD("/audio-file/upload", "upload file"),
    DOWNLOAD("/audio-file/download", "download file"),
    DELETE("/audio-file/delete", "delete file"),
    LIST("/audio-file/list", "list file"),
    SHARE_CREATE("/share/create", "create share code"),
    SHARE_DOWNLOAD("/share/download", "download shared file")
    ;


    private String url;
    private String desc;
    private static final String URLPre = "http://47.96.147.90:8080";
    HttpURLs(String url, String desc) {
        this.url = URLPre + url;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
