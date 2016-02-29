package com.chen.soft.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.soft.R;
import com.chen.soft.activity.SettingActivity;
import com.chen.soft.activity.UserInfoActivity;
import com.chen.soft.user.User;
import com.chen.soft.util.LoginUtil;
import com.chen.soft.util.ServerUtil;
import com.chen.soft.util.StatusUtil;
import com.chen.soft.util.UIShowUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chenchi_94 on 2015/9/17.
 */
public class FragmentUser  extends BaseFragment implements View.OnClickListener{

    private RelativeLayout tou_rl;
    private RelativeLayout nick_rl;
    private RelativeLayout signature_rl;
    private RelativeLayout score_rl;
    private LinearLayout setting;

    private ImageView detail_img;
    private TextView nick_name;
    private TextView signature_tv;
    private TextView score_tv;

    private String signature;
    private String nickname;
    private String score;

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
        detail_img = (ImageView)view.findViewById(R.id.detail_img);
        showText();

        view.findViewById(R.id.signature_rl).setOnClickListener(this);
        view.findViewById(R.id.score_rl).setOnClickListener(this);
        view.findViewById(R.id.setting).setOnClickListener(this);
    }

    private void showText() {
        User user = LoginUtil.user;
        if(user == null){
            Log.d("info", "Error, user not init");
            return;
        }
        nick_name.setText(user.getUserName());
        signature_tv.setText(user.getSignature());
        score_tv.setText("" + user.getScore());
        Ion.with(this).load(user.getPic()).withBitmap()
                // .placeholder(R.drawable.placeholder_image)
                .error(R.mipmap.tou).intoImageView(detail_img)
                .setCallback(new FutureCallback<ImageView>() {

                    @Override
                    public void onCompleted(Exception arg0, ImageView arg1) {
                        // TODO Auto-generated method stub
                        if (arg0 != null) {
                            Log.d("info", arg0.toString());
                        }
                    }

                });

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
                                            StatusUtil.CAMERA_SUCCESS);
                                } else {

                                    Intent getImage = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    startActivityForResult(getImage, StatusUtil.PHOTO_SUCCESS);
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
            case R.id.setting:
                Intent intentSet = new Intent(this.getActivity(), SettingActivity.class);
                startActivity(intentSet);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("info", "activity" + requestCode + " " + resultCode);
        getActivity().getContentResolver();
        if (resultCode != getActivity().RESULT_OK )
            return;
        switch (requestCode) {
            case StatusUtil.SIGNATURE:
                String info = data.getExtras().getString("result");
                Log.d("info", "fragment" + requestCode + " " + resultCode + info);
                LoginUtil.user.setSignature(info);
                signature_tv.setText(info);
                updateInfo("signature", LoginUtil.user.getSignature());
                break;
            default:
                break;
        }
        // 拍照
        if (requestCode == StatusUtil.CAMERA_SUCCESS) {

            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/myImage/newtemp.jpg");
            startPhotoZoom(Uri.fromFile(picture));
        }
        if (data == null) {
            return;
        }
        // 读取相册缩放图片
        if (requestCode == StatusUtil.PHOTO_SUCCESS) {

            startPhotoZoom(data.getData());

        }
        // 处理结果
        if (requestCode == StatusUtil.PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {

                Bitmap photo = extras.getParcelable("data");
                savePicture(photo);
                // 这边要处理提交头像的操作
                // xxxxxxxxxxxxxx
                detail_img.setImageBitmap(photo);
            }
        }
    }

    //更新用户信息
    private void updateInfo(String name, String value) {
        Ion.with(this)
                .load(String.format("%s?qq=%s&name=%s&value=%s",
                        ServerUtil.updateUerUrl, LoginUtil.user.getId(), name, value))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                // TODO Auto-generated method stub
                if (e != null) {
                    Log.d("info", e.toString());
                    UIShowUtil.toastMessage(FragmentUser.this.getActivity(),
                            "网络异常: " + e.toString());
                    return;
                }
                Log.d("info", "login:" + result);
                if (result.get("suc").getAsString().equals("true")) {
                    UIShowUtil.toastMessage(FragmentUser.this.getActivity(),
                            "更新成功");
                } else {
                    UIShowUtil.toastMessage(FragmentUser.this.getActivity(),
                            "更新失败：" + result.get("suc").getAsString());
                }
            }

        });
    }

    /*
     * 将压缩图片保存到自定义的文件中 Bitmap类有一compress成员，可以把bitmap保存到一个stream中。
     */
    @SuppressLint("SdCardPath")
    public void savePicture(Bitmap bitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            // Log.v("TestFile",
            // "SD card is not avaiable/writeable right now.");
            return;
        }
        FileOutputStream b = null;
        File file = new File("/sdcard/myImage/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/myImage/temp.jpg";

        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 进行图片剪裁
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, StatusUtil.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, StatusUtil.PHOTORESOULT);
    }

    private void scanOldImageFile() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "/myImage/newtemp.jpg");
        File file1 = new File(Environment.getExternalStorageDirectory(),
                "/myImage/temp.jpg");
        if (file.exists() || file1.exists()) {
            file.delete();
            file1.delete();
        }
    }
}
