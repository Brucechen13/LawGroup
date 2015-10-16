package com.chen.soft.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chen.soft.R;
import com.chen.soft.util.LoginUtil;
import com.chen.soft.util.ServerUtil;
import com.chen.soft.util.UIShowUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by chenchi_94 on 2015/10/12.
 */
public class AddMsgActivity  extends TitleActivity implements View.OnClickListener {

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
            UIShowUtil.toastMessage(AddMsgActivity.this, "标题或内容不能为空");
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

        setContentView(R.layout.activity_addmsg);
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
        Log.i("info", "upload traffic msg");
        JsonObject json = new JsonObject();
        json.addProperty("qq", LoginUtil.user.getId());
        json.addProperty("title", title.getText().toString());
        json.addProperty("content", content.getText().toString());

        Ion.with(this)
                .load(ServerUtil.addMsgUrl)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i("traffic", e.toString());
                            return;
                        }
                        UIShowUtil.toastMessage(AddMsgActivity.this, "上传成功");
                        AddMsgActivity.this.finish();
                    }
                });
    }
}
