package com.chen.soft.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.activity.UserInfoActivity;
import com.chen.soft.util.StatusUtil;

import java.io.File;

/**
 * Created by chenchi_94 on 2015/9/17.
 */
public class FragmentUser  extends BaseFragment implements View.OnClickListener{

    private RelativeLayout tou_rl;
    private RelativeLayout nick_rl;
    private RelativeLayout signature_rl;
    private RelativeLayout score_rl;

    private ImageView detail_img;
    private TextView nick_name;
    private TextView signature_tv;
    private TextView score_tv;

    private String signature;
    private String nickname;
    private String score;

    private static final int PHOTO_SUCCESS = 1;
    private static final int CAMERA_SUCCESS = 2;
    private static final int NONE = 0;
    private static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nick_name = (TextView) view.findViewById(R.id.nick_name);
        signature_tv = (TextView) view.findViewById(R.id.signature);
        score_tv = (TextView) view.findViewById(R.id.score);
        showText();

        tou_rl = (RelativeLayout) view.findViewById(R.id.tou_rl);
        tou_rl.setOnClickListener(this);
        nick_rl = (RelativeLayout) view.findViewById(R.id.nick_rl);
        nick_rl.setOnClickListener(this);
        signature_rl = (RelativeLayout) view.findViewById(R.id.signature_rl);
        signature_rl.setOnClickListener(this);
        score_rl = (RelativeLayout) view.findViewById(R.id.score_rl);
        score_rl.setOnClickListener(this);
    }

    private void showText() {
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this.getActivity(), UserInfoActivity.class);
        final Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tou_rl:

                final CharSequence[] items = { "手机相册", "相机拍摄" };
                AlertDialog dlg = new AlertDialog.Builder(this.getActivity())
                        .setTitle("选择图片")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // 这里item是根据选择的方式,
                                // 在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                                if (item == 1) {
                                    Intent getImageByCamera = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);

                                    getImageByCamera.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    "myImage/newtemp.jpg")));

                                    startActivityForResult(getImageByCamera,
                                            CAMERA_SUCCESS);
                                } else {

                                    Intent getImage = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    startActivityForResult(getImage, PHOTO_SUCCESS);
                                }
                            }
                        }).create();
                dlg.show();
                break;
            case R.id.signature_rl:
                signature = signature_tv.getText().toString().trim();
                bundle.putString("title", "个性签名");
                bundle.putString("mimecontent", signature);
                bundle.putInt("num", 75);
                intent.putExtras(bundle);
                startActivityForResult(intent, StatusUtil.SIGNATURE);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("info", "fragmetn" + requestCode + " " + resultCode);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        String info = data.getExtras().getString("result");
        Log.d("info", "fragment" + requestCode + " " + resultCode + info);
        switch (requestCode) {
            case StatusUtil.SIGNATURE:
                signature_tv.setText(info);
                break;
            default:
                break;
        }
    }
}
