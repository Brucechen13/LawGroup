package com.chen.soft.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chen.soft.R;
import com.chen.soft.adapt.SampleBean;
import com.chen.soft.user.User;
import com.chen.soft.util.LoginUtil;
import com.chen.soft.util.ServerUtil;
import com.chen.soft.util.UIShowUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chenchi_94 on 2015/10/17.
 */
public class AddSampleActivity  extends TitleActivity implements View.OnClickListener {

    private EditText title;
    private EditText content;

    @Override
    protected void onBackward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
    }

    @Override
    protected void onForward(View backwardView) {
        // TODO Auto-generated method stub
        super.onForward(backwardView);
        if(TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(content.getText())){
            UIShowUtil.toastMessage(AddSampleActivity.this, "标题或内容不能为空");
            return;
        }
        pushMsg();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_addsample);
        setTitle(R.string.add_msg);
        showBackwardView(R.string.button_backward, true);
        showForwardView(R.string.button_submit, true);

        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }

    private void pushMsg(){
        Log.d("info", "upload traffic msg");
        User user = LoginUtil.getUser(this);
        Log.d("info", "user: " + user.getUserName() + " " + user.getGender());
        // 创建帖子信息
        SampleBean post = new SampleBean();
        post.setTitle(title.getText().toString());
        post.setContent(content.getText().toString());
        //添加一对一关联
        post.setAuthor(user);
        post.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.d("info", "save lawSample success");
                UIShowUtil.toastMessage(AddSampleActivity.this, "添加成功");
                AddSampleActivity.this.finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(AddSampleActivity.this, "add LawExample Error"+ msg);
            }
        });
    }
}
