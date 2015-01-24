package com.shoulder.xiaonei.others;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.ExitApplication;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Start_Page extends Activity{
	
	private SharedPreferences preferences_startPage;
	private SharedPreferences.Editor editor;
	
	private ViewPager viewPager;
	private List<View> views;
	private View view0,view1,view2,view3,view4;
	private int currentPage = 0;//当前页面
	private ImageView image0,image1,image2,image3;
	private TextView text_tryNow;
	private Button text_register;
	private ImageView dot_0,dot_1,dot_2,dot_3,dot_4;
	
	private MyVal mv;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page_viewpage); 
        ExitApplication.getInstance().addActivity(this);
        
        mv = (MyVal)getApplication();
        
        initDot();//左右滑动时，最下方提示第几张图的点
        initViewPager();//左右滑动的页面
        
        //记录于缓存，只有这台手机第一次打开社友会看到引导页
        preferences_startPage = getSharedPreferences(MyStatic.Preferences_StartPage, MODE_PRIVATE);
        editor = preferences_startPage.edit();
        editor.putBoolean(MyStatic.Preferences_StartPage_HasShowed, true);
        editor.commit();
	}
	
	private void initDot(){
		dot_0 = (ImageView)findViewById(R.id.dot_0);
		dot_1 = (ImageView)findViewById(R.id.dot_1);
		dot_2 = (ImageView)findViewById(R.id.dot_2);
		dot_3 = (ImageView)findViewById(R.id.dot_3);
		dot_4 = (ImageView)findViewById(R.id.dot_4);
	}
	
	private void initViewPager(){
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		views = new ArrayList<View>();
		LayoutInflater layoutInflater = getLayoutInflater();
		view0=layoutInflater.inflate(R.layout.start_page_image, null);
		view1=layoutInflater.inflate(R.layout.start_page_image, null);
		view2=layoutInflater.inflate(R.layout.start_page_image, null);
		view3=layoutInflater.inflate(R.layout.start_page_image, null);
		view4=layoutInflater.inflate(R.layout.start_page_trynow, null);
		
		image0 = (ImageView)view0.findViewById(R.id.startPage_image);
		image1 = (ImageView)view1.findViewById(R.id.startPage_image);
		image2 = (ImageView)view2.findViewById(R.id.startPage_image);
		image3 = (ImageView)view3.findViewById(R.id.startPage_image);
		
		image0.setImageResource(R.drawable.startpage0);
		image1.setImageResource(R.drawable.startpage1);
		image2.setImageResource(R.drawable.startpage2);
		image3.setImageResource(R.drawable.startpage3);
		
		initLoginAndSoOn();
		
		views.add(view0);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	private void initLoginAndSoOn(){
		text_tryNow=(TextView)view4.findViewById(R.id.text_tryNow);
		text_tryNow.setOnClickListener(new ButtonTryNow());
		text_register=(Button)view4.findViewById(R.id.register);
		text_register.setOnClickListener(new ButtonTryNow());
	}
	
	
	class ButtonLoginListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.putExtra("where_come_from", "login_or_register");
			intent.setClass(Start_Page.this, Login.class);
			startActivity(intent);
		}
    }
	
	class ButtonRegsiterListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(Start_Page.this,Register_Page1.class);
			startActivity(intent);
		}
	}
	
	
	private void TryNow(){
		Intent intent = new Intent(Start_Page.this,Main.class);
		Toast.makeText(Start_Page.this, getResources().getString(R.string.main_remain_youCanChangeSchool), Toast.LENGTH_LONG).show();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(intent);
	}
	public void onBackPressed() {//用户按后退让他/她直接应用
		TryNow();
	}
	class ButtonTryNow implements OnClickListener{
		public void onClick(View v) {
			if(v == text_tryNow){
				TryNow();
			}
			else if(v == text_register){
				Intent intent = new Intent(Start_Page.this,Register_Page1.class);
				startActivity(intent);
			}
		}
	}
	
	
    public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }  
  
        public int getCount() {           
            return  mListViews.size();  
        }  
          
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;  
        }  
    }  
  
    public class MyOnPageChangeListener implements OnPageChangeListener{  

        public void onPageScrollStateChanged(int arg0) {  
        	
        }  
  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
  
        public void onPageSelected(int arg0) {  
        	
        	currentPage = arg0;  
        	
        	dot_0.setBackgroundResource(R.drawable.shape_startpage_hollow);
        	dot_1.setBackgroundResource(R.drawable.shape_startpage_hollow);
        	dot_2.setBackgroundResource(R.drawable.shape_startpage_hollow);
        	dot_3.setBackgroundResource(R.drawable.shape_startpage_hollow);
        	dot_4.setBackgroundResource(R.drawable.shape_startpage_hollow);
        	
        	if(currentPage == 0) dot_0.setBackgroundResource(R.drawable.shape_startpage_solid);
        	else if(currentPage == 1) dot_1.setBackgroundResource(R.drawable.shape_startpage_solid);
        	else if(currentPage == 2) dot_2.setBackgroundResource(R.drawable.shape_startpage_solid);
        	else if(currentPage == 3) dot_3.setBackgroundResource(R.drawable.shape_startpage_solid);
        	else if(currentPage == 4) dot_4.setBackgroundResource(R.drawable.shape_startpage_solid);	
        }  
    }
    
    
    public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
