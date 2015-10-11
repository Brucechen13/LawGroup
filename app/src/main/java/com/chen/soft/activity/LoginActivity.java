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

/**
 * Created by chenchi_94 on 2015/10/11.
 */
public class LoginActivity extends TitleActivity implements View.OnClickListener {

    private Button qqLoginButton;
    private TextView dataTips;

    static Tencent mTencent;
    String APP_ID = "1104503342";
    private UserInfo mInfo;
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

        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());


        // TODO Auto-generated method stub
        qqLoginButton = (Button)this.findViewById(R.id.login);
        qqLoginButton.setOnClickListener(this);
        dataTips = (TextView) findViewById(R.id.dataloading); // 显示1.5秒钟后自动消失
        user = new User();
        SharedPreferences preferences = getSharedPreferences(StatusUtil.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        if(preferences.getString("url", null)!=null){
            ServerUtil.url = preferences.getString("url", null);
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("url", ServerUtil.url);
            editor.commit();
        }
        if(preferences.getBoolean("account", false) &&
                (preferences.getString("qq", null)!=null)){//如果之前已经成功登陆
            validLogin(preferences.getString("qq", null));
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId() == R.id.login){
            onClickLogin();
        }
    }
    private void validLogin(String qq){
        Log.d("info", qq);
        dataTips.setVisibility(View.VISIBLE);

        Ion.with(LoginActivity.this)
                .load(String.format("%s?qq=%s",ServerUtil.loginUrl, qq)).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // TODO Auto-generated method stub
                        if (e != null) {
                            Log.d("info",e.toString());
                            dataTips.setText("登录失败："+e.toString());
                            return;
                        }
                        Log.d("info","login:"+result);
                        if(result.has("suc")){
                            dataTips.setText(R.string.login_success);
                            updateUserInfo();
                        }else{
                            dataTips.setText(R.string.login_success);
                            user.parseJson(result);
                            loginActivity();
                        }
                    }

                });
    }

    private void loginActivity(){
        LoginActivity.this.finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("user", user);
//        intent.putExtras(bundle);
        LoginUtil.isLogin = true;
        LoginUtil.user = user;
        LoginActivity.this.startActivity(intent);
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            Log.d("info", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            mTencent.logout(this);
            updateUserInfo();
            //updateLoginButton();
        }
    }

    private void updateUserInfo() {
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
                            Log.d("info",json.getString("figureurl_qq_2"));
                        }
                        if (json.has("nickname")) {
                            user.setUserName(json.getString("nickname"));
                            Log.d("traffic",json.getString("nickname"));
                        }
                        if (json.has("gender")) {
                            user.setGender(json.getString("gender"));
                            Log.d("info",json.getString("gender"));
                        }

                        Ion.with(LoginActivity.this)
                                .load(String.format("%s?qq=%s&name=%s&pic=%s&gender=%s",ServerUtil.newUerUrl, user.getId(), user.getUserName(), user.getPic(), user.getGender())).asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {

                                    @Override
                                    public void onCompleted(Exception e,
                                                            JsonObject result) {
                                        // TODO Auto-generated method stub
                                        if (e != null) {
                                            Log.d("traffic",e.toString());
                                            return;
                                        }
                                        String suc = result.get("suc").getAsString();
                                        if(suc.equals("true")){
                                            loginActivity();
                                        }else{
                                            Log.d("traffic",suc);
                                            dataTips.setText("登录失败,请联系应用开发者");
                                        }
                                    }
                                });

                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
            Log.d("info","nickname");
        } else {
            Log.d("traffic","error mTencent");
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("info", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            validLogin(user.getId());
        }
    };



    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                UIShowUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                UIShowUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            String openId;
            try {
                openId = jsonResponse.getString(Constants.PARAM_OPEN_ID);
                user.setId(openId);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Util.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
            // handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            UIShowUtil.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            UIShowUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            UIShowUtil.toastMessage(LoginActivity.this, "onCancel: ");
            UIShowUtil.dismissDialog();
        }
    }
}