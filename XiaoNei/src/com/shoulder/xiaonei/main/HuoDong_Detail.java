package com.shoulder.xiaonei.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chat.Chat;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.dimen;
import com.shoulder.xiaonei.R.drawable;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.R.menu;
import com.shoulder.xiaonei.R.string;
import com.shoulder.xiaonei.R.style;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.main.Main.ButtonLogin;
import com.shoulder.xiaonei.main.Main.ButtonRegister;
import com.shoulder.xiaonei.myClass.AlarmReceiver;
import com.shoulder.xiaonei.myClass.ConnectionDetector;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.myClass_myView.MyScrollView;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.others.Register_Page1;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.*;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HuoDong_Detail extends Activity {

	private String methodName;
	protected SharedPreferences preferences;
	protected SharedPreferences.Editor editor;
	protected File cache;
	private String jResult = "";
	private Handler mhandler;
	
	private MyVal mv;
	private  String ipName="";
	private String myDir="";
	
	private com.shoulder.xiaonei.myClass_myView.MyScrollView connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private TextView name;
	private TextView when;
	private TextView where;
	private TextView who;
	private TextView content;
	private ImageView image_view;
	private Button image_button;

	private Button left_btn;
	private RelativeLayout right_rel;
	private Button right_btn;
	private ImageView right_img;
	private TextView text;
	
	private SlidingMenu menu;
	private Button more;
	private TextView org_name;
	private TextView org_introduce;
	private Button count_member;
	private ImageView org_logo;
	
	private Dialog myDialog_login_and_register;
	
	private RelativeLayout guanzhu;
	private TextView guanzhu_text;
	private TextView text_boyCount;
	private TextView text_girlCount;
	private int mySex = 0;
	private Dialog myDialog;
	private Button ok;
	private Button no;
	private String getName_Guanzhu_Baoming="";
	private RelativeLayout linearlayout_org;//点击主办方拉出社团介绍
	private ImageView huodong_stripe;//主办方左边横条颜色
	private RelativeLayout linearLayout_huodong_chat;
	
	private String get_name;
	private String get_time;
	private String get_address;
	private int get_myId;
	private int get_org_id = -1;
	private String get_uri;
	private String get_orgName;
	private String get_org_countmember;
	private String get_org_introduce;
	private String get_org_logo_url;
	private String get_org_rate;
	private String get_activity_content;
	private int get_girlLimit;
	private int get_boyLimit;
	private int get_girlCount;
	private int get_boyCount;
	
	private ConnectionDetector cd;
	
	private UMSocialService mController;
	
	private int dingyue_state = 0;//0表示未订阅社团，1表示已订阅
	private int guanzhu_state = 0;//0表示未关注活动，1表示关注活动
	
//	private Boolean httpBlock = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.d_detail_huodong);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        Intent intent=getIntent();
        
        Bundle extra=getIntent().getExtras();
        get_name=extra.getString("name");
        get_time=extra.getString("time");
        get_address=extra.getString("address");
        get_myId=extra.getInt("mId");
        get_org_id=extra.getInt("orgId");
        get_uri=extra.getString("uri");
        
        initSlidingMenu();
        
        mv=((MyVal)getApplicationContext());
        ipName=mv.getIpName();
        myDir=mv.getMyDir();
        
        Dialog_Register_Login.Set_Dialog_Register_Login(HuoDong_Detail.this,"huodong_detail");
        
        initLoading();
        initHuoDong();
        initShetuan();
        initDialogGuanzhu();
        initTitle();
        
        //页面缓存，现在还没使用
    	preferences = getSharedPreferences("jResult", MODE_PRIVATE);
    	editor = preferences.edit();
    	cache = new File(myDir, "sheyou");  
        if(!cache.exists()){cache.mkdirs();}
        
        cd = new ConnectionDetector(getApplicationContext());
        if(cd.isConnectingToInternet()==true)
        {
        	methodName = "Activity/get_activityx_info";
        	jGet(methodName);
        }
        else
        {
        		jResult = preferences.getString(methodName+get_myId, null);
        }
	}
	
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			jResult = msg.obj.toString();
			switch(msg.what)
			{
			case 0x000:
 				changeLayout();
				break;
			case 0x1121:
				changeLayout();
				BuildHuodong();
				break;
			case 0x1122:
				BuildShetuan();
				break;
			case 0x114:
				String response = msg.obj.toString();
				String toast_msg = "";
				if(response.equals("false")){
					toast_msg = "网络异常";
				}else if (response.equals("true")){
					count_member.setVisibility(4);
					toast_msg = "订阅成功";
				}
				Toast.makeText(HuoDong_Detail.this,toast_msg, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
    public void jGet(final String mtName)
    {
    	new Thread()
		{
			public void run()
			{
				String getParams="";
				if(mtName.equals("Activity/get_activityx_info")){
					getParams="actid="+get_myId;
//					httpBlock = true;
				}
				else if (mtName.equals("Organization/get_orgx_info")){
					getParams="orgid="+get_org_id;
				}
				try
				{
					String result = CustomerHttpClient.get(ipName+mtName+"?"+getParams);
					Message msg = new Message();
					if(mtName.equals("Activity/get_activityx_info")){
						msg.what = 0x1121;
					}else if(mtName.equals("Organization/get_orgx_info")){
						msg.what = 0x1122;
					}
//					httpBlock = false;
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				editor.putString(mtName, jResult);
				editor.commit();
			}
		}.start();
    }
	
	
	private void initLoading(){
        connectSuccessLayout=(com.shoulder.xiaonei.myClass_myView.MyScrollView)findViewById(R.id.connectSuccessLayout);
        loading=(RelativeLayout)findViewById(R.id.loading);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        failToConnect=(TextView)findViewById(R.id.failToConnect);
        failToConnect.setOnClickListener(new TextFailToConnect());
        timing();
	}
	
	private void initHuoDong(){
        name=(TextView)findViewById(R.id.name);
        when=(TextView)findViewById(R.id.time);
        who=(TextView)findViewById(R.id.who);
        where=(TextView)findViewById(R.id.address);
        content=(TextView)findViewById(R.id.content);
        image_view=(ImageView)findViewById(R.id.image_view);
        image_button=(Button)findViewById(R.id.image_button);
        image_button.setOnClickListener(new ImageOnClickListener());
        
        name.setText(get_name);
        when.setText(TimeMachining.DateTranslator(get_time));
        where.setText(get_address);
        try{
	        File file = new File(myDir+mv.getFileName(), get_uri);
	        Uri uri = Uri.fromFile(file);
	        image_view.setImageURI(uri);
        }
        catch(Exception e){
        	Log.i("kkk", "HuoDong_Detail_01" + e.getMessage());
        }
        
        //设置stripe颜色
        huodong_stripe=(ImageView)findViewById(R.id.huodong_stripe);
        int stripeColor[] ={R.color.huodong_stripe_1,R.color.huodong_stripe_2,R.color.huodong_stripe_3,R.color.huodong_stripe_4};
        huodong_stripe.setBackgroundResource(stripeColor[get_myId % 4]);
	}
	
	private void initShetuan(){
		guanzhu=(RelativeLayout)findViewById(R.id.guanzhu);
        guanzhu_text=(TextView)findViewById(R.id.guanzhu_text);
        text_boyCount = (TextView)findViewById(R.id.text_boyCount);
        text_girlCount = (TextView)findViewById(R.id.text_girlCount);
        linearlayout_org=(RelativeLayout)findViewById(R.id.linearlayout_org);
        linearLayout_huodong_chat=(RelativeLayout)findViewById(R.id.linearLaout_huodong_chat);
        guanzhu.setOnClickListener(new ButtonGuanZhu());
        linearlayout_org.setOnClickListener(new ButtonLinearlayoutOrg());
        linearLayout_huodong_chat.setOnClickListener(new ButtonHuoDongChat());
        mySex = mv.getMySex();
        
        more=(Button)findViewById(R.id.more);
        more.setOnClickListener(new ButtonMore());
        org_name=(TextView)findViewById(R.id.org_name);
        org_introduce=(TextView)findViewById(R.id.org_introduce);
        count_member=(Button)findViewById(R.id.count_member);
        org_logo=(ImageView)findViewById(R.id.org_logo);
	}
	
	private void initDialogGuanzhu(){
        myDialog=new Dialog(this, R.style.dialog_huodong_tixing);
        myDialog.setContentView(R.layout.dialog_huodong_guanzhu);
        ok=(Button)myDialog.findViewById(R.id.ok);
        no=(Button)myDialog.findViewById(R.id.no);
        ok.setOnClickListener(new ButtonDialog_Remain());
        no.setOnClickListener(new ButtonDialog_Remain());
	}
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonOnClickListenerL());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_btn = (Button)findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new ButtonShare());
        right_img = (ImageView)findViewById(R.id.function_image);
        right_img.setImageResource(R.drawable.share);
        text=(TextView)findViewById(R.id.text);
        text.setText("校园活动");
	}
	
	private void initUMengShare(){
		final String targetUrl = "http://m.sheyou.me/detail.html?actid=" + get_myId + "E";
		//根据content长度确定要截取多少个字
		String shareContent = "";
		if(get_activity_content.length() > 50)
			shareContent = get_activity_content.substring(0,49).trim() + "... " + targetUrl;
		else 
			shareContent = get_activity_content.trim() + "... " + targetUrl;
		// 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController.setShareContent(shareContent);
        // 根据file获取图片
        mController.setShareImage(new UMImage(HuoDong_Detail.this,myDir+mv.getFileName()+get_uri));
        
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        
        //添加人人网SSO授权功能     
        //APPID:272345      
        //API Key:0253cdcd6e3444328c052c8a5dc236a2  
        //Secret:786711e7ccc34908b78f3d3245cea595     
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(HuoDong_Detail.this,
                    "272345", "0253cdcd6e3444328c052c8a5dc236a2",
                    "786711e7ccc34908b78f3d3245cea595");
        mController.getConfig().setSsoHandler(renrenSsoHandler);
        //使人人跳转到该website界面
        mController.setAppWebSite(SHARE_MEDIA.RENREN, targetUrl);
        
        // wxf4254f773aff067c是你在微信开发平台注册应用的AppID
        String appID = "wxf4254f773aff067c";
        String appSecret = "96e40442e186cfe7e3bdf046bf11e9af";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(HuoDong_Detail.this,appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(HuoDong_Detail.this,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent(shareContent);
        //设置title
        weixinContent.setTitle(get_name);
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(targetUrl);
        //设置分享图片
        weixinContent.setShareImage(new UMImage(HuoDong_Detail.this,myDir+mv.getFileName()+get_uri));
        mController.setShareMedia(weixinContent);
        
        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        //设置朋友圈title
        circleMedia.setTitle(get_name);
        circleMedia.setShareImage(new UMImage(HuoDong_Detail.this,myDir+mv.getFileName()+get_uri));
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);
        
        //设置哪些上分享页面，并规定顺序
        mController.getConfig().setPlatforms(SHARE_MEDIA.RENREN,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA);
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
        										SHARE_MEDIA.SINA , SHARE_MEDIA.RENREN);
	}
	
	class ButtonShare implements OnClickListener{
		public void onClick(View arg0) {
			try {
				mController.openShare(HuoDong_Detail.this,
			        new SnsPostListener() {
					
		                public void onStart() {
		                    Toast.makeText(HuoDong_Detail.this, "开始分享.", Toast.LENGTH_SHORT).show();
		                }
		                
		                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                     if (eCode == 200) {
		                         Toast.makeText(HuoDong_Detail.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                     } else {
		                          String eMsg = "";
		                          if (eCode == -101){
		                              eMsg = "没有授权";
		                          }
		                          Toast.makeText(HuoDong_Detail.this, "分享失败[" + eCode + "] " + 
		                                             eMsg,Toast.LENGTH_SHORT).show();
		                     }
		              }
			  });
			}
			catch (Exception e){
				Log.i("kkk", e.getMessage());
			}
		}
	}
	
	
	class TextFailToConnect implements OnClickListener{
		public void onClick(View arg0) {
			layoutChanged = false;
			timeExhausted = false;
			failToConnect.setVisibility(4);
			progressBar.setVisibility(0);
			timing();
			methodName = "a12";
        	jGet(methodName);	
		}
	}
	
	
    private void BuildHuodong(){
        try {
        	JSONObject jsonObject;
        	
        		//加载主页面
        		jsonObject = new JSONObject(jResult);
	        	JSONArray jsonArray=jsonObject.getJSONArray("activityx_info");
	        	JSONObject jsonpet = jsonArray.getJSONObject(0);
	        	get_activity_content = jsonpet.getString("ActivirtyContent");
	        	content.setText(get_activity_content);
	        	
	        	get_girlLimit = jsonpet.getInt("girllim");
	            get_boyLimit = jsonpet.getInt("boylim");
	            get_girlCount = jsonpet.getInt("girlnum");
	            get_boyCount = jsonpet.getInt("boynum");
	            setBoyGirlCount();
	        	guanzhu_state = jsonpet.getInt("cjState");
	        	ListenState();
	        	SetGuanZhuView(guanzhu_state);
	            
	        	if(TimeMachining.TimeTOEvaluate(get_time)){
	        		guanzhu_state = -1;
	        	}
	        	
	        	//获得content后开始初始化分享
	        	initUMengShare();
	        	
	        	//当加载完主页面后开始加载社团页面
//	        	while(httpBlock);
		        methodName = "Organization/get_orgx_info";
		        jGet(methodName);
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    }
    
    private void BuildShetuan(){
      try {
    	JSONObject jsonObject;
    	jsonObject = new JSONObject(jResult);
    	JSONArray jsonArray=jsonObject.getJSONArray("orgx_info");
    	JSONObject jsonpet = jsonArray.getJSONObject(0);
    	org_introduce.setText(jsonpet.getString("Introduction"));
    	org_name.setText(jsonpet.getString("OrganizationName"));
//    					+ "("+jsonpet.getString("Rate")+"分)");
    	who.setText(jsonpet.getString("OrganizationName"));
    	dingyue_state = jsonpet.getInt("gzstate");
    	
    	if(dingyue_state == 1){
    		count_member.setVisibility(4);
    	}else{
    		int countNumber = jsonpet.getInt("boynum") + jsonpet.getInt("girlnum");
    		count_member.setText("已有"+ countNumber +"人订阅了\n快点击订阅吧");
    	}
    	count_member.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(mv.getLoginState() == 0){
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
							String getName = "Organization/add_gz";
							NameValuePair param1 = new BasicNameValuePair("org_id", get_org_id+"");
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1);
							Message msg = new Message();
							msg.what = 0x114;
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
			}
		});
    	
    	get_orgName = jsonpet.getString("OrganizationName");
    	get_org_logo_url = jsonpet.getString("Logo");
    	get_org_rate = jsonpet.getString("Rate");
    	get_org_introduce = jsonpet.getString("Introduction");
    	get_org_countmember = jsonpet.getInt("boynum") + jsonpet.getInt("girlnum") + "";
    	
    	//从缓存中读取图片，或者从服务器读取 图片并存入缓存 
        final ImageView cp = org_logo;
        final String imageUrl = get_org_logo_url;
        File file = new File(myDir+mv.getFileName(), imageUrl);
        if (file.exists()) {
        	Uri uri = Uri.fromFile(file);
	        org_logo.setImageURI(uri);
        }
        else
        {
        	new Thread(){
        		public void run(){
    				try
    				{
    					String u = mv.getImageIpName()+mv.getImageAddress()+imageUrl;
    					URL url = new URL(u);
    					InputStream is = url.openStream();
    					File file=new File(myDir+mv.getFileName(),imageUrl);
    					FileOutputStream fos = new FileOutputStream(file);
    					byte[] buff = new byte[1024];
    					int hasRead = 0;
    					while((hasRead = is.read(buff)) > 0)
    					{
    						fos.write(buff, 0 , hasRead);
    					}
    					is.close();
    					fos.close();
    					Uri uri = Uri.fromFile(file);
    					cp.setImageURI(uri);
    				}
    				catch (Exception e)
    				{
    					e.printStackTrace();
    				}
        		}
        	}.start();
        }
      } catch (JSONException e) {
      	e.printStackTrace();
      }
    
    }
    
    private void ListenState(){
    	//先增加性别1个人，因为待会SetGuanZhuView时会减去一个人
    	if(mySex == 1){
    		if(guanzhu_state == 0){
    			get_boyCount ++;
    		}
    		else if(guanzhu_state == 1){
    			get_boyCount --;
    		}
		}
		else if(mySex == 0){
			if(guanzhu_state == 0){
				get_girlCount ++;
			}
			else if(guanzhu_state == 1){
				get_girlCount --;
			}
		}
    }
    
    
    private void SetGuanZhuView(int state){
    	if (state == -1){
    		guanzhu_text.setText("已结束");
    		guanzhu_text.setTextColor(getResources().getColor(R.color.text_gray_light));
    		guanzhu.setClickable(false);
    	}
    	else if(state == 0){
    		guanzhu_text.setText("关注");
    		guanzhu_text.setTextColor(getResources().getColor(R.color.text_blue_dark));
    		if(mySex == 1){
    			get_boyCount --;
    		}
    		else if(mySex == 0){
    			get_girlCount --;
    		}
    	}else if(state ==1){
    		guanzhu_text.setText("已关注");
    		guanzhu_text.setTextColor(getResources().getColor(R.color.text_gray_light));
    		if(mySex == 1){
    			get_boyCount ++;
    		}
    		else if(mySex == 0){
    			get_girlCount ++;
    		}
    	}
    	setBoyGirlCount();
    }
    
    
    class ButtonMore implements OnClickListener{
		public void onClick(View arg0) {
			if(get_org_id != -1){
			Intent intent=new Intent();
			intent.setClass(HuoDong_Detail.this, SheTuan_Detail.class);
			intent.putExtra("where_come_from", "huodong_detail");
			intent.putExtra("org_id", get_org_id); 
			startActivityForResult(intent, 0);
			}
		}
    }
    
    class ImageOnClickListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(HuoDong_Detail.this, Image_HuoDong.class);
			intent.putExtra("title_text", name.getText());
			intent.putExtra("uri", get_uri);
			startActivity(intent);
		}
    }
    
    private void initSlidingMenu() {  
        menu = new SlidingMenu( this); 
        menu.setMode(SlidingMenu. LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN );
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset );
        menu.setFadeDegree(0.75f);
        menu.attachToActivity( this, SlidingMenu.SLIDING_CONTENT );
        menu.setMenu(R.layout.menu_huodong_detail);
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
        //点击返回键关闭滑动菜单  
        if (menu.isMenuShowing()) {  
            menu.showContent();  
        } else {  
        	GoBack();
        }  
    }  
    
    class ButtonRightListener implements OnClickListener{
		public void onClick(View v) {
	        if (menu.isMenuShowing()) {  
	            menu.showContent();  
	        } else {  
	            menu.showMenu();  
	        }
		}
    }
    
    class ButtonLinearlayoutOrg implements OnClickListener{
		public void onClick(View arg0) {
			if(!menu.isMenuShowing()){
				menu.showMenu();
			}
		}
    }
    
    class ButtonHuoDongChat implements OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(HuoDong_Detail.this, Chat.class);
			intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_HUODONG);
			intent.putExtra(MyStatic.KEYNAME_HuoDongId, get_myId + "");
			intent.putExtra(MyStatic.KEYNAME_HuoDongName, get_name);
			intent.putExtra(MyStatic.KEYNAME_Avator, get_org_logo_url);
			startActivity(intent);
		}
    }
    
//    class ButtonShare implements OnClickListener{
//		public void onClick(View arg0) {
//			mController.openShare(HuoDong_Detail.this, false);
//		}
//    }
    
    
    class ButtonGuanZhu implements OnClickListener{
		public void onClick(View v) {
			if(mv.getLoginState() == 0){
    		 if (myDialog_login_and_register == null) 
     		{
     			myDialog_login_and_register = Dialog_Register_Login.getMyDialog();
     			myDialog_login_and_register.show();
     		}
     		else 
    		{
    			myDialog_login_and_register.show();
    		}
		}
			else{   //登陆状态下才可以关注或报名
				if(v == guanzhu){  // 点击关注的触发事件
					if(guanzhu_state == 0){
						getName_Guanzhu_Baoming = "Activity/add_cj_activity";
						if (myDialog == null) 
						{
							myDialog = new Dialog(HuoDong_Detail.this,R.style.dialog);
							myDialog.show();
						}
						else
						{
							myDialog.show();
						}
					}
					else if(guanzhu_state == 1){
						getName_Guanzhu_Baoming ="Activity/delete_cj_activity";
					}
					guanzhu_state = (guanzhu_state + 1) % 2;
					SetGuanZhuView(guanzhu_state);
					}
				}
				new Thread()
				{
					public void run()
					{
						try
						{
							NameValuePair param1 = new BasicNameValuePair("activity_id", get_myId+"");
							String getResponse = CustomerHttpClient.post(ipName+getName_Guanzhu_Baoming, param1);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
    	
    }
   
    //活动提醒发送的通知
    class ButtonDialog_Remain implements OnClickListener{
		public void onClick(View v) {
			if(v == no){
			}else if(v == ok){
				Intent intent = new Intent(HuoDong_Detail.this,AlarmReceiver.class);
				intent.putExtra("uri", get_uri);
		        intent.putExtra("title", get_name);
		        intent.putExtra("introduce", get_activity_content);
		        intent.putExtra("time", get_time);
		        intent.putExtra("address", get_address);
		        PendingIntent pendingIntent = PendingIntent.getBroadcast(HuoDong_Detail.this, TimeMachining.getTimeRelatedId(), intent, 0);  
		        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); //获取闹钟管理器  
		        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeMachining.BeforeHand(get_time), pendingIntent);
		        Toast.makeText(HuoDong_Detail.this, getResources().getString(R.string.huodong_detail_remain_done), Toast.LENGTH_SHORT).show();
			}
			myDialog.dismiss();
		}
    }
    
    
    //设置菜单
	public boolean onMenuOpened(int featureId, Menu menu) {
		menu.removeItem(0);
		if (myDialog == null) 
		{
			myDialog = new Dialog(HuoDong_Detail.this,R.style.dialog);
			myDialog.show();
		}
		else 
		{
			myDialog.show();
		}
		return true;
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0){
			if(resultCode==0){
				int timeCount=data.getIntExtra("time",0);
				if(timeCount==1){
				if(menu.isMenuShowing()){
					menu.showContent();
				}
				}
			}
		}
		
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	
	
	
	
	
	private void timing(){
        Timer timerLoading=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
    			timeExhausted = true;
    			Message msg = new Message();
    			msg.what = 0x000;
    			msg.obj = "";
    			handler.sendMessage(msg);
			}
		}; 
		timerLoading.schedule(timertask, MyStatic.LoadingTime);
	}
	
	private void changeLayout(){
		if(!layoutChanged){
			if(timeExhausted){
				progressBar.setVisibility(4);
				failToConnect.setVisibility(0);
			}
			else {
				loading.setVisibility(4);
				connectSuccessLayout.setVisibility(0);
			}
		}
		layoutChanged = true;
	}
	
	
	private void setBoyGirlCount(){
        if(get_boyLimit == -1){
        	text_boyCount.setText(get_boyCount + " / " + "∞");
        }
        else {
        	text_boyCount.setText(get_boyCount + " / " + get_boyLimit);
        }
        if(get_girlLimit == -1){
        	text_girlCount.setText(get_girlCount + " / " + "∞");
        }
        else {
        	text_girlCount.setText(get_girlCount + " / " +get_girlLimit);
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

