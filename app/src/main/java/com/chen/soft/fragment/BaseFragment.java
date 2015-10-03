package com.chen.soft.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenchi_94 on 2015/8/30.
 */
public abstract class BaseFragment extends Fragment implements FragmentInterface, View.OnTouchListener {

    /**
     * 模拟后退键
     */
    protected void back() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    /* (non-Javadoc)
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 拦截触摸事件，防止传递到下一层的View
        return true;
    }

    public void dispatchCommand(int id, Bundle args) {
        if (getActivity() instanceof FragmentCallback) {
            FragmentCallback callback = (FragmentCallback) getActivity();
            callback.onFragmentCallback(this, id, args);
        } else {
            throw new IllegalStateException("The host activity does not implement FragmentCallback.");
        }
    }

    public void refreshViews() {

    }

    public boolean commitData() {
        return false;
    }
}