package com.chen.soft.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

import cn.bmob.v3.BmobUser;

/**
 * Created by chenchi_94 on 2015/10/11.
 */
public class User extends BmobUser implements Parcelable {

    private String id;
    private String pic;
    private String userName;
    private String gender;
    private String signature;
    private int score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void parseJson(JsonObject result){
        if(result.has("_id")){
            this.setId(result.get("_id").getAsString());
        }
        if(result.has("name")){
            this.setUserName(result.get("name").getAsString());
        }
        if(result.has("toupic")){
            this.setPic(result.get("toupic").getAsString());
        }
        if(result.has("signature")){
            this.setSignature(result.get("signature").getAsString());
        }
        if(result.has("gender")){
            this.setGender(result.get("gender").getAsString());
        }
        if(result.has("score")) {
            this.setScore(Integer.parseInt(result.get("score").getAsString()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pic);
        dest.writeString(userName);
        dest.writeString(gender);
        dest.writeString(signature);
        dest.writeInt(score);
    }

    public static final Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>() {

                @Override
                public User createFromParcel(Parcel source) {
                    // TODO Auto-generated method stub
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    // TODO Auto-generated method stub
                    return new User[size];
                }
            };

    private User(Parcel in) {
        id = in.readString();
        pic = in.readString();
        userName = in.readString();
        gender = in.readString();
        signature = in.readString();
        score = in.readInt();
    }

    public User(){

    }
}
