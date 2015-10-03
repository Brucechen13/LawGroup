package com.chen.soft.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;

import com.chen.soft.R;
import com.chen.soft.adapt.LawBean;
import com.chen.soft.adapt.LawsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/3.
 */
public class LawsActivity extends TitleActivity implements View.OnClickListener {

    private List<LawBean> laws;
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
        LawBean law = (LawBean)getIntent().getParcelableExtra("law");
        laws = law.getLawChilds();

        setContentView(R.layout.activity_laws);
        setTitle(law.getHanzi());
        showBackwardView(R.string.button_backward, true);

        if(laws == null){
            Log.d("info", "NULL"+law.getHanzi());
            return;
        }

        msgList = (PullToRefreshListView) this.findViewById(R.id.lawList);
        adapter = new LawsAdapter(this,
                laws);
        initPullToRefreshListView(msgList, adapter);
    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("traffic", "create list");
        msgList.setMode(PullToRefreshBase.Mode.BOTH);
        msgList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }
}
