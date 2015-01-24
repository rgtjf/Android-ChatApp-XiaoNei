package com.shoulder.xiaonei.tucao;

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
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.others.Set_Up_GiveMeFive;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TuCaoQiang_Comment_Fabu extends Activity{

	private Button left_btn;
	private Button right_btn;
	private ImageView function_image;
	private TextView text;
	
	private String ipName;
	
	private int get_tucao_id = -1;
	private Boolean threadIsWork = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.tucaoqiang_comment_fabu);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        MyVal mv = (MyVal)getApplicationContext();
        ipName = mv.getIpName();
        
        initTitle();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
        right_btn=(Button)findViewById(R.id.right_btn);
        function_image=(ImageView)findViewById(R.id.function_image);
        function_image.setImageResource(R.drawable.done);
        text=(TextView)findViewById(R.id.text);
        
        text.setText("发表吐槽");
        right_btn.setOnClickListener(new ButtonTuCaoFaBu());
	}
	
	
	private void GoBack(){
		setResult(MyStatic.FINISHTHIS);
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
	
	
	class ButtonTuCaoFaBu implements OnClickListener{
		public void onClick(View arg0) {
			right_btn.setClickable(false);
			EditText myEdit = (EditText)findViewById(R.id.myEditText);
			final String myEditText = myEdit.getText().toString();
			if(myEditText.trim().equals("")){
				Toast.makeText(TuCaoQiang_Comment_Fabu.this,getResources().getString(R.string.tucaoqiang_comment_fabu_editIsNull), Toast.LENGTH_SHORT).show();
			}
			else{
			
			//关闭虚拟键盘
			myEdit.clearFocus();
			InputMethodManager inputmanger = (InputMethodManager)getSystemService(Set_Up_GiveMeFive.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(myEdit.getWindowToken(), 0);
				
			//计时器，3秒后没有发送到服务器则提醒网络异常
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					if(threadIsWork == true){
						Looper.prepare();
						Toast.makeText(TuCaoQiang_Comment_Fabu.this, "网络出了点问题",Toast.LENGTH_SHORT).show();
						right_btn.setClickable(true);
						Looper.loop();
					}
				}
			};
			timer.schedule(timerTask, MyStatic.TuCaoQiang_Comment_Fabu_SendMessageTime);
			
			
			new Thread()
			{
				public void run()
				{
					try
					{
						threadIsWork = true;
						String getName="Tucao/insert_tocao";
						NameValuePair param1 = new BasicNameValuePair("content", myEditText);
						String getResponse = CustomerHttpClient.post(ipName+getName, param1);
							Looper.prepare();
							if(getResponse.equals("true")){
								threadIsWork = false;
								Toast.makeText(TuCaoQiang_Comment_Fabu.this, "发布成功", Toast.LENGTH_SHORT).show();
								Intent intent = getIntent();
								setResult(MyStatic.SUCCESSTHIS);
								finish();
							}
							else{
								threadIsWork = false;
								Toast.makeText(TuCaoQiang_Comment_Fabu.this, "网络出了点问题",Toast.LENGTH_SHORT).show();
								right_btn.setClickable(true);
							}
							Looper.loop();
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
