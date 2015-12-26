package com.chen.soft;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.chen.soft.fragment.FragmentCallback;
import com.chen.soft.fragment.FragmentSample;
import com.chen.soft.fragment.FragmentLaw;
import com.chen.soft.fragment.FragmentSocial;
import com.chen.soft.fragment.FragmentUser;
import com.chen.soft.fragment.FragmentUtils;
import com.chen.soft.util.LoginUtil;

import cn.bmob.v3.Bmob;


public class MainActivity extends FragmentActivity implements
        TabView.OnTabChangeListener, FragmentCallback {

    private Fragment mCurrentFragment;
    private FragmentManager fragmentManager;

    private TabView mTabView;
    private TextView mTitleTextView;

    /**
     * 上一次的状态
     */
    private int mPreviousTabIndex = 1;
    /**
     * 当前状态
     */
    private int mCurrentTabIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mCurrentTabIndex = 0;
        mPreviousTabIndex = 0;
        setupViews();

        Bmob.initialize(this, "eba5bc0bae50550f95b0a934d37b62e5");
    }

    private void setupViews() {
        // TODO Auto-generated method stub
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mTitleTextView.setText(R.string.text_tab_message);
        mTabView = (TabView) findViewById(R.id.view_tab);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(mCurrentTabIndex);
        //mCurrentFragment = new FragmentLaw();
        mCurrentFragment = FragmentUtils.replaceFragment(fragmentManager, R.id.layout_content,
                FragmentLaw.class, null, false);

    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {

        mCurrentFragment = FragmentUtils
                .switchFragment(fragmentManager, R.id.layout_content,
                        mCurrentFragment, newFragment, null, false);
    }

    private void replaceFragment(Class<? extends Fragment> newFragment,
                                 Bundle bundle) {

        mCurrentFragment = FragmentUtils.switchFragment(fragmentManager,
                R.id.layout_content, mCurrentFragment, newFragment, bundle,
                false);
    }

    @Override
    public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
        // TODO Auto-generated method stub
        mTabView.setCurrentTab(1);
    }

    @Override
    public void onTabChange(String tag) {
        // TODO Auto-generated method stub

        if (tag != null) {
            if (tag.equals("message")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 0;
                mTitleTextView.setText(R.string.text_tab_message);
                replaceFragment(FragmentLaw.class);
                // 检查，如果没有登录则跳转到登录界面
				/*
				 * final UserConfigManager manager =
				 * UserConfigManager.getInstance(); if (manager.getId() <= 0) {
				 * startActivityForResult(new Intent(this, LoginActivity.class),
				 * BaseActivity.REQUEST_OK_LOGIN); }
				 */
            } else if ("service".equals(tag)) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 1;
                mTitleTextView.setText(R.string.text_tab_service);
                replaceFragment(FragmentSample.class);
            } else if (tag.equals("personal")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 2;
                mTitleTextView.setText(R.string.text_tab_profile);
                replaceFragment(FragmentSocial.class);//, bundle
                // 检查，如果没有登录则跳转到登录界面
				/*
				 * final UserConfigManager manager =
				 * UserConfigManager.getInstance(); if (manager.getId() <= 0) {
				 * startActivityForResult(new Intent(this, LoginActivity.class),
				 * BaseActivity.REQUEST_OK_LOGIN); }
				 */
            } else if (tag.equals("settings")) {
                if(LoginUtil.Login(this)) {
                    mPreviousTabIndex = mCurrentTabIndex;
                    mCurrentTabIndex = 3;
                    mTitleTextView.setText(R.string.text_tab_setting);
                    replaceFragment(FragmentUser.class);
                }
                // 检查，如果没有登录则跳转到登录界面
				/*
				 * final UserConfigManager manager =
				 * UserConfigManager.getInstance(); if (manager.getId() <= 0) {
				 * startActivityForResult(new Intent(this, LoginActivity.class),
				 * BaseActivity.REQUEST_OK_LOGIN); }
				 */
            }
        }
    }
}
