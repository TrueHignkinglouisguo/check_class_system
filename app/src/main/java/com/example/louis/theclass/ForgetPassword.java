package com.example.louis.theclass;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
public class ForgetPassword extends Activity {
    private Activity activity;
    private EditText forgetpwpage_username,forgetpwpage_password,forgetpwpage_admin_password;
    private TextView forgetpwpage_submit;
    private View forgetpwpage_progress,forgetpwpage_input;
    Handler handler = new Handler(new Handler.Callback(){
        public boolean handleMessage(Message message){
            String result=(String)message.obj;
            if(result.equals("alter_password_ok")){
                MyToast.show(ForgetPassword.this,"重设密码成功!");
                finish();
            }else if(result.equals("alter_password_error")){
                forgetpwpage_progress.setVisibility(View.INVISIBLE);
                forgetpwpage_input.setVisibility(View.VISIBLE);
                MyToast.show(ForgetPassword.this,"重设密码失败!");
            }else if(result.equals("admin_password_error")){
                forgetpwpage_progress.setVisibility(View.INVISIBLE);
                forgetpwpage_input.setVisibility(View.VISIBLE);
                MyToast.show(ForgetPassword.this,"管理员密码错误!");
            }
            return false;
        }
    });
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpwpage);
        CreateView();
        CreateData();
    }
    protected void CreateView(){
        forgetpwpage_input=(View) findViewById(R.id.forgetpwpage_input);
        forgetpwpage_progress=(View) findViewById(R.id.forgetpwpage_progress);
        forgetpwpage_submit=(TextView) findViewById(R.id.forgetpwpage_signinbutton);
        forgetpwpage_username=(EditText)forgetpwpage_input.findViewById(R.id.forgetpwpage_username);
        forgetpwpage_password=(EditText)forgetpwpage_input.findViewById(R.id.forgetpwpage_password);
        forgetpwpage_admin_password=(EditText)forgetpwpage_input.findViewById(R.id.forgetpwpage_admin_password);
    }
    protected void CreateData() {
        activity=this;
        MyActivityManager.getInstance().add(activity);
        forgetpwpage_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String username=forgetpwpage_username.getText().toString().trim();
                final String username_md5= MD5.getmd5(username);
                final String password=forgetpwpage_password.getText().toString().trim();
                final String password_md5=MD5.getmd5(password);
                final String admin_password=forgetpwpage_admin_password.getText().toString().trim();
                if(username.isEmpty() || password.isEmpty()){
                    MyToast.show(ForgetPassword.this,"输入不能为空!");
                }else{
                    forgetpwpage_input.setVisibility(View.INVISIBLE);
                    forgetpwpage_progress.setVisibility(View.VISIBLE);
                    progressAnimator(forgetpwpage_progress);
                    MyToast.show(ForgetPassword.this,"请求中......");
                    TimerTask task = new TimerTask() {
                        public void run() {
                            new Thread(new Runnable() {
                                public void run() {
                                    FormBody.Builder params=new FormBody.Builder();
                                    params.add("username",username_md5)
                                            .add("password",password_md5)
                                            .add("admin_password",admin_password)
                                            .add("type","forget");
                                    OkHttpClient okHttpClient=new OkHttpClient();
                                    Request request=new Request.Builder()
                                            .url("https://www.louisguo.cn/check_class/userinfo.php")
                                            .post(params.build())
                                            .build();
                                    Call call=okHttpClient.newCall(request);
                                    call.enqueue(new Callback() {
                                        public void onResponse(Call arg0, Response response) throws IOException {
                                            //   响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                            final String result= response.body().string();
                                            Message msg = handler.obtainMessage();;
                                            msg.obj=result;
                                            handler.sendMessage(msg);
                                        }
                                        public void onFailure(Call arg0, IOException arg1) {
                                            //   响应失败
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
