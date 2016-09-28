package com.xyzc.gemini.wechat.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xyzc on 2016/9/28.
 */

public class MediaIdMessage {
    @XStreamAlias("MediaId")
    @XStreamCDATA
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

}
