package com.chen.soft.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chen.soft.R;
import com.chen.soft.util.StatusUtil;

/**
 * Created by chenchi_94 on 2015/10/7.
 */
public class SettingActivity  extends TitleActivity implements View.OnClickListener {

    private Button exit;

    @Override
    protected void onBackward(View backwardView) {
        // TODO Auto-generated method stub
        super.onBackward(backwardView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_setting);
        showBackwardView(R.string.button_backward, true);
        setTitle(R.string.user_setting);

        findViewById(R.id.exit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        final Intent intent = new Intent(this, UserInfoActivity.class);
        final Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.exit:
                SystemExit();
                break;
            default:
                break;
        }
    }

    private void SystemExit()
    {
        AlertDialog exitDialog = new AlertDialog.Builder(this).
                setTitle("提示").
                setMessage("是否退出本程序？").
                setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        //取消广播
//                        Intent iRefreshService =new Intent(Intent.ACTION_RUN);
//                        iRefreshService.setClass(sys_Context, BootReceiver.class);
//                        iRefreshService.setAction("ASYNCREFRESH");
//                        PendingIntent sender=PendingIntent.getBroadcast(sys_Context, 0, iRefreshService, 0);
//                        AlarmManager am=(AlarmManager)sys_Context.getSystemService(sys_Context.ALARM_SERVICE);
//                        am.cancel(sender);
//                        //退出停止服务
//                        BackNetService.isrun=false;
//                        Intent iRefresh = new Intent(Intent.ACTION_RUN);
//                        iRefresh.setClass( getActivity(), BackNetService.class);
//                        getActivity().stopService(iRefresh);
//                        //清除通知栏
//                        CommonUtil common = new CommonUtil();
//                        common.removeNotification(sys_Context, -1);
//                        Util.clearAllNotification(getActivity());
//
//                        ActivityManager activityManager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
//                        activityManager.restartPackage(getActivity().getPackageName());
                        System.exit(0);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                return;
            }
        }).create();
        exitDialog.show();
    }

}
