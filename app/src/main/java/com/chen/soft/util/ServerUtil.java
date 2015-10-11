package com.chen.soft.util;

/**
 * 存储服务器的访问地址
 * Created by chenchi_94 on 2015/10/11.
 */
public class ServerUtil {

    public static String url = "http://192.168.1.109:3000/api";
    public static String loginUrl = url+"/login";
    public static String newUerUrl = url+"/newuser";
    public static String updateUerUrl = url+"/updateinfo";
    public static String addMsgUrl = url+"/addmsg";
    public static String getMsgsUrl = url+"/getmsgs";
    public static String getNewestMsgsUrl = url+"/getrecentmsgs";
    public static String addCommentUrl = url+"/addcomment";
    public static String addUpMsgUrl = url+"/addUpMsg";
    public static String getCommentsUrl = url+"/getcomments";
}