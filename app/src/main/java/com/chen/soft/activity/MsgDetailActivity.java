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
import android.widget.Toast;

import com.chen.soft.R;
import com.chen.soft.adapt.CommentBean;
import com.chen.soft.adapt.CommentsBeanAdapter;
import com.chen.soft.adapt.SocialMsgBean;
import com.chen.soft.user.User;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by chenchi_94 on 2015/10/13.
 */
public class MsgDetailActivity extends TitleActivity implements View.OnClickListener {

    private PullToRefreshListView commentsList = null;
    private CommentsBeanAdapter adapter;
    private SocialMsgBean msg;

    private int offset = 0;
    private Date lastDate;
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

        msg = (SocialMsgBean)getIntent().getSerializableExtra("msg");
        Log.d("info", "getMseeage:" + msg.getAuthor().getUserName());
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

        username.setText(msg.getAuthor().getUserName());
        time.setText(msg.getUpdatedAt());
        content.setText(msg.getContent());
        good_num.setText("" + msg.getUpCount());
        comment_num.setText(""+msg.getCmCount());
        Ion.with(this)
                .load(msg.getAuthor().getPic())
                .withBitmap()
                        //.placeholder(R.drawable.placeholder_image)
                .error(R.mipmap.tou)
                .intoImageView(userimg_iv).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception arg0, ImageView arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
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
                Integer num = msg.getUpCount();
                msg.setUpCount(num + 1);
                MsgDetailActivity.this.good_num.setText(""+msg.getUpCount());
                MsgDetailActivity.this.comgoodhandle.setVisibility(View.INVISIBLE);
                Log.d("info", "msg: " + msg.getObjectId());
                msg.increment("upCount");
                msg.update(MsgDetailActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("info", "up successfule");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("info", "up fail");
                        UIShowUtil.toastMessage(MsgDetailActivity.this, "Up Msg Error" + msg);
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


        adapter = new CommentsBeanAdapter(this, new ArrayList<CommentBean>());
        commentsList = (PullToRefreshListView)findViewById(R.id.commentsList);
        initPullToRefreshListView(commentsList, adapter);
    }

    private void initPullToRefreshListView(PullToRefreshListView commentsList2,
                                           BaseAdapter adapter) {
        // TODO Auto-generated method stub
        commentsList2.setMode(PullToRefreshBase.Mode.BOTH);
        commentsList2.setOnRefreshListener(new MyOnRefreshListener2(commentsList2));
        commentsList2.setAdapter(adapter);
        loadData();
    }

    private void loadData(){
        BmobQuery<CommentBean> query = new BmobQuery<CommentBean>();
        query.setSkip(offset);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        lastDate = new Date();
        query.findObjects(this, new FindListener<CommentBean>() {
            @Override
            public void onSuccess(List<CommentBean> objects) {
                // TODO Auto-generated method stub
                adapter.addNews(objects);
                offset += objects.size();
                adapter.notifyDataSetChanged();
                commentsList.onRefreshComplete();

                if (objects.size() > 0) {
                    offset += objects.size();
                } else {
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "没有数据啦");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(MsgDetailActivity.this, "查询失败:" + msg);
                commentsList.onRefreshComplete();
            }
        });
    }

    private void loadNewestData(){
        BmobQuery<CommentBean> query = new BmobQuery<CommentBean>();
        query.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(lastDate));
        query.order("-updatedAt");
        query.include("author.userName");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(this, new FindListener<CommentBean>() {
            @Override
            public void onSuccess(List<CommentBean> objects) {
                // TODO Auto-generated method stub
                adapter.addNews(objects);
                offset += objects.size();
                adapter.notifyDataSetChanged();
                commentsList.onRefreshComplete();
                if (objects.size() > 0) {
                    offset += objects.size();
                } else {
                    UIShowUtil.toastMessage(MsgDetailActivity.this, "没有最新数据");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                UIShowUtil.toastMessage(MsgDetailActivity.this, "查询失败:" + msg);
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
                msg.setCmCount(msg.getCmCount()+1);
                this.comment_num.setText("" + msg.getCmCount());
                User user = LoginUtil.getUser(this);
                Log.d("info", "user: " + user.getUserName() + " " + user.getGender());
                // 创建帖子信息
                CommentBean post = new CommentBean();
                post.setContent(cText);
                post.setUser(user);
                post.setMsg(msg);
                post.save(this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.d("info", "save lawSample success");
                        UIShowUtil.toastMessage(MsgDetailActivity.this, "添加成功");
                        loadNewestData();

                        msg.increment("cmCount");
                        msg.update(MsgDetailActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("info", "add Cm successful");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                UIShowUtil.toastMessage(MsgDetailActivity.this, "add MsgComment Error" + msg);
                                Log.d("info", "Error:" + msg);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        UIShowUtil.toastMessage(MsgDetailActivity.this, "add MsgComment Error" + msg);
                        Log.d("info", "Error:" + msg);
                    }
                });
                break;
            default:
                break;
        }
    }
}
