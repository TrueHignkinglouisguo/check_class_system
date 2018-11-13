package com.example.louis.theclass;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import MyTools.MyActivityManager;
public class TaskInfo_Activity extends Activity {
    private Activity activity;
    private View info;
    private TextView cancel,cr,cn,cl,ac,date,teacher,yd,sd,absent;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskinfopage);
        CreateView();
        CreateData();
    }
    protected void CreateView(){
        info=(View)findViewById(R.id.taskinfopage_info);
        cancel=(TextView)findViewById(R.id.taskinfopage_cancel);
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
        String data = getdata.getString("data");
        try{
            JSONObject jsonObject=new JSONObject(data);
            date.setText(jsonObject.optString("unit"));
            cr.setText(jsonObject.optString("classroom"));
            cn.setText(jsonObject.optString("classname"));
            cl.setText(jsonObject.optString("class"));
            ac.setText(jsonObject.optString("ac"));
            teacher.setText(jsonObject.optString("teacher"));
            yd.setText(jsonObject.optString("yd"));
            sd.setText(jsonObject.optString("sd"));
            absent.setText(jsonObject.optString("absent"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyActivityManager.getInstance().destorySpecActivity(activity);
            }
        });
    }
}
