package com.xyzc.gemini.wechat.model.local;

/**
 * Created by xyzc on 2016/9/28.
 */
public class WechatMessage {
    private int id;
    private String openId;
    private String type;
    private String content;

    public int getId() {
        return id;
    }

    public String getOpenId() {
        return openId;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }
}