package com.shoulder.xiaonei.myZone;

import java.io.File;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import u.aly.ba;

import chat.Chat;
import chat.Chat_GourpMember;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SendCallback;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_myView.CircularImage;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.MovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YourZone extends Activity{
	
	private String jResult = "";
	private final static int MSG_getYourInformation = 0x701;
	private final static int MSG_guanzhu = 0x114;
	private String methodName_getYourInformation = "getYourInformation";
	
	private Dialog myDialog_login_and_register;

	private Button back;
	private Button guanzhu;
	private Button letsChat;
	private CircularImage photo_before_big;
	private CircularImage photo_before_small;
	private TextView yourName;
	private TextView yourIntroduce;
	private ImageView backgroundTop;
	private AsyncImageLoader asyncImageLoader;
	
	private String getId = "";
	private String jGetName = "";
	private String jGetPhotoUrl = "";
	private String jGetIntroduce = "";
	private int jGetYourSex = 0;
	
	private int currentX;//手势操作的移动量
	private int currentY;
	private int moveDistanceX;
	private int moveDistanceY;
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone_yours);
		
		Bundle extras = getIntent().getExtras();
		getId = extras.getString(MyStatic.KEYNAME_SingleId);
		
		Dialog_Register_Login.Set_Dialog_Register_Login(YourZone.this,"YourZone");
		
		initWidgets();
		
		jGet(methodName_getYourInformation);
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			jResult = msg.obj.toString();
			switch(msg.what){
			case MSG_getYourInformation:
				initYourInformation();
				break;
			case MSG_guanzhu:
				String response = msg.obj.toString();
				String toast_msg = "";
				if(response.equals("false")){
					toast_msg = getString(R.string.yourZone_guanzhu_fail);
				}else if (response.equals("true")){
					toast_msg = getString(R.string.yourZone_guanzhu_success);
				}
				Toast.makeText(YourZone.this, toast_msg, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	public void jGet(final String mtName){
		new Thread(){
			public void run(){
				String getMethod="";
				String getParams="";
				if(mtName.equals(methodName_getYourInformation)){
					getMethod = "manage/get_member_info";
					getParams = "mid=" + getId;
				}
				try{
					String result = CustomerHttpClient.get(MyVal.getApplication().getIpName()+getMethod+"?"+getParams);
					Message msg = new Message();
					if(mtName.equals(methodName_getYourInformation) ){
						msg.what = MSG_getYourInformation;
					}
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch(Exception e){
				}
			}
		}.start();
	}
	
	
	private void initWidgets(){
		back = (Button)findViewById(R.id.left_btn);
		guanzhu = (Button)findViewById(R.id.guanzhu);
		letsChat = (Button)findViewById(R.id.let_us_chat);
		photo_before_big = (CircularImage)findViewById(R.id.photo_before_bigger);
		photo_before_small = (CircularImage)findViewById(R.id.photo_before_smaller);
		yourName = (TextView)findViewById(R.id.yourName);
		yourIntroduce = (TextView)findViewById(R.id.yourIntroduce);
		backgroundTop = (ImageView)findViewById(R.id.background_top);
		
		back.setOnClickListener(new ButtonOnClickListenerL());
		guanzhu.setOnClickListener(new ButtonGuanzhu());
		letsChat.setOnClickListener(new ButtonGoToChat());
		photo_before_big.setImageResource(R.drawable.white);
		photo_before_small.setImageResource(R.drawable.white);
	}
	
	
	private void initYourInformation(){
		try {
			JSONObject jsonObject;
	    	jsonObject = new JSONObject(jResult);
	    	
	    	//获得个人简单信息
	    	JSONArray jsonArray=jsonObject.getJSONArray("mem_info");
	    	JSONObject jsonpet = jsonArray.getJSONObject(0);
	    	jGetName = jsonpet.getString("nickname");
	    	jGetPhotoUrl = jsonpet.getString("photo");
	    	jGetIntroduce = jsonpet.getString("introduce");
	    	jGetYourSex = jsonpet.getInt("sex");
	    	yourName.setText(jGetName);
	    	yourIntroduce.setText(jGetIntroduce);
	    	
	        //图片缓存
	    	asyncImageLoader = new AsyncImageLoader(YourZone.this);
			final String imageUrl = jGetPhotoUrl;
			photo_before_small.setTag(imageUrl);
	        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
	        		photo_before_small); 
	        //说明此图片已被下载并缓存
	        if(drawable!=null)
	        	photo_before_small.setImageDrawable(drawable); 
		}
		catch(Exception e){
		}
	}
	
	
    private void GoBack(){
		finish();
    }
    class ButtonOnClickListenerL implements OnClickListener{
		public void onClick(View v) {
			GoBack();
		}
    }
	public void onBackPressed() {
		GoBack();
	}
	
	class ButtonGuanzhu implements OnClickListener{
		public void onClick(View v) {
			//发送请求到我们的后台，关注此人
			if(MyVal.getApplication().getLoginState() == 0){
	    		 if (myDialog_login_and_register == null) 
	     		{
	     			myDialog_login_and_register = Dialog_Register_Login.getMyDialog();
	     			myDialog_login_and_register.show();
	     		}
	     		else 
	    		{
	    			myDialog_login_and_register.show();
	    		}
			}else {
			new Thread()
			{
				public void run()
				{
					try
					{
						String getName = "Manage/add_per_gz";
						NameValuePair param1 = new BasicNameValuePair("loginid", getId);
						String getResponse = CustomerHttpClient.post(MyVal.getApplication().getIpName()+getName, param1);
						Message msg = new Message();
						msg.what = MSG_guanzhu;
						msg.obj = getResponse;
						handler.sendMessage(msg);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}.start();
			}
			
			//获得installation值，准备推送
			AVQuery<AVObject> query = new AVQuery<AVObject>(MyStatic.LeanCloud_myUser);
			query.whereEqualTo(MyStatic.LeanCloud_myUser_Name, getId);
			query.findInBackground(new FindCallback<AVObject>() {
				public void done(List<AVObject> avObjects, AVException e) {
					if(e == null){
						String installationId = avObjects.get(0).getString(MyStatic.LeanCloud_myUser_Installation);
						pushGuanZhu(installationId);
					}
					else {
					}
				}
			});
		}
	}
	//发送请求到leanCloud，推送通知
	private void pushGuanZhu(String installationId){
		try{
			AVPush push = new AVPush();
			AVQuery<AVInstallation> pushQuery = AVInstallation.getQuery();
			pushQuery.whereEqualTo(MyStatic.LeanCloud_Installation_installationId, installationId);
			push.setQuery(pushQuery);
			
			JSONObject jsonObect = new JSONObject();
			jsonObect.put(MyStatic.KEYNAME_Push_Action, MyStatic.LeanCloud_Push_Action_GuanZhu);
			jsonObect.put(MyStatic.KEYNAME_Push_Icon, MyVal.getApplication().getMyAvator());
			jsonObect.put(MyStatic.KEYNAME_Push_Title, MyVal.getApplication().getMyName() + getString(R.string.push_guanzhu_title));
			String sex = "";
			if(MyVal.getApplication().getMySex() == MyStatic.Girl){
				sex = "她";
			}
			else if(MyVal.getApplication().getMySex() == MyStatic.Boy){
				sex = "他";
			}
			jsonObect.put(MyStatic.KEYNAME_Push_Content, "快回复" + sex + "吧");
			//根据手机号确定notificationId
			String tempId_s = MyVal.getApplication().getMyId().substring(6);
			int tempId_i = Integer.parseInt(tempId_s);
			jsonObect.put(MyStatic.KEYNAME_Push_NotificationId, tempId_i + MyStatic.LeanCloud_Push_NotificationId_Guanzhu);
			push.setData(jsonObect);
			
			//关注独有的部分
			jsonObect.put(MyStatic.KEYNAME_SingleId, MyVal.getApplication().getMyId());
			jsonObect.put(MyStatic.KEYNAME_SingleName, MyVal.getApplication().getMyName());
			
			push.sendInBackground(new SendCallback() {
				public void done(AVException arg0) {
					Toast.makeText(YourZone.this, "send successfully", Toast.LENGTH_SHORT).show();
				}
			});
		}
		catch(Exception e){
		}
		
	}
	
	class ButtonGoToChat implements OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(YourZone.this, Chat.class);
			intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_SINGLE);
			intent.putExtra(MyStatic.KEYNAME_SingleId, getId);
			intent.putExtra(MyStatic.KEYNAME_SingleName, jGetName);
			intent.putExtra(MyStatic.KEYNAME_Avator, jGetPhotoUrl);
			startActivity(intent);
		}
	}
	
	
	//滑动显示该人信息
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			currentX = (int)event.getRawX();
//			currentY = (int)event.getRawY();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int afterX = (int)event.getRawX();
//			int afterY = (int)event.getRawY();
//			moveDistanceX = currentX - afterX;
//			moveDistanceY = currentY - afterY;
//			
//			backgroundTop.scrollBy(0, moveDistanceY);
//			yourName.scrollBy(0, moveDistanceY);
//			yourIntroduce.scrollBy(0, moveDistanceY);
//			
//			photo_before_small.scrollBy(moveDistanceX, moveDistanceY);
//			photo_before_big.scrollBy(moveDistanceX, moveDistanceY);
//			
//			
//			
//			if(letsChat.getVisibility() == 0 && moveDistanceY > 50){
//				letsChat.setVisibility(8);
//			}
//			else if(letsChat.getVisibility() == 8 &&   moveDistanceY < 50){
//				letsChat.setVisibility(0);
//			}
//			
//			currentX = afterX;
//			currentY =afterY;
//			break;
//		case MotionEvent.ACTION_UP:
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
