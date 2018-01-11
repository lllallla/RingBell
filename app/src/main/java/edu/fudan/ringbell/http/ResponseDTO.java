package edu.fudan.ringbell.http;

import java.util.List;

/**
 * Created by pc on 2018/1/11.
 */

public class ResponseDTO {
    private boolean ok;
    private String message;
    private List<String> object;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getObject() {
        return object;
    }

    public void setObject(List<String> object) {
        this.object = object;
    }
}
