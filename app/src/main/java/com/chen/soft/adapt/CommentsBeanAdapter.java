package com.chen.soft.adapt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.soft.R;

import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/13.
 */
public class CommentsBeanAdapter extends BaseAdapter {

    private List<CommentBean> data;
    private Context mContext;

    public CommentsBeanAdapter(Context context, List<CommentBean> list) {
        this.data = list;
        this.mContext = context;
    }

    public class ViewHolder {
        public TextView username;
        public TextView time;
        public TextView content;
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
                    R.layout.item_comment, parent, false);
            holder.username = (TextView) convertView
                    .findViewById(R.id.username);
            holder.time = (TextView) convertView
                    .findViewById(R.id.time);
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);

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

        CommentBean bean = data.get(position);
        holder.username.setText(bean.getUser().getUserName());
        holder.time.setText(bean.getUpdatedAt());
        holder.content.setText(bean.getContent());


        holder.username.setOnClickListener(userInfoListener);

        return convertView;
    }


    public void addNews(List<CommentBean> addNews) {
        data.addAll(addNews);
    }

    public int addFirstNews(List<CommentBean> addNews) {
        int offset = 0;
        for (CommentBean bean:addNews
             ) {
            Log.d("info", "offset: " + offset + " " + bean.getContent());
            if(data.contains(bean)){
                break;
            }
            data.add(offset, bean);
            offset++;
        }
        return offset;
    }
}
