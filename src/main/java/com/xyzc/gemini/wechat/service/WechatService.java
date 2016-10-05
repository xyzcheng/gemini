package com.xyzc.gemini.wechat.service;

import com.xyzc.gemini.wechat.mapper.WechatMediaDescMapper;
import com.xyzc.gemini.wechat.mapper.WechatMediaMapper;
import com.xyzc.gemini.wechat.model.local.WechatAllMediaDescResult;
import com.xyzc.gemini.wechat.model.local.WechatMedia;
import com.xyzc.gemini.wechat.model.local.WechatMediaDesc;
import com.xyzc.gemini.wechat.model.local.WechatMediaDescManage;
import com.xyzc.gemini.wechat.utils.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by xyzc on 2016/9/28.
 */
@Service
public class WechatService {

    private static Logger logger = Logger.getLogger(WechatService.class);

    @Resource
    WechatMediaMapper wechatMediaMapper;
    @Resource
    WechatMediaDescMapper wechatMediaDescMapper;

    public int addMedia(String opendId, String mediaId, String mediaUrl, String mediaType) {
        if (!findUnFinishedMedia(opendId).isEmpty()) {
            return ErrorCode.ERROR_HAS_UNFINISHED_PIC;
        }
        WechatMedia wechatMedia = new WechatMedia();
        wechatMedia.setMediaId(mediaId);
        wechatMedia.setMediaUrl(mediaUrl);
        wechatMedia.setMediaType(mediaType);
        wechatMediaMapper.insert(wechatMedia);
        WechatMediaDesc mediaDesc = new WechatMediaDesc();
        mediaDesc.setOpenId(opendId);
        mediaDesc.setMediaId(mediaId);
        wechatMediaDescMapper.insert(mediaDesc);
        return ErrorCode.ERROR_SUCCESS;
    }

    public List<WechatMediaDesc> findUnFinishedMedia(String openId) {
        List<WechatMediaDesc> mediaDescs = wechatMediaDescMapper.findByOpenId(openId);
        List<WechatMediaDesc> unFinishedMediaDescs = new ArrayList<WechatMediaDesc>();
        if (mediaDescs == null || mediaDescs.isEmpty()) {
            return unFinishedMediaDescs;
        }
        for (WechatMediaDesc mediaDesc : mediaDescs) {
            if (StringUtils.isEmpty(mediaDesc.getTime())
                    || StringUtils.isEmpty(mediaDesc.getLocation())
                    || StringUtils.isEmpty(mediaDesc.getStory())) {
                unFinishedMediaDescs.add(mediaDesc);
            }
        }
        return unFinishedMediaDescs;
    }

    public int addMediaDescription(String openId, WechatMediaDesc mediaDesc) {
        List<WechatMediaDesc> unFinishedMediaDescs = findUnFinishedMedia(openId);
        if (unFinishedMediaDescs.isEmpty()) {
            return ErrorCode.ERROR_DESC_NEED_PIC;
        }
        WechatMediaDesc updateMediaDesc = unFinishedMediaDescs.get(0);
        updateMediaDesc.setTime(mediaDesc.getTime());
        updateMediaDesc.setLocation(mediaDesc.getLocation());
        updateMediaDesc.setStory(mediaDesc.getStory());
        wechatMediaDescMapper.updateById(updateMediaDesc);
        return ErrorCode.ERROR_SUCCESS;
    }

    public WechatMediaDesc findUnSelfRadomMedia(String openId) {
        List<WechatMediaDesc> mediaDescs = wechatMediaDescMapper.findMend(); //弥补微信缓存
        if (mediaDescs == null || mediaDescs.isEmpty()) {
            return null;
        }
        List<WechatMediaDesc> unSelfMediaDescs = new ArrayList<WechatMediaDesc>();
        List<WechatMediaDesc> selfMediaDescs = new ArrayList<WechatMediaDesc>();
        Set<Integer> alreadyExId = new HashSet<Integer>();
        for (WechatMediaDesc mediaDesc : mediaDescs) {
            if (mediaDesc.getId() < 185 || StringUtils.isEmpty(mediaDesc.getTime())
                    || StringUtils.isEmpty(mediaDesc.getLocation())
                    || StringUtils.isEmpty(mediaDesc.getStory())) {
                continue;
            }
            if (!mediaDesc.getOpenId().equals(openId)) {
                unSelfMediaDescs.add(mediaDesc);
            } else {
                selfMediaDescs.add(mediaDesc);
                alreadyExId.add(mediaDesc.getExchangeId());
            }
        }
        if (selfMediaDescs.isEmpty()) {
            return null;
        }
        WechatMediaDesc exchangeMediaDesc = null;
        WechatMediaDesc lastMediaDesc = selfMediaDescs.get(selfMediaDescs.size() - 1);
        int exchangeId = lastMediaDesc.getExchangeId();
        if (exchangeId > 0) {
            for (WechatMediaDesc mediaDesc : mediaDescs) {
                if (mediaDesc.getId() == exchangeId) {
                    exchangeMediaDesc = mediaDesc;
                }
            }
        }
        // 有可能原来的exchangeId对应的media description 被删除了
        if (exchangeMediaDesc == null) {
            if (unSelfMediaDescs.size() <= 0) {
                /*for (WechatMediaDesc mediaDesc : selfMediaDescs) {
                    if (!alreadyExId.contains(mediaDesc.getId())) {
                        exchangeMediaDesc = mediaDesc;
                        break;
                    }
                }*/
                return null; // 不存在别人的图片，就返回空
            } else {
                /*for (WechatMediaDesc mediaDesc : unSelfMediaDescs) {
                    if (!alreadyExId.contains(mediaDesc.getId())) {
                        exchangeMediaDesc = mediaDesc;
                        break;
                    }
                }*/
                Random random = new Random();
                int randomCount = 0;
                while (true) {
                    randomCount++;
                    exchangeMediaDesc = unSelfMediaDescs.get(random.nextInt(unSelfMediaDescs.size()));
                    if (!alreadyExId.contains(exchangeMediaDesc.getId())
                            || randomCount >= unSelfMediaDescs.size()) {
                        break;
                    }
                }
            }
            if (exchangeMediaDesc == null) {
                return null;
            }
            lastMediaDesc.setExchangeId(exchangeMediaDesc.getId());
            wechatMediaDescMapper.updateById(lastMediaDesc);
        }
        return exchangeMediaDesc;
    }

    /**delete unfinished media*/
    public int delUnFinishedMedia(String openId) {
        try{
            List<WechatMediaDesc> unFinishedMediaDesc = findUnFinishedMedia(openId);
            System.out.println("unFinishedMediaDesc: " + unFinishedMediaDesc);
            if (unFinishedMediaDesc == null || unFinishedMediaDesc.isEmpty()) {
                return ErrorCode.ERROR_NO_UNFINISHED_PIC;
            }
            for (WechatMediaDesc mediaDesc : unFinishedMediaDesc) {
                wechatMediaDescMapper.deleteById(mediaDesc.getId());
            }
        } catch (Exception e) {
            System.out.println(e);
            return ErrorCode.ERROR_UNKNOWN;
        }
        return ErrorCode.ERROR_SUCCESS;
    }

    public WechatAllMediaDescResult getAllFinishedMediaDescs(int pageIndex) {
        int pageSize = 10;
        WechatAllMediaDescResult result = new WechatAllMediaDescResult();
        List<WechatMediaDesc> mediaDescs = wechatMediaDescMapper.findAll();
        List<WechatMediaDescManage> manages = new ArrayList<WechatMediaDescManage>();
        if (mediaDescs == null || mediaDescs.isEmpty()) {
            result.setDescManages(manages);
            result.setPageIndex(1);
            result.setPageNum(1);
            return result;
        }
        for (WechatMediaDesc desc : mediaDescs) {
            if (StringUtils.isEmpty(desc.getTime())
                    || StringUtils.isEmpty(desc.getLocation())
                    || StringUtils.isEmpty(desc.getStory())) {
                continue;
            }
            WechatMedia media = wechatMediaMapper.findByMediaId(desc.getMediaId());
            if (media == null) {
                continue;
            }
            WechatMediaDescManage manage = new WechatMediaDescManage();
            manage.setId(desc.getId());
            manage.setOpenId(desc.getOpenId());
            manage.setTime(desc.getTime());
            manage.setLocation(desc.getLocation());
            manage.setStory(desc.getStory());
            manage.setMediaId(desc.getMediaId());
            manage.setMediaUrl(media.getMediaUrl());
            manages.add(manage);
        }
        int pageNum = (manages.size() - 1) / pageSize + 1;
        if (pageIndex < pageNum) {
            result.setDescManages(manages.subList((pageIndex - 1) * pageSize, pageIndex * pageSize));
        } else {
            result.setDescManages(manages.subList((pageIndex - 1) * pageSize, manages.size()));
        }
        result.setPageIndex(pageIndex);
        result.setPageNum(pageNum);
        return result;
    }

    public void deleteDescById(int id) {
        wechatMediaDescMapper.deleteById(id);
    }
}
