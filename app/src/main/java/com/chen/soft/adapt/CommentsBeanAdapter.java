package com.chen.soft.adapt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
