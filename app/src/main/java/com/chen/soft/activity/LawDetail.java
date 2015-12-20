package com.chen.soft.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.soft.R;
import com.chen.soft.adapt.LawBean;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by chenchi_94 on 2015/10/3.
 */
public class LawDetail  extends TitleActivity implements View.OnClickListener {

    private LawBean law;

    private TextView lawText;

    private WebView webView;

    private String html;

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

        new GetNewsTask().execute();
    }

    private void loadData(){
//        String htm = "<html>\n" +
//                "<head>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "wuliu\n" +
//                "</body>\n" +
//                "</html>";
        //webView.loadData(htm, "text/html; charset=UTF-8", null);//loadDataWithBaseURL
        try {
            webView.loadData(html, "text/html", "utf-8");
        }catch (Exception e){
            Log.d("info", e.toString() + "load web error");
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }

    class GetNewsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = "laws/"+ law.getRoot() + "/" + law.getPinyin();
                Log.d("info", path);
                InputStream is = getResources().getAssets().open(path);
                byte[] buff = new byte[is.available()];
                is.read(buff);
                html = new String(buff, "gbk");
                return -1;
            }catch (Exception e){
                Log.d("info", e.toString());
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("info", result.toString());
            switch (result) {
                case -1:
                    Log.d("info","showLaws");
                    loadData();
                    break;
                case 0:
                    Toast.makeText(LawDetail.this, "搜索出错", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    }
}