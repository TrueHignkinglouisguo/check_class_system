package com.example.louis.theclass;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import Method.JellyInterpolator;
import MyTools.MyActivityManager;
import MyTools.MyToast;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class SubmitActivity extends Activity{
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private View info,progress;
    private String classinfo;
    private TextView submit,cancel,cr,cn,cl,ac,date,teacher,yd,sd,absent;
    Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if (((String) msg.obj).equals("login_ok")) {
                MyToast.show(SubmitActivity.this, "提交成功!");
                finish();
            } else{
                MyToast.show(SubmitActivity.this,"提交失败!");
                info.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    });
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitpage);
        CreateView();
        CreateData();
    }
    protected void CreateView(){
        info=(View)findViewById(R.id.submitpage_info);
        progress=(View)findViewById(R.id.submitpage_progress);
        submit=(TextView)findViewById(R.id.submitpage_submit);
        cancel=(TextView)findViewById(R.id.submitpage_cancel);
        cr=(TextView)info.findViewById(R.id.submitpage_classroom);
        cn=(TextView)info.findViewById(R.id.submitpage_classname);
        cl=(TextView)info.findViewById(R.id.submitpage_class);
        ac=(TextView)info.findViewById(R.id.submitpage_ac);
        date=(TextView)info.findViewById(R.id.submitpage_date);
        teacher=(TextView)info.findViewById(R.id.submitpage_teacher);
        yd=(TextView)info.findViewById(R.id.submitpage_yd);
        sd=(TextView)info.findViewById(R.id.submitpage_sd);
        absent=(TextView)info.findViewById(R.id.submitpage_absent);
    }
    protected void CreateData() {
        activity=this;
        MyActivityManager.getInstance().add(activity);
        Bundle getdata = getIntent().getExtras();
        final String[] data = getdata.getStringArray("data");
        date.setText(data[2]);
        cr.setText(data[0]);
        cn.setText(data[1]);
        cl.setText(data[4]);
        ac.setText(data[3]);
        if(data[9].equals("是")){
            teacher.setText(data[8]+"(缺勤)");
        }else{
            teacher.setText(data[8]);
        }
        yd.setText(data[5]);
        sd.setText(data[6]);
        absent.setText(data[7]);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                info.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                String id=sharedPreferences.getString("id",null);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time",gettime())
                              .put("cr",cr.getText().toString().trim())
                              .put("date",date.getText().toString().trim())
                              .put("cn",cn.getText().toString().trim())
                              .put("cl",cl.getText().toString().trim())
                              .put("ac",ac.getText().toString().trim())
                              .put("teacher",teacher.getText().toString().trim())
                              .put("yd",yd.getText().toString().trim())
                              .put("sd",sd.getText().toString().trim())
                              .put("absent",absent.getText().toString().trim())
                              .put("tea_absent",data[9])
                              .put("id",id);
                    classinfo=jsonObject.toString();
                    submit();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyActivityManager.getInstance().destorySpecActivity(activity);
            }
        });
    }
    private void submit(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), classinfo);
                Request request = new Request.Builder()
                        .url("https://www.louisguo.cn/check_class/login_data.php")
                        .post(requestBody)
                        .build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful()){
                        String result=response.body().string();
                        Message message=handler.obtainMessage();
                        message.obj=result;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private String gettime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date=df.format(new Date());
        return  date;
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
