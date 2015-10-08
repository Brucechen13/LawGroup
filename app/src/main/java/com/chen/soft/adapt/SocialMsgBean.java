package com.chen.soft.adapt;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SocialMsgBean {

    private String id;
    private String userId;
    private String userName;
    private String upTime;
    private String content;
    private String upCount;
    private String cmCount;

    public SocialMsgBean(String id, String userId, String userName, String content, String time,
                   String goodNum, String commentNum) {
        super();
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.upTime = time;
        this.upCount = goodNum;
        this.cmCount = commentNum;
    }

    public SocialMsgBean(){
        super();
        this.id = "000";
        this.userId = "000";
        this.userName = "000";
        this.content = "000";
        this.upTime = "000";
        this.upCount = "000";
        this.cmCount = "000";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCmCount() {
        return cmCount;
    }

    public void setCmCount(String cmCount) {
        this.cmCount = cmCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpCount() {
        return upCount;
    }

    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }
}
