package com.shoulder.xiaonei.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_myOrg;
import com.shoulder.xiaonei.card.Card_school;
import com.shoulder.xiaonei.card.Card_school_adapter;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class School_List extends Activity implements IXListViewListener{
	
	private String methodName;
	private String jResult ="";
	
    private RelativeLayout right_rel;	
	private Button left_btn;
    private TextView text;
    
    private MyVal mv;
    
    private SharedPreferences preferences_schoolId;
	private SharedPreferences.Editor editor;
    
    private Timer timer;
    private int timeCount = 0;
    
	private LinearLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
    
    private XListView mListView;
	private List<Card_school> mCards;
	private Card_school_adapter mAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_school_listview); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        mv = (MyVal)getApplication();
        
        initCards();
        initLoading();
        initTitle();
        
        timer=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				timeCount=1;
			}
		}; 
		timer.schedule(timertask, MyStatic.CloseTime);
		
		methodName = "Manage/get_schoollist";
    	jGet(methodName);	
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
			case 0x112:
				jResult = msg.obj.toString();
				refresh();
				changeLayout();
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
				String getName=mtName;
				String getParams="";
				try
				{
					String result = CustomerHttpClient.get(mv.getIpName()+ getName + "?" +getParams);
					Message msg = new Message();
					if(mtName == "Manage/get_schoollist")
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
	
	
	private void initCards(){
    	mListView=(XListView)findViewById(R.id.ListView);  
    	mAdapter=new Card_school_adapter(this, getItems());  
        mListView.setPullRefreshEnable(false);//设置下拉刷新
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new ItemOnClickListener());
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
        left_btn.setOnClickListener(new ButtonOnClickListener());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText("选择学校");
	}
	
	private List<Card_school> getItems(){
		mCards = new ArrayList<Card_school>();
		buildCards();
        return mCards;
	}
	
    private void buildCards(){
    	try {
          	JSONObject jsonObject;
          	jsonObject = new JSONObject(jResult);
          	JSONArray jsonArray=jsonObject.getJSONArray("schoollist");
          	int temp_arrayLength = jsonArray.length();
          	for(int i=0;i<temp_arrayLength;i++)  
          	{
          		JSONObject jsonpet = jsonArray.getJSONObject(i);
          		Card_school mCard=new Card_school(jsonpet.getString("Schoolid") ,jsonpet.getString("SchoolName") , 
          															jsonpet.getString("BranchSchool")); 
          		mCards.add(mCard);
          	}
        } catch (JSONException e) {
          	e.printStackTrace();
        }
    }
    
	private void refresh(){
		buildCards();
		mAdapter.notifyDataSetChanged();
		mListView.setAdapter(mAdapter);
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
	
	
	class TextFailToConnect implements OnClickListener{
		public void onClick(View arg0) {
			layoutChanged = false;
			timeExhausted = false;
			failToConnect.setVisibility(4);
			progressBar.setVisibility(0);
			timing();
			methodName = "Manage/get_schoollist";
        	jGet(methodName);	
		}
	}
	
	
	class ItemOnClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			int position = index - 1;
			String schoolId = mCards.get(position).getSchoolId();
			String schoolName = mCards.get(position).getSchoolName();
			String schoolBranch = mCards.get(position).getSchoolBranch();
			
			//缓存schoolId
			preferences_schoolId = getSharedPreferences("schoolId", MODE_PRIVATE);
			editor = preferences_schoolId.edit();
			editor.putString("school_id", schoolId);
			editor.commit();
			mv.setSchoolId(schoolId);
			
			Intent intent=getIntent();
			intent.putExtra("schoolId", schoolId);
			intent.putExtra("schoolName", schoolName);
			intent.putExtra("schoolBranch", schoolBranch);
			setResult(MyStatic.SUCCESSTHIS,intent);
			finish();
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


	public void onRefresh() {
	}
	public void onLoadMore() {
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
