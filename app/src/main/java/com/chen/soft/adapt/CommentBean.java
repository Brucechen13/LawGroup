package com.chen.soft.adapt;

import com.chen.soft.user.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenchi_94 on 2015/10/13.
 * 评论类，社交评论和案例评论共有
 */
public class CommentBean extends BmobObject{

    private User user;
    private SocialMsgBean msg;
    private SampleBean sample;
    private String content;

    public SocialMsgBean getMsg() {
        return msg;
    }

    public void setMsg(SocialMsgBean msg) {
        this.msg = msg;
    }

    public SampleBean getSample() {
        return sample;
    }

    public void setSample(SampleBean sample) {
        this.sample = sample;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
