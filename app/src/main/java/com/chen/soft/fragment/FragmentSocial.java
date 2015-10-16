package com.chen.soft.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.soft.R;
import com.chen.soft.activity.AddMsgActivity;
import com.chen.soft.activity.LoginActivity;
import com.chen.soft.adapt.SocialMsgBean;
import com.chen.soft.adapt.SocialBeansAdapter;
import com.chen.soft.util.CommonUtil;
import com.chen.soft.util.LoginUtil;
import com.chen.soft.util.ParseUtil;
import com.chen.soft.util.ServerUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenchi_94 on 2015/9/13.
 */
public class FragmentSocial extends BaseFragment {

    private TextView hint;
    private PullToRefreshListView msgList = null;
    private SocialBeansAdapter adapter;
    private int offset = 0;

    private LinearLayout addMsg;

    private Date date;
    private static SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social, container,
                false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        msgList = (PullToRefreshListView) getView().findViewById(R.id.msgList);
        hint = (TextView)getView().findViewById(R.id.hint);
        addMsg = (LinearLayout)getView().findViewById(R.id.create);

        adapter = new SocialBeansAdapter(getView().getContext(),
                getMsgs(null));
        initPullToRefreshListView(msgList, adapter);


        addMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUtil.isLogin){
                    startActivity(new Intent(FragmentSocial.this.getActivity(), AddMsgActivity.class));
                }else{
                    startActivity(new Intent(FragmentSocial.this.getActivity(), LoginActivity.class));
                }
            }
        });

    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("info", "create list");
        msgList.setMode(PullToRefreshBase.Mode.BOTH);
        msgList.setOnRefreshListener(new MyOnRefreshListener2(msgList));
        msgList.setAdapter(adapter);
        date = new Date();
        loadData();
    }

    private void loadData(){
        hint.setText(R.string.data_loading);
        Ion.with(FragmentSocial.this)
                .load(String.format("%s?skip=%s", ServerUtil.getMsgsUrl, offset))
                .asJsonArray().setCallback(new FutureCallback<JsonArray>() {

            @Override
            public void onCompleted(Exception arg0, JsonArray arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    Log.d("info", arg0.toString());
                    Toast.makeText(getActivity(), "请检查网络",
                            Toast.LENGTH_SHORT).show();
                } else if (arg1.size() == 0) {
                    Toast.makeText(getActivity(), "已经没有数据啦",
                            Toast.LENGTH_SHORT).show();
                } else {
                    adapter.addNews(getMsgs(arg1));
                    offset += arg1.size();
                }
                adapter.notifyDataSetChanged();
                msgList.onRefreshComplete();
                hint.setText(R.string.data_loaded);
            }
        });
    }

    private void loadNewestData(){
        hint.setText(R.string.data_loading);
        String time = sfd.format(date);
        time = ParseUtil.ParseUrl(time);
        Ion.with(FragmentSocial.this)
                .load(String.format("%s?date=%s", ServerUtil.getNewestMsgsUrl, time))
                .asJsonArray().setCallback(new FutureCallback<JsonArray>() {

            @Override
            public void onCompleted(Exception arg0, JsonArray arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    Log.d("info", arg0.toString());
                    Toast.makeText(getActivity(), "请检查网络",
                            Toast.LENGTH_SHORT).show();
                } else if (arg1.size() == 0) {
                    Toast.makeText(getActivity(), "已经没有数据啦",
                            Toast.LENGTH_SHORT).show();
                } else {
                    date = new Date();
                    offset += arg1.size();
                    adapter.addFirstNews(getMsgs(arg1));
                }
                adapter.notifyDataSetChanged();
                msgList.onRefreshComplete();
                hint.setText(R.string.data_loaded);
            }
        });
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2<ListView> {

        private PullToRefreshListView mPtflv;

        public MyOnRefreshListener2(PullToRefreshListView ptflv) {
            this.mPtflv = ptflv;
        }

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 下拉刷新
            String label = DateUtils.formatDateTime(getView().getContext(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            loadNewestData();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 上拉加载
            // new GetNewsTask(mPtflv).execute();
            loadData();
        }

    }


    public ArrayList<SocialMsgBean> getMsgs(JsonArray res) {
        ArrayList<SocialMsgBean> ret = new ArrayList<SocialMsgBean>();
        if(res == null){
            return ret;
        }
        for (int i = 0; i < res.size(); i++) {
            JsonElement je = res.get(i);
            JsonObject jo = je.getAsJsonObject();
            Date date = ParseUtil.parseISODate(jo.get("time").getAsString());
            String[] times = date.toLocaleString().split(" ");
            SocialMsgBean msg = new SocialMsgBean(jo.get("_id").getAsString(),
                    jo.get("user").getAsJsonObject().get("_id").getAsString(),
                    jo.get("user").getAsJsonObject().get("name").getAsString(),
                    jo.get("user").getAsJsonObject().get("toupic").getAsString(),
                    jo.get("msg").getAsJsonObject().get("title").getAsString(),
                    jo.get("msg").getAsJsonObject().get("content").getAsString(),
                    times[0],
                    jo.get("upCount").getAsString(),
                    jo.get("ctCount").getAsString());
            ret.add(msg);
        }
        return ret;
}

}