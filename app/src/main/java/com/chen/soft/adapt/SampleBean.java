package com.chen.soft.adapt;

import android.os.Parcel;
import android.os.Parcelable;

import com.chen.soft.user.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SampleBean extends BmobObject{

    private User author;
    private Integer cmCount;
    private String title;
    private String content;

    public SampleBean(){
        this.cmCount = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getCmCount() {
        return cmCount;
    }

    public void setCmCount(Integer cmCount) {
        this.cmCount = cmCount;
    }

//    @Override
//    public int describeContents() {
//        // TODO Auto-generated method stub
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        // TODO Auto-generated method stub
//        dest.writeParcelable(author, PARCELABLE_WRITE_RETURN_VALUE);
//        dest.writeInt(cmCount);
//        dest.writeString(title);
//        dest.writeString(content);
//    }
//
//    public static final Parcelable.Creator<SampleBean> CREATOR =
//            new Parcelable.Creator<SampleBean>() {
//
//                @Override
//                public SampleBean createFromParcel(Parcel source) {
//                    // TODO Auto-generated method stub
//                    return new SampleBean(source);
//                }
//
//                @Override
//                public SampleBean[] newArray(int size) {
//                    // TODO Auto-generated method stub
//                    return new SampleBean[size];
//                }
//
//            };
//
//    private SampleBean(Parcel in) {
//        author = in.readParcelable(User.class.getClassLoader());
//        cmCount = in.readInt();
//        title = in.readString();
//        content = in.readString();
//    }
}
