package com.chen.soft.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.test.UiThreadTest;
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
import com.chen.soft.util.UIShowUtil;
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
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by chenchi_94 on 2015/9/13.
 */
public class FragmentSocial extends BaseFragment {

    private PullToRefreshListView msgList = null;
    private SocialBeansAdapter adapter;
    private int offset = 0;

    private LinearLayout addMsg;

    private Date lastDate;
    private static SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private boolean dataLoaded;

    private BmobPushManager bmobPushManager;

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
        addMsg = (LinearLayout)getView().findViewById(R.id.create);

        adapter = new SocialBeansAdapter(getView().getContext(),
                new ArrayList<SocialMsgBean>());
        initPullToRefreshListView(msgList, adapter);



        bmobPushManager = new BmobPushManager(this.getActivity());
        addMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIShowUtil.toastMessage(FragmentSocial.this.getActivity(), "show toast");
                //bmobPushManager.pushMessageAll("这是给所有设备推送的一条消息。");
                if (LoginUtil.Login(FragmentSocial.this.getActivity())) {
                    startActivity(new Intent(FragmentSocial.this.getActivity(), AddMsgActivity.class));
                }
            }
        });

        dataLoaded = true;

    }

    private void initPullToRefreshListView(PullToRefreshListView msgList,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        Log.d("info", "create list");
        msgList.setMode(PullToRefreshBase.Mode.BOTH);
        msgList.setOnRefreshListener(new MyOnRefreshListener2(msgList));
        msgList.setAdapter(adapter);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataLoaded){
            dataLoaded = false;
        }else {
            loadNewestData();
        }
    }

    private void loadData(){
        BmobQuery<SocialMsgBean> query = new BmobQuery<SocialMsgBean>();
        query.setSkip(offset);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        lastDate = new Date();
        query.findObjects(this.getActivity(), new FindListener<SocialMsgBean>() {
            @Override
            public void onSuccess(List<SocialMsgBean> objects) {
                // TODO Auto-generated method stub
                adapter.addNews(objects);
                offset += objects.size();
                adapter.notifyDataSetChanged();
                msgList.onRefreshComplete();

                if (objects.size() > 0) {
                    offset += objects.size();
                } else if(offset != 0){
                    UIShowUtil.toastMessage(getActivity(), "没有数据啦");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(getActivity(), "查询失败:" + msg);
                msgList.onRefreshComplete();
            }
        });
    }

    private void loadNewestData(){
        BmobQuery<SocialMsgBean> query = new BmobQuery<SocialMsgBean>();
        query.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(lastDate));
        query.order("-updatedAt");
        query.include("author.userName");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(this.getActivity(), new FindListener<SocialMsgBean>() {
            @Override
            public void onSuccess(List<SocialMsgBean> objects) {
                // TODO Auto-generated method stub
                adapter.addNews(objects);
                offset += objects.size();
                adapter.notifyDataSetChanged();
                msgList.onRefreshComplete();
                if (objects.size() > 0) {
                    offset += objects.size();
                } else if(offset != 0){
                    UIShowUtil.toastMessage(getActivity(), "没有最新数据");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(getActivity(), "查询失败:" + msg);
                msgList.onRefreshComplete();
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

}