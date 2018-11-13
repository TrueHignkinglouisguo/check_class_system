package com.example.louis.theclass;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import Method.SelectInfo_Adapter;
import MyTools.MyActivityManager;
import MyTools.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class SelectInfoActivity extends BaseActivity{
    private String time;
    private String[] text;
    private int item_num=0;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private TextView title;
    private ImageView back;
    private JSONObject jsonObject;
    private RecyclerView recyclerView;
    private SelectInfo_Adapter selectInfo_adapter;
    Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                if(!msg.obj.equals("null")){
                    ob_json((String)msg.obj);
                    createitem();
                }else{
                    MyToast.show(SelectInfoActivity.this,"没有查询到相关信息!");
                }
            }
            return false;
        }
    });
    protected int  setStatusBarColor(){
        return 0;
    }
    protected int CreatetLayout(){
        return R.layout.selectinfopage;
    }
    protected void CreateView(){
        title=(TextView) findViewById(R.id.selectinfo_title);
        back=(ImageView) findViewById(R.id.selectinfo_back);
        recyclerView = (RecyclerView)findViewById(R.id.selectinfo_rv);
        notSetStatusBarColor();
    }
    protected void CreateData(){
        activity=this;
        MyActivityManager.getInstance().add(activity);
        getmytask();
        Bundle data=getIntent().getExtras();
        title.setText(data.getString("date"));
        time=data.getString("date");
        MyToast.show(SelectInfoActivity.this,time);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyActivityManager.getInstance().destorySpecActivity(activity);
            }
        });
    }
    private void createitem(){
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectInfoActivity.this));
        selectInfo_adapter = new SelectInfo_Adapter(SelectInfoActivity.this,SelectInfoActivity.this, item_text());
        selectInfo_adapter.setClickListener(new SelectInfo_Adapter.ItemClickListener(){
            public void OnItemClick(View view, int position){
                for(int i=0;i<item_num;i++){
                    if(position==i){
                        Intent intent=new Intent(SelectInfoActivity.this, TaskInfo_Activity.class);
                        intent.putExtra("data",jsonObject.optString(String.valueOf(i+1)));
                        startActivity(intent);
                    }
                }
            }
        });
        recyclerView.setAdapter(selectInfo_adapter);
    }
    private List<String> item_text() {
        List<String> item_text_data = new ArrayList<String>();
        for (int i = 0; i < item_num; i++) {
            item_text_data.add(text[i]);
        }
        return item_text_data;
    }
    private void ob_json(String result){
        try {
            jsonObject = new JSONObject(result);
            item_num=Integer.parseInt(jsonObject.optString("item_num"));
            String[] item_text=new String[item_num];
            for(int i=0;i<item_num;i++){
                JSONObject jsonObject2 = new JSONObject(jsonObject.optString(String.valueOf(i+1)));
                item_text[i]=jsonObject2.optString("classroom")+"-----"+jsonObject2.optString("class");
            }
            text=item_text;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getmytask(){
        new Thread(new Runnable(){
            public void run() {
                sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                String id=sharedPreferences.getString("id",null);
                FormBody.Builder params=new FormBody.Builder();
                params.add("userid",id)
                        .add("time",time);
                OkHttpClient okHttpClient=new OkHttpClient();
                okHttpClient.newBuilder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS).build();
                Request request=new Request.Builder()
                        .url("https://www.louisguo.cn/check_class/selectmytask.php")
                        .post(params.build())
                        .build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    public void onResponse(Call arg0, Response response) throws IOException {
                        String result= response.body().string();
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj=result;
                        handler.sendMessage(msg);
                    }
                    public void onFailure(Call arg0, IOException arg1) {
                    }
                });
            }
        }).start();
    }
}
