package com.chen.soft.adapt;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SampleBean {

    private String content;
    private String author;
    private String time;
    private String cmCount;

    public SampleBean(String content, String author, String time, String cmCount){
        super();
        this.content = content;
        this.author = author;
        this.time = time;
        this.cmCount = cmCount;
    }
    public SampleBean(){
        this.content = "aaa";
        this.author = "aaa";
        this.time = "aaa";
        this.cmCount = "aaa";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCmCount() {
        return cmCount;
    }

    public void setCmCount(String cmCount) {
        this.cmCount = cmCount;
    }
}
