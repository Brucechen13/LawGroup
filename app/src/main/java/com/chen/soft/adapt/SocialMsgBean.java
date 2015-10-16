package com.chen.soft.adapt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SocialMsgBean implements Parcelable{

    private String id;
    private String userId;
    private String userName;
    private String userPic;
    private String upTime;
    private String title;
    private String content;
    private String upCount;
    private String cmCount;

    public SocialMsgBean(String id, String userId, String userName, String userPic, String content, String title,
                         String time, String goodNum, String commentNum) {
        super();
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userPic = userPic;
        this.title = title;
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
        this.title = "000";
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

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpCount() {
        return upCount;
    }

    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPic);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(upTime);
        dest.writeString(upCount);
        dest.writeString(cmCount);
    }

    public static final Parcelable.Creator<SocialMsgBean> CREATOR =
            new Parcelable.Creator<SocialMsgBean>() {

                @Override
                public SocialMsgBean createFromParcel(Parcel source) {
                    // TODO Auto-generated method stub
                    return new SocialMsgBean(source);
                }

                @Override
                public SocialMsgBean[] newArray(int size) {
                    // TODO Auto-generated method stub
                    return new SocialMsgBean[size];
                }

            };

    private SocialMsgBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        userName = in.readString();
        userPic = in.readString();
        title = in.readString();
        content = in.readString();
        upTime = in.readString();
        upCount = in.readString();
        cmCount = in.readString();
    }
}
