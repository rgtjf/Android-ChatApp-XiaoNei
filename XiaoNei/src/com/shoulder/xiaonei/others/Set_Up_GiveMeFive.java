package com.shoulder.xiaonei.others;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Set_Up_GiveMeFive extends Activity{
	
	private Button right_btn;
	private Button left_btn;
	private TextView right_btn_text;
	private EditText search_edit;
	private ImageView search_line;
	
	private EditText advice_EditText;
	private TextView advice_TextView;
	
	private MyVal mv;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.set_up_givemefive);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_search);
        
        mv = (MyVal)getApplicationContext();
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        right_btn=(Button)findViewById(R.id.right_btn);
        left_btn=(Button)findViewById(R.id.left_btn);
        right_btn_text = (TextView)findViewById(R.id.function_text);
        right_btn_text.setText("发送");
        left_btn.setOnClickListener(new ButtonBack());
        right_btn.setOnClickListener(new ButtonSend());
	}
	
	private void initWidgets(){
        search_edit = (EditText)findViewById(R.id.search);
        search_line = (ImageView)findViewById(R.id.search_line);
        search_line.setVisibility(8);
        search_edit.setVisibility(4);
        advice_EditText = (EditText)findViewById(R.id.advice_editText);
        advice_TextView = (TextView)findViewById(R.id.advice_textView);
	}
	
	
	private void GoBack(){
		finish();
	}
	class ButtonBack implements android.view.View.OnClickListener{
		public void onClick(View arg0) {
			GoBack();
		}
	}
	public void onBackPressed(){
		GoBack();
	}
	
	class ButtonSend implements OnClickListener{
		public void onClick(View arg0) {
			final String advice = advice_EditText.getText().toString().trim();
			if(!advice.equals("")){
				
				advice_TextView.setText(advice);
				advice_EditText.clearFocus();
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Set_Up_GiveMeFive.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(advice_EditText.getWindowToken(), 0);
				
				new Thread(){
					public void run(){
						try
						{
							String getName="Home/Addfankui";
							NameValuePair param1 = new BasicNameValuePair("content", advice);
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1);
								if(getResponse.equals("true")){
								}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
				
				//使paper消失
				Animation_Gone(advice_EditText);
				
				//在纸片消失后toast消息
				Timer timerToast = new Timer();
				TimerTask timerToastTask = new TimerTask() {
					public void run() {
						Looper.prepare();
						Toast.makeText(Set_Up_GiveMeFive.this, getResources().getString(R.string.give_me_five_thanks_word), Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				};
				timerToast.schedule(timerToastTask,MyStatic.GiveMeFive_Toast);
				
			}
		}
	}
	
	private void Animation_Gone(final View view){
		final Animation animation = (Animation) AnimationUtils.loadAnimation(Set_Up_GiveMeFive.this, R.anim.out_item_alpha);
	    animation.setAnimationListener(new AnimationListener() {   
	          public void onAnimationStart(Animation animation) {}   
	          public void onAnimationRepeat(Animation animation) {}   
	          public void onAnimationEnd(Animation animation) {
	        	  view.setVisibility(4);
	              animation.cancel();   
	          }   
	      });   
	    view.setAnimation(animation);
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
