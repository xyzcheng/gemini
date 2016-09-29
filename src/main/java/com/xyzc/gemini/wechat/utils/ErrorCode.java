package com.xyzc.gemini.wechat.utils;

/**
 * Created by xyzc on 2016/9/29.
 */
public class ErrorCode {
    public static final int ERROR_SUCCESS = 0;
    public static final int ERROR_UNKNOWN = -1;
    public static final int ERROR_FORM = -2; //回复格式错误
    public static final int ERROR_HAS_UNFINISHED_PIC = -3; //没有为图片添加描述信息
    public static final int ERROR_NO_UNFINISHED_PIC = -4;
    public static final int ERROR_DESC_NEED_PIC = -5; //没有为描述信息添加图片
}
