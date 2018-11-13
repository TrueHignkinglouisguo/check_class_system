package com.example.louis.theclass;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import Method.JellyInterpolator;
import MyTools.MD5;
import MyTools.MyActivityManager;
import MyTools.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class LoginActivity extends BaseActivity{
    private Activity activity;
    private EditText input_username,input_password;
    private TextView login_loginbutton,login_signin,login_forget_password;
    private View login_progress,login_input;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Message msg;
    private Bitmap bitmap=null;
    private boolean gethead=false;
    Handler handler = new Handler(new Handler.Callback(){
        public boolean handleMessage(Message message){
            String[] result=(String[])message.obj;
            if(result[0].equals("true")){
                    String username=input_username.getText().toString().trim();
                    getuserhead("https://www.louisguo.cn/check_class/userhead/"+username+".jpg");
                    do{}while (gethead==false);
                    create_head(username);
                    login_info(username,result[3]);
                    MyToast.show(LoginActivity.this,"登录成功!");
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    activity=LoginActivity.this;
                    MyActivityManager.getInstance().destorySpecActivity(activity);
            }else if(result[1].equals("true")){
                view_show();
                MyToast.show(LoginActivity.this,"账号不存在!");
            }else if(result[2].equals("true")){
                view_show();
                MyToast.show(LoginActivity.this,"密码错误!");
            }
            return false;
        }
    });
        protected int setStatusBarColor() {
            return 0;
        }
        protected int CreatetLayout(){
            return R.layout.loginpage;
        }
        protected void CreateView(){
            login_loginbutton=(TextView)findViewById(R.id.login_loginbutton);
            login_signin=(TextView)findViewById(R.id.login_singin);
            login_forget_password=(TextView)findViewById(R.id.loginpage_forget_password);
            login_progress=(View)findViewById(R.id.login_progress);
            login_input=(View)findViewById(R.id.login_input);
            input_username=(EditText)login_input.findViewById(R.id.login_username);
            input_password=(EditText)login_input.findViewById(R.id.login_password);
        }
        protected void CreateData(){
            activity=this;
            MyActivityManager.getInstance().add(activity);
            requestAllPower();
            login_loginbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final String username=input_username.getText().toString().trim();
                    final String username_md5= MD5.getmd5(username);
                    final String password=input_password.getText().toString().trim();
                    final String password_md5=MD5.getmd5(password);
                    if(username.isEmpty() || password.isEmpty()){
                        MyToast.show(LoginActivity.this,"输入不能为空!");
                    }else{
                        login_input.setVisibility(View.INVISIBLE);
                        login_progress.setVisibility(View.VISIBLE);
                        progressAnimator(login_progress);
                        login_loginbutton.setClickable(false);
                        login_signin.setClickable(false);
                        login_forget_password.setClickable(false);
                        TimerTask task = new TimerTask() {
                            public void run() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        FormBody.Builder params=new FormBody.Builder();
                                        params.add("username",username_md5)
                                                .add("password",password_md5)
                                                .add("type","login");
                                        OkHttpClient okHttpClient=new OkHttpClient();
                                        okHttpClient.newBuilder()
                                                .connectTimeout(5, TimeUnit.SECONDS)
                                                .readTimeout(5, TimeUnit.SECONDS).build();
                                        Request request=new Request.Builder()
                                                .url("https://www.louisguo.cn/check_class/userinfo.php")
                                                .post(params.build())
                                                .build();
                                        Call call=okHttpClient.newCall(request);
                                        call.enqueue(new Callback() {
                                            public void onResponse(Call arg0, Response response) throws IOException {
                                                //   响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                                final String result= response.body().string();
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    final String[] results = {jsonObject.optString("login_ok"), jsonObject.optString("login_ok"),jsonObject.optString("username_error"),
                                                            jsonObject.optString("password_error")};
                                                    msg = handler.obtainMessage();
                                                    msg.obj = results;
                                                    handler.sendMessage(msg);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            public void onFailure(Call arg0, IOException arg1) {
                                            }
                                        });
                                    }
                                }).start();
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 1000);//1秒后执行TimeTask的run方法
                    }
                }
            });
            login_signin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(LoginActivity.this,SigninActivity.class);
                    startActivity(intent);
                }
            });
            login_forget_password.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(LoginActivity.this,ForgetPassword.class);
                    startActivity(intent);
                }
            });
        }
    private void login_info(String login_id,String name){
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String key_id = "id";
        String value_id = login_id;
        editor.putString(key_id, value_id);
        String key_name = "name";
        String value_name = name;
        editor.putString(key_name, value_name);
        editor.commit();
    }
    private void view_show(){
        login_progress.setVisibility(View.INVISIBLE);
        login_input.setVisibility(View.VISIBLE);
        login_loginbutton.setClickable(true);
        login_signin.setClickable(true);
        login_forget_password.setClickable(true);
    }
    private void requestAllPower(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        }, 1);
            }
        }
    }
    private void getuserhead(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).method("GET",null).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Call call,IOException e){
            }
            public void onResponse(Call call, Response response){
                InputStream inputStream = response.body().byteStream();
                bitmap= BitmapFactory.decodeStream(inputStream);
                gethead=true;
            }
        });
    }
    private void create_head(String id){
        if(bitmap!=null) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), id + ".jpg");
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
}
