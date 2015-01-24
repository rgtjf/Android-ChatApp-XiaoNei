package com.shoulder.xiaonei.main;

import android.R.bool;
import android.app.Activity;  
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chat.Chat;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.drawable;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_shetuan_wangqi;
import com.shoulder.xiaonei.card.Card_shetuan_wangqi_adapter;
import com.shoulder.xiaonei.card.Card_shetuan_yugao;
import com.shoulder.xiaonei.card.Card_shetuan_yugao_adapter;
import com.shoulder.xiaonei.main.HuoDong_Detail.ButtonDialog_Remain;
import com.shoulder.xiaonei.myClass.*;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;  
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;  
import android.graphics.Matrix;  
import android.net.Uri;
import android.os.Bundle;  
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.util.DisplayMetrics;  
import android.util.Log;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;  
import android.view.animation.Animation;  
import android.view.animation.TranslateAnimation;  
import android.view.ViewGroup;  
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;  
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;  
import android.widget.Toast;  
import android.widget.AdapterView.OnItemClickListener;

public class SheTuan_Detail extends Activity implements IXListViewListener{

	private String methodName;
	protected File cache;
	private String jResult_view1 = "",jResult_view2 = "",jResult_view3 = "";
	private boolean connection_release;
	private String getName_dingyue_baoming;
	private String getParams_dingyue_baoming;
	
	private View view1,view2,view3;
	private ViewPager viewPager;
	private List<View> views;
	private XListView mXListView2;  
	private List<Card_shetuan_wangqi> mCards2;
	private Card_shetuan_wangqi_adapter mAdapter2;
	
	private Button leftButton;
	private Button rightButton;
	private ImageView rightImage;
	private TextView text;
	
	private MyVal mv;
    private String myDir="";
    private String myIpName="";
	
	private ImageButton logo;
	private LinearLayout shetuan_zongpingfen;
	private LinearLayout shetuan_fensishu;
	private TextView name;
	private TextView shetuan_zongpingfen_detail;
	private TextView shetuan_fensishu_detail;
	private TextView org_name;
	private TextView org_introduce;
	
	private LinearLayout shetuan3_notYet;
	private LinearLayout shetuan3_Yet;
	private TextView shetuan3_notYetText;
	private LinearLayout linear_YuGao;
	private ImageView huodong_image;
	private TextView huodong_name;
	private TextView huodong_address;
	private TextView huodong_time;
	private TextView huodong_guanzhu_count;
	private TextView huodong_function_guanzhu;
	private TextView huodong_content;
	private ImageView huodong_line;
	private String huodong_time_crude;
	private String huodong_image_url;
	private int huodong_guanzhu_state = 0;//0表示未关注，1表示已关注
	private String getName_Guanzhu_Baoming;
	private int huodong_countNumber;
	
	private Dialog myDialog_login_and_register;//未登录时，操作一些需要登录的行为会弹出提醒
	
	private Dialog myDialog;//活动提醒
	private Button ok;
	private Button no;
	
	private Button dingyue;
	
	private ImageView page1; //点击右上角页码可以跳转另一页
	private ImageView page2;
	private ImageView page3;

    private Timer timer;
    private int timeCount=0;	
    
    private int count_loop = 50;
    
	private String get_orgName;
	private int get_org_id;
	private String get_org_countmember;
	private String get_org_introduce;
	private String get_org_logo_uri;
	private String get_org_rate;
	private String get_objectId;
	
	private LinearLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private int shetuan2_InforCount = 0;//计算往期活动中的活动数量，若为0，显示暂无往期活动
	
	private int dingyue_state = 0;//0表示未订阅，1表示已订阅
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.shetuan_detail);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        Bundle extras = getIntent().getExtras();
    	get_org_id = extras.getInt("org_id");
    	
        mv=((MyVal)getApplicationContext());
        myDir=mv.getMyDir();
        myIpName=mv.getIpName();
    	
        InitViewPager();
        initLoading();
        initTitle();
        initDialogRemain();
       
        connection_release = false;//保证两个线程不会使用同一个httpclient
        
        methodName = "Organization/get_orgx_info";
        jGet(methodName);
        
        //进入计时，3秒内后退不关闭首页的menu
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, 3000);
		
		Dialog_Register_Login.Set_Dialog_Register_Login(SheTuan_Detail.this,"shetuan_detail");
	}
	
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0x000:
 				changeLayout();
				break;
			case 0x112:
				jResult_view2 = msg.obj.toString();
				initView1();
				initView2();
				break;
			case 0x116:
				jResult_view1 = msg.obj.toString();
				buildIt();
				break;
			case 0x117:
				ListenState();
				break;
			case 0x118:
				jResult_view3 = msg.obj.toString();
				changeLayout();
				initView3();
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
				String getName="";
				String getParams="";
				if (mtName.equals("Organization/get_orgx_info")){
					getName=mtName;
					getParams="orgid="+get_org_id;
				} 
				else if(mtName.equals("Organization/get_orgx_activity")){
					getName = mtName;
					getParams ="orgid=" + get_org_id;
				}
				else if(mtName.equals("Organization/get_orgx_nowact")){
					getName = mtName;
					getParams = "orgid=" + get_org_id;
				}
				connection_release = false;
				try
				{
					String result = CustomerHttpClient.get(myIpName+getName+"?"+getParams);
					Message msg = new Message();
					if (mtName.equals("Organization/get_orgx_info")){
						msg.what = 0x116;
					} 
					else if(mtName.equals("Organization/get_orgx_activity")){
						msg.what = 0x112;
					}
					else if(mtName.equals("Organization/get_orgx_nowact")){
						msg.what = 0x118;
					}
					msg.obj = result;
					handler.sendMessage(msg);
					connection_release = true;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
    }
    
    
    private void initLoading(){
        connectSuccessLayout=(LinearLayout)findViewById(R.id.connectSuccessLayout);
        loading=(RelativeLayout)findViewById(R.id.loading);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        failToConnect=(TextView)findViewById(R.id.failToConnect);
        failToConnect.setOnClickListener(new TextFailToConnect());
        timing();
    }
    
    private void initTitle(){
        leftButton=(Button)findViewById(R.id.left_btn);
        leftButton.setOnClickListener(new ButtonOnClickListenerL());
        rightImage=(ImageView)findViewById(R.id.function_image);
        rightImage.setImageResource(R.drawable.chat);
        rightButton=(Button)findViewById(R.id.right_btn);
        text=(TextView)findViewById(R.id.text);
    }
    
    private void initDialogRemain(){
        myDialog=new Dialog(this, R.style.dialog_huodong_tixing);
        myDialog.setContentView(R.layout.dialog_huodong_guanzhu);
        ok=(Button)myDialog.findViewById(R.id.ok);
        no=(Button)myDialog.findViewById(R.id.no);
        ok.setOnClickListener(new ButtonDialog_Remain());
        no.setOnClickListener(new ButtonDialog_Remain());
    }
    
	private void InitViewPager() {
        viewPager=(ViewPager)findViewById(R.id.vPager);  
        views=new ArrayList<View>();  
        LayoutInflater inflater=getLayoutInflater();  
        view1=inflater.inflate(R.layout.shetuan_1, null);
        view2=inflater.inflate(R.layout.shetuan_2, null);
        view3=inflater.inflate(R.layout.shetuan_3, null);
    	
        views.add(view1);  
        views.add(view2);  
        views.add(view3);
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
	}
	
	private void initView1(){
		shetuan_zongpingfen=(LinearLayout)view1.findViewById(R.id.shetuan_zongpingfen);
        shetuan_fensishu=(LinearLayout)view1.findViewById(R.id.shetuan_fensishu);
        shetuan_zongpingfen_detail=(TextView)view1.findViewById(R.id.shetuan_zongpingfen_detail);
        shetuan_fensishu_detail=(TextView)view1.findViewById(R.id.shetuan_fensishu_detail);
        logo=(ImageButton)view1.findViewById(R.id.logo);
        dingyue=(Button)view1.findViewById(R.id.dingyue);
        page1=(ImageView)view1.findViewById(R.id.Page1);
        page1.setOnClickListener(new MyOnClickListener(1));
        ListenState(); 
        org_introduce=(TextView)view1.findViewById(R.id.introduce);
        org_name=(TextView)view1.findViewById(R.id.name);
        org_name.setText(get_orgName);
        org_introduce.setText(get_org_introduce);
        shetuan_fensishu_detail.setText(get_org_countmember);
        shetuan_zongpingfen_detail.setText(get_org_rate);
        
        OnClickListener listener = new OnClickListener() {
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
				}else {
				
				if (v == dingyue && dingyue_state == 0){
					getName_dingyue_baoming = "Organization/add_gz";
					dingyue_state = ChangeState(dingyue_state);
				}
				new Thread()
				{
					public void run()
					{
						try
						{
							NameValuePair param1 = new BasicNameValuePair("org_id", get_org_id+"");
							String getResponse = CustomerHttpClient.post(myIpName+getName_dingyue_baoming, param1);
							if(getResponse.equals("true")){
								Message msg = new Message();
								msg.what = 0x117;
								msg.obj = "";
								handler.sendMessage(msg);
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
		};
		dingyue.setOnClickListener(listener);
		getImageFile();
	}
	
	private void initView2(){
        mXListView2=(XListView)view2.findViewById(R.id.ListView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View vTop = mLayoutInflater.inflate(R.layout.shetuan2_top, null);
        mXListView2.addHeaderView(vTop, null, false);
        mAdapter2=new Card_shetuan_wangqi_adapter(this, getItems2());
        if (shetuan2_InforCount == 0){
        	View vBottom = mLayoutInflater.inflate(R.layout.include_notyet, null);
        	TextView textNotYet = (TextView)vBottom.findViewById(R.id.notYet_text);
        	textNotYet.setText(getResources().getString(R.string.noYet_huodongWangQi));
        	mXListView2.addFooterView(vBottom, null, false);
        }
        mXListView2.setXListViewListener(this);
        mXListView2.setPullLoadEnable(false);//设置上拉刷新
        mXListView2.setPullRefreshEnable(false);//设置下拉刷新
        mXListView2.setAdapter(mAdapter2);
        //点击页码跳转另一页
        page2=(ImageView)view2.findViewById(R.id.Page2);
        page2.setOnClickListener(new MyOnClickListener(2));
//        SetListView.setListViewHeightBasedOnChildren(mXListView2);
        
        jGet("Organization/get_orgx_nowact");//开始加载view3
	}
	
	private void initView3(){
		shetuan3_notYet=(LinearLayout)view3.findViewById(R.id.notYet);//当无信息时，显示暂无的消息
		shetuan3_Yet=(LinearLayout)view3.findViewById(R.id.Yet);
		shetuan3_notYetText=(TextView)view3.findViewById(R.id.notYet_text);
		linear_YuGao=(LinearLayout)view3.findViewById(R.id.linear_YuGao);
        huodong_image=(ImageView)view3.findViewById(R.id.huodong_image);
        huodong_name=(TextView)view3.findViewById(R.id.huodong_name);
        huodong_address=(TextView)view3.findViewById(R.id.huodong_address);
        huodong_time=(TextView)view3.findViewById(R.id.huodong_time);
        huodong_guanzhu_count=(TextView)view3.findViewById(R.id.huodong_guanzhu_count);
        huodong_function_guanzhu=(TextView)view3.findViewById(R.id.function_guanzhu);
        huodong_content=(TextView)view3.findViewById(R.id.huodong_content);
        huodong_line=(ImageView)view3.findViewById(R.id.line);
        page3=(ImageView)view3.findViewById(R.id.Page3);
        page3.setOnClickListener(new MyOnClickListener(0));
        
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult_view3);
        	JSONArray jsonArray=jsonObject.getJSONArray("nowact_info");
        	JSONObject jsonpet = jsonArray.getJSONObject(0);
        	huodong_name.setText(jsonpet.getString("ActivityName"));
        	huodong_address.setText(jsonpet.getString("Address"));
        	huodong_time_crude = jsonpet.getString("ActivityTime");
        	huodong_time.setText(TimeMachining.DateTranslator(huodong_time_crude));
        	huodong_countNumber = jsonpet.getInt("boynum") + jsonpet.getInt("girlnum");
        	setHuodongGuanzhuCount();
        	huodong_guanzhu_state = jsonpet.getInt("cjState");
        	SetGuanZhuView(huodong_guanzhu_state);
        	huodong_content.setText(jsonpet.getString("ActivirtyContent"));
        	huodong_image_url=jsonpet.getString("PhotoDir");//获取活动图片url地址
        	initHuodongImage();
        	huodong_function_guanzhu.setOnClickListener(new ButtonDialog_Huodong_Guanzhu());
        	huodong_name.setVisibility(0);
        	linear_YuGao.setVisibility(0);
        	huodong_line.setVisibility(0);
        	}
        catch (JSONException e) {
        	e.printStackTrace();
        	shetuan3_notYetText.setText(getResources().getString(R.string.noYet_huodongYuGao));
        	shetuan3_Yet.setVisibility(4);
        	shetuan3_notYet.setVisibility(0);
        }
	}
    
    
    public void buildIt(){
        	try {
        		JSONObject jsonObject;
        		jsonObject = new JSONObject(jResult_view1);
	        	JSONArray jsonArray=jsonObject.getJSONArray("orgx_info");
	        	JSONObject jsonpet = jsonArray.getJSONObject(0);
	        	
	        	get_orgName = jsonpet.getString("OrganizationName");
	        	get_org_logo_uri = jsonpet.getString("Logo");
	        	get_org_rate = jsonpet.getString("Rate");
	        	get_org_introduce = jsonpet.getString("Introduction");
	        	get_org_countmember = jsonpet.getInt("boynum") + jsonpet.getInt("girlnum") + "";
	        	get_objectId = jsonpet.getString("GroupID");
	        	
	        	dingyue_state = jsonpet.getInt("gzstate");
	        	
	        	text.setText(get_orgName);//这里才加载，因为要获取orgName
	        	rightButton.setOnClickListener(new ButtonChat());
	        	
	        	jGet("Organization/get_orgx_activity");//开始加载view2
	        	
        	} catch (JSONException e) {
        		e.printStackTrace();
        	}
    }
	
	private void ListenState(){
		if(dingyue_state == 1){
			dingyue.setText("已订阅");
			dingyue.setTextColor(getResources().getColor(R.color.shetuan_1_button_text_dingyue_done));
			dingyue.setBackgroundResource(R.drawable.shape_shetuan1_button_done);
		}
	}
	
	private void getImageFile(){
        try{
	        File file = new File(myDir+mv.getFileName(), get_org_logo_uri); //从缓存中找到图片文件
	        Uri uri = Uri.fromFile(file);
		    logo.setImageURI(uri);
		    logo.setOnClickListener(new ButtonLogo());
        }
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void initHuodongImage(){
        final ImageView cp = huodong_image;
        File file = new File(myDir+mv.getFileName(), huodong_image_url);
        if (file.exists()) {
        	Uri uri = Uri.fromFile(file);
	        cp.setImageURI(uri);
	        cp.setOnClickListener(new ButtonHuodongImage());
        }
        else
        {
        	new Thread()
        	{
    			public void run()
    			{
    				try
    				{
    					String u = mv.getImageIpName()+mv.getImageAddress()+huodong_image_url;
    					URL url = new URL(u);
    					InputStream is ;
    					is = url.openStream();
    					File file=new File(myDir+mv.getFileName(),huodong_image_url);
    					if(!file.exists()){
    						file.createNewFile();
    					}
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
    					cp.setImageURI(uri);//http://sheyou.me/
    					cp.setOnClickListener(new ButtonHuodongImage());
    				}
    				catch (Exception e)
    				{
    					e.printStackTrace();
    				}
    			}
    		}.start();
        }
	}
	
	
    public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }  
  
        public int getCount() {           
            return  mListViews.size();  
        }  
          
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;  
        }  
    }  
    
    
    private List<Card_shetuan_wangqi> getItems2(){
    	mCards2=new ArrayList<Card_shetuan_wangqi>();
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult_view2);
        	JSONArray jsonArray=jsonObject.getJSONArray("orgx_activity");
        	shetuan2_InforCount = jsonArray.length();
        	for(int i=jsonArray.length()-1; i>=0; i--)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_shetuan_wangqi mCard=new Card_shetuan_wangqi(jsonpet.getString("ActivityName"),jsonpet.getString("PhotoDir"),
        														jsonpet.getString("ActivityID"),jsonpet.getString("rate"),
        														jsonpet.getString("cnt"));
        		mCards2.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        return mCards2;
    }
    
    public void onRefresh()
    {
    }
    public void onLoadMore()
	{
	}
    
    
    private void GoBack(){
		Intent intent=getIntent();
		intent.putExtra("time", timeCount);
		setResult(0,intent);
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

	class ButtonLogo implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(SheTuan_Detail.this, Image_HuoDong.class);
			intent.putExtra("title_text",org_name.getText());
			intent.putExtra("uri",get_org_logo_uri);
			SheTuan_Detail.this.startActivity(intent);
		}
    }
	
	class ButtonHuodongImage implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(SheTuan_Detail.this, Image_HuoDong.class);
			intent.putExtra("title_text",huodong_name.getText());
			intent.putExtra("uri",huodong_image_url);
			SheTuan_Detail.this.startActivity(intent);
		}
	}
	
    class ButtonDialog_Huodong_Guanzhu implements OnClickListener{
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
				if(v == huodong_function_guanzhu){  // 点击关注的触发事件
					if(huodong_guanzhu_state == 0){
						getName_Guanzhu_Baoming = "Activity/add_cj_activity";
						if (myDialog == null) 
						{
							myDialog = new Dialog(SheTuan_Detail.this,R.style.dialog);
							myDialog.show();
						}
						else
						{
							myDialog.show();
						}
					}
					else if(huodong_guanzhu_state == 1){
						getName_Guanzhu_Baoming ="Activity/delete_cj_activity";
					}
					huodong_guanzhu_state = (huodong_guanzhu_state + 1) % 2;
					SetGuanZhuView(huodong_guanzhu_state);
					}
				}
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							NameValuePair param1 = new BasicNameValuePair("activity_id", get_org_id+"");
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName_Guanzhu_Baoming, param1);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
    }
    
    class ButtonChat implements OnClickListener{
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
			}else {
				Intent intent = new Intent(SheTuan_Detail.this,Chat.class);
				intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_SHETUAN);
				intent.putExtra(MyStatic.KEYNAME_OrgName, get_orgName);
				intent.putExtra(MyStatic.KEYNAME_OrgId, get_org_id + "");
				intent.putExtra(MyStatic.KEYNAME_ObjectId, get_objectId);
				intent.putExtra(MyStatic.KEYNAME_Avator, get_org_logo_uri);
				startActivity(intent);
			}
		}
    }
	
    class ButtonDialog_Remain implements OnClickListener{
		public void onClick(View v) {
			if(v == no){
			}else if(v == ok){
				Intent intent = new Intent(SheTuan_Detail.this,AlarmReceiver.class);
				intent.putExtra("uri", huodong_image_url);
		        intent.putExtra("title", huodong_name.getText());
		        intent.putExtra("introduce", huodong_content.getText());
		        intent.putExtra("time", huodong_time_crude);
		        intent.putExtra("address", huodong_address.getText());
		        PendingIntent pendingIntent = PendingIntent.getBroadcast(SheTuan_Detail.this, TimeMachining.getTimeRelatedId(), intent, 0);  
		        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); //获取闹钟管理器  
		        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeMachining.BeforeHand(huodong_time_crude), pendingIntent);
		        Toast.makeText(SheTuan_Detail.this, getResources().getString(R.string.huodong_detail_remain_done), Toast.LENGTH_SHORT).show();
			}
			myDialog.dismiss();
		}
    }
	
    
	public int ChangeState(int state){
		int result = (state + 1) % 2;
		return result;
	}
	
    private void SetGuanZhuView(int state){
    	if(state == 0){
    		huodong_function_guanzhu.setText("关注");
    		huodong_function_guanzhu.setTextColor(getResources().getColor(R.color.shetuan_3_text_guanzhu));
    		huodong_countNumber -- ;
    	}else if(state ==1){
    		huodong_function_guanzhu.setText("已关注");
    		huodong_function_guanzhu.setTextColor(getResources().getColor(R.color.text_gray_light));
    		huodong_countNumber ++ ;
    	}
    	setHuodongGuanzhuCount();
    }
    
    private void setHuodongGuanzhuCount(){
    	huodong_guanzhu_count.setText(huodong_countNumber + "人已关注");
    }
	
    private class MyOnClickListener implements OnClickListener{  
        private int index=0;  
        public MyOnClickListener(int i){  
            index=i;  
        }  
        public void onClick(View v) {  
            viewPager.setCurrentItem(index);              
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
		timerLoading.schedule(timertask,MyStatic.LoadingTime);
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
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}

}
