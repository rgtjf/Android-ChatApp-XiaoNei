package com.shoulder.xiaonei.main;

import java.io.BufferedReader;
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
import com.shoulder.xiaonei.card.Card_search_shetuan;
import com.shoulder.xiaonei.card.Card_search_shetuan_adapter;
import com.shoulder.xiaonei.card.Card_shetuan_subscribe;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Shetuan_Search extends Activity implements IXListViewListener{

	private Button right_btn;
	private Button left_btn;
	private EditText search;
	
	private LinearLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private XListView mListView;
	private List<Card_search_shetuan> mCards;
	private Card_search_shetuan_adapter mAdapter;
	
	private MyVal mv;
	
	private String jResult="";
	
	private int temp_arrayCount;//当json长度为0时，说明没有搜索结果
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_search_listview);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_search);
        
        mv=((MyVal)getApplicationContext());
        
        initCards();
        initLoading();
        initTitle();
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage (Message msg){
			jResult = msg.obj.toString();
			switch (msg.what) {
			case 0x000:
				changeLayout();
				break;
			case 0x112:
				changeLayout();
				refresh();
				break;
			}
		}
	};
	
	
	private void initLoading(){
        connectSuccessLayout=(LinearLayout)findViewById(R.id.connectSuccessLayout);
        loading=(RelativeLayout)findViewById(R.id.loading);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        failToConnect=(TextView)findViewById(R.id.failToConnect);
        failToConnect.setOnClickListener(new TextFailToConnect());
	}
	
	private void initTitle(){
        right_btn=(Button)findViewById(R.id.right_btn);
        left_btn=(Button)findViewById(R.id.left_btn);
        search=(EditText)findViewById(R.id.search);
        left_btn.setOnClickListener(new ButtonBack());
        right_btn.setOnClickListener(new ButtonSearch());
	}
	
	
	class TextFailToConnect implements OnClickListener{
		public void onClick(View arg0) {
			layoutChanged = false;
			timeExhausted = false;
			failToConnect.setVisibility(4);
			progressBar.setVisibility(0);
			timing();
		}
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
	
	class ButtonSearch implements android.view.View.OnClickListener{
		public void onClick(View arg0) {
			if(mCards!= null){
				mCards.clear();
			}
			if(!search.getText().toString().trim().equals("")) 
			{
				clearValue();
				timing();
		    	new Thread()
				{
					public void run()
					{
						String getMethod="";
						String getParams="";
						getMethod="Organization/get_org_byname";
						getParams="name=" + search.getText().toString();
						try
						{
							String result = CustomerHttpClient.get(mv.getIpName()+getMethod+"?"+getParams);
							Message msg = new Message();
							msg.obj = result;
							msg.what = 0x112;
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
	}

	
	public void onRefresh() {
	}
	public void onLoadMore() {
	}
	
	
	private void initCards(){
    	mListView=(XListView)findViewById(R.id.ListView);  
    	mAdapter=new Card_search_shetuan_adapter(this, getItems());  
        mListView.setPullRefreshEnable(false);//设置下拉刷新
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);  
	}
	
	private List<Card_search_shetuan> getItems(){
		mCards = new ArrayList<Card_search_shetuan>();
		buildCards();
        return mCards;
	}
	
	private void refresh(){
		buildCards();
		if(temp_arrayCount == 0){
			Toast.makeText(Shetuan_Search.this,"没有找到合适的结果", Toast.LENGTH_SHORT).show();
		}else{
			mAdapter.notifyDataSetChanged();
			mListView.setAdapter(mAdapter);
		}
	}
	
	private void buildCards(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("org_byclass");
        	temp_arrayCount =jsonArray.length();
        	for(int i=0;i<temp_arrayCount;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
          		Card_search_shetuan mCard=new Card_search_shetuan(jsonpet.getString("OrganizationName") , 
          														  jsonpet.getString("Logo") , jsonpet.getString("Rate") ,
          														  jsonpet.getInt("OrganizationID"));  
        		mCards.add(mCard);
        	}
        }
        catch (JSONException e) {
        	e.printStackTrace();
        }
	}
	
	
	//重新搜索时将所有值初始化
	private void clearValue(){
		layoutChanged = false;
		timeExhausted = false;
		connectSuccessLayout.setVisibility(4);
		progressBar.setVisibility(0);
		failToConnect.setVisibility(4);
		loading.setVisibility(0);
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
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}

}
