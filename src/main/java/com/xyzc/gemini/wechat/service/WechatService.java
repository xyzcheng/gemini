package com.xyzc.gemini.wechat.service;

import com.xyzc.gemini.wechat.mapper.WechatMediaMapper;
import com.xyzc.gemini.wechat.model.local.WechatMedia;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * Created by xyzc on 2016/9/28.
 */
@Service
public class WechatService {
    @Resource
    WechatMediaMapper wechatMediaMapper;

    public void insertMedia(String mediaId, String mediaUrl) {
        WechatMedia wechatMedia = new WechatMedia();
        wechatMedia.setMediaId(mediaId);
        wechatMedia.setMediaUrl(mediaUrl);
        wechatMediaMapper.insert(wechatMedia);
    }

    public WechatMedia selectRadomMedia(String mediaId) {
        List<WechatMedia> wechatMedias = wechatMediaMapper.findAll();
        if (wechatMedias == null || wechatMedias.size() <= 1) {
            WechatMedia wechatMedia = new WechatMedia();
            wechatMedia.setMediaId(mediaId);
            return wechatMedia;
        }
        Random random = new Random();
        return wechatMedias.get(random.nextInt(wechatMedias.size() - 1));
    }
}
