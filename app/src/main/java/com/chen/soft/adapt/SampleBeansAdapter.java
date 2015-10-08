package com.chen.soft.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.soft.R;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
//                Intent intent = new Intent(mContext, TraffficMsgDetail.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("msg", data.get(position));
//                Log.d("traffic", "push msg:" + data.get(position).getUserName());
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
            }
        };

        SampleBean bean = data.get(position);
        holder.username.setText(bean.getAuthor());
        holder.time.setText(bean.getTime());
        holder.content.setText(bean.getContent());
        holder.comment_num.setText(bean.getCmCount());


        holder.username.setOnClickListener(userInfoListener);

        return convertView;
    }
}
