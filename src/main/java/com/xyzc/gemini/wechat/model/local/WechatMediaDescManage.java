package com.xyzc.gemini.wechat.model.local;

/**
 * Created by xyzc on 2016/9/30.
 */
public class WechatMediaDescManage {
    private int id;
    private String openId;
    private String location;
    private String time;
    private String story;
    private String mediaId;
    private int exchangeId;
    private String mediaUrl;

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getOpenId() {
        return openId;
    }

    public String getStory() {
        return story;
    }

    public String getTime() {
        return time;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
