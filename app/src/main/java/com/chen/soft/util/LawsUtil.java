package com.chen.soft.util;

import android.util.Log;

import com.chen.soft.adapt.LawBean;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by chenc on 2015/12/12.
 */
public class LawsUtil {

    private static List<LawBean> laws = new ArrayList<>();

    public static boolean SundaySubString(String str, String subStr){

        return false;
    }

    public static void addToLisy(LawBean bean){
        laws.add(bean);
    }

    public static List<LawBean> searchForString(String word){
        List<LawBean> rets = new ArrayList<>();
        for (LawBean bean:
             laws) {
            String str = bean.getHanzi();
            if(str.contains(word)){
                rets.add(bean);
            }
        }
        return rets;
    }

}
