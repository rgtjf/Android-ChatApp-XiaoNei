package com.shoulder.xiaonei.myZone;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.card.Card_myActivity;
import com.shoulder.xiaonei.card.Card_myActivity_adapter;
import com.shoulder.xiaonei.card.Card_myOrg;
import com.shoulder.xiaonei.card.Card_myOrg_adapter;
import com.shoulder.xiaonei.card.Card_myTucao;
import com.shoulder.xiaonei.card.Card_myTucao_adapter;
import com.shoulder.xiaonei.card.Card_shetuan_subscribe;
import com.shoulder.xiaonei.card.Card_tucaoqiang;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.main.Shetuan_Subscribe_Main;
import com.shoulder.xiaonei.myClass.ConnectionDetector;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.tucao.TuCaoQiang_Comment_Fabu;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyZone extends Activity implements com.shoulder.xiaonei.XListView.XListView.IXListViewListener{
	
	private String methodName;
	private String jResult ="";
	
	private MyVal mv;
	
	public LinearLayout connectSuccessLayout_myOrg,connectSuccessLayout_myAct,connectSuccessLayout_myTucao;
	private RelativeLayout loading_myOrg,loading_myAct,loading_myTucao;
	private ProgressBar progressBar_myOrg,progressBar_myAct,progressBar_myTucao;
	private TextView failToConnect_myOrg,failToConnect_myAct,failToConnect_myTucao;
	private Boolean layoutChanged_myOrg = false,layoutChanged_myAct = false,layoutChanged_myTucao = false;
	private Boolean timeExhausted_myOrg = false,timeExhausted_myAct = false,timeExhausted_myTucao = false;
	
    private RelativeLayout right_rel;	
	private Button left_btn;
    private TextView text;
    private LinearLayout myMenu;
    
    public RelativeLayout clue_linear_myOrg,clue_linear_myAct,clue_linear_myTucao;
	private TextView clue_text1_myOrg,clue_text1_myAct,clue_text1_myTucao;
	private TextView clue_text2_myOrg,clue_text2_myAct,clue_text2_myTucao;
	private Boolean buildCard_myOrg = false;//当该页面第一次获取信息时，若无数据，则使clue页面可见
	private Boolean buildCard_myAct = false;
	private Boolean buildCard_myTucao = false;
    
    private TextView myOrg;
    private TextView myAct;
    private TextView myTucao;
    
    private Timer timer;
    private int timeCount = 0;
    
    private ImageView imageView;// 动画图片  
	private ViewPager viewPager;//页卡内容  
    private List<View> views;// Tab页面列表  
    private int currIndex = 0;// 当前页卡编号  
    private View view_myOrg,view_myAct,view_myTucao;//各个页卡  
    private int offset = 0;// 动画图片偏移量  
    private int bmpW;// 动画图片宽度  
    
    private XListView mListView1,mListView2,mListView3;
    private Card_myOrg_adapter mAdapter_myOrg;
    private Card_myActivity_adapter mAdapter_myActivity;
    private Card_myTucao_adapter mAdapter_myTucao;
    private List<Card_myOrg> mCards_myOrg;
    private List<Card_myActivity> mCards_myActivity;
    private List<Card_myTucao> mCards_myTucao;
    private Handler mHandler_myOrg,mHandler_myAct,mHandler_myTucao;
    
    private Boolean flag_ListView_myOrg_isInit = false;
    private Boolean flag_ListView_myAct_isInit = false;
    private Boolean flag_ListView_myTucao_isInit = false;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.zone_mine); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        mv = (MyVal)getApplicationContext();
        
        InitImageView();//左右滑动那个滑动的绿条
        InitTextView();//左右滑动改变上面字的颜色
        initViewPager();//活动、社团、吐槽三个界面
        initTitle();
        
        //进入计时，3秒内后退不关闭首页的menu
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, MyStatic.CloseTime);
	}
	
	Handler handler = new Handler()
	{ 
		public void handleMessage(Message msg)
		{
			jResult = msg.obj.toString();
			switch(msg.what)
			{
			case 0x000:
 				changeLayout();
				break;
			case 0x1:
				changeLayout();
				refresh();
				break;
			case 0x2:
				changeLayout();
				refresh();
				break;
			case 0x3:
				changeLayout();
				refresh();
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
				String getMethod = "";
				String getParams = "";
				if(mtName.equals("initMyOrg")){
					getMethod = "Manage/get_my_org";
				}else if(mtName.equals("initMyAct")){
					getMethod = "Manage/get_my_activity";
				}else if(mtName.equals("initMyTucao")){
					getMethod = "Manage/get_my_tucao";
				}
				try
				{
					String result = CustomerHttpClient.get(mv.getIpName() + getMethod + "?" + getParams);
					Message msg = new Message();
					if(mtName.equals("initMyOrg")){
						msg.what = 0x1;
					}
					else if(mtName.equals("initMyAct"))
					{
						msg.what = 0x2;
					}
					else if(mtName.equals("initMyTucao"))
					{
						msg.what = 0x3;
					}
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
    }
    
    
    private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonOnClickListener());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText(R.string.menu_main_buttonText_myZone_login);
    }
    
    
    private void initViewPager() {  
        viewPager=(ViewPager)findViewById(R.id.vPager);  
        views=new ArrayList<View>();  
        LayoutInflater inflater=getLayoutInflater();
        view_myAct=inflater.inflate(R.layout.card_myact_listview, null);
        view_myOrg=inflater.inflate(R.layout.card_myorg_listview, null);
        view_myTucao=inflater.inflate(R.layout.card_mytucao_listview, null);
        views.add(view_myAct);
        views.add(view_myOrg);  
        views.add(view_myTucao);
        
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener()); 
        
        if(flag_ListView_myAct_isInit == false && currIndex == 0){
        	initListView_myAct();
        	methodName = "initMyAct";
        	jGet(methodName);
        }

    } 
    
    private void initListView_myOrg(){
    	if(!flag_ListView_myOrg_isInit){
		    connectSuccessLayout_myOrg=(LinearLayout)view_myOrg.findViewById(R.id.connectSuccessLayout);
		    loading_myOrg=(RelativeLayout)view_myOrg.findViewById(R.id.loading);
		    progressBar_myOrg=(ProgressBar)view_myOrg.findViewById(R.id.progressBar);
		    failToConnect_myOrg=(TextView)view_myOrg.findViewById(R.id.failToConnect);
		    failToConnect_myOrg.setOnClickListener(new TextViewConnectFail());
		    timing(1);
		    flag_ListView_myOrg_isInit = true;
		    mv.setConnectSuccessLayoutMyOrg(0,connectSuccessLayout_myOrg);
    	}
    	
    	mListView1=(XListView)view_myOrg.findViewById(R.id.Lv1);
	    mAdapter_myOrg=new Card_myOrg_adapter(this,getItems_myOrg()); 
        mv.setMyOrgAdapter(mAdapter_myOrg);
        mHandler_myOrg= new Handler();
        mListView1.setPullLoadEnable(false);//设置上拉刷新
        mListView1.setPullRefreshEnable(false);//设置下拉刷新
        mListView1.setXListViewListener(this);
        mListView1.setAdapter(mAdapter_myOrg);
        
    	clue_linear_myOrg = (RelativeLayout)view_myOrg.findViewById(R.id.clue_linear);
        clue_text1_myOrg = (TextView)view_myOrg.findViewById(R.id.clue_text1);
        clue_text2_myOrg = (TextView)view_myOrg.findViewById(R.id.clue_text2);
        clue_linear_myOrg.setOnLongClickListener(new ClueLongClick());
        clue_text1_myOrg.setText(getResources().getString(R.string.clue_myorg_text1));
        clue_text2_myOrg.setText(getResources().getString(R.string.clue_myorg_text2));
        mv.setClue(0,clue_linear_myOrg);
    }
    
    private void initListView_myAct(){
    	if(!flag_ListView_myAct_isInit){
	        connectSuccessLayout_myAct=(LinearLayout)view_myAct.findViewById(R.id.connectSuccessLayout);
	        loading_myAct=(RelativeLayout)view_myAct.findViewById(R.id.loading);
	        progressBar_myAct=(ProgressBar)view_myAct.findViewById(R.id.progressBar);
	        failToConnect_myAct=(TextView)view_myAct.findViewById(R.id.failToConnect);
	        failToConnect_myAct.setOnClickListener(new TextViewConnectFail());
	        timing(0);
	        flag_ListView_myAct_isInit = true;
	        mv.setConnectSuccessLayoutMyOrg(1, connectSuccessLayout_myAct);
        }
    	
        mListView2=(XListView)view_myAct.findViewById(R.id.Lv1);  
        mAdapter_myActivity=new Card_myActivity_adapter(this,getItems_myAct()); 
        mv.setMyActAdapter(mAdapter_myActivity);
        mHandler_myAct = new Handler();
        mListView2.setPullLoadEnable(false);//设置上拉刷新
        mListView2.setPullRefreshEnable(false);//设置下拉刷新
        mListView2.setXListViewListener(this);
        mListView2.setAdapter(mAdapter_myActivity);
        
        clue_linear_myAct = (RelativeLayout)view_myAct.findViewById(R.id.clue_linear);
        clue_text1_myAct = (TextView)view_myAct.findViewById(R.id.clue_text1);
        clue_text2_myAct = (TextView)view_myAct.findViewById(R.id.clue_text2);
        clue_linear_myAct.setOnLongClickListener(new ClueLongClick());
        clue_text1_myAct.setText(getResources().getString(R.string.clue_myact_text1));
        clue_text2_myAct.setText(getResources().getString(R.string.clue_myact_text2));
        mv.setClue(1, clue_linear_myAct);
    }
    
    private void initListView_myTucao(){
    	if(!flag_ListView_myTucao_isInit){
	        connectSuccessLayout_myTucao=(LinearLayout)view_myTucao.findViewById(R.id.connectSuccessLayout);
	        loading_myTucao=(RelativeLayout)view_myTucao.findViewById(R.id.loading);
	        progressBar_myTucao=(ProgressBar)view_myTucao.findViewById(R.id.progressBar);
	        failToConnect_myTucao=(TextView)view_myTucao.findViewById(R.id.failToConnect);
	        failToConnect_myTucao.setOnClickListener(new TextViewConnectFail());
	        connectSuccessLayout_myTucao.setOnClickListener(new LinearLayout_MyTucao_ToFaBiao());
	        timing(2);
	        flag_ListView_myTucao_isInit = true;
	        mv.setConnectSuccessLayoutMyOrg(2, connectSuccessLayout_myTucao);
        }
    	
        mListView3=(XListView)view_myTucao.findViewById(R.id.Lv1);  
        mAdapter_myTucao=new Card_myTucao_adapter(this,getItems_myTucao()); 
        mv.setMyTucaoAdapter(mAdapter_myTucao);
        mHandler_myTucao = new Handler();
        mListView3.setPullLoadEnable(false);//设置上拉刷新
        mListView3.setPullRefreshEnable(false);//设置下拉刷新
        mListView3.setXListViewListener(this);
        mListView3.setAdapter(mAdapter_myTucao);
        
        clue_linear_myTucao = (RelativeLayout)view_myTucao.findViewById(R.id.clue_linear);
        clue_text1_myTucao = (TextView)view_myTucao.findViewById(R.id.clue_text1);
        clue_text2_myTucao = (TextView)view_myTucao.findViewById(R.id.clue_text2);
        clue_linear_myTucao.setOnLongClickListener(new ClueLongClick());
        clue_text1_myTucao.setText(getResources().getString(R.string.clue_mytucao_text1));
        clue_text2_myTucao.setText(getResources().getString(R.string.clue_mytucao_text2));
        mv.setClue(2, clue_linear_myTucao);
    }
    
    
	public RelativeLayout getClu(){
		return clue_linear_myOrg;
	}
	
    
    public void refresh()
    {
    	if(currIndex == 0){
	    	mHandler_myAct.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_myActivity!=null)
	    				mCards_myActivity.clear();
	    			buildMyActCard();
	    			mAdapter_myActivity.notifyDataSetChanged();
	    			mListView2.setAdapter(mAdapter_myActivity);
	    		}
	    	});
    	}
    	else if(currIndex == 1){
	    	mHandler_myOrg.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_myOrg!=null)
	    				mCards_myOrg.clear();
	    			buildMyOrgCard();
	    			mAdapter_myOrg.notifyDataSetChanged();
	    			mListView1.setAdapter(mAdapter_myOrg);
	    		}
	    	});
    	}
    	else if(currIndex == 2){
	    	mHandler_myTucao.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_myTucao!=null)
	    				mCards_myTucao.clear();
	    			buildMyTucaoCard();
	    			mAdapter_myTucao.notifyDataSetChanged();
	    			mListView3.setAdapter(mAdapter_myTucao);
	    		}
	    	});
    	}
    }
    
    private List<Card_myOrg> getItems_myOrg(){
    	mCards_myOrg=new ArrayList<Card_myOrg>();
    	buildMyOrgCard();
        return mCards_myOrg;  
    }
    private void buildMyOrgCard(){
    	try {
          	JSONObject jsonObject;
          	jsonObject = new JSONObject(jResult);
          	JSONArray jsonArray=jsonObject.getJSONArray("my_org");
          	int temp_arrayLength = jsonArray.length();
          	
          	if(temp_arrayLength == 0 && buildCard_myOrg == false && currIndex == 0){
          		clue_linear_myOrg.setVisibility(0);
    			connectSuccessLayout_myOrg.setVisibility(4);
    			buildCard_myOrg = true;
          	}
          	
          	for(int i=0;i<temp_arrayLength;i++)  
          	{
          		JSONObject jsonpet = jsonArray.getJSONObject(i);
          		Card_myOrg mCard=new Card_myOrg(jsonpet.getString("OrganizationName") ,jsonpet.getString("Logo") , 
          										jsonpet.getString("Rate") ,jsonpet.getInt("OrganizationID"),
          										1); 
          		mCards_myOrg.add(mCard);
          	}
        } catch (JSONException e) {
          	e.printStackTrace();
        }
    }
    
    private List<Card_myActivity> getItems_myAct(){
    	mCards_myActivity = new ArrayList<Card_myActivity>();
    	buildMyActCard();
        return mCards_myActivity;
    }
    private void buildMyActCard(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("my_activity");
        	int temp_arrayCount =jsonArray.length();
        	
          	if(temp_arrayCount == 0 && buildCard_myAct == false && currIndex == 1){
          		clue_linear_myAct.setVisibility(0);
    			connectSuccessLayout_myAct.setVisibility(4);
    			buildCard_myAct = true;
          	}
        	
        	for(int i=0; i<temp_arrayCount; i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_myActivity mCard=new Card_myActivity(jsonpet.getString("ActivityName"),jsonpet.getString("ActivityTime"), jsonpet.getString("Address"),
        										jsonpet.getString("PhotoDir"),
        										jsonpet.getInt("ActivityID"),jsonpet.getInt("OrganazationID"),
        										1,4.0,
        										jsonpet.getString("girllim"),jsonpet.getString("boylim"),
        										jsonpet.getString("girlnum"),jsonpet.getString("boynum"));  
        		mCards_myActivity.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    }
    
    private List<Card_myTucao> getItems_myTucao(){
    	mCards_myTucao = new ArrayList<Card_myTucao>();
    	buildMyTucaoCard();
        return mCards_myTucao;
    }
    private void buildMyTucaoCard(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("my_tucao");
        	int temp_arrayLength = jsonArray.length();
        	
          	if(temp_arrayLength == 0 && buildCard_myTucao == false && currIndex == 2){
          		clue_linear_myTucao.setVisibility(0);
    			connectSuccessLayout_myTucao.setVisibility(4);
    			buildCard_myTucao = true;
          	}
        	
        	for(int i=0;i<temp_arrayLength;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_myTucao mCard=new Card_myTucao(jsonpet.getString("content") , jsonpet.getString("nickname"),
        				jsonpet.getString("zan_cnt") ,jsonpet.getString("Tucaocnt") ,    getRandomColor(),
        												  jsonpet.getInt("tucao_id") ,   jsonpet.getString("tucao_time"),0);  
        		mCards_myTucao.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    }
    
    
    //因为进入吐槽墙页面需要背景颜色的值，所以这里随机生成一个
    public int getRandomColor(){
    	int result=0;
    	int temp=(int)(Math.random()*10);
    	switch(temp%7){
    	case 0:
    		result=R.color.random_0;break;
    	case 1:
    		result=R.color.random_1;break;
    	case 2:
    		result=R.color.random_2;break;
    	case 3:
    		result=R.color.random_3;break;
    	case 4:
    		result=R.color.random_4;break;
    	case 5:
    		result=R.color.random_5;break;
    	case 6:
    		result=R.color.random_6;break;
    	}
    	return result;
    }
    
    
    
    private void GoBack(){
		Intent intent=getIntent();
		intent.putExtra("time", timeCount);
		setResult(MyStatic.FINISHTHIS,intent);
		finish();
    }
    class ButtonOnClickListener implements OnClickListener{
		public void onClick(View v) {
			GoBack();
		}
    }
	public void onBackPressed() {
		GoBack();
	}
	
	class TextViewConnectFail implements OnClickListener{
		public void onClick(View v) {
			if(v == failToConnect_myOrg){
				layoutChanged_myOrg = false;
				timeExhausted_myOrg = false;
				failToConnect_myOrg.setVisibility(4);
				progressBar_myOrg.setVisibility(0);
				timing(1);
				methodName = "initMyOrg";
	        	jGet(methodName);
			}
			else if(v == failToConnect_myAct){
				layoutChanged_myAct = false;
				timeExhausted_myAct = false;
				failToConnect_myAct.setVisibility(4);
				progressBar_myAct.setVisibility(0);
				timing(0);
				methodName = "initMyAct";
	        	jGet(methodName);
			}
			else if(v == failToConnect_myTucao){
				layoutChanged_myTucao = false;
				timeExhausted_myTucao = false;
				failToConnect_myTucao.setVisibility(4);
				progressBar_myTucao.setVisibility(0);
				timing(2);
				methodName = "initMyTucao";
	        	jGet(methodName);
			}
		}
	}
	
	class LinearLayout_MyTucao_ToFaBiao implements OnClickListener{
		public void onClick(View arg0) {
			Toast.makeText(MyZone.this, "gogogo", Toast.LENGTH_SHORT).show();
		}
	}
	
	class ClueLongClick implements OnLongClickListener{
		public boolean onLongClick(View v) {
			if(v == clue_linear_myOrg){
				Intent intent = new Intent(MyZone.this,Shetuan_Subscribe_Main.class);
				startActivity(intent);
			}
			else if(v == clue_linear_myAct){
				Intent intent=new Intent(MyZone.this,Main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else if(v == clue_linear_myTucao){
				Intent intent = new Intent(MyZone.this,TuCaoQiang_Comment_Fabu.class);
				startActivityForResult(intent, MyStatic.TUCAOQIANGFABU);
				
			}
			return true;
		}
	}
	
	
	//长按去发表吐槽，返回时刷新页面
	//其实可以在onResume()里实现不需要onactivityresult，但已经实现了就算了不改了
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MyStatic.TUCAOQIANGFABU){
			if(resultCode == MyStatic.SUCCESSTHIS){
				flag_ListView_myAct_isInit = false;
				timeExhausted_myTucao = false;
				layoutChanged_myTucao = false;
				buildCard_myTucao = false;
				initListView_myTucao();
				methodName = "initMyTucao";
            	jGet(methodName);
			}
		}
	}
	
	
	public void onRefresh() {
	}
	public void onLoadMore() {
	}
	
	
	private void timing(final int i){
        Timer timerLoading=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				if(i == 0){
					timeExhausted_myAct =true;
				}
				else if(i == 1){
					timeExhausted_myOrg = true;
				}
				else if(i == 2){
					timeExhausted_myTucao = true;
				}
    			Message msg = new Message();
    			msg.what = 0x000;
    			msg.obj = "";
    			handler.sendMessage(msg);
			}
		}; 
		timerLoading.schedule(timertask, MyStatic.LoadingTime);
	}
	
	private void changeLayout(){
		if(currIndex == 0){
			if(!layoutChanged_myAct){
				if(timeExhausted_myAct){
					progressBar_myAct.setVisibility(4);
					failToConnect_myAct.setVisibility(0);
				}
				else {
					loading_myAct.setVisibility(4);
					connectSuccessLayout_myAct.setVisibility(0);
				}
			}
			layoutChanged_myAct = true;
		}
		else if(currIndex == 1){
			if(!layoutChanged_myOrg){
				if(timeExhausted_myOrg){
					progressBar_myOrg.setVisibility(4);
					failToConnect_myOrg.setVisibility(0);
				}
				else {
					loading_myOrg.setVisibility(4);
					connectSuccessLayout_myOrg.setVisibility(0);
				}
			}
			layoutChanged_myOrg = true;
		}
		else if(currIndex == 2){
			if(!layoutChanged_myTucao){
				if(timeExhausted_myTucao){
					progressBar_myTucao.setVisibility(4);
					failToConnect_myTucao.setVisibility(0);
				}
				else {
					loading_myTucao.setVisibility(4);
					connectSuccessLayout_myTucao.setVisibility(0);
				}
			}
			layoutChanged_myTucao = true;
		}
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
    
    
    private void InitTextView() {
    	myAct=(TextView)findViewById(R.id.my_act);
        myOrg=(TextView)findViewById(R.id.my_org);
        myTucao=(TextView)findViewById(R.id.my_tucao);
        myAct.setOnClickListener(new MyOnClickListener(0));
        myOrg.setOnClickListener(new MyOnClickListener(1));
        myTucao.setOnClickListener(new MyOnClickListener(2));
    }
    
    private void InitImageView() {  
        imageView= (ImageView) findViewById(R.id.cursor);  
        bmpW = imageView.getWidth();// 获取图片宽度 
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;// 获取分辨率宽度  
        LayoutParams para = new LayoutParams(screenW / 3, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(para);
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);// 设置动画初始位置  
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
  
    public class MyOnPageChangeListener implements OnPageChangeListener{  
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量  
        int two = one * 2;// 页卡1 -> 页卡3 偏移量  
        public void onPageScrollStateChanged(int arg0) {  
        }  
  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
  
        public void onPageSelected(int arg0) {  
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。  
            currIndex = arg0;  
            
            //给tab文字加选中颜色,先都置为浅黑色然后再根据currIndex修改当前页的导航文字颜色为绿色
            myOrg.setTextColor(getResources().getColor(R.color.text_black_tab));
            myAct.setTextColor(getResources().getColor(R.color.text_black_tab));
            myTucao.setTextColor(getResources().getColor(R.color.text_black_tab));
            if(currIndex == 0)
            	myAct.setTextColor(getResources().getColor(R.color.widget_tab_text));
            else if (currIndex == 1)
            	myOrg.setTextColor(getResources().getColor(R.color.widget_tab_text));
            else if (currIndex == 2)
            	myTucao.setTextColor(getResources().getColor(R.color.widget_tab_text));
            
            //当打开该tab时才初始化
            if(flag_ListView_myAct_isInit == false && currIndex == 0){
            	initListView_myAct();
            	methodName = "initMyAct";
            	jGet(methodName);
            }
            else if(flag_ListView_myOrg_isInit == false && currIndex == 1){
            	initListView_myOrg();
            	methodName = "initMyOrg";
            	jGet(methodName);
            }else if(flag_ListView_myTucao_isInit == false && currIndex == 2){
            	initListView_myTucao();
            	methodName = "initMyTucao";
            	jGet(methodName);
            }
            animation.setFillAfter(true);//True:图片停在动画结束位置  
            animation.setDuration(300);  
            imageView.startAnimation(animation);  
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
