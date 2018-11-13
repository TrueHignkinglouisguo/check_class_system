package com.example.louis.theclass;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import Method.GetImageUri;
import MyTools.MyActivityManager;
import MyTools.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class UserInfoActivity extends BaseActivity{
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int CROP_PHOTO = 2;
    public static final int REQUEST_CODE_IMAGE_OP=3;
    private Activity activity;
    private RelativeLayout sethead;
    private ImageView thehead,back;
    private Uri imageUri;
    private File outputImage;
    private SharedPreferences sharedPreferences;
    private String id;
    protected int  setStatusBarColor(){
        return 0;
    }
    protected int CreatetLayout(){
        return R.layout.userinfopage;
    }
    protected void CreateView(){
        sethead=(RelativeLayout) findViewById(R.id.userinfopage_sethead);
        thehead = (ImageView) findViewById(R.id.userinfopage_thehead);
        back = (ImageView) findViewById(R.id.userinfo_back);
        notSetStatusBarColor();
    }
    protected void CreateData(){
        activity=this;
        MyActivityManager.getInstance().add(activity);
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        id=sharedPreferences.getString("id",null);
        Bitmap bitmap=GetImageUri.getimguri(UserInfoActivity.this,id);
        if(bitmap!=null){
            thehead.setImageBitmap(bitmap);
        }
        sethead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("请选择一张图片")
                        .setIcon(R.mipmap.icon_me)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 1:
                                        openCamera();
                                        break;
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/*");
                                        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                        break;
                                    default:;
                                }
                            }
                        })
                        .show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().destorySpecActivity(activity);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                    startPhotoZoom(imageUri);
                break;
            case REQUEST_CODE_IMAGE_OP:
                if(data!=null){
                    Uri Path = data.getData();
                    startPhotoZoom(Path);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        thehead.setImageBitmap(bitmap);
                        outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        uphead();
                        MainActivity.instance.CreateData();
                        MyToast.show(UserInfoActivity.this,"修改头像成功!");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    private void openCamera() {
        //创建一个保存图片的路径
        outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果android7.0以上的系统，需要做个判断
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.example.louis.theclass", outputImage);//7.0
        } else {
            imageUri = Uri.fromFile(outputImage); //7.0以下
        }
        //利用隐式Intent 打开系统相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);//这里的TAKE_PHOTO是定义的一个静态常数变量
    }
    private void startPhotoZoom(Uri uri) {
        File CropPhoto = new File(Environment.getExternalStorageDirectory(), id+".jpg");//这个是创建一个截取后的图片路径和名称。
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
        imageUri = Uri.fromFile(CropPhoto);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PHOTO);//这里的CROP_PHOTO是在startActivityForResult里使用的返回值。
    }
    private void uphead(){
        new Thread(new Runnable(){
            public void run() {
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), id+".jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", id+".jpg",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url("https://www.louisguo.cn/check_class/fileupload.php")
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.show(UserInfoActivity.this,"上传失败!");
                    }
                });
            }
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
            }
        }).start();
    }
}
