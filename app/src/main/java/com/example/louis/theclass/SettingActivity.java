package com.example.louis.theclass;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import MyTools.MyActivityManager;
import MyTools.MyToast;
public class SettingActivity extends BaseActivity {
    private Activity activity;
    private ImageView back;
    private TableRow midypassword;
    protected int  setStatusBarColor(){
        return 0;
    }
    protected int CreatetLayout(){
        return R.layout.settingpage;
    }
    protected void CreateView(){
        back=(ImageView)findViewById(R.id.setting_back);
        midypassword=(TableRow)findViewById(R.id.settingpage_midypassword);
        notSetStatusBarColor();
    }
    protected void CreateData(){
        activity=this;
        MyActivityManager.getInstance().add(activity);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().destorySpecActivity(activity);
            }
        });
        midypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.show(SettingActivity.this,"如需修改密码请点击登录界面的忘记密码!");
            }
        });
    }
}