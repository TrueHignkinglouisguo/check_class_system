package Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.louis.theclass.LoginActivity;
import com.example.louis.theclass.R;
import com.example.louis.theclass.SubmitActivity;
import com.example.louis.theclass.UserInfoActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import android.os.Handler;
import org.json.JSONException;
import org.json.JSONObject;
import Method.GetImageUri;
import Method.GetUnit;
import Method.Scan;
import MyTools.MyActivityManager;
import MyTools.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class Home extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView head,scan,nav_head;
    private View sta_item1,sta_item2,sta_item3,nav,nav_userinfo;
    private EditText cr,cn,ac,cl,unit,teacher,sta_text11,sta_text12,sta_text13;
    private TextView submit_buootn, home_week,nav_name,nav_id;
    private CheckBox tea_absent;
    private String query_unit;
    private String teacher_absent="否";
    public static Home newInstance(String name){
        Home fragment=new Home();
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homepage,container,false);
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CreateView();
        CreateData();
    }
    Handler handler=new Handler(new Handler.Callback(){
        public boolean handleMessage(Message msg) {
            try {
                GetUnit getUnit=new GetUnit();
                query_unit=getUnit.getthe_time();
            }catch (ParseException e){
                e.printStackTrace();
            }
            String[] result=(String[])msg.obj;
            cr.setText(result[0]);
            cn.setText(result[1]);
            unit.setText(query_unit);
            ac.setText(result[3]);
            cl.setText(result[2]);
            teacher.setText(result[5]);
            sta_text11.setText(result[4]);
            return false;
        }
    });
    private void CreateView(){
        toolbar=(Toolbar)getActivity().findViewById(R.id.homepage_toolbar);
        tea_absent=(CheckBox)getActivity().findViewById(R.id.homepage_classroominfo_tea_absent);
        head=(ImageView)getActivity().findViewById(R.id.homepage_head);
        scan=(ImageView)getActivity().findViewById(R.id.homepage_scan);
        sta_item1=(View)getActivity().findViewById(R.id.homepage_sta_item1);
        sta_item2=(View)getActivity().findViewById(R.id.homepage_sta_item2);
        sta_item3=(View)getActivity().findViewById(R.id.homepage_sta_item3);
        home_week=(TextView)getActivity().findViewById(R.id.homepage_week);
        submit_buootn=(TextView)getActivity().findViewById(R.id.homepage_submitbutton);
        cr=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_cr);
        cn=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_cn);
        unit=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_unit);
        ac=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_ac);
        cl=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_cl);
        teacher=(EditText)getActivity().findViewById(R.id.homepage_classroominfo_teacher);
        sta_text11=(EditText)sta_item1.findViewById(R.id.homepage_sta_text1);
        sta_text12=(EditText)sta_item2.findViewById(R.id.homepage_sta_text1);
        sta_text13=(EditText) sta_item3.findViewById(R.id.homepage_sta_text1);
        drawer = (DrawerLayout) getActivity().findViewById(R.id.mainpage_nav);
        navigationView = (NavigationView)getActivity().findViewById(R.id.mainpage_nav_view);
        nav=navigationView.getHeaderView(0);
        nav_userinfo=(View) nav.findViewById(R.id.mainpage_nav_userinfo);
        nav_head=(ImageView) nav.findViewById(R.id.nav_header_head);
        nav_name=(TextView) nav.findViewById(R.id.mainpage_nav_user_name);
        nav_id=(TextView) nav.findViewById(R.id.mainpage_nav_user_id);
        navigationView.setNavigationItemSelectedListener(Home.this);
    }
    private void CreateData(){
        home_week.setText("第"+getDay_of_Week()+"周");
        ((TextView)sta_item1.findViewById(R.id.homepage_sta_title1)).setText("应到人数");
        ((TextView)sta_item2.findViewById(R.id.homepage_sta_title1)).setText("实到人数");
        ((TextView)sta_item3.findViewById(R.id.homepage_sta_title1)).setText("缺勤人数");
        sta_text11.setTextColor(getResources().getColor(R.color.blue_deep));
        sta_text12.setTextColor(getResources().getColor(R.color.green_deep));
        sta_text13.setTextColor(getResources().getColor(R.color.red));
        sharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String id=sharedPreferences.getString("id",null);
        Bitmap bitmap=GetImageUri.getimguri(getActivity(),id);
        if(bitmap!=null){
            nav_head.setImageBitmap(bitmap);
            head.setImageBitmap(bitmap);
        }
        nav_name.setText(sharedPreferences.getString("name",null));
        nav_id.setText("ID:"+sharedPreferences.getString("id",null));
        nav_head.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserInfoActivity.class);
                startActivity(intent);
            }
        });
        tea_absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   teacher_absent="是";
                }else{
                    teacher_absent="否";
                }
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawer.openDrawer(navigationView);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Scan.getscan_fragment(Home.this);
            }
        });
        submit_buootn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
              String  cr_gettext=cr.getText().toString().trim();
              String  cn_gettext=cn.getText().toString().trim();
              String  unit_gettext=unit.getText().toString().trim();
              String  ac_gettext=ac.getText().toString().trim();
              String  cl_gettext=cl.getText().toString().trim();
              String  teacher_gettext=teacher.getText().toString().trim();
              String  sta_text11_gettext=sta_text11.getText().toString().trim();
              String  sta_text12_gettext=sta_text12.getText().toString().trim();
              String  sta_text13_gettext=sta_text13.getText().toString().trim();
              String[] data={cr_gettext,cn_gettext,unit_gettext,ac_gettext,cl_gettext,sta_text11_gettext,sta_text12_gettext,sta_text13_gettext,teacher_gettext,teacher_absent};
                if(cr_gettext.isEmpty() || cn_gettext.isEmpty() || unit_gettext.isEmpty() || teacher_gettext.isEmpty()|| ac_gettext.isEmpty()||
                        cl_gettext.isEmpty()||  sta_text11_gettext.isEmpty()|| sta_text12_gettext.isEmpty() || sta_text13_gettext.isEmpty()){
                    MyToast.show(getActivity(),"存在没有输入的项!");
                }else {
                    Intent intent = new Intent(getActivity(), SubmitActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
            }
        });
    }
    private int getDay_of_Week(){
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.set(2018, 8, 27, 0, 0, 0);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH)+1, endCalendar.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));//先算出两时间的毫秒数之差大于一天的天数
        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)){//比较两日期的DAY_OF_MONTH是否相等
            betweenDays++;
        }//相等说明确实跨天了
        int week=betweenDays/7;
        return week+1;
    }
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_scan) {
            Scan.getscan_fragment(Home.this);
        } else if (id == R.id.nav_tools) {
        }else if (id == R.id.nav_more) {
            String url ="https://www.louisguo.cn";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            startActivity(i);
        }else if (id == R.id.nav_logout) {
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
            MyToast.show(getActivity(),"注销成功!");
            MyActivityManager.getInstance().destorySpecActivity(getActivity());
        }
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
            } else {
                final String ScanResult= intentResult.getContents();
                drawer.closeDrawer(navigationView);
                new Thread(new Runnable() {
                    public void run() {
                        FormBody.Builder params=new FormBody.Builder();
                        params.add("classroom_md5",ScanResult)
                                .add("week","mod")
                                .add("unit","1-2");
                        OkHttpClient okHttpClient=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url("https://www.louisguo.cn/check_class/class_info.php")
                                .post(params.build())
                                .build();
                        Call call=okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            public void onResponse(Call arg0, Response response) throws IOException {
                                //   响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                    String result= response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String the_classroom = jsonObject.optString("classroom");
                                    String the_classname = jsonObject.optString("classname");
                                    String the_class = jsonObject.optString("class");
                                    String the_ac= jsonObject.optString("ac");
                                    String the_num= jsonObject.optString("num");
                                    String the_teacher=jsonObject.optString("teacher");
                                    String[] results = {the_classroom, the_classname,the_class,the_ac,the_num,the_teacher};
                                    Message msg = new Message();
                                    msg.obj = results;
                                    handler.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            public void onFailure(Call arg0, IOException arg1) {
                                //   响应失败
                            }
                        });
                    }
                }).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
