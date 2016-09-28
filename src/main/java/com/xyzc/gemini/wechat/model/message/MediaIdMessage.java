package com.xyzc.gemini.wechat.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.xyzc.gemini.wechat.model.XStreamCDATA;

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
