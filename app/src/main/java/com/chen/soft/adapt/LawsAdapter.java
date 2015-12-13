package com.chen.soft.adapt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.activity.LawDetail;
import com.chen.soft.activity.LawsActivity;

import java.util.List;

/**
 * Created by chenchi_94 on 2015/10/2.
 */
public class LawsAdapter extends BaseAdapter {

    private List<LawBean> data;
    private Context mContext;

    public LawsAdapter(Context context, List<LawBean> list) {
        this.data = list;
        this.mContext = context;
    }

    public LawsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new LawsAdapter.ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_law, parent, false);
            holder.law = (TextView)convertView.findViewById(R.id.law);
            holder.content = (RelativeLayout)convertView.findViewById(R.id.content);
            convertView.setTag(holder);
            convertView.setId(position);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LawBean law = data.get(position);
        holder.law.setText(law.getHanzi());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LawBean law = data.get(position);
                if(law.getLawChilds().size() != 0) {
                    Bundle mBundle = new Bundle();
                    Intent intent = new Intent();
                    mBundle.putParcelable("law", law);
                    intent.setClass(mContext, LawsActivity.class);
                    intent.putExtras(mBundle);
                    mContext.startActivity(intent);
                }else{
                    //打开详细法律
                    Bundle mBundle = new Bundle();
                    Intent intent = new Intent();
                    mBundle.putParcelable("law", law);
                    intent.setClass(mContext, LawDetail.class);
                    intent.putExtras(mBundle);
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView law;
        public RelativeLayout content;
    }
}
