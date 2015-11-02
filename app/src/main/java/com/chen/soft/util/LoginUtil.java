package com.chen.soft.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chen.soft.activity.LoginActivity;
import com.chen.soft.user.User;

/**
 * 用户登录管理
 * Created by chenchi_94 on 2015/10/11.
 */
public class LoginUtil {

    //public static String userId;

    public static boolean isLogin;

    public static User user;

    public static boolean Login(Context context){
        if(isLogin)
            return true;
        context.startActivity(new Intent(context, LoginActivity.class));
        return false;
    }
}
