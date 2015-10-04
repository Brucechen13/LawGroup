package com.chen.soft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.soft.R;

/**
 * Created by chenchi_94 on 2015/10/4.
 */
public class UserInfoActivity extends TitleActivity implements View.OnClickListener {

    private EditText info_et;
    private int num;

    @Override
    protected void onBackward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
    }

    @Override
    protected void onForward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
        Log.d("info", "submit");
        String resultContent = info_et.getText().toString().trim();
        int length = resultContent.length();

        if (length > num) {
            Toast.makeText(getApplicationContext(), "字数超过限制字数",
                    Toast.LENGTH_LONG);
            return;
        }

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultContent);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        Log.d("info", "set value");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_userinfo);
        showBackwardView(R.string.button_backward, true);
        showForwardView(R.string.button_submit, true);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String mimecontent = bundle.getString("mimecontent");
        String titleString = bundle.getString("title");
        num = bundle.getInt("num");
        setTitle(titleString);
        info_et = (EditText) findViewById(R.id.editText);
        info_et.setText(mimecontent);
    }
}
