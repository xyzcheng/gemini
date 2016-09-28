package com.xyzc.gemini.wechat.model.local;

/**
 * Created by xyzc on 2016/9/28.
 */
public class WechatUser {
    private int id;
    private String openId;
    private String wechatId;
    private String wechatNick;
    private int sex;
    private int age;
    private String city;
    private String school;

    public String getOpenId() {
        return openId;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getSex() {
        return sex;
    }

    public String getCity() {
        return city;
    }

    public String getSchool() {
        return school;
    }

    public String getWechatId() {
        return wechatId;
    }

    public String getWechatNick() {
        return wechatNick;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public void setWechatNick(String wechatNick) {
        this.wechatNick = wechatNick;
    }
}
