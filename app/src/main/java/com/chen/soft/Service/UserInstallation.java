package com.chen.soft.Service;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by chenc on 2015/12/27.
 */
public class UserInstallation extends BmobInstallation {
    /**
     * 用户id-这样可以将设备与用户之间进行绑定
     */
    private String uid;
    private BmobGeoPoint gpsAdd;

    public UserInstallation(Context context) {
        super(context);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
