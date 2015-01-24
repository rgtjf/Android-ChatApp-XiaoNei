package com.shoulder.xiaonei.others;

import java.util.ArrayList;
import java.util.List;

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
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.others.Set_Up_Change_Password.ButtonBack;
import com.shoulder.xiaonei.others.Set_Up_Change_Password.ButtonChange;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Set_up_Change_Phone extends Activity{
	
	private Button left_btn;
	private Button right_btn;
	private TextView text;
	private ImageView function_image;
	
	private EditText changeSomething;
	
	private MyVal mv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.set_up_change);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        mv = (MyVal)getApplicationContext();
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        right_btn=(Button)findViewById(R.id.right_btn);
        function_image=(ImageView)findViewById(R.id.function_image);
        text=(TextView)findViewById(R.id.text);
        left_btn.setOnClickListener(new ButtonBack());
        text.setText("更改手机号");
        function_image.setVisibility(4);
        right_btn.setText("更改");
        right_btn.setOnClickListener(new ButtonChange());
	}
	
	private void initWidgets(){
        changeSomething=(EditText)findViewById(R.id.changeSomething);
        changeSomething.setHint("输入新手机号..");
        changeSomething.setInputType(InputType.TYPE_CLASS_PHONE);
	}
	
	
	private void GoBack(){
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
	
	class ButtonChange implements OnClickListener{
		public void onClick(View arg0) {
			if(changeSomething.getText().toString().trim().equals("")){
				Toast.makeText(Set_up_Change_Phone.this, getResources().getString(R.string.set_up_change_phone_editIsNull),Toast.LENGTH_SHORT).show();
			}
			else {
				
			new Thread()
			{
				public void run()
				{
					try
					{
						String getName="Manage/change_mobile";
						NameValuePair param1 = new BasicNameValuePair("phone", changeSomething.getText().toString());
						String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1);
							if(getResponse.equals("true")){
								Looper.prepare();
								Toast toast=Toast.makeText(Set_up_Change_Phone.this,"更改成功", Toast.LENGTH_SHORT);
								toast.show();
								finish();
								Looper.loop();
							}else {
								Looper.prepare();
								Toast.makeText(Set_up_Change_Phone.this,"出了些问题，重新登陆试试吧", Toast.LENGTH_LONG).show();
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
