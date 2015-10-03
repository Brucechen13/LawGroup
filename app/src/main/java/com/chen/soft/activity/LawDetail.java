package com.chen.soft.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.adapt.LawBean;

import org.w3c.dom.Text;

import java.io.InputStream;

/**
 * Created by chenchi_94 on 2015/10/3.
 */
public class LawDetail  extends TitleActivity implements View.OnClickListener {

    private LawBean law;

    private TextView lawText;

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
        law = (LawBean) getIntent().getParcelableExtra("law");

        setContentView(R.layout.activity_law);
        setTitle(law.getHanzi());
        showBackwardView(R.string.button_backward, true);

        lawText = (TextView)findViewById(R.id.lawText);

        loadData();
    }

    private void loadData(){
        try {
            String path = "laws/"+ law.getRoot() + "/" + law.getPinyin() +".txt";
            Log.d("info", path);
            InputStream is = getResources().getAssets().open(path);
            byte[] buff = new byte[is.available()];
            is.read(buff);
            lawText.setText(new String(buff, "gbk"));
        }catch (Exception e){
            Log.d("info", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }
}
