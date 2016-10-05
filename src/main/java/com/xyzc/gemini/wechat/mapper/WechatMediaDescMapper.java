package com.xyzc.gemini.wechat.mapper;

import com.xyzc.gemini.wechat.model.local.WechatMediaDesc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xyzc on 2016/9/29.
 */
@Component
public interface WechatMediaDescMapper {
    void insert(WechatMediaDesc mediaDesc);
    void deleteById(int id);
    void updateById(WechatMediaDesc mediaDesc);
    List<WechatMediaDesc> findByOpenId(String openId);
    List<WechatMediaDesc> findSelfMediaDesc(String openId);
    List<WechatMediaDesc> findAll();
    List<WechatMediaDesc> findMend();// 弥补未注意微信缓存导致的问题
}
