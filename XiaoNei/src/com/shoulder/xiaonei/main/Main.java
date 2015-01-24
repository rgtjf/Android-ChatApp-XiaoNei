package com.shoulder.xiaonei.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;  
import java.util.List;  
import java.util.Timer;
import java.util.TimerTask;

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
import org.w3c.dom.Text;

import chat.Contacts;

import com.avos.avoscloud.AVAnalytics;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
  

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.color;
import com.shoulder.xiaonei.R.dimen;
import com.shoulder.xiaonei.R.drawable;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.R.menu;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.card.Card_main_adapter;
import com.shoulder.xiaonei.card.Card_shetuan_yugao;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.others.Login_or_Register;
import com.shoulder.xiaonei.others.MyInformation;
import com.shoulder.xiaonei.others.Register_Page1;
import com.shoulder.xiaonei.others.School_List;
import com.shoulder.xiaonei.others.Set_Up;
import com.shoulder.xiaonei.others.Start_Page;
import com.shoulder.xiaonei.tucao.Text_Full;
import com.shoulder.xiaonei.tucao.TuCaoQiang;
import com.shoulder.xiaonei.myClass.*;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;
import com.shoulder.xiaonei.myClass_Machine.ConvertDensity;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.LoginMachine;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.myClass_myView.CircularImage;
import com.shoulder.xiaonei.myZone.MyZone;
import com.shoulder.xiaonei.myZone.YourZone;
import com.shoulder.xiaonei.myZone.MyZone.MyOnPageChangeListener;
import com.shoulder.xiaonei.myZone.MyZone.MyViewPagerAdapter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.net.Uri;
import android.os.Bundle;  
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.System;
import android.annotation.SuppressLint;
import android.app.Activity;  
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;  
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.view.Window;  
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;  
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
  
public class Main extends Activity implements IXListViewListener,OnDismissCallback {  
  
	private String versionThis = "1.0";
	private String versionNew = "1.0";
	private Dialog dialogUpdate;
	private TextView description;
	private Button yes_update;
	private String updateAddress = "http://sheyou.me/XiaoNei.apk";
	private String description_text;
	
	public static HttpClient httpClient = new DefaultHttpClient();
	private String methodName;
	private String jResult ="";
	private Handler mHandler_all,mHandler_mySubscribe;
	private String method_getNewFriends = "getNewFreinds";
	private final static int MSG_getNewFriends = 0x601;
	
	protected SharedPreferences preferences,preferences_account,preferences_schoolId;
	protected SharedPreferences.Editor editor;
	protected File cache;
	
	public String ipName="";
	private String myDir="";
	private MyVal mv;
	
	private com.shoulder.xiaonei.myClass_myView.MyPageView viewPager;
	private List<View> views;
	private View view_all,view_mySubscribe;
	
	private RelativeLayout clue_linear_main,clue_linear_mysubscribe;
	private TextView clue_text1_main,clue_text1_mysubscribe;
	private TextView clue_text2_main,clue_text2_mysubscribe;
	private Boolean buildCard_main = false;//当该页面第一次获取信息时，若无数据，则使clue页面可见
	private Boolean buildCard_mySubscribe = false;
	private Boolean hasBuildedCard_main = false;//如果已经获取过信息，则为true，保证clue出现时是在第一次获取信息时
	private Boolean hasBuildedCard_mySubscribe = false;
	
	private RelativeLayout connectSuccessLayout_all,connectSuccessLayout_mySubscribe;
	private RelativeLayout loading_all,loading_mySubscribe;
	private ProgressBar progressBar_all,progressBar_mySubscribe;
	private TextView failToConnect_all,failToConnect_mySubscribe;
	private Boolean layoutChanged_all = false,layoutChanged_mySubscribe = false;
	private Boolean timeExhausted_all = false,timeExhausted_mySubscribe = false;
	
	private Button buttonLeft;
	private Button buttonRight_btn;
	private ImageView logoMenu;
	private RelativeLayout rel_spinnerTitle;
	private TextView text;
	private ImageView spinner_payAttentionTo;
	private PopupWindow pop_spinner;
	private TextView spinner_all;
	private TextView spinner_mySubscribe;
	private int animationDuration = 300;
	private int animationStartOffSet = 50;
	private int moveDistance_dp = 5;
	private int moveDistance_px = 0;
			
	private CircularImage myAvatar;
	private TextView myName;
	private TextView myIntroduce;
	private TextView textMyZone;
	private Button buttonMyZone;
	private Button buttonShetuan;
	private Button buttonTuCao;
	private Button buttonContacts;
	private Button buttonChangeSchool;
	private Button buttonSetup;
	private AsyncImageLoader asyncImageLoader;
	
	private SlidingMenu menu;  
	
	private XListView mListView_all,mListView_mySubscribe;  
	private List<Card_main> mCards_all,mCards_mySubscribe;
	private Card_main_adapter mAdapter_all,mAdapter_mySubscribe;
	
	private Dialog mDialog;

	private PopupWindow pop;
	private RelativeLayout scan;
	private RelativeLayout goToSubscribe;
	
	private TextView historyActivities;
	private long actTime_before_history_gap = 60 * 48;//获取往期活动时，每次下拉将请求时间往前两天
	private int historyButton_closeTime = 1200;
	
	private static final int LISTSWING = 500;//listview动画的时间
	
	private final int MSG_CloseHistoryButton = 0x924;
	
	private String change_schoolId = "1";
    private String change_schoolName = "";
    private String change_schoolBranch = "";
	
	private int currentPage = 0;//1表示“我的订阅”下活动，0表示“所有活动”下活动
	private String schoolIdArray[] = new String[15];//获取相关学校id
	private int schoolIdArrayLength = 100;
	private int schoolIdCount = 0;
	private int schoolIdFlag = 0;//当前请求到第几所学校
	private int count_loop = 10; //一次加载多少内容
	private Boolean timeBlock = false;//为真时，下一个jGet()等待
	private int flag_actId = MyStatic.MAXINT;
	private String flag_actTime;
	private long actTime_before = 180;//以3小时前的时间为节点获取活动
	
	private Boolean wait_double = true;//连续点击退出应用，仅当wait_double为false可以退出应用
	
    private Boolean flag_ListView_all_isInit = false;
    private Boolean flag_ListView_mySubscribe_isInit = false;
    
    private int flag_allFirstPullToRefresh = 0;//if 1,just init;if flag_ = 2,is the first time pull
    private Boolean flag_wantHistoryActivities = false;//当第一次下拉刷新时，提问是否需要更多往期活动，是则下拉表示加载往期活动
    
	protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        
        UmengUpdateAgent.update(this);//友盟的更新
        
        AVAnalytics.trackAppOpened(getIntent());//leanCloud的数据统计
        
        mv=((MyVal)getApplicationContext());
        ipName=mv.getIpName();
        myDir=mv.getMyDir();
        
        flag_actTime = TimeMachining.getCurrentTime(actTime_before);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title1model);
        
        ExitApplication.getInstance().addActivity(Main.this);//利用单例模式管理Activity，使能完全退出
        
        initSlidingMenu();//左拉菜单
        initViewPager();//首页现在有“所有活动”、“订阅活动”两个listview，用viewpage管理
        InitPopMenu();//初始化function菜单的popwindow
        initPopSpinner();//初始化左上角“所有活动/订阅活动”的popwindow
        
        
        cache = new File(myDir, mv.getFileNameHo());
        if(!cache.exists()){cache.mkdirs();} //该文件夹存放图片等缓存，若不存在，则创建之
        
        change_schoolId = mv.getSchoolId(); //假如从login界面跳转过来，这里就先获取schoolId;
        
        preferences = getSharedPreferences("jResult", MODE_PRIVATE);
        editor = preferences.edit();
        
		new Thread()
		{
			public void run()
			{
				String getResponse = LoginMachine.Login();
				if(!getResponse.equals("false")){
					Message msgTextMyZone = new Message();
					msgTextMyZone.what = 0x221;
					msgTextMyZone.obj = "";
					handler.sendMessage(msgTextMyZone);
					
					change_schoolId = mv.getSchoolId();
					
				}
		        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		        if(cd.isConnectingToInternet()==true)
		        {
	            	getAllActivity();
		        }
		        else
		        {
		        	try{
			        	jResult = preferences.getString("jRef", null);
			        	if(jResult==null)
			        	{
			        		jResult = preferences.getString("jInit", null);
			        	}
		        	}
		        	catch (Exception e){
		        		e.printStackTrace();
		        	}
		        }
			}
		}.start();
		
		initWidgets();
		
        Dialog_Register_Login.Set_Dialog_Register_Login(Main.this,"main");
        
//      jGet("getUpdate");
    }  
	
	
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0x000:
				jResult = msg.obj.toString();
 				changeLayout();
				break;
			case 0x111:
				jResult = msg.obj.toString();
				refresh();
				editor.putString("jRef", jResult);
				editor.commit();
				break;
			case 0x112:
				jResult = msg.obj.toString();
				changeLayout();
				refresh();
				editor.putString("jInit", jResult);
				editor.commit();
				break;
			case 0x113:
				jResult = msg.obj.toString();
				loadMore();
				break;
			case 0x114:
				jResult = msg.obj.toString();
				changeLayout();
				refresh();
				editor.putString("jInit_MySubscribe", jResult);
				editor.commit();
				break;
			case 0x115:
				jResult = msg.obj.toString();
				buildSchoolArray();
				break;
			case 0x116:
				jResult = msg.obj.toString();
				refresh();
				changeLayout();
				editor.putString("jInit_All", jResult);
				editor.commit();
				break;
			case 0x220:
				jResult = msg.obj.toString();
				getUpdate();
				break;
			case 0x221:
				jResult = msg.obj.toString();
				setMainMenu();
				break;
			case MSG_getNewFriends:
				getNewFriends(msg.obj.toString());
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
				String getMethod="";
				String getParams="";
				if(mtName.equals("mySubscribe")){
					getMethod="Activity/get_activity_info";
				}else if(mtName.equals("allActivity") || mtName.equals("a11_all") || mtName.equals("a13_all")){
					if(!(schoolIdFlag + 1> schoolIdArrayLength)){
						getMethod = "Activity/get_actbytime";
						getParams = "schoolid=" + schoolIdArray[schoolIdFlag] + "&" 
								   +"actid=" + flag_actId +"&"
								   +"acttime=" + flag_actTime ;
					}
				}else if(mtName.equals("getSchoolId")){
					getMethod ="Activity/get_relatedschool";
					getParams = "schoolid=" + change_schoolId;
				}
				else if(mtName.equals("getUpdate")){
					getMethod = "Home/get_version";
				}
				else if(mtName.equals(method_getNewFriends)){
					getMethod = "manage/get_my_guanzhuinfo";
				}
				else {
					getMethod="Activity/get_activity_info";
				}
				

				try
				{
					String result = CustomerHttpClient.get(ipName+getMethod+"?"+getParams);
					
						Message msg = new Message();
						if(mtName == "a11" || mtName.equals("a11_all"))
						{
							msg.what = 0x111;
						}
						else if(mtName == "a12")
						{
							msg.what = 0x112;
						}
						else if(mtName == "a13" || mtName.equals("a13_all"))
						{
							msg.what = 0x113;
						}
						else if(mtName=="mySubscribe")
						{
							msg.what = 0x114;
						}
						else if(mtName.equals("getSchoolId")){
							msg.what = 0x000;
							jResult = result;
							buildSchoolArray();
						}
						else if(mtName.equals("allActivity")){
							msg.what = 0x116;
						}
						else if(mtName.equals("getUpdate")){
							msg.what = 0x220;
						}
						else if(mtName.equals(method_getNewFriends)){
							msg.what = MSG_getNewFriends;
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
	
	
	private void initWidgets(){
		buttonLeft=(Button)findViewById(R.id.left_btn);
		buttonRight_btn=(Button)findViewById(R.id.right_btn);
		logoMenu=(ImageView)findViewById(R.id.logoMenu);
		text=(TextView)findViewById(R.id.text);
		rel_spinnerTitle=(RelativeLayout)findViewById(R.id.rel_spinnerTitle);
		spinner_payAttentionTo=(ImageView)findViewById(R.id.spinner_payAttentionTo);
		
		buttonLeft.setOnClickListener(new ButtonLeftListener());
		buttonRight_btn.setOnClickListener(new ButtonRightListener());
		spinner_payAttentionTo.setVisibility(0);
		rel_spinnerTitle.setOnClickListener(new TextTitleSpinner());
	}
	
	private void initPopSpinner(){
		LinearLayout layoutSpinner = (LinearLayout)getLayoutInflater().inflate(R.layout.popmenu_main_spinners, null);
        pop_spinner = new PopupWindow(layoutSpinner,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_spinner.setFocusable(true);
        pop_spinner.setOutsideTouchable(true);
        pop_spinner.update();
        pop_spinner.setBackgroundDrawable(new BitmapDrawable());
        
        spinner_all=(TextView)layoutSpinner.findViewById(R.id.all);
        spinner_mySubscribe=(TextView)layoutSpinner.findViewById(R.id.mySubscribe);
        
	    spinner_all.setOnClickListener(new OnClickListener() {
	      public void onClick(View v) {
	     	if(currentPage != 0){
	             pop_spinner.dismiss();
	             viewPager.setCurrentItem(0);  
	     	}
	      }
	    });
  
		spinner_mySubscribe.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		     	 if(mv.getLoginState() == 1){
		     		if(currentPage != 1){
		         		viewPager.setCurrentItem(1);   
		             	pop_spinner.dismiss();
		     		}
		     	 }
		     	 else if(mv.getLoginState() == 0){
		     		 if (mDialog == null) 
		     		{
		     			mDialog = Dialog_Register_Login.getMyDialog();
		     			mDialog.show();
		     		}
		     		else 
		     		{
		     			mDialog.show();
		     		}
		     	 }
		      }
		  });
	}
	
	private void InitPopMenu(){
		LinearLayout layout = (LinearLayout)getLayoutInflater().inflate(R.layout.popmenu_main_function, null);
        pop = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
        pop.setBackgroundDrawable(new BitmapDrawable());
        
        goToSubscribe=(RelativeLayout)layout.findViewById(R.id.goToSubscribe);
        scan=(RelativeLayout)layout.findViewById(R.id.scan);
        
        goToSubscribe.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
           	 Intent intent=new Intent();
           	 intent.setClass(Main.this, Shetuan_Subscribe_Main.class);
           	 startActivityForResult(intent, MyStatic.MAIN_GOTOSUBSCRIBE);
            }
        });
        
        scan.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Main.this,Scan.class);
				startActivity(intent);
				pop.dismiss();
			}
		});
        }
	
	private void InitValue(){
		change_schoolId = mv.getSchoolId();
		schoolIdCount = 0;
		schoolIdFlag = 0;
		flag_actId = MyStatic.MAXINT;
		flag_actTime = TimeMachining.getCurrentTime(actTime_before);
	}
	
	
    private void initViewPager() {  
        viewPager=(com.shoulder.xiaonei.myClass_myView.MyPageView)findViewById(R.id.vPager);  
        views=new ArrayList<View>();  
        LayoutInflater inflater=getLayoutInflater();  
        view_all=inflater.inflate(R.layout.card_main_listview, null);
        view_mySubscribe=inflater.inflate(R.layout.card_main_listview, null);
        
        initListView_all_loading();//先让loading实现，之后数据到了再initlistview，避免先看到“查看更多”的文字
        
        views.add(view_all);
        views.add(view_mySubscribe);
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    } 

    private void initListView_all() {  
    	InitValue();
    	
        clue_linear_main = (RelativeLayout)view_all.findViewById(R.id.clue_linear);
        clue_text1_main = (TextView)view_all.findViewById(R.id.clue_text1);
        clue_text2_main = (TextView)view_all.findViewById(R.id.clue_text2);
        clue_linear_main.setOnLongClickListener(new ClueLongClick());
        clue_text1_main.setText(getResources().getString(R.string.clue_main_text1));
        clue_text2_main.setText(getResources().getString(R.string.clue_main_text2));
    	 
        historyActivities=(TextView)view_all.findViewById(R.id.historyActivities);
        historyActivities.setOnClickListener(new TextHistoryActivities());
        
    	mListView_all=(XListView)view_all.findViewById(R.id.ListView); 
    	mAdapter_all=new Card_main_adapter(this, getItems());  
    	
    	//listview动画效果
    	SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = 
    			new SwingBottomInAnimationAdapter(mAdapter_all);
    	swingBottomInAnimationAdapter.setAbsListView(mListView_all);
    	swingBottomInAnimationAdapter.getViewAnimator().reset();
    	assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(LISTSWING);
    	
        mHandler_all = new Handler();
        mListView_all.setPullRefreshEnable(true);//设置下拉刷新
        mListView_all.setPullLoadEnable(true);//设置上拉刷新
        mListView_all.setXListViewListener(this);
        mListView_all.setAdapter(swingBottomInAnimationAdapter);
        
        //下拉显示“查看更多历史信息”的button后，滑动listview会让这个button消失
        mListView_all.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_MOVE:
					if(historyActivities.getVisibility() == 0){
						Animation animation =  new AlphaAnimation(100, 0);
						animation.setDuration(historyButton_closeTime);
						historyActivities.setAnimation(animation);
						historyActivities.setVisibility(8);
					}
				break;
				}
				return false;
			}
		});
    }  
    
    private void initListView_mySubscribe() {  
        clue_linear_mysubscribe = (RelativeLayout)view_mySubscribe.findViewById(R.id.clue_linear);
        clue_text1_mysubscribe = (TextView)view_mySubscribe.findViewById(R.id.clue_text1);
        clue_text2_mysubscribe = (TextView)view_mySubscribe.findViewById(R.id.clue_text2);
        clue_linear_mysubscribe.setOnLongClickListener(new ClueLongClick());
        clue_text1_mysubscribe.setText(getResources().getString(R.string.clue_mysubscribe_text1));
        clue_text2_mysubscribe.setText(getResources().getString(R.string.clue_mysubscribe_text2));
    	
    	mListView_mySubscribe=(XListView)view_mySubscribe.findViewById(R.id.ListView);  
    	mAdapter_mySubscribe=new Card_main_adapter(this, getItems());  
    	
    	//listview动画效果
    	SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = 
    			new SwingBottomInAnimationAdapter(mAdapter_mySubscribe);
//    	swingBottomInAnimationAdapter.getViewAnimator().reset();
//    	assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(LISTSWING);
    	swingBottomInAnimationAdapter.setAbsListView(mListView_mySubscribe);
    	
        mHandler_mySubscribe = new Handler();
        mListView_mySubscribe.setPullRefreshEnable(true);//设置下拉刷新
        mListView_mySubscribe.setPullLoadEnable(false);//设置上拉刷新
        mListView_mySubscribe.setXListViewListener(this);
        mListView_mySubscribe.setSelector(new ColorDrawable(color.selector));
        mListView_mySubscribe.setAdapter(swingBottomInAnimationAdapter);  
        
    }  
    
    private void initListView_all_loading(){
    	if(!flag_ListView_all_isInit){
	        connectSuccessLayout_all=(RelativeLayout)view_all.findViewById(R.id.connectSuccessLayout);
	        loading_all=(RelativeLayout)view_all.findViewById(R.id.loading);
	        progressBar_all=(ProgressBar)view_all.findViewById(R.id.progressBar);
	        failToConnect_all=(TextView)view_all.findViewById(R.id.failToConnect);
	        failToConnect_all.setOnClickListener(new TextViewConnectFail());
	        LinearInterpolator lir = new LinearInterpolator();
			progressBar_all.setInterpolator(lir);
	        timing(0);
	        flag_ListView_all_isInit = true;
        }
    }
    
    private void initListView_mySubscribe_loading(){
    	if(!flag_ListView_mySubscribe_isInit){
	        connectSuccessLayout_mySubscribe=(RelativeLayout)view_mySubscribe.findViewById(R.id.connectSuccessLayout);
	        loading_mySubscribe=(RelativeLayout)view_mySubscribe.findViewById(R.id.loading);
	        progressBar_mySubscribe=(ProgressBar)view_mySubscribe.findViewById(R.id.progressBar);
	        failToConnect_mySubscribe=(TextView)view_mySubscribe.findViewById(R.id.failToConnect);
	        failToConnect_mySubscribe.setOnClickListener(new TextViewConnectFail());
	        timing(1);
	        flag_ListView_mySubscribe_isInit = true;
        }
    }
    
    
	private void onLoad() {
		if (currentPage == 1){
			mListView_mySubscribe.stopRefresh();
			mListView_mySubscribe.stopLoadMore();
			mListView_mySubscribe.setRefreshTime("刚刚");
		}
		else {
			mListView_all.stopRefresh();
			mListView_all.stopLoadMore();
			mListView_all.setRefreshTime("让我想想..");
		}
	}
    
	//XListView的下拉刷新后的方法，因为要网络请求，所以onRefresh（）获得网络信息成功后，用handler调用refresh（）来更新主线程
    public void onRefresh()
    {
    	if(flag_wantHistoryActivities == true){
    		actTime_before += actTime_before_history_gap;
    	}
    	
    	InitValue();
    	methodName = "getSchoolId";
    	timeBlock = true;
    	jGet(methodName);
    	new Thread(){
    		public void run(){
            	while(timeBlock){};
            	if(currentPage == 1){
        	    	methodName = "a11";
        	    	jGet(methodName);
            	}else {
            		methodName = "a11_all";
            		jGet(methodName);
            	}
    		}
    	}.start();
    }
    public void refresh()
    {
    	if(currentPage == 1){
	    	mHandler_mySubscribe.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_mySubscribe!=null)
	    				mCards_mySubscribe.clear();
	    			buildCards();
	    			mAdapter_mySubscribe.notifyDataSetChanged();
	    			mListView_mySubscribe.setAdapter(mAdapter_mySubscribe);
	    			onLoad();
	    		}
	    	});
    	}
    	else{
    		initListView_all();
    		flag_allFirstPullToRefresh ++;
    		if(flag_allFirstPullToRefresh == 2){
    			historyActivities.setVisibility(0);
    		}
	    	mHandler_all.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_all!=null)
	    				mCards_all.clear();
	    			buildCards();
	    			mAdapter_all.notifyDataSetChanged();
	    			mListView_all.setAdapter(mAdapter_all);
	    			onLoad();
	    		}
	    	});
    	}
    	
    	jGet(method_getNewFriends);//每次刷新界面成功都请求一遍是否有好友
    	
    }
    
  //XListView的上拉加载更多后的方法，因为要网络请求，所以onLoadMore()获得网络信息成功后，用handler调用loadMore()来更新主线程
    public void onLoadMore()
    {
    	if(currentPage == 1 ){
	    	methodName = "a13";
	    	jGet(methodName);
    	}else {
    		methodName = "a13_all";
    		jGet(methodName);
    	}
    }
    public void loadMore()
    {
    	if(currentPage == 1){
	    	mHandler_mySubscribe.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			buildCards();
	    			mAdapter_mySubscribe.notifyDataSetChanged();
	    			onLoad();
	    		}
	    	});
    	}
    	else {
    		mHandler_all.post(new Runnable() {
				public void run() {
					buildCards();
					mAdapter_all.notifyDataSetChanged();
					onLoad();
				}
			});
    	}
    }
    
    private void initSlidingMenu() {//详见slidingMenu的文档
    	moveDistance_px = ConvertDensity.dip2px(Main.this, moveDistance_dp);
    	
    	menu = new SlidingMenu(this); 
        menu.setMode(SlidingMenu. LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset); 
        menu.setFadeDegree(0.75f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_main_version2);
        menu.setOnCloseListener(new MenuOnCloseListener());
        menu.setOnOpenListener(new MenuOnOpenListener());
        
        myAvatar=(CircularImage)findViewById(R.id.myAvatar);
        myName=(TextView)findViewById(R.id.myName);
        myIntroduce=(TextView)findViewById(R.id.myIntroduce);
        textMyZone=(TextView)findViewById(R.id.text_myZone);
        buttonMyZone=(Button)findViewById(R.id.myZone);//避免未声明就修改为“个人中心”的情况
		buttonShetuan=(Button)findViewById(R.id.shetuan);
		buttonTuCao=(Button)findViewById(R.id.tucaoqiang);
		buttonContacts=(Button)findViewById(R.id.contacts);
		buttonChangeSchool=(Button)findViewById(R.id.change_school);
		buttonSetup=(Button)findViewById(R.id.set_up);
		
		myAvatar.setOnClickListener(new ButtonGOMyIntroduce());
		myName.setOnClickListener(new ButtonGOMyIntroduce());
		myIntroduce.setOnClickListener(new ButtonGOMyIntroduce());
		buttonMyZone.setOnClickListener(new ButtonMyZoneListeneer());
		buttonShetuan.setOnClickListener(new ButtonSheTuan());
		buttonTuCao.setOnClickListener(new ButtonTuCaoQiangListeneer());
		buttonContacts.setOnClickListener(new ButtonContacts());
		buttonSetup.setOnClickListener(new ButtonSetUp());
		buttonChangeSchool.setOnClickListener(new ButtonChangeSchool());
    } 
    class MenuOnOpenListener implements OnOpenListener{
		public void onOpen() {
			slideLogoMenu(0, -moveDistance_px);
		}
    }
    class MenuOnCloseListener implements OnCloseListener{
		public void onClose() {
			slideLogoMenu(0, moveDistance_px);
		}
    }
    private void setMainMenu(){//登陆成功后更新菜单上的个人信息，包括头像等
    	asyncImageLoader = new AsyncImageLoader(Main.this);
    	
    	textMyZone.setText(getResources().getString(R.string.menu_main_buttonText_myZone_login));
		myName.setText(MyVal.getApplication().getMyName());
		myIntroduce.setText(MyVal.getApplication().getMyIntroduce());
		
        //图片缓存
		final String imageUrl = MyVal.getApplication().getMyAvator();
		myAvatar.setTag(imageUrl);
        myAvatar.setImageResource(R.drawable.fill_color);
        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
        		myAvatar); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	myAvatar.setImageDrawable(drawable); 
    }
    
    
    public void onBackPressed() {  
        //点击返回键关闭滑动菜单  
        if (menu.isMenuShowing()) {  
            menu.showContent();  
        } else {
        	 //用一个新线程判断是否连续两次双击，这里设为监听3.5秒内的点击
	         if(wait_double == true){
	             wait_double = false;
	             Thread thread = new Thread(){
	                public void run(){
	                   try {
	                      sleep(3500);
	                      if(wait_double == false){
	                         wait_double = true;
	                      }
	                   } catch (InterruptedException e) {
	                      e.printStackTrace();
	                   }
	                }
	             };
	             thread.start();
	             Toast.makeText(Main.this, "再点击一次退出应用", Toast.LENGTH_LONG).show();
	          }
	          else{
	             wait_double = true;
	             finish();
	             ExitApplication.getInstance().exit();
	          }
        }  
    }  
    
  
	private List<Card_main> getItems()   
	    {
			if(currentPage == 1){
		        mCards_mySubscribe=new ArrayList<Card_main>();
		        buildCards();
		        return mCards_mySubscribe;  
			}else {
				mCards_all=new ArrayList<Card_main>();
				buildCards();
				return mCards_all;
			}
	    }  
  
	public void buildCards(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("activity_all_info");
        	int temp_arrayCount =jsonArray.length();
        	
        	//当第一次接受数据时，若无数据，则显示clue页面
        	if(temp_arrayCount == 0){
        		if(buildCard_main == false && currentPage == 0 && hasBuildedCard_main == false){
        			clue_linear_main.setVisibility(0);
        			connectSuccessLayout_all.setVisibility(4);
        		}
        		else if (buildCard_mySubscribe == false && currentPage == 1 && hasBuildedCard_mySubscribe == false){
        			clue_linear_mysubscribe.setVisibility(0);
        			connectSuccessLayout_mySubscribe.setVisibility(4);
        			buildCard_mySubscribe = true;
        		}
        	}
        	
        	for(int i=0;i<count_loop;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_main mCard=new Card_main(jsonpet.getString("ActivityName"),jsonpet.getString("ActivityTime"), jsonpet.getString("Address"),
        										jsonpet.getString("PhotoDir"),
        										jsonpet.getInt("ActivityID"),jsonpet.getInt("OrganazationID"),
        										jsonpet.getInt("girllim"),jsonpet.getInt("boylim"),
        										jsonpet.getInt("girlnum"),jsonpet.getInt("boynum")); 
        		if(currentPage == 1){
        			mCards_mySubscribe.add(mCard);
        			hasBuildedCard_mySubscribe = true;
        		}
        		else {
        			mCards_all.add(mCard);
        			hasBuildedCard_main = true;
        		}
        		if (i == temp_arrayCount-1){
        			flag_actId = jsonpet.getInt("ActivityID");
        			flag_actTime = TimeMachining.TimeTranslatorToGetJson(jsonpet.getString("ActivityTime"));
        		}
        		
        	}
        } catch (JSONException e) {
        	//当获取活动数少于10条时，更新学校为下一所关联学校（下个版本这个功能就不需要了）
        	if(currentPage == 0){
	        	e.printStackTrace();
	    		schoolIdFlag++;
	    		flag_actTime = TimeMachining.getCurrentTime(actTime_before);
        	}
        }
	}

	
	//点击左上角，左拉菜单
    class ButtonLeftListener implements OnClickListener{
		public void onClick(View v) {
	        if (menu.isMenuShowing()) {  
	            menu.showContent();  
	        } else {  
	            menu.showMenu();  
	        }
		}
    }
    
    class ButtonGOMyIntroduce implements OnClickListener{
		public void onClick(View v) {
			if(mv.getLoginState() == 1){
				Intent intent = new Intent(Main.this, MyInformation.class);
				startActivity(intent);
			}
	      	 else if(mv.getLoginState() == 0){
        		 if (mDialog == null) 
        		{
        			mDialog = Dialog_Register_Login.getMyDialog();
        			mDialog.show();
        		}
        		else 
        		{
        			mDialog.show();
        		}
      	 }
		}
    }
    
	class ButtonTuCaoQiangListeneer implements OnClickListener{
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(Main.this,TuCaoQiang.class);
			startActivityForResult(intent, MyStatic.MAIN_GOTOSECOND);
		}
	}
	class ButtonContacts implements OnClickListener{
		public void onClick(View v) {
	       	 if(mv.getLoginState() == 1){
	  			Intent intent=new Intent();
	  			intent.setClass(Main.this,Contacts.class);
	  			startActivityForResult(intent, MyStatic.MAIN_GOTOSECOND);
	      	 }
	      	 else if(mv.getLoginState() == 0){
	        		 if (mDialog == null) 
	        		{
	        			mDialog = Dialog_Register_Login.getMyDialog();
	        			mDialog.show();
	        		}
	        		else 
	        		{
	        			mDialog.show();
	        		}
	      	 }
		}
	}
	class ButtonMyZoneListeneer implements OnClickListener{
		public void onClick(View v) {
       	 if(mv.getLoginState() == 1){
 			Intent intent=new Intent();
 			intent.setClass(Main.this,MyZone.class);
 			startActivityForResult(intent, MyStatic.MAIN_GOTOSECOND);
     	 }
     	 else if(mv.getLoginState() == 0){
       		 if (mDialog == null) 
       		{
       			mDialog = Dialog_Register_Login.getMyDialog();
       			mDialog.show();
       		}
       		else 
       		{
       			mDialog.show();
       		}
     	 }
		}
	}
	class ButtonSheTuan implements OnClickListener{
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(Main.this,Shetuan_Subscribe_Main.class);
			startActivityForResult(intent, MyStatic.MAIN_GOTOSECOND);
		}
		
	}
	class ButtonSetUp implements OnClickListener{
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(Main.this,Set_Up.class);
			startActivityForResult(intent, MyStatic.MAIN_GOTOSECOND);
		}
	}
	class ButtonChangeSchool implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(Main.this,School_List.class);
			startActivityForResult(intent, MyStatic.GOTOCHANGESCHOO);
		}
	}
	
	//未登陆点击很多需要登录的地方会提醒你先登录，这时弹出dialog的登陆和注册的button
	class ButtonLogin implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent(Main.this, Login.class);
			intent.putExtra(MyStatic.KEYNAME_From, "Main");
			startActivity(intent);
			//将页面的所有dialog、menu关闭，登陆成功回到main界面时用户能看到干净的页面
			mDialog.dismiss();
			if (pop != null && pop.isShowing()) {
				pop.dismiss();
			}
			if (menu.isMenuShowing()){
				menu.showContent();
			}
		}
		
	}
	class ButtonRegister implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent(Main.this,Register_Page1.class);
			startActivity(intent);
			mDialog.dismiss();
			if (pop != null && pop.isShowing()) {
				pop.dismiss();
			}
			if (menu.isMenuShowing()){
				menu.showContent();
			}
		}
	}
	
	//网络连接失败时，点击“网络连接失败”重新获取数据
	class TextViewConnectFail implements OnClickListener{
		public void onClick(View v) {
			if(v == failToConnect_all){
				layoutChanged_all = false;
				timeExhausted_all = false;
				failToConnect_all.setVisibility(4);
				progressBar_all.setVisibility(0);
				timing(0);
				getAllActivity();
			}
			else if(v == failToConnect_mySubscribe){
				layoutChanged_mySubscribe = false;
				timeExhausted_mySubscribe = false;
				failToConnect_mySubscribe.setVisibility(4);
				progressBar_mySubscribe.setVisibility(0);
				timing(1);
				methodName = "a12";
	        	jGet(methodName);
			}
		}
	}
	
	//左上切换标签
	class TextTitleSpinner implements OnClickListener{
		public void onClick(View arg0) {
			if(pop_spinner!=null && pop_spinner.isShowing()) {
				pop_spinner.dismiss();
	        }
			else  {
		         // 弹出自定义的菜单
				pop_spinner.showAsDropDown(rel_spinnerTitle);
		         
				 if(menu.isMenuShowing()){
					menu.showContent();
				 }
			}
		}
	}
	
	//点击获取更多历史信息，此时将lag_wantHistoryActivities置为true，刷新页面
	class TextHistoryActivities implements OnClickListener{
		public void onClick(View v) {
			flag_wantHistoryActivities = true;
			historyActivities.setVisibility(8);
			onRefresh();
		}
	}
	
	//右上button
	class ButtonRightListener implements OnClickListener{
		public void onClick(View arg0) {
			ActionMenuWork();
		}
	}
	private void ActionMenuWork(){
        if(pop!=null && pop.isShowing()) {
            pop.dismiss();
        }
		else  {
	         // 弹出自定义的菜单
	         pop.showAsDropDown(buttonRight_btn);
	         
			 if(menu.isMenuShowing()){
				menu.showContent();
			 }
		}
	}
	
	//当没有数据时，会显示clue页面，长按可以跳转相应的页面，如订阅活动为0时，长按可以跳转去订阅更多社团
	class ClueLongClick implements OnLongClickListener{
		public boolean onLongClick(View v) {
			if(v == clue_linear_main){
				Intent intent = new Intent(Main.this,School_List.class);
				startActivityForResult(intent, MyStatic.GOTOCHANGESCHOO);
			}
			else if(v == clue_linear_mysubscribe){
				Intent intent = new Intent(Main.this,Shetuan_Subscribe_Main.class);
				startActivity(intent);
			}
			return true;
		}
	}
	
	
	private void buildSchoolArray(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("relateschool");
        	schoolIdCount = jsonArray.length() - 1;
        	
        	JSONObject jsonpetLength = jsonArray.getJSONObject(schoolIdCount);
        	schoolIdArrayLength = jsonpetLength.getInt("cnt");
        	
        	for (int i=0;i<schoolIdCount;i++){
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		schoolIdArray[i] = jsonpet.getString(i+"");
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        timeBlock = false;
	}
	
	
	//查看是否有人加我为好友而我还没确定或拒绝，若有该类信息，弹出dialog
	private void getNewFriends(String jResult_friends){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult_friends);
        	JSONArray jsonArray=jsonObject.getJSONArray("guanzhuinfo");
        	int friendsNumber = jsonArray.length();
        	if(friendsNumber == 0)
        		return ;
        	
        	for(int i=0; i<friendsNumber ;i++){
        		
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		String fId = jsonpet.getString("loginid");
        		String fName = jsonpet.getString("nickname");
        		int fSex = jsonpet.getInt("Sex"); 
        		String fAvator = jsonpet.getString("photo");
        		
        		showDialogNewFriends(fId, fName, fSex, fAvator);
        	}
        	
        } catch (JSONException e) {
        	e.printStackTrace();
        }
	}
	private void showDialogNewFriends(final String fId ,final String fName ,int fSex ,String fAvator){
		final Dialog dialogNewFriends = new Dialog(Main.this, R.style.dialog_huodong_tixing);
		dialogNewFriends.setContentView(R.layout.dialog_youhavefriends);
		TextView hiMan = (TextView)dialogNewFriends.findViewById(R.id.hiMan);
		TextView watchYou = (TextView)dialogNewFriends.findViewById(R.id.someoneWATCHyou);
		CircularImage friendsAvator = (CircularImage)dialogNewFriends.findViewById(R.id.friendsAvator);
		Button agree = (Button)dialogNewFriends.findViewById(R.id.agree);
		Button uncleBuYue = (Button)dialogNewFriends.findViewById(R.id.uncleBuYue);
		
		hiMan.setText("Hi " + mv.getMyName().trim());
		
		String s_sex = "她";
		if(fSex == 0){
			s_sex = "她";
		}
		else if(fSex == 1){
			s_sex = "他";
		}
		watchYou.setText(fName + "想加你为好友");
		
        //图片缓存
		final String imageUrl = fAvator;
		friendsAvator.setTag(imageUrl);
        friendsAvator.setImageResource(R.drawable.fill_color);
        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
        		friendsAvator); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	friendsAvator.setImageDrawable(drawable);
        
        friendsAvator.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, YourZone.class);
				intent.putExtra(MyStatic.KEYNAME_SingleId, fId);
				startActivity(intent);
			}
		});
		
		agree.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread()
				{
					public void run()
					{
						try
						{
							String getMethod = "Manage/confirm_per_gz";
							NameValuePair param1 = new BasicNameValuePair("loginid", fId);
							NameValuePair param2 = new BasicNameValuePair("b", "1");
							String getResponse = CustomerHttpClient.post(ipName+getMethod, param1 ,param2);
							if(getResponse.equals("true")){
								Looper.prepare();
								dialogNewFriends.dismiss();
								Toast.makeText(Main.this, "添加" + fName + "为好友", Toast.LENGTH_SHORT).show();
								Looper.loop();
							}
							else{
								Toast.makeText(Main.this, "网络异常:(", Toast.LENGTH_SHORT).show();
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		
		//拒绝别人加好友的请求
		uncleBuYue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread()
				{
					public void run()
					{
						try
						{
							String getMethod = "Manage/confirm_per_gz";
							NameValuePair param1 = new BasicNameValuePair("loginid", fId);
							NameValuePair param2 = new BasicNameValuePair("b", "0");
							String getResponse = CustomerHttpClient.post(ipName+getMethod, param1 ,param2);
							if(getResponse.equals("true")){
								Looper.prepare();
								dialogNewFriends.dismiss();
								Toast.makeText(Main.this, "已残忍拒绝:)", Toast.LENGTH_SHORT).show();
								Looper.loop();
							}
							else{
								Toast.makeText(Main.this, "网络异常:(", Toast.LENGTH_SHORT).show();
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		
		dialogNewFriends.show();
	}
	

	//从我们的后台获取版本更新，已改用友盟的更新sdk，暂时留着可以删掉
	private void getUpdate(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("version");
        	
        	JSONObject jsonpet = jsonArray.getJSONObject(0);
        	versionNew = jsonpet.getString("Version");
        	updateAddress = jsonpet.getString("Address");
        	description_text = jsonpet.getString("Description");
        	
        	try {
				versionThis = getVersionName();
			} catch (Exception e) {
				e.printStackTrace();
			}
        	if(!versionThis.equals(versionNew)){
        		ShowDialogUpdate();
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
	}
	private void ShowDialogUpdate(){
		dialogUpdate = new Dialog(Main.this, R.style.dialog_huodong_tixing);
		dialogUpdate.setContentView(R.layout.dialog_update);
		yes_update = (Button)dialogUpdate.findViewById(R.id.yes_update);
		description = (TextView)dialogUpdate.findViewById(R.id.description);
		yes_update.setOnClickListener(new ButtonYesUpdate());
		description.setText(description_text);
		dialogUpdate.show();
	}
	class ButtonYesUpdate implements OnClickListener{
		public void onClick(View arg0) {
			Uri uri = Uri.parse(updateAddress);  
			Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			startActivity(it);
		}
	}
	private String getVersionName() throws Exception{
	    //获取packagemanager的实例    
	    PackageManager packageManager = getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息   
	    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);  
	    return packInfo.versionName;   
	}
	
	
	//startIntentForResult(intent)表示去其他页面要求返回鞋带信息，如：左拉菜单导航进入其他页面，
	//为了避免误操作后退带来的不适感，3秒内从其它页面后退到主页面时左拉菜单仍然打开，保证操作顺畅，超过3秒则说明非误操作，回退时关闭左拉菜单
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==MyStatic.MAIN_GOTOSECOND && resultCode==MyStatic.FINISHTHIS || requestCode ==MyStatic.GOTOCHANGESCHOO && resultCode == MyStatic.FINISHTHIS){
				int timeCount=data.getIntExtra("time",0);
				if(timeCount==1){
				if(menu.isMenuShowing()){
					menu.showContent(false);
				}
			}
		}
		else if(requestCode==MyStatic.MAIN_GOTOSUBSCRIBE && resultCode==MyStatic.FINISHTHIS){
			pop.dismiss();
		}
		else if (requestCode == MyStatic.GOTOCHANGESCHOO &&resultCode == MyStatic.SUCCESSTHIS){
			if(menu.isMenuShowing()){
				menu.showContent(false);
			}
			change_schoolId = data.getStringExtra("schoolId");
			change_schoolName = data.getStringExtra("schoolName");
			change_schoolBranch = data.getStringExtra("schoolBranch");
			
			layoutChanged_all = false;
			timeExhausted_all = false;
			failToConnect_all.setVisibility(4);
			progressBar_all.setVisibility(0);
			
			//使提醒最近没有活动的clue消失
				clue_linear_main.setVisibility(4);
				connectSuccessLayout_all.setVisibility(0);
				hasBuildedCard_main = false;
				buildCard_main = false;
			
			timing(0);
			getAllActivity();
			
			Toast.makeText(Main.this, change_schoolName +" - " + change_schoolBranch,Toast.LENGTH_SHORT).show();
		}
	}

	
	//dialog出现时，点击空白处都可以关闭该dialog
	public boolean onTouchEvent(MotionEvent event) {
		   if (pop != null && pop.isShowing()) {
		   pop.dismiss();
		   }
		   return super.onTouchEvent(event);
		}
	
	
	//同一个线程计算从网络获取数据到加载的时间，超过一定时间则显示“网络连接失败”
	private void timing(final int i){
        Timer timerLoading=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				if(i == 1){
					timeExhausted_mySubscribe = true;
				}
				else if(i == 0){
					timeExhausted_all =true;
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
		if(currentPage == 1){
			if(!layoutChanged_mySubscribe){
				if(timeExhausted_mySubscribe){
					progressBar_mySubscribe.setVisibility(4);
					failToConnect_mySubscribe.setVisibility(0);
				}
				else {
					loading_mySubscribe.setVisibility(4);
					connectSuccessLayout_mySubscribe.setVisibility(0);
				}
				layoutChanged_mySubscribe = true;
			}
		}
		else {
			if(!layoutChanged_all){
				if(timeExhausted_all){
					progressBar_all.setVisibility(4);
					failToConnect_all.setVisibility(0);
				}
				else {
					loading_all.setVisibility(4);
					connectSuccessLayout_all.setVisibility(0);
				}
				layoutChanged_all = true;
			}
		}
	}
	
	
	//ViewPage实现的方法
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
        public void onPageScrollStateChanged(int arg0) {  
        }  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
        public void onPageSelected(int arg0) {  
        	
            currentPage = arg0;  
            
            if(currentPage == 0){
            	text.setText(R.string.main_titleText_all);
            }
            else if (currentPage == 1){
            	text.setText(R.string.main_titleText_mySubscribe);
            }
            
            if(flag_ListView_mySubscribe_isInit == false && currentPage == 1){
            	initListView_mySubscribe_loading();//loading动画
            	initListView_mySubscribe();
            	methodName = "mySubscribe";
            	jGet(methodName);
            }
        }  
    }
    private void getAllActivity(){
    	InitValue();
    	methodName = "getSchoolId";
    	timeBlock = true;
    	jGet(methodName);
    	new Thread(){
    		public void run(){
            	while(timeBlock){};
                methodName = "allActivity";
                jGet(methodName);
    		}
    	}.start();
    }
    
    
    //所有活动、订阅活动两个页面切换的动画
    @SuppressLint("NewApi")
	public class ZoomOutPageTransformer implements PageTransformer {
    	private static final float MIN_SCALE = 0.85f;

    	private static final float MIN_ALPHA = 0.5f;

    	public void transformPage(View view, float position) {
    		int pageWidth = view.getWidth();
    		int pageHeight = view.getHeight();

    		if (position < -1) { // [-Infinity,-1)
    								// This page is way off-screen to the left.
    			view.setAlpha(0);
    		} else if (position <= 1) { // [-1,1]
    									// Modify the default slide transition to
    									// shrink the page as well
    			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
    			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
    			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
    			if (position < 0) {
    				view.setTranslationX(horzMargin - vertMargin / 2);
    			} else {
    				view.setTranslationX(-horzMargin + vertMargin / 2);
    			}
    			// Scale the page down (between MIN_SCALE and 1)
    			view.setScaleX(scaleFactor);
    			view.setScaleY(scaleFactor);
    			// Fade the page relative to its size.
    			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
    					/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    		} else { // (1,+Infinity]
    					// This page is way off-screen to the right.
    			view.setAlpha(0);
    		}
    	}
    }
    
    
    public class DepthPageTransformer implements PageTransformer {
    	private static final float MIN_SCALE = 0.75f;

    	@SuppressLint("NewApi")
    	public void transformPage(View view, float position) {
    		int pageWidth = view.getWidth();
    		if (position < -1) { // [-Infinity,-1)
    								// This page is way off-screen to the left.
    			view.setAlpha(0);
    		} else if (position <= 0) { // [-1,0]
    									// Use the default slide transition when
    									// moving to the left page
    			view.setAlpha(1);
    			view.setTranslationX(0);
    			view.setScaleX(1);
    			view.setScaleY(1);
    		} else if (position <= 1) { // (0,1]
    									// Fade the page out.
    			view.setVisibility(View.VISIBLE);
    			view.setAlpha(1 - position);
    			// Counteract the default slide transition
    			view.setTranslationX(pageWidth * -position);
    			// Scale the page down (between MIN_SCALE and 1)
    			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
    					* (1 - Math.abs(position));
    			view.setScaleX(scaleFactor);
    			view.setScaleY(scaleFactor);
    		} else { // (1,+Infinity]
    					// This page is way off-screen to the right.
    			view.setAlpha(0);

    		}
    	}
    }
    
    
    public void slideLogoMenu(final float p1, final float p2) {
    	   TranslateAnimation animation = new TranslateAnimation(p1, p2, 0, 0);
    	   animation.setInterpolator(new OvershootInterpolator());
    	   animation.setDuration(animationDuration);
    	   animation.setStartOffset(animationStartOffSet);
    	   animation.setAnimationListener(new Animation.AnimationListener() {
    		   public void onAnimationStart(Animation animation) {}
    		   public void onAnimationRepeat(Animation animation) {}
    		   public void onAnimationEnd(Animation animation) {
    		        int left = logoMenu.getLeft()+(int)(p2-p1);
    		        int top = logoMenu.getTop();
    		        int width = logoMenu.getWidth();
    		        int height = logoMenu.getHeight();
    		        logoMenu.clearAnimation();
    		        logoMenu.layout(left, top, left+width, top+height);
    		       }
    		   });
    	   logoMenu.startAnimation(animation);
    }
    
    
    public void onDismiss(final ViewGroup listView, final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
        }
    }
    
    
    public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);//友盟页面跳转统计需要，每个页面的onResume()和onPaust()都要加这两个方法
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);//友盟页面跳转统计需要，每个页面的onResume()和onPaust()都要加这两个方法
		}
}  