package com.chen.soft.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.adapt.SampleBean;
import com.chen.soft.adapt.SampleBeansAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenchi_94 on 2015/9/19.
 */
public class FragmentSample extends BaseFragment{

    private TextView hint;
    private PullToRefreshListView msgList = null;
    private SampleBeansAdapter adapter;
    private int offset = 0;

    private Date date;
    private static SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container,
                false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        msgList = (PullToRefreshListView) getView().findViewById(R.id.msgList);

        adapter = new SampleBeansAdapter(getView().getContext(),
                getMsgs(null));
        initPullToRefreshListView(msgList, adapter);

    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("traffic", "create list");
        msgList.setMode(PullToRefreshBase.Mode.BOTH);
        //msgList.setOnRefreshListener(new MyOnRefreshListener2(msgList));
        msgList.setAdapter(adapter);
        date = new Date();
    }

    public ArrayList<SampleBean> getMsgs(Object res){
        ArrayList<SampleBean> ret = new ArrayList<SampleBean>();
        for (int i = 0; i < 10; i++) {
            SampleBean msg = new SampleBean();
            ret.add(msg);
        }
        return ret;

    }
}