package com.chen.soft.adapt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.activity.MsgDetailActivity;
import com.chen.soft.activity.SampleDetailActivity;

import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SampleBeansAdapter extends BaseAdapter {

    private List<SampleBean> data;
    private Context mContext;

    public SampleBeansAdapter(Context context, List<SampleBean> data){
        this.mContext = context;
        this.data = data;
    }

    public class ViewHolder {
        public TextView username;
        public TextView time;
        public TextView content;
        public LinearLayout content_ll;
        public TextView comment_num;
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
                    R.layout.item_sample, parent, false);
            holder.username = (TextView) convertView
                    .findViewById(R.id.author);
            holder.time = (TextView) convertView
                    .findViewById(R.id.time);
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);
            holder.comment_num = (TextView) convertView
                    .findViewById(R.id.comment_num);
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
//                Intent intent = new Intent(mContext, UserInfoView.class);
//                intent.putExtra("userid", data.get(position).getUserId());
//                mContext.startActivity(intent);
            }
        };
        View.OnClickListener infoDetailListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//              Log.d("info", "into");
                Intent intent = new Intent(mContext, SampleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sample", data.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        };

        SampleBean bean = data.get(position);
        holder.username.setText(bean.getAuthor().getUserName());
        holder.time.setText(bean.getUpdatedAt());
        holder.content.setText(bean.getTitle());
        holder.comment_num.setText(""+bean.getCmCount());
        holder.content_ll.setOnClickListener(infoDetailListener);


        holder.username.setOnClickListener(userInfoListener);

        return convertView;
    }

    public void addNews(List<SampleBean> addNews) {
        data.addAll(addNews);
    }

    public void addFirstNews(List<SampleBean> addNews) {
        data.addAll(0, addNews);
    }
}
