package com.shoulder.xiaonei.tucao;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_huodong_pingjia;
import com.shoulder.xiaonei.card.Card_main;
import com.shoulder.xiaonei.card.Card_tucaoqiang_comment;
import com.shoulder.xiaonei.card.Card_tucaoqiang_comment_adapter;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.HuoDong_PingJia;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.SetListView;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.others.Set_Up_GiveMeFive;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TuCaoQiang_Comment extends Activity implements IXListViewListener{

	private Button left_btn;
	private RelativeLayout right_rel;
	private TextView text;
	
	private MyVal mv;
	public String ipName="";
	
	private RelativeLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private String methodName;
	protected SharedPreferences preferences;
	protected SharedPreferences.Editor editor;
	private String jResult ="";
    private Handler mHandler;

	private XListView mListView;
	private Card_tucaoqiang_comment_adapter mAdapter;
	private List<Card_tucaoqiang_comment> mCards;
	
	private TextView content_main;
	private View v;
	
	private Dialog myDialog_login_and_register;
	
	private String get_text="";
    private int get_id;
    private int get_color;
    
	private Button evaluate;
	private Button comment;
	private EditText edit_bottom;
	
	private InputMethodManager inputmanger;
	
	private int TuCaoComment_InforCount = 0;//如果评论数为0，显示暂无
	
	private boolean waitDouble = true; 
	
    
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_tucaoqiang_comment_listview);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        Bundle extras=getIntent().getExtras();
        get_text=extras.getString("content");
        get_color=extras.getInt("color");
        get_id=extras.getInt("mId");
        
        mv=((MyVal)getApplicationContext());
        ipName=mv.getIpName();
        
        Dialog_Register_Login.Set_Dialog_Register_Login(TuCaoQiang_Comment.this,"tucaoqiang_comment");
        
        inputmanger = (InputMethodManager)getSystemService(TuCaoQiang_Comment.INPUT_METHOD_SERVICE);
        //管理虚拟键盘
        
        initLoading();
        initTitle();
        initWidgets();
        initBottom();//下方评论、点赞等
        
    	methodName = "a12";
    	jGet(methodName);
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
				jResult = msg.obj.toString();
				changeLayout();
				InitListView();
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
				if(mtName=="a12"){
					getName="Tucao/get_tucao_all_cnmment";
					getParams="tucaoid="+get_id;
				}
				try
				{
					String result = CustomerHttpClient.get(ipName+getName+"?"+getParams);
					Message msg = new Message();
					if(mtName == "a12")
					{
						msg.what = 0x112;
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
        connectSuccessLayout=(RelativeLayout)findViewById(R.id.connectSuccessLayout);
        loading=(RelativeLayout)findViewById(R.id.loading);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        failToConnect=(TextView)findViewById(R.id.failToConnect);
        failToConnect.setOnClickListener(new TextFailToConnect());
        loading.setBackgroundColor(0x00ffffff);
        timing();
	}
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText("吐槽评论");
	}
	
	private void initWidgets(){
    	LayoutInflater mLayoutInflater = LayoutInflater.from(this);
    	v = mLayoutInflater.inflate(R.layout.tucaoqiang_comment_top, null);
    	v.setOnClickListener(new ButtonDoubleTucao());
        content_main=(TextView)v.findViewById(R.id.content);
        content_main.setBackgroundResource(get_color);
        content_main.setText(get_text);
	}
	
	private void initBottom(){
    	evaluate=(Button)findViewById(R.id.evaluate);
    	comment=(Button)findViewById(R.id.comment);
    	edit_bottom=(EditText)findViewById(R.id.edit_bottom);
    	comment.setOnClickListener(new ButtonComment());
    	evaluate.setText("赞");
    	evaluate.setOnClickListener(new ButtonZan());
	}
	
    private void InitListView(){
    	mListView =(XListView)findViewById(R.id.ListView);
    	mListView.addHeaderView(v, null, false);
    	mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setPullRefreshEnable(false);//设置下拉刷新
    	mAdapter=new Card_tucaoqiang_comment_adapter(this, getItems());
    	
    	//当滑动评论时，自动关闭软键盘
    	mListView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					inputmanger.hideSoftInputFromWindow(edit_bottom.getWindowToken(), 0);
					return false;
				}
				return false;
			}
		});
    	
//        if (TuCaoComment_InforCount == 0){
//        	LayoutInflater mLayoutInflater = LayoutInflater.from(this);
//        	View vBottom = mLayoutInflater.inflate(R.layout.include_notyet, null);
//        	TextView textNotYet = (TextView)vBottom.findViewById(R.id.notYet_text);
//        	textNotYet.setText(getResources().getString(R.string.noYet_tucaoPingJia));
//        	mListView.addFooterView(vBottom, null, false);
//        }
    	
    	mListView.setAdapter(mAdapter);
    }
    
    private List<Card_tucaoqiang_comment> getItems(){
    	mCards=new ArrayList<Card_tucaoqiang_comment>();
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("comment_info");
        	int temp_arrayLenght = jsonArray.length();
        	TuCaoComment_InforCount = jsonArray.length();
        	for(int i=0;i<temp_arrayLenght;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_tucaoqiang_comment mCard=new Card_tucaoqiang_comment(jsonpet.getString("nickname"),jsonpet.getString("comment_time"),jsonpet.getString("content"));  
        		mCards.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    	return mCards;
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
	
    private void GoBack(){
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
	 
    class ButtonZan implements OnClickListener{
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
							String getName = "Tucao/tucao_add_zan";
							NameValuePair param1 = new BasicNameValuePair("tucaoid",get_id+"");
							String getResponse = CustomerHttpClient.post(ipName+getName, param1);
								if(getResponse.equals("true")){
									Looper.prepare();
									Toast toast=Toast.makeText(TuCaoQiang_Comment.this, "赞", Toast.LENGTH_SHORT);
									toast.show();
									Looper.loop();
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
    }
    
    
	class ButtonComment implements OnClickListener{
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
				final String editBottomText = edit_bottom.getText().toString().trim();
				if(editBottomText.equals("")){
					edit_bottom.clearFocus();
				}
				else {
					
				new Thread()
				{
					public void run()
					{
						try
						{
							String getName="Tucao/insert_comment";
							NameValuePair param1 = new BasicNameValuePair("tucao_id", get_id+"");
							NameValuePair param2 = new BasicNameValuePair("comment", editBottomText);
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1,param2);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
				Card_tucaoqiang_comment mCard=new Card_tucaoqiang_comment("我",MyStatic.MAXDATE_STANDARD,editBottomText); 
				mCards.add(mCard);
				edit_bottom.setText("");
				mAdapter.notifyDataSetChanged();
				mListView.smoothScrollToPosition(1000);
				
				}
			}
		}
	}
	
	class ButtonDoubleTucao implements OnClickListener{
		public void onClick(View arg0) {
			if(waitDouble == true){
				waitDouble =false;
				new Thread(){
					public void run(){
		                   try {
			                      sleep(MyStatic.DOUBLE_CLICK_TIME);
			                      if(waitDouble == false){
			                         waitDouble = true;
			                      }
			                   } catch (InterruptedException e) {
			                      e.printStackTrace();
			                   }
					}
				}.start();
			}
			else{
	             waitDouble = true;
	             Intent intent=new Intent(TuCaoQiang_Comment.this,Text_Full.class);
	             intent.putExtra("content",get_text);
	             startActivity(intent);
	          }
		}
	}
	
	
    //评论后回到该界面，刷新该界面
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == MyStatic.TUCAOQIANGCOMMENTFABU && resultCode == MyStatic.SUCCESSTHIS){
	    	methodName = "a12";
	    	jGet(methodName);
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
