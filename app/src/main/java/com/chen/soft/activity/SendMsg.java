package com.chen.soft.activity;

import android.os.Bundle;
import android.os.Debug;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chen.soft.R;
import com.chen.soft.adapt.ExpressionAdapter;
import com.chen.soft.adapt.ExpressionPagerAdapter;
import com.chen.soft.widget.ExpandGridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchi_94 on 2015/8/31.
 */
public class SendMsg extends TitleActivity implements View.OnClickListener {

    private ImageView iv_emoticons_normal, img_right;
    private ImageView iv_emoticons_checked;
    private ViewPager expressionViewpager;
    private View more;
    private LinearLayout emojiIconContainer;

    private List<String> reslist;

    @Override
    protected void onBackward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
    }

    @Override
    protected void onForward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
        Log.d("info", "submit");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setUpView();
    }

    private void initView(){

        setContentView(R.layout.activity_sendmsg);
        setTitle(R.string.send_msg);
        showBackwardView(R.string.button_backward, true);
        showForwardView(R.string.button_submit, true);

        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        more = findViewById(R.id.more);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);

        // 表情list
        reslist = getExpressionRes(62);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
    }

    private void setUpView() {
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_emoticons_normal:
                // 点击显示表情框
                more.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.INVISIBLE);
                iv_emoticons_checked.setVisibility(View.VISIBLE);
                emojiIconContainer.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.iv_emoticons_checked:// 点击隐藏表情框
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**表情加载**/
    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 0; x <= getSum; x++) {
            String filename = "f_static_0" + x;

            reslist.add(filename);

        }
        return reslist;

    }
    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 21);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(21, reslist.size()));
        } else if (i == 3) {
            list.addAll(reslist.subList(42, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
            }
        });
        return view;
    }


        /**
         * 隐藏软键盘
         */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ;
               // manager.hideSoftInputFromWindow(getCurrentFocus()
              //          .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
