package com.chen.soft.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.soft.R;
import com.chen.soft.adapt.LawBean;
import com.chen.soft.adapt.LawsAdapter;
import com.chen.soft.util.LawsUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenc on 2015/12/12.
 */
public class LawSearch   extends TitleActivity implements View.OnClickListener{

    private EditText editText;
    private PullToRefreshListView msgList = null;
    private LawsAdapter adapter;

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
        setContentView(R.layout.activity_lawsearch);
        setTitle(R.string.search_law);
        showBackwardView(R.string.button_backward, true);
        findViewById(R.id.search).setOnClickListener(this);
        msgList =  (PullToRefreshListView)findViewById(R.id.lawList);
        editText = (EditText)findViewById(R.id.edit);
        List<LawBean> laws = new ArrayList<>();
        adapter = new LawsAdapter(this, laws);
        initPullToRefreshListView(msgList, adapter);
    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("info", "create list");
        msgList.setMode(PullToRefreshBase.Mode.DISABLED);
        msgList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch(v.getId()){
            case R.id.search:
                new GetNewsTask().execute();
                break;
            default:
                break;
        }
    }

    void searchString(){
        Log.d("info", editText.getText().toString().trim());
        List<LawBean> laws = LawsUtil.searchForString(editText.getText().toString().trim());
        adapter = new LawsAdapter(this,
                laws);
        Log.d("info", "laws:" + laws.size());
    }

    void showLaws(){
        initPullToRefreshListView(msgList, adapter);
    }

    class GetNewsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            searchString();
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("info",result.toString());
            switch (result) {
                case -1:
                    Log.d("info","showLaws");
                    showLaws();
                    break;
                case 0:
                    Toast.makeText(LawSearch.this, "搜索出错", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    }
}
