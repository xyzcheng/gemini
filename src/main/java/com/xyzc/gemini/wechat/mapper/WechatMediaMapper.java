package com.xyzc.gemini.wechat.mapper;

import com.xyzc.gemini.wechat.model.local.WechatMedia;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xyzc on 2016/9/28.
 */
@Component
public interface WechatMediaMapper {
    void insert(WechatMedia wechatMedia);
    List<WechatMedia> findAll();
}
