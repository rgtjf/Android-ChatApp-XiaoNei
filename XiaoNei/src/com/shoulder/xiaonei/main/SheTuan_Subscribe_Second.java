package com.shoulder.xiaonei.main;

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
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.card.Card_main_adapter;
import com.shoulder.xiaonei.card.Card_shetuan_subscribe;
import com.shoulder.xiaonei.card.Card_shetuan_subscribe_adapter;
import com.shoulder.xiaonei.card.Card_tucaoqiang;
import com.shoulder.xiaonei.myClass.ConnectionDetector;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SheTuan_Subscribe_Second extends Activity implements IXListViewListener{

	private XListView mListView;
	private Card_shetuan_subscribe_adapter mAdapter;
	private List<Card_shetuan_subscribe> mCards;
	private Handler mHandler;
	
	private Button left_btn;
	private RelativeLayout right_rel;
	private TextView text;
	
	public String ipName="";
	private String myDir="";
	private String schoolId = "1";
	
	private String methodName;
	private String jResult ="";
	
	private LinearLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private String get_className;
	private String get_classx;
	
//	private TextView top_name;
//	private View v;//listview的headview
	
//	private int countLoop = 10 ;
//	private double flag_rate = 6.0;
//	private int flag_id = 0;
	
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_shetuan_subscribe_listview);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        Bundle extras=getIntent().getExtras();
        get_className=extras.getString("selectClass");
        get_classx=extras.getString("classx");
        text=(TextView)findViewById(R.id.text);
        text.setText(get_className);
        
        MyVal mv=((MyVal)getApplicationContext());
        ipName=mv.getIpName();
        myDir=mv.getMyDir();
        schoolId = mv.getSchoolId();
        
        initLoading();
        initTitle();
        
//        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
//    	v = mLayoutInflater.inflate(R.layout.include_shetuandingyue_second_top, null);
//    	initTopName();
        
        methodName = get_classx;
        jGet(methodName);
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
			case 0x112:
				changeLayout();
				InitListView();
				break;
			case 0x113:
				loadMore();
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
//				if(mtName.equals("1")||mtName.equals("2")||mtName.equals("3")||mtName.equals("4")||mtName.equals("5")||mtName.equals("6")||mtName.equals("7")||mtName.equals("a13")){
				getName="Organization/get_org_byclass";
				getParams= "classid=" + mtName + "&" +
							"schoolid=" + schoolId;
//				}
				try
				{
					String result = CustomerHttpClient.get(ipName + getName + "?" + getParams);
					Message msg = new Message();
					if(mtName.equals("1")||mtName.equals("2")||mtName.equals("3")||mtName.equals("4")||mtName.equals("5")||mtName.equals("6")||mtName.equals("7"))
					{
						msg.what = 0x112;
					}else if (mtName.equals("a13")){
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
        left_btn.setOnClickListener(new ButtonBack());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
	}
	
	
//	@SuppressLint("NewApi") private void initTopName(){
//		top_name=(TextView)v.findViewById(R.id.top_name);
//        top_name.setText(get_className);
//        if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_art))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_1);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_sport))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_2);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_outdoor))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_3);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_science))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_4);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_culture))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_5);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_mix))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_6);
//        }
//        else if(get_className.equals(getResources().getString(R.string.shetuandingyue_main_others))){
//        	top_name.setBackgroundResource(R.color.shetuandingyue_main_7);
//        }
//	}
	
	
    private void InitListView() {  
    	mListView=(XListView)findViewById(R.id.ListView);  
//    	mListView.addHeaderView(v, null, false);
    	mAdapter=new Card_shetuan_subscribe_adapter(this, getItems());  
        mHandler = new Handler();
        mListView.setPullRefreshEnable(false);//设置下拉刷新
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);  
    }  
    
    private List<Card_shetuan_subscribe> getItems()   
    {  
    	mCards=new ArrayList<Card_shetuan_subscribe>();
    	buildCard();
        return mCards;  
    }  
    
    private void buildCard(){
  	  try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("org_byclass");
        	int temp_arrayLength = jsonArray.length();
        	for(int i=0;i<temp_arrayLength;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_shetuan_subscribe mCard=new Card_shetuan_subscribe(jsonpet.getString("OrganizationName") ,
        																jsonpet.getString("Logo") , jsonpet.getString("Rate") ,
        																jsonpet.getInt("OrganizationID") , get_className,
        																jsonpet.getInt("gzstate")); 
//      		if (i == temp_arrayLength-1){
//      			flag_rate = jsonpet.getDouble("Rate");
//      			flag_id = jsonpet.getInt("OrganizationID");
//      		}
        		mCards.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    }
    
    
    private void GoBack(){
    	finish();
    }
    class ButtonBack implements OnClickListener{
		public void onClick(View arg0) {
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
			methodName = get_classx;
        	jGet(methodName);		
		}
	}
	
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("算不清了..");
	}
	
	public void onRefresh() {
	}
	
	public void onLoadMore() {
    	methodName = "a13";
    	jGet(methodName);
	}
    public void loadMore()
    {
    	mHandler.post(new Runnable()
    	{
    		public void run()
    		{
    			buildCard();
    			mAdapter.notifyDataSetChanged();
    			onLoad();
    		}
    	});
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
