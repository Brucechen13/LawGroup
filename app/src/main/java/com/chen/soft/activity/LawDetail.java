package com.chen.soft.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

    private WebView webView;

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

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        loadData();
    }

    private void loadData(){
        try {
            String path = "laws/"+ law.getRoot() + "/" + law.getPinyin();
            Log.d("info", path);
            InputStream is = getResources().getAssets().open(path);
            byte[] buff = new byte[is.available()];
            is.read(buff);
            String html = new String(buff, "gbk");
            String htm = "<Html><head></head><body>haha</body></html>";
            Log.d("info", "start" + html.length());
            webView.loadData(htm, "text/html; charset=UTF-8", null);//loadDataWithBaseURL
            Log.d("info", "EMD");
            //lawText.setText(html);
        }catch (Exception e){
            Log.d("info", "error: " + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }
}
