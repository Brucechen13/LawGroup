package com.chen.soft.adapt;

import android.os.Parcel;
import android.os.Parcelable;

import com.chen.soft.user.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SocialMsgBean extends BmobObject{

    private User author;
    private String title;
    private String content;
    private Integer upCount;
    private Integer cmCount;

    public SocialMsgBean(){
        super();
        this.upCount = 0;
        this.cmCount = 0;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpCount() {
        return upCount;
    }

    public void setUpCount(Integer upCount) {
        this.upCount = upCount;
    }

    public Integer getCmCount() {
        return cmCount;
    }

    public void setCmCount(Integer cmCount) {
        this.cmCount = cmCount;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(author, PARCELABLE_WRITE_RETURN_VALUE);
//        dest.writeString(title);
//        dest.writeString(content);
////        dest.writeString(objectId);
////        dest.writeString(createdAt);
////        dest.writeString(updatedAt);
//        dest.writeInt(upCount);
//        dest.writeInt(cmCount);
//    }
//
//    public static final Parcelable.Creator<SocialMsgBean> CREATOR =
//            new Parcelable.Creator<SocialMsgBean>() {
//
//                @Override
//                public SocialMsgBean createFromParcel(Parcel source) {
//                    // TODO Auto-generated method stub
//                    return new SocialMsgBean(source);
//                }
//
//                @Override
//                public SocialMsgBean[] newArray(int size) {
//                    // TODO Auto-generated method stub
//                    return new SocialMsgBean[size];
//                }
//
//            };
//
//    private SocialMsgBean(Parcel in) {
//        author = in.readParcelable(User.class.getClassLoader());
//        title = in.readString();
//        content = in.readString();
//        upCount = in.readInt();
//        cmCount = in.readInt();
//    }
}
