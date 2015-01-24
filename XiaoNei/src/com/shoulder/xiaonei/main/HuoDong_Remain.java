package com.shoulder.xiaonei.main;

import java.io.File;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HuoDong_Remain extends Activity{

	private Button left_btn;
	
	private ImageView image;
	private TextView title;
	private TextView introduce;
	private TextView time;
	private TextView address;
	
	private String get_uri;
	private String get_title;
	private String get_introduce;
	private String get_time;
	private String get_address;
	
	private MyVal mv;
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huodong_detail_remain);
        
        Bundle extras = getIntent().getExtras();
        get_uri = extras.getString("uri");
        get_title = extras.getString("title");
        get_introduce = extras.getString("introduce");
        get_time = extras.getString("time");
        get_address = extras.getString("address");
        
        mv = (MyVal)getApplicationContext();
        
        initTitle();
        initHuoDong();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
	}
	
	private void initHuoDong(){
        image=(ImageView)findViewById(R.id.image);
        title=(TextView)findViewById(R.id.title);
        introduce=(TextView)findViewById(R.id.introduce);
        time=(TextView)findViewById(R.id.time);
        address=(TextView)findViewById(R.id.address);
        title.setText(get_title);
        introduce.setText(get_introduce);
        time.setText(TimeMachining.DateTranslator(get_time));
        address.setText(get_address);
    	//从缓存中读取图片
        //这里需要改进，可能存在活动提醒的时候缓存已经被清除的情况，此时需要从服务器获取图片
        File file = new File(mv.getMyDir()+mv.getFileName(), get_uri);
        Uri uri = Uri.fromFile(file);
	    image.setImageURI(uri);
	    image.setOnClickListener(new ButtonImage());
	}
	
	
	private void GoBack(){
		Intent intent = new Intent(HuoDong_Remain.this,Main.class);
		startActivity(intent);
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
	
	class ButtonImage implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(HuoDong_Remain.this, Image_HuoDong.class);
			intent.putExtra("title_text", get_title);
			intent.putExtra("uri", get_uri);
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
