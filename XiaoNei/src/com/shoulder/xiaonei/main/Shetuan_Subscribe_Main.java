package com.shoulder.xiaonei.main;

import java.util.Timer;
import java.util.TimerTask;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.others.School_List;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Shetuan_Subscribe_Main extends Activity{

	private Button left_btn;
	private Button right_btn;
	private TextView text;
	
	private Button first;
	private Button second;
	private Button third;
	private Button fourth;
	private Button fifth;
	private Button sixth;
	private Button seventh;
	private TextView help;
	
	private PopupWindow pop;
	private LinearLayout layout;
	private Button change_school;
	private Button search;
	
    private Timer timer;
    private int timeCount = 0;
    
    private String change_schoolId = "";
    private String change_schoolName = "";
    private String change_schoolBranch = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.shetuan_dingyue_main);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        initPopMenu();
        initTitle();
        initCatalogue();//初始化各分类
        
        //进入计时，3秒内后退不关闭首页的menu
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, MyStatic.CloseTime);
	}
	
	private void initPopMenu(){
		layout = (LinearLayout) getLayoutInflater().inflate(R.layout.popmenu_shetuan_subscribe, null);
        pop = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
        pop.setBackgroundDrawable(new BitmapDrawable());
        
        change_school=(Button)layout.findViewById(R.id.change_school);
        search=(Button)layout.findViewById(R.id.search);
        change_school.setOnClickListener(new ButtonChangeSchool());
        search.setOnClickListener(new ButtonSearch());
	}
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
        right_btn=(Button)findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new ButtonRightListener());
        text=(TextView)findViewById(R.id.text);
        text.setText("订阅中心");
	}
	
	private void initCatalogue(){
        help=(TextView)findViewById(R.id.help);
        help.setText(getRandomHelpText());
        first=(Button)findViewById(R.id.category_science);
        second=(Button)findViewById(R.id.category_culture);
        third=(Button)findViewById(R.id.category_outdoor);
        fourth=(Button)findViewById(R.id.category_art);
        fifth=(Button)findViewById(R.id.category_sport);
        sixth=(Button)findViewById(R.id.category_mix);
        seventh=(Button)findViewById(R.id.category_others);
        first.setOnClickListener(goToSecondListener);
        second.setOnClickListener(goToSecondListener);
        third.setOnClickListener(goToSecondListener);
        fourth.setOnClickListener(goToSecondListener);
        fifth.setOnClickListener(goToSecondListener);
        sixth.setOnClickListener(goToSecondListener);
        seventh.setOnClickListener(goToSecondListener);
	}
	
	
	private void GoBack(){
		Intent intent=getIntent();
		intent.putExtra("time", timeCount);
		setResult(MyStatic.FINISHTHIS,intent);
		finish();
	}
    class ButtonBack implements OnClickListener{
		public void onClick(View v) {
			GoBack();
		}
    	
    }
	public void onBackPressed() {
		GoBack();
	}
	
    class ButtonRightListener implements OnClickListener{
		public void onClick(View arg0) {
			if(pop!=null && pop.isShowing()){
				pop.dismiss();
			}
			else{
	             pop.showAsDropDown(right_btn);
			}
		}
    }
    
    class ButtonChangeSchool implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(Shetuan_Subscribe_Main.this,School_List.class);
			startActivityForResult(intent, MyStatic.GOTOCHANGESCHOO);
			pop.dismiss();
		}
    }
    
    class ButtonSearch implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent = new Intent(Shetuan_Subscribe_Main.this,Shetuan_Search.class);
			startActivity(intent);
			pop.dismiss();
		}
    }
    
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MyStatic.GOTOCHANGESCHOO &&resultCode == MyStatic.SUCCESSTHIS){
			change_schoolId = data.getStringExtra("schoolId");
			change_schoolName = data.getStringExtra("schoolName");
			change_schoolBranch = data.getStringExtra("schoolBranch");
			Toast.makeText(Shetuan_Subscribe_Main.this,"现在在：" + change_schoolBranch + "_" + change_schoolName,Toast.LENGTH_SHORT).show();
		}
	}
    
    OnClickListener goToSecondListener =new OnClickListener() {
		public void onClick(View v) {
			Intent intent=new Intent(Shetuan_Subscribe_Main.this,SheTuan_Subscribe_Second.class);
			if(v==first){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_science));
				intent.putExtra("classx", "1");
			}
			else if(v==second){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_culture));
				intent.putExtra("classx", "2");
			}
			else if(v==third){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_outdoor));
				intent.putExtra("classx", "3");
			}
			else if(v==fourth){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_art));
				intent.putExtra("classx", "4");
			}
			else if(v==fifth){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_sport));
				intent.putExtra("classx", "5");
			}
			else if(v==sixth){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_mix));
				intent.putExtra("classx", "6");
			}
			else if(v==seventh){
				intent.putExtra("selectClass", getResources().getString(R.string.shetuandingyue_main_others));
				intent.putExtra("classx", "7");
			}
			startActivity(intent);
		}
	};
    
	
	private String getRandomHelpText(){
		String result = "";
    	int temp=(int)(Math.random()*10);
    	switch(temp%4){
    	case 0:
    		result=getResources().getString(R.string.shetuandingyue_help_1);break;
    	case 1:
    		result=getResources().getString(R.string.shetuandingyue_help_2);break;
    	case 2:
    		result=getResources().getString(R.string.shetuandingyue_help_3);break;
    	case 3:
    		result=getResources().getString(R.string.shetuandingyue_help_4);break;
    	}
    	return result;
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
