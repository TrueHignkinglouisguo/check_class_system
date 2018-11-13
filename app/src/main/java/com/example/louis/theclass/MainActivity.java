package com.example.louis.theclass;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import Fragment.*;
import Method.ViewPagerAdapter;
import MyTools.MyActivityManager;
import MyTools.MyToast;
public class MainActivity extends BaseActivity{
    public static MainActivity instance = null;
    private Activity activity;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private Bitmap bitmap=null;
    private long exitTime = 0;
    private SharedPreferences sharedPreferences;
    protected int setStatusBarColor(){
        return 0;
    }
    protected int CreatetLayout(){
        return R.layout.mainpage;
    }
    protected void CreateView(){
        instance = this;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainpage_bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

    }
    protected void CreateData(){
        activity = this;
        MyActivityManager.getInstance().add(activity);
        viewPager = (ViewPager) findViewById(R.id.mainpage_viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(Function.newInstance(""));
        list.add(Home.newInstance(""));
        list.add(Me.newInstance(""));
        viewPagerAdapter.setList(list);
        viewPager.setCurrentItem(1);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
               MyToast.show(MainActivity.this,"再按一次退出程序!");
                exitTime = System.currentTimeMillis();
            } else {
                MyActivityManager.getInstance().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.bnv_function:
                    viewPager.setCurrentItem(0,false);
                    return true;
                case R.id.bnv_home:
                    viewPager.setCurrentItem(1,false);
                    return true;
                case R.id.bnv_me:
                    viewPager.setCurrentItem(2,false);
                    return true;
            }
            return false;
        }
    };
}
