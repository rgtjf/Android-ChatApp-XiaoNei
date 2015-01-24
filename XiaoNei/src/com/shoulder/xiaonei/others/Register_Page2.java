package com.shoulder.xiaonei.others;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.ExitApplication;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.others.Register_Page1.ButtonBack;
import com.shoulder.xiaonei.others.Register_Page1.TextViewSchool;
import com.umeng.analytics.MobclickAgent;

public class Register_Page2 extends Activity{
	
	private Button left_btn;
	
	private EditText phone_number;
	private EditText password;
	private EditText password_again;
	private EditText code;
	private Button confirm;
	
	private SharedPreferences preferences , preferences_schoolId;
	private SharedPreferences.Editor editor;
	
	private String get_schoolId="";
	private int get_sex = 0;
	private String get_nickName = "";
	
	private Boolean nameIsOk = false;//判断用户是否重复了
	private int codeCount = 0; //记录用户点击注册，显示“验证码错误”次数，当为3时，提醒“回去看看手机有没有输错”
	
	private MyVal mv ;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page2);  
        
        ExitApplication.getInstance().addActivity(this);
        
        Bundle extra = getIntent().getExtras();
        get_schoolId = extra.getString(MyStatic.KEYNAME_Register_Page_schoolId);
        get_sex = extra.getInt(MyStatic.KEYNAME_Register_Page_sex);
        get_nickName = extra.getString(MyStatic.KEYNAME_Register_Page_nickName);
        
        mv = (MyVal)getApplication();
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
	}
	
	private void initWidgets(){
        confirm=(Button)findViewById(R.id.confirm);
        password = (EditText)findViewById(R.id.password);
        password_again = (EditText)findViewById(R.id.password_again);
        phone_number = (EditText)findViewById(R.id.phone_number);
        code = (EditText)findViewById(R.id.code);
        
        phone_number.setOnFocusChangeListener(new EditTextPhoneNumberFocusChangeListener());
        //输入完手机号码后，焦点从editText框移开时就判断该手机号是否已被注册
        confirm.setOnClickListener(new ButtonConfirm());
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
	
	
	class EditTextPhoneNumberFocusChangeListener implements OnFocusChangeListener{
		public void onFocusChange(View arg0, boolean arg1) {
			if(arg1 == true){
				nameIsOk = false;
			}
			//如果失去焦点
			if(arg1 == false){
				final String this_name = phone_number.getText().toString().trim();
				if(!this_name.equals("")){
					new Thread(){
						public void run(){
							try
							{
								String getName="Home/regcode";
								NameValuePair param1 = new BasicNameValuePair("name", this_name);
								String getResponse = CustomerHttpClient.post(mv.getIpName() + getName, param1);
								if (getResponse.equals("reg already")){
									//确定手机号码是否被注册
									Looper.prepare();
									Toast.makeText(Register_Page2.this, "\"" + this_name + "\"" + "已被注册", Toast.LENGTH_SHORT).show();
									Looper.loop();
								}
								else if(getResponse.equals("true")){
									//发送验证码
									Looper.prepare();
									Toast.makeText(Register_Page2.this, "\"" + this_name + "\"" + "验证码已发送", Toast.LENGTH_SHORT).show();
									nameIsOk = true;
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
		}
	}
	
	
	class ButtonConfirm implements OnClickListener{
		public void onClick(View arg0) {
			final String this_phone = phone_number.getText().toString().trim();
			final String this_nickName = get_nickName;
			final String this_password = password.getText().toString().trim();
			final String this_password_again = password_again.getText().toString().trim();
			final String this_schoolId = get_schoolId;
			final String this_code = code.getText().toString().trim();
			final int this_sex = get_sex;
			
			if(this_nickName.equals("")){
				Toast.makeText(Register_Page2.this, "请输入昵称", Toast.LENGTH_SHORT).show();
			}
			else if(this_code.equals("")){
				Toast.makeText(Register_Page2.this, "请输入验证码", Toast.LENGTH_SHORT).show();
			}
			else if(this_phone.equals("")){
				Toast.makeText(Register_Page2.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			}
			else if(this_password.equals("")){
				Toast.makeText(Register_Page2.this, "请输入密码", Toast.LENGTH_SHORT).show();
			}
			else if(!this_password_again.equals(this_password)){
				Toast.makeText(Register_Page2.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
				password_again.setText("");
			}
			else if(nameIsOk == false){
				Toast.makeText(Register_Page2.this, "\"" + this_phone + "\"" + "已被注册", Toast.LENGTH_SHORT).show();
			}
			else{
//				phone_number.clearFocus();
				new Thread()
				{
					public void run()
					{
						try
						{
							String getName="Home/regx";
							NameValuePair param1 = new BasicNameValuePair("rinfcode", this_code);
							NameValuePair param2 = new BasicNameValuePair("passwd", this_password);
							NameValuePair param3 = new BasicNameValuePair("nickname", this_nickName);
							NameValuePair param4 = new BasicNameValuePair("schid", this_schoolId);
							NameValuePair param5 = new BasicNameValuePair("sex", this_sex +"");
							String getResponse = CustomerHttpClient.post(mv.getIpName() + getName, param1,param2,param3,param4,param5);
							if(getResponse.equals("true")){
								Looper.prepare();//把所有放入一个looper中，因为发现仅把对主线程有影响的
											 	 //toast放入looper，其它的操作没有进行
								
								mv.setLoginState(1);
								preferences = getSharedPreferences("user_account", MODE_PRIVATE);
								editor = preferences.edit();
								editor.putString("user_name", this_phone);
								editor.putString("user_password", this_password);
								editor.putInt("user_sex", this_sex);
								editor.commit();
								
								//缓存schoolId
								preferences_schoolId = getSharedPreferences("schoolId", MODE_PRIVATE);
								editor = preferences_schoolId.edit();
								editor.putString("school_id", this_schoolId);
								editor.commit();
								
								mv.setMySex(this_sex);
								mv.setSchoolId(this_schoolId);
								
								Toast.makeText(Register_Page2.this, "注册成功", Toast.LENGTH_SHORT).show();
								Intent intent=new Intent(Register_Page2.this,Main.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								Looper.loop();
							}
							else if (getResponse.equals("wrong code")){
								if(codeCount < 3){
									codeCount ++ ;
									Looper.prepare();
									Toast.makeText(Register_Page2.this, "验证码错误..", Toast.LENGTH_SHORT).show();
									Looper.loop();
								}
								else {
									Looper.prepare();
									Toast.makeText(Register_Page2.this, "验证码错误..回上一步看看手机号码是否输错了吧", Toast.LENGTH_LONG).show();
									Looper.loop();
								}
							}
							else {
								Looper.prepare();
								Toast.makeText(Register_Page2.this, "网络好像出了点问题..", Toast.LENGTH_SHORT).show();
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
