package com.chen.soft.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.soft.MainActivity;
import com.chen.soft.R;
import com.chen.soft.user.User;
import com.chen.soft.util.LoginUtil;
import com.chen.soft.util.ServerUtil;
import com.chen.soft.util.StatusUtil;
import com.chen.soft.util.UIShowUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.async.future.FutureCallback;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by chenchi_94 on 2015/10/11.
 */
public class LoginActivity extends TitleActivity implements View.OnClickListener {

    private Button qqLoginButton;
    private TextView dataTips;

    static Tencent mTencent;
    String APP_ID = "1104503342";
    private User user;
    private View view;

    @Override
    protected void onBackward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_login);
        setTitle(R.string.login);
        showBackwardView(R.string.button_backward, true);

        UIShowUtil.toastMessage(this, "登录ing...");
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());


        // TODO Auto-generated method stub
        user = BmobUser.getCurrentUser(this, User.class);
        if(user!=null){
            Log.d("info", user.getGender() + " " + user.getUserName());
            loginActivity();
        }else{
            qqAuthorize();
            Log.d("info", "QQ LOGIN");
        }
    }

    private void loginActivity(){
        LoginUtil.isLogin = true;
        LoginUtil.user = user;
        LoginActivity.this.finish();
        Bundle bundle = new Bundle();
        bundle.putInt("curView", 3);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        LoginActivity.this.startActivity(intent);
    }

    private void qqAuthorize(){
        if(mTencent==null){
            mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
        }
        mTencent.logout(this);
        mTencent.login(this, "all", new IUiListener() {

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        mTencent.setAccessToken(token, expires);
                        mTencent.setOpenId(openId);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, token, expires, openId);
                        loginWithAuth(authInfo);
                    } catch (JSONException e) {
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(LoginActivity.this, "QQ授权出错：" + arg0.errorCode + "--" + arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(LoginActivity.this, "取消qq授权");
            }
        });
    }

    /**
     * @method loginWithAuth
     * @param context
     * @param authInfo
     * @return void
     * @exception
     */
    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo){
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.d("info", authInfo.getSnsType() + "登陆成功返回:" + userAuth);
                User bmobUser = BmobUser.getCurrentUser(LoginActivity.this, User.class);
                if(bmobUser.getUserName() == null) {
                    updateUserInfo(bmobUser);
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("json", userAuth.toString());
                intent.putExtra("from", authInfo.getSnsType());
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(LoginActivity.this, "第三方登陆失败：" + msg);
            }

        });
    }

    private void updateUserInfo(final BmobUser bmobUser) {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject)response;
                    try {
                        if(json.has("figureurl")){
                            user.setPic(json.getString("figureurl_qq_2"));
                            Log.d("info", "login: " + json.getString("figureurl_qq_2"));
                        }
                        if (json.has("nickname")) {
                            user.setUserName(json.getString("nickname"));
                            Log.d("info", "login: " + json.getString("nickname"));
                        }
                        if (json.has("gender")) {
                            user.setGender(json.getString("gender"));
                            Log.d("info", "login: " + json.getString("gender"));
                        }
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    user.update(LoginActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.d("info", "更新用户信息成功:");
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            UIShowUtil.toastMessage(LoginActivity.this, "更新用户信息失败:" + msg);
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            };
            UserInfo mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
            Log.d("info","nickname");
        } else {
            Log.d("info","error mTencent");
        }
    }
}