package com.qryl.qryl.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qryl.qryl.R;
import com.qryl.qryl.util.DialogUtil;
import com.qryl.qryl.view.MyAlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MeSettingActivity extends AppCompatActivity {

    private static final String TAG = "MeSettingActivity";

    private TextView tvTitle;
    private TextView tvReturn;

    private TextView tvName, tvIdentity, tvGender, tvTel, tvLocation, tvYbh, tvStature, tvWeight;
    private RelativeLayout myHead, realName, identity, gender, tel, location, ybh, stature, weight;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private static final String HEAD_KEY = "head_key";

    private Uri imageUri;
    private Bitmap bitmap;
    private CircleImageView civHead;
    private String[] genderArray;
    private String nameDialogText;
    private String identityDialogText;
    private String genderDialogText;
    private String telDialogText;
    private File headFile;
    private int genderNum;//1是女，0是男
    private String ybhDialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        genderArray = getResources().getStringArray(R.array.gender);
        initView();
        tvReturn.setVisibility(View.VISIBLE);
        //有些条目不能为空

        //点击每个条目实现dialog或者activity
        clickItemShowDialog();
    }

    /**
     * 点击每个条目实现dialog或者activity
     */
    private void clickItemShowDialog() {

        //点击更换头像
        myHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        //点击更换手机号
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MeSettingActivity.this, R.layout.text_item_dialog_num, null);
                TextView tvTitileDialog = (TextView) view.findViewById(R.id.tv_title_dialog);
                final EditText etHintDialog = (EditText) view.findViewById(R.id.et_hint_dialog);
                tvTitileDialog.setText("电话号码");
                new MyAlertDialog(MeSettingActivity.this, view)
                        //.setView(view)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                telDialogText = etHintDialog.getText().toString();
                                tvTel.setText(telDialogText);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        //点击修改姓名
        realName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MeSettingActivity.this, R.layout.text_item_dialog_text, null);
                TextView tvTitileDialog = (TextView) view.findViewById(R.id.tv_title_dialog);
                final EditText etHintDialog = (EditText) view.findViewById(R.id.et_hint_dialog);
                tvTitileDialog.setText("姓名");
                new MyAlertDialog(MeSettingActivity.this, view)
                        //.setView(view)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nameDialogText = etHintDialog.getText().toString();
                                tvName.setText(nameDialogText);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        //身份证
        identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MeSettingActivity.this, R.layout.text_item_dialog_num, null);
                TextView tvTitileDialog = (TextView) view.findViewById(R.id.tv_title_dialog);
                final EditText etHintDialog = (EditText) view.findViewById(R.id.et_hint_dialog);
                tvTitileDialog.setText("身份证");
                new MyAlertDialog(MeSettingActivity.this, view)
                        //.setView(view)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                identityDialogText = etHintDialog.getText().toString();
                                tvIdentity.setText(identityDialogText);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        //选择性别
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.showMultiItemsDialog(MeSettingActivity.this, "选择性别", R.array.gender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvGender.setText(genderArray[which]);
                        genderDialogText = tvGender.getText().toString();
                        if (genderDialogText.equals("男")) {
                            genderNum = 0;
                        } else if (genderDialogText.equals("女")) {
                            genderNum = 1;
                        }
                        Log.i(TAG, "onClick: 设置的性别" + genderDialogText);
                        dialog.dismiss();
                    }
                });
            }
        });
        //选择地址
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转选择地址界面
            }
        });

        //输入医保号
        ybh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MeSettingActivity.this, R.layout.text_item_dialog_num, null);
                TextView tvTitileDialog = (TextView) view.findViewById(R.id.tv_title_dialog);
                final EditText etHintDialog = (EditText) view.findViewById(R.id.et_hint_dialog);
                tvTitileDialog.setText("医保号");
                new MyAlertDialog(MeSettingActivity.this, view)
                        //.setView(view)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ybhDialogText = etHintDialog.getText().toString();
                                tvYbh.setText(ybhDialogText);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        //我的身高

    }

    /**
     * 将注册信息上传到服务器
     */
    private void postData() {
        SharedPreferences pref = getSharedPreferences("image", Context.MODE_PRIVATE);
        String headImage = pref.getString(HEAD_KEY, null);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File headFile = new File(storageDir, headImage);
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (headFile != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), headFile);
            builder.addFormDataPart("txImg", headFile.getName(), requestBody);
        }
        builder.addFormDataPart("", telDialogText);
        builder.addFormDataPart("", nameDialogText);
        builder.addFormDataPart("", identityDialogText);
        builder.addFormDataPart("", String.valueOf(genderNum));
        builder.addFormDataPart("", ybhDialogText);
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url("").post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 点击显示选择本地相册或者拍照
     */
    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popup_choose_pic, null);
        Button btnPopAlbum = (Button) popView.findViewById(R.id.btn_pop_album);
        Button btnPopCamera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button btnPopCancel = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels * 1 / 3;
        final PopupWindow popupWindow = new PopupWindow(popView, widthPixels, heightPixels);
        popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击popup外部消失
        popupWindow.setOutsideTouchable(true);
        //消失时屏幕变为半透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        //出现时屏幕变为透明
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);
        btnPopAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //调用相机
                invokeAlbum();
            }
        });
        btnPopCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //打开相机
                openCarema();
            }
        });
        btnPopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void openCarema() {
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MeSettingActivity.this, "com.qryl.qrylyh.activity.login.complete.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void invokeAlbum() {
        //动态申请危险时权限，运行时权限
        if (ContextCompat.checkSelfPermission(MeSettingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MeSettingActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 动态获取到的权限后的重写
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(MeSettingActivity.this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //将拍摄的图片显示出来
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headFile = saveMyBitmap(bitmap, "head");
                        //保存file到sp
                        saveFile(headFile.getName());
                        Glide.with(this).load(headFile).thumbnail(0.1f).into(civHead);
                        Log.i("wechat", "压缩后图片的大小" + ("字节码：" + " 宽度为:" + bitmap.getWidth() + " 高度为:" + bitmap.getHeight()));
                        Log.i(TAG, "File:" + headFile.getName() + " 路径:" + headFile.getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        //Log.i("uri", uri + "");
        if (DocumentsContract.isDocumentUri(MeSettingActivity.this, uri)) {
            //如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            Log.i("type of document", docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                Log.i("type of document id", id);
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.i("selection", selection);
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，就用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            headFile = saveMyBitmap(bitmap, "head");
            //保存file到sp
            saveFile(headFile.getName());
            Glide.with(this).load(headFile).thumbnail(0.1f).into(civHead);
        } else {
            Toast.makeText(MeSettingActivity.this, "failed to get image ", Toast.LENGTH_SHORT).show();
        }
    }


    //将bitmap转化为png格式
    public File saveMyBitmap(Bitmap mBitmap, String prefix) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = null;
        try {
            file = File.createTempFile(
                    prefix,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 保存file到sp
     *
     * @param fileName
     */
    private void saveFile(String fileName) {
        SharedPreferences sp = getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(HEAD_KEY, fileName);
        //提交edit
        edit.commit();
        Log.i(TAG, "saveFile: 保存成功" + sp.getString(HEAD_KEY, null));
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("设置");
        findViewById(R.id.tv_location).setVisibility(View.GONE);
        tvReturn = (TextView) findViewById(R.id.tv_return);
        //点击事件区域
        civHead = (CircleImageView) findViewById(R.id.civ_head);
        tel = (RelativeLayout) findViewById(R.id.tel);
        myHead = (RelativeLayout) findViewById(R.id.my_head);
        realName = (RelativeLayout) findViewById(R.id.real_name);
        identity = (RelativeLayout) findViewById(R.id.identity);
        gender = (RelativeLayout) findViewById(R.id.gender);
        location = (RelativeLayout) findViewById(R.id.location);
        ybh = (RelativeLayout) findViewById(R.id.ybh);
        stature = (RelativeLayout) findViewById(R.id.stature);
        weight = (RelativeLayout) findViewById(R.id.weight);
        //返回的数据
        tvTel = (TextView) findViewById(R.id.tv_tel);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvIdentity = (TextView) findViewById(R.id.tv_identity);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvYbh = (TextView) findViewById(R.id.tv_ybh);
        tvStature = (TextView) findViewById(R.id.tv_stature);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不能为空时点击跳转
                postData();
            }
        });
    }

}