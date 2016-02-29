package com.chen.soft.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chen.soft.activity.LoginActivity;
import com.chen.soft.user.User;

import cn.bmob.v3.BmobUser;

/**
 * 用户登录管理
 * Created by chenchi_94 on 2015/10/11.
 */
public class LoginUtil {

    //public static String userId;

    public static boolean isLogin = false;

    public static User user = null;

    public static User getUser(Context context){
        if(user == null){
            user = BmobUser.getCurrentUser(context, User.class);
        }
        return user;
    }

    public static boolean Login(Context context){
        if(isLogin && user!=null)
            return true;
        context.startActivity(new Intent(context, LoginActivity.class));
        return false;
    }

    public static void loginOut(Context context){
        BmobUser.logOut(context);   //清除缓存用户对象
    }
}
