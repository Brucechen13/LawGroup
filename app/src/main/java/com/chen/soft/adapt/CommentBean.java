package com.chen.soft.adapt;

/**
 * Created by chenchi_94 on 2015/10/13.
 */
public class CommentBean {

    private String id;
    private String userId;
    private String userName;
    private String content;
    private String time;

    public CommentBean(String id, String userId, String userName, String content, String time){
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.time = time;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentBean)) return false;

        CommentBean that = (CommentBean) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
