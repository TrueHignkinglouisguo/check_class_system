package Fragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import Method.Function_Adapter;
import com.example.louis.theclass.R;
import com.example.louis.theclass.SelectInfoActivity;
import com.example.louis.theclass.SettingActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import Method.Function_menu_Adapter;
import MyTools.MyToast;
public class Function extends Fragment {
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    private RecyclerView function_recyclerView;
    private RecyclerView function_menu_recyclerView;
    private Function_Adapter function_adapter;
    private Function_menu_Adapter function_menu_adapter;
    private int[] item_icon_left = {R.mipmap.thelogo};
    private int[] menu_img={R.mipmap.icon_task,R.mipmap.icon_all_task,R.mipmap.icon_permission};
    private String[] menu_text = {"已录信息查询","信息统计","我的权限"};
    private String[] item_text = {"教学9号楼"};
    public static Function newInstance(String name){
        Function fragment = new Function();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functionpage, container, false);
        return view;
    }
    public void onViewCreated(View view,  Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CreateView();
        CreateData();
    }
    private void CreateView(){
        function_recyclerView = (RecyclerView) getActivity().findViewById(R.id.functionpage_rv);
        function_menu_recyclerView = (RecyclerView) getActivity().findViewById(R.id.functionpage_rv2);
    }
    private void CreateData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        function_menu_recyclerView.setLayoutManager(linearLayoutManager);
        function_menu_adapter = new Function_menu_Adapter(getActivity(),getContext(), set_menu_icon(), set_menu_text());
        function_menu_adapter.setClickListener(new Function_menu_Adapter.ItemClickListener(){
            public void OnItemClick(View view, int position){
                if(position==0){
                    MyToast.show(getActivity(),"请选择要查询的日期!");
                    calendar = Calendar.getInstance();
                    dialog = new DatePickerDialog(getContext(),
                     new DatePickerDialog.OnDateSetListener() {
                      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                          Intent intent=new Intent(getActivity(),SelectInfoActivity.class);
                          monthOfYear=monthOfYear+1;
                          String day_str=null;
                          if(dayOfMonth<10){
                              day_str="0"+String.valueOf(dayOfMonth);
                          }
                          intent.putExtra("date",year + "-" + monthOfYear + "-" + day_str);
                          startActivity(intent);
                            }
                            }, calendar.get(Calendar.YEAR), calendar
                       .get(Calendar.MONTH), calendar
                         .get(Calendar.DAY_OF_MONTH));dialog.show();
                }else if(position==1){
                    MyToast.show(getActivity(),"1");
                }else if(position==2){
                    MyToast.show(getActivity(),"2");
                }
            }
        });
        function_menu_recyclerView.setAdapter(function_menu_adapter);

        function_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        function_adapter = new Function_Adapter(getActivity(),getContext(), icon_left(), item_text());
        function_adapter.setClickListener(new Function_Adapter.ItemClickListener() {
            public void OnItemClick(View view, int position){
                if(position==0){
                    Intent intent=new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }else if(position==1){
                    MyToast.show(getActivity(),"1");
                }else if(position==2){
                    MyToast.show(getActivity(),"2");
                }
            }
        });
        function_recyclerView.setAdapter(function_adapter);
    }
    private List<Integer> icon_left(){
        List<Integer> icon_left_data = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            icon_left_data.add(item_icon_left[0]);
        }
        return icon_left_data;
    }
    private List<String> item_text() {
        List<String> item_text_data = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            item_text_data.add(item_text[0]);
        }
        return item_text_data;
    }
    private List<Integer> set_menu_icon(){
        List<Integer> icon_left_data = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            icon_left_data.add(menu_img[i]);
        }
        return icon_left_data;
    }
    private List<String> set_menu_text() {
        List<String> item_text_data = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            item_text_data.add(menu_text[i]);
        }
        return item_text_data;
    }
}