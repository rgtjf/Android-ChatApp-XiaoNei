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
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Set_Up_Change_Password extends Activity{
	
	private Button left_btn;
	private Button right_btn;
	private TextView text;
	private ImageView function_image;
	
	private EditText changeSomething;
	private EditText change_checkOut;
	
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
        function_image.setVisibility(4);
        text.setText("更改密码");
        right_btn.setText("更改");
        right_btn.setOnClickListener(new ButtonChange());
	}
	
	private void initWidgets(){
        changeSomething=(EditText)findViewById(R.id.changeSomething);
        change_checkOut=(EditText)findViewById(R.id.change_checkOut);
        left_btn.setOnClickListener(new ButtonBack());
        changeSomething.setHint("输入新密码..");
        change_checkOut.setHint("请再输入一遍密码..");
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
				Toast.makeText(Set_Up_Change_Password.this, getResources().getString(R.string.set_up_change_password_editIsNull),Toast.LENGTH_SHORT).show();
			}
			else if(!change_checkOut.getText().toString().trim().equals(changeSomething.getText().toString().trim())){
				Toast.makeText(Set_Up_Change_Password.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
				change_checkOut.setText("");
			}
			else{
				
			new Thread()
			{
				public void run()
				{
					try
					{
						String getName="Manage/change_password";
						NameValuePair param1 = new BasicNameValuePair("passwd", changeSomething.getText().toString());
						String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1);
						if(getResponse.equals("true")){
								
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
										}
										catch (Exception e)
										{
											e.printStackTrace();
										}
									}
							}.start();
								
								Looper.prepare();
								Toast toast=Toast.makeText(Set_Up_Change_Password.this,"修改成功，请重新登陆", Toast.LENGTH_SHORT);
								toast.show();
								Intent intent = new Intent(Set_Up_Change_Password.this,Login.class);
								intent.putExtra("where_come_from", "set_up_change_password");
								startActivity(intent);
								finish();
								Looper.loop();
							}
						else {
								Toast.makeText(Set_Up_Change_Password.this, "哎呀连不上服务器", Toast.LENGTH_LONG).show();
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
