package com.shoulder.xiaonei.others;

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
import org.json.JSONArray;
import org.json.JSONObject;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.ExitApplication;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.LoginMachine;
import com.shoulder.xiaonei.others.Register_Page1.ButtonBack;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Login extends Activity{

	private Button left_btn;
	private Button login;

	private String where_come_from;
	
	private MyVal mv;
	private String ipName;
	
	private SharedPreferences preferences , preferences_schoolId;
	private SharedPreferences.Editor editor;
	
	private Boolean isConnect = false;//true表示连接到网络，登陆成功或账号密码不匹配
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);  
        
        ExitApplication.getInstance().addActivity(this);
        
        Bundle extras = getIntent().getExtras();
        where_come_from = extras.getString(MyStatic.KEYNAME_From);
        
        mv = (MyVal)getApplicationContext();
        ipName = mv.getIpName();
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
	}
	
	private void initWidgets(){
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new ButtonLogin());
	}

	
	private void GoBack(){
		finish();
	}
	class ButtonBack implements OnClickListener{
		public void onClick(View arg0) {
			GoBack();
		}
	}
	public void onBackPressed() {
		GoBack();
	}
	
	class ButtonLogin implements OnClickListener{
		public void onClick(View v) {
					final String name = ((EditText)findViewById(R.id.name)).getText().toString();
					final String pass = ((EditText)findViewById(R.id.password)).getText().toString();
					
					Timer timerConnect = new Timer();
					TimerTask timerTask = new TimerTask() {
						public void run() {
							if(isConnect == false){
								Looper.prepare();
								Toast.makeText(Login.this, "网络连接失败", Toast.LENGTH_SHORT).show();
								Looper.loop();
							}
						}
					};
					timerConnect.schedule(timerTask, MyStatic.LoadingPost);
					
					new Thread()
					{
						@Override
						public void run()
						{
							try
							{
								String getName="home/login";
								NameValuePair param1 = new BasicNameValuePair("name", name);
								NameValuePair param2 = new BasicNameValuePair("passwd", pass);
								String getResponse = CustomerHttpClient.post(mv.getIpName() + getName, param1,param2);
								isConnect = true;
								if(getResponse.equals("false")){
									Looper.prepare();
									Toast.makeText(Login.this, "账号密码不匹配", Toast.LENGTH_SHORT).show();
									Looper.loop();
								}
								else {
									Looper.prepare();
									
									preferences = getSharedPreferences("user_account", MODE_PRIVATE);
									editor = preferences.edit();
									editor.putString("user_name", name);
									editor.putString("user_password", pass);
									editor.commit();
									
									
									if(where_come_from.equals("Main") || where_come_from.equals("set_up_change_password")|| where_come_from.equals("login_or_register")){
										LoginMachine.Login();
										Intent intent=new Intent(Login.this,Main.class);
										Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										finish();
										startActivity(intent);
									}else {
										finish();
									}
									Looper.loop();
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}.start();
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
