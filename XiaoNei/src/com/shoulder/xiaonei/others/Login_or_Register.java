package com.shoulder.xiaonei.others;

import java.util.Timer;
import java.util.TimerTask;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.R.style;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.ExitApplication;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Login_or_Register extends Activity{

	
	private Button left_btn_maybe;
	private Button login;
	private Button register;
	
	private MyVal mv;
	
	private Timer timer;
	private int timeCount = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_or_register);
		ExitApplication.getInstance().addActivity(this);
		
		mv = (MyVal)getApplication();
		
		initWidgets();
		
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, MyStatic.CloseTime);
	}
	
	
	private void initWidgets(){
		left_btn_maybe=(Button)findViewById(R.id.left_btn_maybe);
		left_btn_maybe.setOnClickListener(new ButtonBackMayBe());
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new ButtonLoginListener());
		register=(Button)findViewById(R.id.register);
		register.setOnClickListener(new ButtonRegsiterListener());
	}
	
	
	private void GoBackMayBe(){
		Intent intent = new Intent(Login_or_Register.this,Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mv.setInitPageIsInit(true);
		Toast.makeText(Login_or_Register.this, getResources().getString(R.string.main_remain_youCanChangeSchool), Toast.LENGTH_LONG).show();
		finish();
		startActivity(intent);
	}
	class ButtonBackMayBe implements OnClickListener{
		public void onClick(View arg0) {
			GoBackMayBe();
		}
	}
	public void onBackPressed() {
		GoBackMayBe();
	}

	class ButtonLoginListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.putExtra("where_come_from", "login_or_register");
			intent.setClass(Login_or_Register.this, Login.class);
			startActivity(intent);
		}
    }
	
	class ButtonRegsiterListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(Login_or_Register.this,Register_Page1.class);
			startActivity(intent);
		}
	}
    
    public boolean onCreateOptionsMenu1(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
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
