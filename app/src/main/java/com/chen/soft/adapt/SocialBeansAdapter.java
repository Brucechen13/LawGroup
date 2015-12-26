package com.chen.soft.adapt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.activity.MsgDetailActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SocialBeansAdapter extends BaseAdapter {

    private List<SocialMsgBean> data;
    private Context mContext;

    public SocialBeansAdapter(Context context, List<SocialMsgBean> data){
        this.mContext = context;
        this.data = data;
    }

    public class ViewHolder {
        public ImageView userimg_iv;
        public TextView username;
        public TextView time;
        public TextView content;
        public LinearLayout content_ll;
        public TextView good_num;
        public TextView comment_num;
        public ImageView comgood_iv;
        public ImageView good_img;
        public ImageView comment_img;
        public RelativeLayout comgoodhandle;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_social, parent, false);
            holder.username = (TextView) convertView
                    .findViewById(R.id.username);
            holder.time = (TextView) convertView
                    .findViewById(R.id.time);
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);
            holder.good_num = (TextView) convertView
                    .findViewById(R.id.good_num);
            holder.comment_num = (TextView) convertView
                    .findViewById(R.id.comment_num);

            holder.userimg_iv = (ImageView) convertView
                    .findViewById(R.id.user_img);
            holder.comgood_iv = (ImageView) convertView
                    .findViewById(R.id.comgood_iv);
            holder.good_img = (ImageView) convertView
                    .findViewById(R.id.good_img);
            holder.comment_img = (ImageView) convertView
                    .findViewById(R.id.comment_img);
            holder.comgoodhandle = (RelativeLayout) convertView
                    .findViewById(R.id.comgoodhandle);
            holder.content_ll = (LinearLayout) convertView
                    .findViewById(R.id.content_ll);
            convertView.setTag(holder);
            convertView.setId(position);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        View.OnClickListener userInfoListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("info", "into user" + v.toString());
//                Intent intent = new Intent(mContext, UserInfoView.class);
//                intent.putExtra("userid", data.get(position).getUserId());
//                mContext.startActivity(intent);
            }
        };
        View.OnClickListener infoDetailListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("info", "into");
                Intent intent = new Intent(mContext, MsgDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("msg", data.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        };

        SocialMsgBean bean = data.get(position);
        holder.username.setText(bean.getAuthor().getUserName());
        holder.time.setText(bean.getUpdatedAt());
        holder.content.setText(bean.getTitle());
        holder.good_num.setText(""+bean.getUpCount());
        holder.comment_num.setText(""+bean.getCmCount());

        Ion.with(mContext)
                .load(bean.getAuthor().getPic())
                .withBitmap()
                        //.placeholder(R.drawable.placeholder_image)
                .error(R.mipmap.tou)
                .intoImageView(holder.userimg_iv).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception arg0, ImageView arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    Log.d("info", arg0.toString());
                }
            }

        });

        holder.content.setOnClickListener(userInfoListener);
        holder.userimg_iv.setOnClickListener(userInfoListener);
        holder.content_ll.setOnClickListener(infoDetailListener);


        holder.comgood_iv.setOnClickListener(new View.OnClickListener() {
            Boolean flag = false;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!flag) {

                    holder.comgoodhandle.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    holder.comgoodhandle.setVisibility(View.GONE);
                    flag = false;
                }

            }
        });
        return convertView;
    }

    public void addNews(List<SocialMsgBean> addNews) {
        data.addAll(addNews);
    }

    public void addFirstNews(List<SocialMsgBean> addNews) {
        data.addAll(0, addNews);
    }
}
