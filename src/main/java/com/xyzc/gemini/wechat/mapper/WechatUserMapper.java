package com.xyzc.gemini.wechat.mapper;

import com.xyzc.gemini.wechat.model.local.WechatUser;
import org.springframework.stereotype.Component;

/**
 * Created by xyzc on 2016/9/28.
 */
@Component
public interface WechatUserMapper {
    void insert(WechatUser wechatUser);
}
