package com.chen.soft.adapt;

/**
 * Created by chenchi_94 on 2015/9/19.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chen.soft.R;

import java.util.List;


public class ExpressionAdapter extends ArrayAdapter<String> {
    Context context;

    public ExpressionAdapter(Context context, int textViewResourceId,
                             List<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.row_expression,
                    null);
        }

        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.iv_expression);
        String filename = getItem(position);
        int resId = getContext().getResources().getIdentifier(filename,
                "mipmap", getContext().getPackageName());
        Log.d("info", "resId: "+ resId);
        imageView.setImageResource(resId);

        return convertView;
    }
}

