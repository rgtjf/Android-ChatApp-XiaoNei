package com.shoulder.xiaonei.tucao;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Text_Full extends Activity{

	private LinearLayout text_full_linearlayout;
	private TextView text_full_content;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_full);
		
		Bundle extras=getIntent().getExtras();
		String get_text=extras.getString(MyStatic.KEYNAME_Content);
		text_full_content=(TextView)findViewById(R.id.text_full_content);
		text_full_content.setText(get_text);
		
		initWidgets();
	}
	
	
	private void initWidgets(){
		text_full_linearlayout=(LinearLayout)findViewById(R.id.text_full_linearlayout);
		text_full_linearlayout.setOnClickListener(new ButtonBack());
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
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}

}
