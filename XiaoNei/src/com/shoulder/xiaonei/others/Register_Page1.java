package com.shoulder.xiaonei.others;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.ExitApplication;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Register_Page1 extends Activity{

	private Button left_btn;
	
	private TextView school;
	private RadioGroup radioGroup;
	private EditText nickname;
	private Button next;
	
	private String schoolId="";
	private String schoolName="";
	private String schoolBranch="";
	private int sexState = 0;//0表示女，1表示男
	
	private MyVal mv ;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page1);
        
        ExitApplication.getInstance().addActivity(this);
        
        mv = (MyVal)getApplication();
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
	}
	
	private void initWidgets(){
        school=(TextView)findViewById(R.id.school);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup1); 
        nickname = (EditText)findViewById(R.id.nickname);
        next=(Button)findViewById(R.id.next);
        school.setOnClickListener(new TextViewSchool());
        next.setOnClickListener(new ButtonNext());
        radioGroup.setOnCheckedChangeListener(new RadioGroupOnCheckChangeListener());
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
	
	class TextViewSchool implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(Register_Page1.this,School_List.class);
			startActivityForResult(intent, MyStatic.GOTOCHANGESCHOO);
		}
	}
	
	class RadioGroupOnCheckChangeListener implements OnCheckedChangeListener{
		public void onCheckedChanged(RadioGroup arg0, int checkedId) {
			switch (checkedId) {
			case R.id.radioGirl:
				sexState = 0;
				break;
			case R.id.radioBoy:
				sexState = 1;
				break;
			}
		}
	}
	
	class ButtonNext implements OnClickListener{
		public void onClick(View arg0) {
			final String this_schoolId = schoolId;
			final int this_sex = sexState;
			final String this_nickName = nickname.getText().toString().trim();
			
			if(this_schoolId.equals("")){
				Toast.makeText(Register_Page1.this, "请选择学校", Toast.LENGTH_SHORT).show();
			}
			else if(this_nickName.equals("")){
				Toast.makeText(Register_Page1.this, "请输入昵称", Toast.LENGTH_SHORT).show();
			}
			else{
				Intent intent = new Intent(Register_Page1.this,Register_Page2.class);
				intent.putExtra(MyStatic.KEYNAME_Register_Page_schoolId, this_schoolId);
				intent.putExtra(MyStatic.KEYNAME_Register_Page_sex, this_sex);
				intent.putExtra(MyStatic.KEYNAME_Register_Page_nickName, this_nickName);
				startActivity(intent);
			}
		}
	}
	
	
	//选择完学校后返回该页面携带的信息
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MyStatic.GOTOCHANGESCHOO &&resultCode == MyStatic.SUCCESSTHIS){
			schoolId = data.getStringExtra("schoolId");
			schoolName = data.getStringExtra("schoolName");
			schoolBranch = data.getStringExtra("schoolBranch");
			school.setText(schoolName + " _" + schoolBranch); 
			school.setTextColor(0xff000000);
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
