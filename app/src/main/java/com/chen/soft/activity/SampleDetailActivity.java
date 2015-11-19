package com.chen.soft.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.adapt.SampleBean;

/**
 * Created by chenc on 2015/11/19.
 */
public class SampleDetailActivity extends TitleActivity implements View.OnClickListener  {

    private SampleBean sample;
    private TextView content;

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
        setContentView(R.layout.activity_sampledetail);
        showBackwardView(R.string.button_backward, true);
        sample = (SampleBean)getIntent().getParcelableExtra("sample");

        setTitle(sample.getTitle());
        content = (TextView)this
                .findViewById(R.id.content);
        content.setText(sample.getContent());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }
}
