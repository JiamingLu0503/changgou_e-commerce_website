package entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class Message implements Serializable {
    private int code;
    private Object content;
    @JSONField(serialize = false)
    private String routekey;
    @JSONField(serialize = false)
    private String exechange;

    public Message() {
    }

    public Message(final int code, final Object content) {
        this.code = code;
        this.content = content;
    }

    public Message(final int code, final Object content, final String routekey, final String exechange) {
        this.code = code;
        this.content = content;
        this.routekey = routekey;
        this.exechange = exechange;
    }

    public String getRoutekey() {
        return this.routekey;
    }

    public void setRoutekey(final String routekey) {
        this.routekey = routekey;
    }

    public String getExechange() {
        return this.exechange;
    }

    public void setExechange(final String exechange) {
        this.exechange = exechange;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(final Object content) {
        this.content = content;
    }
}