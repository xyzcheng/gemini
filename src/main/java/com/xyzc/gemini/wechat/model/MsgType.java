package com.xyzc.gemini.wechat.model;

/**
 * Created by xyzc on 2016/9/28.
 */
public enum MsgType {
    Text("text"), Image("image");

    private String type;
    MsgType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return this.type;
    }
}
