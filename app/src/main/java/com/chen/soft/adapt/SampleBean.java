package com.chen.soft.adapt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SampleBean  implements Parcelable {

    private String id;
    private String userId;
    private String author;
    private String time;
    private String cmCount;
    private String title;
    private String content;

    public SampleBean(String id, String userId, String author,  String title, String content, String time, String cmCount){
        super();
        this.content = content;
        this.author = author;
        this.time = time;
        this.cmCount = cmCount;
        this.id = id;
        this.userId = userId;
        this.title = title;
    }
    public SampleBean(){
        this.content = "aaa";
        this.author = "aaa";
        this.time = "aaa";
        this.cmCount = "aaa";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCmCount() {
        return cmCount;
    }

    public void setCmCount(String cmCount) {
        this.cmCount = cmCount;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(author);
        dest.writeString(time);
        dest.writeString(cmCount);
        dest.writeString(title);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<SampleBean> CREATOR =
            new Parcelable.Creator<SampleBean>() {

                @Override
                public SampleBean createFromParcel(Parcel source) {
                    // TODO Auto-generated method stub
                    return new SampleBean(source);
                }

                @Override
                public SampleBean[] newArray(int size) {
                    // TODO Auto-generated method stub
                    return new SampleBean[size];
                }

            };

    private SampleBean(Parcel in) {
        id = in.readString();
        userId = in.readString();
        author = in.readString();
        time = in.readString();
        cmCount = in.readString();
        title = in.readString();
        content = in.readString();
    }
}
