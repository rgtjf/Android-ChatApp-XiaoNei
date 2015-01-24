package com.shoulder.xiaonei.others;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Set_Up extends Activity{

	protected File cache;
	private Button clear;
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Button logOff;
	private MyVal mv;
	
	private Button changePassword;
	
	private Button giveMeFive;
	
	private Button left_btn;
	private RelativeLayout right_rel;
	private TextView text;
	
    private Timer timer;
    private int timeCount;
	
	private Dialog mDialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.set_up);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        mv =(MyVal)getApplicationContext();
        initTitle();
        initWidgets();
        
        //进入计时，3秒内后退不关闭首页的menu
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, 3000);
        
		Dialog_Register_Login.Set_Dialog_Register_Login(Set_Up.this,"set_up");
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText("设置");
	}
	
	private void initWidgets(){
        cache = new File(mv.getMyDir(), mv.getFileNameHo()); 
        clear=(Button)findViewById(R.id.clear);
        clear.setOnClickListener(new ButtonClear());
        
        logOff=(Button)findViewById(R.id.logOff);
        logOff.setOnClickListener(new ButtonLogOff());
        
        changePassword=(Button)findViewById(R.id.change_password);
        giveMeFive=(Button)findViewById(R.id.giveMeFive);
        changePassword.setOnClickListener(new ButtonChangePassword());
        giveMeFive.setOnClickListener(new ButtonGiveMeFive());
	}
	
	
	private void GoBack(){
		Intent intent=getIntent();
		intent.putExtra("time", timeCount);
		setResult(MyStatic.FINISHTHIS,intent);
		finish();
	}
	class ButtonBack implements OnClickListener{
		public void onClick(View arg0) {
			GoBack();
		}
	}
	public void onBackPressed(){
		GoBack();
	}
	
	
	class ButtonClear implements OnClickListener{
		public void onClick(View arg0) {
				//清空缓存
				File[] files = cache.listFiles();
				if(files!=null){
						for(File file :files){
							file.delete();
						}
						cache.delete();
					}
				//删除旧版文件夹下的图片，下个版本去掉
				File cache_old = new File(mv.getMyDir(), "sheyouApp"); 
				File[] files_old = cache_old.listFiles();
				if(files_old!=null){
						for(File file :files_old){
							file.delete();
						}
						cache_old.delete();
					}
				Toast.makeText(Set_Up.this, "缓存清理完毕", Toast.LENGTH_SHORT).show();
		}
	}
	
	class ButtonLogOff implements OnClickListener{
		public void onClick(View arg0) {
			
			if(mv.getLoginState() == 1){
				preferences = getSharedPreferences("user_account", MODE_PRIVATE);
				editor = preferences.edit();
				editor.clear();
				editor.commit();
				
				new Thread()
				{
					public void run()
					{
						try
						{
							String getName="Home/logout";
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName);
								if(getResponse.equals("logout sucess")){
									mv.setLoginState(0);
								}
							Looper.prepare();
							Intent intent=new Intent(Set_Up.this,Login_or_Register.class);
							Toast.makeText(Set_Up.this, "注销成功", Toast.LENGTH_SHORT).show();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							Looper.loop();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
			else if (mv.getLoginState() == 0){
				Toast.makeText(Set_Up.this, "当前无账号登陆", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	
	class ButtonChangePassword implements OnClickListener{
		public void onClick(View arg0) {
			if(mv.getLoginState() == 0){
	     		 if (mDialog == null) 
	      		{
	      			mDialog = Dialog_Register_Login.getMyDialog();
	      			mDialog.show();
	      		}
	      		else 
	      		{
	      			mDialog.show();
	      		}
			}else {
				Intent intent = new Intent(Set_Up.this,Set_Up_Change_Password.class);
				startActivity(intent);
			}
		}
	}
	
	class ButtonGiveMeFive implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent = new Intent(Set_Up.this,Set_Up_GiveMeFive.class);
			startActivity(intent);
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
