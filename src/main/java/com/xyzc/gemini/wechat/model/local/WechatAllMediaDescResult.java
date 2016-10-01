package com.xyzc.gemini.wechat.model.local;

import java.util.List;

/**
 * Created by xyzc on 2016/10/1.
 */
public class WechatAllMediaDescResult {
    private List<WechatMediaDescManage> descManages;
    private int pageIndex;
    private int pageNum;

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageNum() {
        return pageNum;
    }

    public List<WechatMediaDescManage> getDescManages() {
        return descManages;
    }

    public void setDescManages(List<WechatMediaDescManage> descManages) {
        this.descManages = descManages;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
