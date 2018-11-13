package Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.louis.theclass.R;
import com.example.louis.theclass.SettingActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Method.Me_Adapter;
import MyTools.GetVersion;
import MyTools.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class Me extends Fragment {
    private RecyclerView recyclerView;
    private Me_Adapter function_adapter;
    private int[] item_icon_left = {R.mipmap.icon_setting, R.mipmap.icon_update, R.mipmap.icon_about};
    private String[] item_text = {"设置", "检查更新", "关于"};
    private float version=0,the_version;
    public static Me newInstance(String name) {
        Me fragment = new Me();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mepage, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void CreateView(){
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.mepage_rv);
    }
    private void CreateData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        function_adapter = new Me_Adapter(getActivity(),getContext(), icon_left(), item_text());
        function_adapter.setClickListener(new Me_Adapter.ItemClickListener() {
            public void OnItemClick(View view, int position){
                if(position==0){
                    Intent intent=new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }else if(position==1){
                    getversion("https://www.louisguo.cn/check_class/getversion.php");
                    do{ }while(version==0);
                    try{
                        the_version=Float.parseFloat(GetVersion.getversion(getContext()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(version>the_version){
                        MyToast.show(getActivity(),"检测到新版本！");
                        String url ="https://www.louisguo.cn/check_class/app-debug.apk";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                        startActivity(i);
                    }else{MyToast.show(getActivity(),"您的软件版本是最新的！"); }
                }else if(position==2){ MyToast.show(getActivity(),"作者:16网络班郭荣"); }
            }
        });
        recyclerView.setAdapter(function_adapter);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CreateView();
        CreateData();
    }
    private List<Integer> icon_left() {
        List<Integer> icon_left_data = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            icon_left_data.add(item_icon_left[i]);
        }
        return icon_left_data;
    }
    private List<String> item_text() {
        List<String> item_text_data = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            item_text_data.add(item_text[i]);
        }
        return item_text_data;
    }
    private void getversion(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET",null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            public void onFailure(Call call,IOException e) {
            }
            //请求成功执行的方法
            public void onResponse(Call call, Response response) throws IOException {
                version=Float.parseFloat(response.body().string());
            }
        });
    }

}