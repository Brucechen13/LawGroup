package com.chen.soft.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.adapt.CommentBean;
import com.chen.soft.adapt.CommentsBeanAdapter;
import com.chen.soft.adapt.SocialMsgBean;
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

/**
 * Created by chenchi_94 on 2015/10/13.
 */
public class MsgDetailActivity extends TitleActivity implements View.OnClickListener {

    private PullToRefreshListView commentsList = null;
    private CommentsBeanAdapter adapter;
    private SocialMsgBean msg;

    private int offset = 0;
    private Date date;
    private static SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private boolean start = true;

    private ImageView userimg_iv;
    private TextView username;
    private TextView time;
    private TextView content;
    private TextView area;
    private TextView good_num;
    private TextView comment_num;
    private ImageView comgood_iv;
    private ImageView good_img;
    private ImageView comment_img;
    private RelativeLayout comgoodhandle;

    private EditText text;


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

    private void initView(){
        setContentView(R.layout.activity_socialdetail);
        showBackwardView(R.string.button_backward, true);

        msg = (SocialMsgBean)getIntent().getParcelableExtra("msg");
        Log.d("info", "getMseeage:" + msg.getUserName());
        setTitle(msg.getTitle());

        username = (TextView) this
                .findViewById(R.id.username);
        userimg_iv = (ImageView) this
                .findViewById(R.id.user_img);
        time = (TextView) this
                .findViewById(R.id.time);
        content = (TextView) this
                .findViewById(R.id.content);
        area = (TextView) this
                .findViewById(R.id.area_tv);
        good_num = (TextView) this
                .findViewById(R.id.good_num);
        comment_num = (TextView) this
                .findViewById(R.id.comment_num);
        comgood_iv = (ImageView) this
                .findViewById(R.id.comgood_iv);
        good_img = (ImageView) this
                .findViewById(R.id.good_img);
        comment_img = (ImageView) this
                .findViewById(R.id.comment_img);
        comgoodhandle = (RelativeLayout)this
                .findViewById(R.id.comgoodhandle);

        this.findViewById(R.id.send).setOnClickListener(this);

        text=(EditText)this.findViewById(R.id.text);

        username.setText(msg.getUserName());
        time.setText(msg.getUpTime());
        content.setText(msg.getContent());
        good_num.setText(msg.getUpCount());
        comment_num.setText(msg.getCmCount());
        username.setText(msg.getUserName());
        Ion.with(this)
                .load(msg.getUserPic())
                .withBitmap()
                        //.placeholder(R.drawable.placeholder_image)
                .error(R.mipmap.tou)
                .intoImageView(userimg_iv).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception arg0, ImageView arg1) {
                // TODO Auto-generated method stub
                if(arg0 != null){
                    Log.d("info", arg0.toString());
                }
            }

        });

        this.comgood_iv.setOnClickListener(new View.OnClickListener()
        {
            Boolean flag = false;

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                if (!flag)
                {

                    comgoodhandle.setVisibility(View.VISIBLE);
                    flag = true;
                }
                else
                {
                    comgoodhandle.setVisibility(View.GONE);
                    flag = false;
                }

            }
        });

        this.good_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String num = msg.getUpCount();
                msg.setUpCount(String.valueOf(Integer.parseInt(num) + 1));
                MsgDetailActivity.this.good_num.setText(msg.getUpCount());
                MsgDetailActivity.this.comgoodhandle.setVisibility(View.VISIBLE);
                Ion.with(MsgDetailActivity.this)
                        .load(String.format("%s?msg=%s",ServerUtil.addUpMsgUrl,
                                msg.getId())).asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {

                            @Override
                            public void onCompleted(Exception arg0, JsonObject arg1) {
                                // TODO Auto-generated method stub
                                if(arg0!=null || !"true".equals(arg1.get("suc").getAsString())){
                                    Log.i("info", "failed");
                                }
                            }

                        });
            }
        });

        this.comment_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                UIShowUtil.toastMessage(MsgDetailActivity.this, "点击下方文本框输入评论");
            }
        });


        adapter = new CommentsBeanAdapter(this, getComments(null));
        commentsList = (PullToRefreshListView)findViewById(R.id.commentsList);
        initPullToRefreshListView(commentsList, adapter);
    }

    private void initPullToRefreshListView(PullToRefreshListView commentsList2,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        commentsList2.setMode(PullToRefreshBase.Mode.BOTH);
        commentsList2.setOnRefreshListener(new MyOnRefreshListener2(commentsList2));
        commentsList2.setAdapter(adapter);
        date = new Date();
        loadData();
    }

    private void loadData(){
        Ion.with(MsgDetailActivity.this)
                .load(String.format("%s?msg=%s&skip=%s", ServerUtil.getCommentsUrl, msg.getId(),
                        offset))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>(){

            @Override
            public void onCompleted(Exception arg0, JsonObject arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    Log.d("info", arg0.toString());
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "请检查网络");
                    commentsList.onRefreshComplete();
                    return;
                }
                JsonArray comments = arg1.get("comments").getAsJsonArray();
                if(!start && comments.size()==0){
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "已经没有数据啦");
                    commentsList.onRefreshComplete();
                    return;
                }
                Log.d("info", comments.toString());
                adapter.addNews(getComments(comments));
                adapter.notifyDataSetChanged();
                commentsList.onRefreshComplete();
                offset+=comments.size();
                start = false;
            }
        });
    }

    private void loadNewestData(){
        String time = sfd.format(date);
        time = ParseUtil.ParseUrl(time);
        Log.d("info", time);
        Ion.with(MsgDetailActivity.this)
                .load(String.format("%s?msg=%s&date=%s", ServerUtil.getCommentsUrl, msg.getId(),
                        time))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>(){

            @Override
            public void onCompleted(Exception arg0, JsonObject arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "请检查网络");
                    commentsList.onRefreshComplete();
                    return;
                }
                JsonArray comments = arg1.get("comments").getAsJsonArray();
                if(!start && comments.size()==0){
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "已经没有数据啦");
                    commentsList.onRefreshComplete();
                    return;
                }
                date = new Date();
                offset+=comments.size();
                adapter.addFirstNews(getComments(comments));
                adapter.notifyDataSetChanged();
                commentsList.onRefreshComplete();
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
            String label = DateUtils.formatDateTime(MsgDetailActivity.this,
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            loadNewestData();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 上拉加载
            //new GetNewsTask(mPtflv).execute();
            loadData();
        }

    }

    public ArrayList<CommentBean> getComments(JsonArray res) {
        ArrayList<CommentBean> ret = new ArrayList<CommentBean>();
        if(res == null){
            return ret;
        }
        CommentBean hm;
        for (int i = 0; i < res.size(); i++) {
            JsonElement je = res.get(i);
            JsonObject jo = je.getAsJsonObject();
            Date date = ParseUtil.parseISODate(jo.get("time").getAsString());
            Log.d("info", date.toLocaleString());
            hm=new CommentBean(jo.get("_id").getAsString(),jo.get("user").getAsString(),
                    jo.get("userName").getAsString(),
                    jo.get("content").getAsString(), date.toLocaleString());
            ret.add(hm);
        }
        return ret;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            //   Intent intent=new Intent(this, UpdateMsg.class);
            case R.id.send:
                if(!LoginUtil.Login(this))
                    return;
                String cText = text.getText().toString().trim();
                Log.d("info", cText);
                this.comment_num.setText(String.valueOf(Integer.parseInt(
                        msg.getCmCount())+1));
                Ion.with(MsgDetailActivity.this)
                        .load(String.format("%s?user=%s&msg=%s&content=%s",ServerUtil.addCommentUrl,
                                LoginUtil.user.getId(), msg.getId(), cText)).asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception arg0, JsonObject arg1) {
                                // TODO Auto-generated method stub
                                if (arg0 != null || !arg1.get("suc").getAsString().equals("true")) {
                                    UIShowUtil.toastMessage(MsgDetailActivity.this, "添加失败");
                                }else{
                                    UIShowUtil.toastMessage(MsgDetailActivity.this, "添加成功");
                                }
                                text.setText("");
                                text.setFocusable(false);

                            }
                        });
                break;
            default:
                break;
        }
    }
}
