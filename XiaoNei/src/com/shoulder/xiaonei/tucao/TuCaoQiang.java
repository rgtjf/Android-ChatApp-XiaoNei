package com.shoulder.xiaonei.tucao;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
  


import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.dimen;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.R.menu;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.card.Card_tucaoqiang;
import com.shoulder.xiaonei.card.Card_tucaoqiang_adapter;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.ConnectionDetector;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.app.Activity;  
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;  
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;  
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;  
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
  
public class TuCaoQiang extends Activity implements IXListViewListener{  
  
	public String ipName="";
	private String myDir="";
	private String methodName;
	protected SharedPreferences preferences;
	protected SharedPreferences.Editor editor;
	private String jResult ="";
    private Handler mHandler;
    
    private MyVal mv;
	
	private LinearLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private Dialog mDialog;//当未登陆时，点击发布吐槽会弹出 登陆/注册 dialog
	
    private Button right_btn;
    private Button left_btn;
    private TextView text;
    private ImageView function_image;
    
    private List<Card_tucaoqiang> mCards;
    private Card_tucaoqiang_adapter mAdapter;
    private XListView mListView;  
    
    private Timer timer;
    private int timeCount = 0;
    
    private int flag = MyStatic.MAXINT;//标记id
    private int count_loop = 10;//一次读取几条内容
    
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_tucaoqiang_listview);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        mv = (MyVal)getApplication();
        ipName=mv.getIpName();
        myDir=mv.getMyDir();
        
        Dialog_Register_Login.Set_Dialog_Register_Login(TuCaoQiang.this,"TuCaoQiang");
        
        initLoading();
        initTitle();
        
        //缓存，只保留一开始可刷新页面
    	preferences = getSharedPreferences("jResult", MODE_PRIVATE);
    	editor = preferences.edit();
        //确定手机是否连接网络，若否，则调用缓存内容
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if(cd.isConnectingToInternet()==true)
        {
        	methodName = "a12";
        	jGet(methodName);
        }
        else
        {
        	jResult = preferences.getString("jRef", null);
        	if(jResult==null)
        	{
        		jResult = preferences.getString("jInit", null);
        	}
        	initListView();
        }
        
        //进入计时，3秒内后退不关闭首页的menu
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				// TODO Auto-generated method stub
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, MyStatic.CloseTime);
    }  
    
    
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0x000:
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
				initListView();
				changeLayout();
				editor.putString("jInit", jResult);
				editor.commit();
				break;
			case 0x113:
				jResult = msg.obj.toString();
				loadMore();
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
				if(mtName.equals("a11")||mtName.equals("a12")){
					getName="Tucao/get_all_info";
					flag = MyStatic.MAXINT;
					getParams="ID="+flag;
				}
				else if(mtName.equals("a13")){
					getName="Tucao/get_all_info";
					getParams="ID="+flag;
				}
				try
				{
					String result = CustomerHttpClient.get(ipName+ getName + "?" +getParams);
					Message msg = new Message();
					if(mtName == "a11")
					{
						msg.what = 0x111;
					}
					else if(mtName == "a12")
					{
						msg.what = 0x112;
					}
					else if(mtName == "a13")
					{
						msg.what = 0x113;
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
    
    
    private void initLoading(){
        connectSuccessLayout=(LinearLayout)findViewById(R.id.connectSuccessLayout);
        loading=(RelativeLayout)findViewById(R.id.loading);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        failToConnect=(TextView)findViewById(R.id.failToConnect);
        failToConnect.setOnClickListener(new TextFailToConnect());
        timing();
    }
    
    private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonOnClickListenerL());
        function_image=(ImageView)findViewById(R.id.function_image);
        function_image.setImageResource(R.drawable.fabu);
        right_btn=(Button)findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new ButtonRightListener());
        text=(TextView)findViewById(R.id.text);
        text.setText("吐槽墙");
    }

    
    private void GoBack(){
		Intent intent=getIntent();
		intent.putExtra("time", timeCount);
		setResult(MyStatic.FINISHTHIS,intent);
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
    
    class ButtonRightListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(mv.getLoginState() == 1){
				Intent intent=new Intent(TuCaoQiang.this,TuCaoQiang_Comment_Fabu.class);
				intent.putExtra("where_come_from", "tucaoqiang");
				startActivityForResult(intent, MyStatic.TUCAOQIANGFABU);
			}
			else{
          		 if (mDialog == null){
          			mDialog = Dialog_Register_Login.getMyDialog();
          			mDialog.show();
          		}
          		else {
          			mDialog.show();
          		}
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

	
	//发表完吐槽返回该页面时刷新该页面
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub\
		if (requestCode == MyStatic.TUCAOQIANGFABU){
			if(resultCode == MyStatic.SUCCESSTHIS){
				onRefresh();
			}
		}
	}
    
    
    //吐槽墙listview实现
    public void initListView(){
        mListView=(XListView)findViewById(R.id.ListView);  
        mAdapter=new Card_tucaoqiang_adapter(this, getItems()); 
        mHandler=new Handler();
        mListView.setPullLoadEnable(true);//设置上拉刷新
        mListView.setPullRefreshEnable(true);//设置下拉刷新
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter); 
        mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1=new Intent();
				intent1.setClass(TuCaoQiang.this,TuCaoQiang_Comment.class);
				startActivity(intent1);
			}
        	
		});
    }
  
    private List<Card_tucaoqiang> getItems()   
    {  
        mCards=new ArrayList<Card_tucaoqiang>();  
        buildCards();
        return mCards;  
    }  
    
    public void buildCards(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("tucao_info");
        	int temp_arrayLength = jsonArray.length();
        	for(int i=0;i<count_loop;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		String temp_comment_cnt="查看评论("+jsonpet.getString("Tucaocnt")+"条评论）";
        		Card_tucaoqiang mCard=new Card_tucaoqiang(jsonpet.getString("content"), jsonpet.getString("nickname"),
        												  jsonpet.getInt("zan_cnt"),  temp_comment_cnt,  getRandomColor(),
        												  jsonpet.getInt("tucao_id"),   jsonpet.getString("tucao_time"),
        												  jsonpet.getInt("zanstate"));  
        		if (i == temp_arrayLength-1){
        			flag = jsonpet.getInt("tucao_id");
        		}
        		mCards.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    }
    
    //获取吐槽背景的随机颜色
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
    
    
    
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("算不清了..");
	}
    
    public void onRefresh()
    {
    	flag = MyStatic.MAXINT;
    	methodName = "a11";
    	jGet(methodName);
    }
    public void refresh()
    {
    	mHandler.post(new Runnable()
    	{
    		public void run()
    		{
    			if(mCards!=null)
    				mCards.clear();
    			buildCards();
    			mAdapter.notifyDataSetChanged();
    			mListView.setAdapter(mAdapter);
    			onLoad();
    		}
    	});
    }
    
    public void onLoadMore()
    {
    	methodName = "a13";
    	jGet(methodName);
    }   
    public void loadMore()
    {
    	mHandler.post(new Runnable()
    	{
    		public void run()
    		{
    			buildCards();
    			mAdapter.notifyDataSetChanged();
    			onLoad();
    		}
    	});
    }
  
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
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