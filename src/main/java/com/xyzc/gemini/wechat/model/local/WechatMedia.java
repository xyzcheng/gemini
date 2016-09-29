package com.xyzc.gemini.wechat.model.local;

/**
 * Created by xyzc on 2016/9/28.
 */
public class WechatMedia {
    private int id;
    private String mediaId;
    private String mediaUrl;
    private String mediaType;

    public int getId() {
        return id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
