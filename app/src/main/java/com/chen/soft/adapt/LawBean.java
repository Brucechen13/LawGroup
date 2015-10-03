package com.chen.soft.adapt;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/2.
 */
public class LawBean implements Parcelable {

    private String hanzi;

    private String pinyin;

    private String root;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    private List<LawBean> lawChilds  = new ArrayList();;

    public LawBean() {
        super();
    }

    public LawBean(String pinyin, String hanzi) {
        super();
        this.pinyin = pinyin;
        this.hanzi = hanzi;
    }

    public LawBean(String pinyin, String hanzi, String root) {
        super();
        this.pinyin = pinyin;
        this.hanzi = hanzi;
        this.root = root;
    }

    public List<LawBean> getLawChilds() {
        return lawChilds;
    }

    public void setLawChilds(List<LawBean> lawChilds) {
        this.lawChilds = lawChilds;
    }

    public String getHanzi() {
        return hanzi;
    }

    public void setHanzi(String hanzi) {
        this.hanzi = hanzi;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(pinyin);
        dest.writeString(hanzi);
        dest.writeString(root);
        dest.writeTypedList(lawChilds);
    }

    public static final Parcelable.Creator<LawBean> CREATOR =
            new Parcelable.Creator<LawBean>() {

                @Override
                public LawBean createFromParcel(Parcel source) {
                    // TODO Auto-generated method stub
                    return new LawBean(source);
                }

                @Override
                public LawBean[] newArray(int size) {
                    // TODO Auto-generated method stub
                    return new LawBean[size];
                }

            };

    private LawBean(Parcel in) {
        pinyin = in.readString();
        hanzi = in.readString();
        root = in.readString();
        in.readTypedList(lawChilds, LawBean.CREATOR);
    }

}
