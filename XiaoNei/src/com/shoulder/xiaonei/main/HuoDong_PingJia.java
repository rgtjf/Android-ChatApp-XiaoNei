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

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_huodong_pingjia;
import com.shoulder.xiaonei.card.Card_huodong_pingjia_adapter;
import com.shoulder.xiaonei.main.HuoDong_Detail.ImageOnClickListener;
import com.shoulder.xiaonei.main.SheTuan_Detail.ButtonOnClickListenerL;
import com.shoulder.xiaonei.main.SheTuan_Subscribe_Second.TextFailToConnect;
import com.shoulder.xiaonei.main.Shetuan_Subscribe_Main.ButtonSearch;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.SetListView;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.tucao.TuCaoQiang;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony.Mms.Rate;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class HuoDong_PingJia extends Activity implements IXListViewListener{
	
	private String methodName;
	protected SharedPreferences preferences;
	protected SharedPreferences.Editor editor;
	private String jResult ="";
	
	private Button leftButton;
	private RelativeLayout right_rel;
	private TextView text;
	
	private Dialog mDialog;//当未登陆时，点击发布吐槽会弹出 登陆/注册 dialog
	
	private RelativeLayout connectSuccessLayout;
	private RelativeLayout loading;
	private ProgressBar progressBar;
	private TextView failToConnect;
	private Boolean layoutChanged = false;
	private Boolean timeExhausted = false;
	
	private String get_name;
	private String get_uri;
	private String get_rate;
	private String get_mId;
	
	private TextView huodong_name;
	private ImageView huodong_image;
	private TextView huodong_rate;
	private View v;
	
	private Button evaluate;
	private Button comment;
	private EditText edit_bottom;
	
	private PopupWindow pop;
	private LinearLayout layout;
	private Button top_1;
	private Button top_2;
	private Button top_3;
	private Button top_4;
	private Button top_5;
	
	private XListView mListView;
	private Card_huodong_pingjia_adapter mAdapter;
	private List<Card_huodong_pingjia> mCards;
	
	private MyVal mv;
	
	private int temp_arrayLenght = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_huodong_pingjia_listview);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        Bundle extras = getIntent().getExtras();
        get_name = extras.getString("name");
        get_mId = extras.getString("mId");
        get_rate = extras.getString("rate");
        get_uri = extras.getString("uri");
        
        mv = (MyVal)getApplicationContext();
        
        Dialog_Register_Login.Set_Dialog_Register_Login(HuoDong_PingJia.this,"TuCaoQiang");
        
        initLoading();
        initTitle();
        initHuoDong();
        initBottom();
        
    	methodName = "a12";
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
 				Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, "0x000");
				break;
			case 0x112:
				jResult = msg.obj.toString();
				changeLayout();
				InitListView();
				Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, "0x112");
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
					getName="Activity/get_actx_comment";
					getParams="actid="+get_mId;
				}
				try
				{
					String result = CustomerHttpClient.get(mv.getIpName()+getName+"?"+getParams);
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
        leftButton=(Button)findViewById(R.id.left_btn);
        leftButton.setOnClickListener(new ButtonOnClickListenerL());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText("活动评价");
	}
	
	private void initHuoDong(){
    	LayoutInflater mLayoutInflater = LayoutInflater.from(this);
    	v = mLayoutInflater.inflate(R.layout.huodong_pingjia_top, null);
        huodong_name=(TextView)v.findViewById(R.id.huodong_name);
        huodong_image=(ImageView)v.findViewById(R.id.huodong_image);
        huodong_rate=(TextView)v.findViewById(R.id.huodong_rate);
        
        huodong_name.setText(get_name);
        huodong_image.setOnClickListener(new ImageOnClickListener());
        //图片缓存
        final String imageUrl = get_uri;
        final ImageView cp = huodong_image;
        File file = new File(mv.getMyDir()+mv.getFileName(), imageUrl);
        if (file.exists()) {
        	Uri uri = Uri.fromFile(file);
	        huodong_image.setImageURI(uri);
        }
        else
        {
        	new Thread()
        	{
    			public void run()
    			{
    				try
    				{
    					String u = mv.getImageIpName()+mv.getImageAddress()+imageUrl;
    					URL url = new URL(u);
    					InputStream is = url.openStream();
    					File file=new File(mv.getMyDir()+mv.getFileName(),imageUrl);
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
        huodong_rate.setText(get_rate+"分");
	}
	
	private void initBottom(){
    	evaluate=(Button)findViewById(R.id.evaluate);
    	comment=(Button)findViewById(R.id.comment);
    	edit_bottom=(EditText)findViewById(R.id.edit_bottom);
    	comment.setOnClickListener(new ButtonComment());
    	evaluate.setOnClickListener(new ButtonEvaluate());
	}
	
    private void InitListView(){
    	mListView =(XListView)findViewById(R.id.ListView);
    	mListView.addHeaderView(v, null, false);
    	mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setPullRefreshEnable(false);//设置下拉刷新
    	mAdapter=new Card_huodong_pingjia_adapter(this, getItems());
    	mListView.setAdapter(mAdapter);
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
	
	//点击海报可以查看大图
    class ImageOnClickListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent=new Intent();
			intent.setClass(HuoDong_PingJia.this, Image_HuoDong.class);
			intent.putExtra("title_text", get_name);
			intent.putExtra("uri", get_uri);
			startActivity(intent);
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
	
	class ButtonComment implements OnClickListener{
		public void onClick(View arg0) {
			if(mv.getLoginState() == 1){
				if(edit_bottom.getText().toString().trim().equals("")){
					edit_bottom.clearFocus();
					return;
				}
				else {
				final String edit_text = edit_bottom.getText().toString().trim();
				new Thread()
				{
					public void run()
					{
						try
						{
							String getName="Activity/add_comment";
							NameValuePair param1 = new BasicNameValuePair("activity_id", get_mId);
							NameValuePair param2 = new BasicNameValuePair("content", edit_text);
							String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1,param2);
								if(getResponse.equals("true")){
									Looper.prepare();
									Toast.makeText(HuoDong_PingJia.this,"评论成功", Toast.LENGTH_SHORT).show();
									Looper.loop();
								}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}.start();
				Card_huodong_pingjia mCard=new Card_huodong_pingjia("我",edit_text,MyStatic.MAXDATE); 
				mCards.add(mCard);
				edit_bottom.clearFocus();
				edit_bottom.setText("");
				mAdapter.notifyDataSetChanged();
				mListView.smoothScrollToPosition(1000);
				
				}
			}
			else{
         		if(mDialog == null){
         			mDialog = Dialog_Register_Login.getMyDialog();
         			mDialog.show();
         		}
         		else {
         			mDialog.show();
         		}
			}
		}
	}
	
	class ButtonEvaluate implements OnClickListener{
		public void onClick(View v) {
			if(mv.getLoginState() == 1){
				if(pop!=null && pop.isShowing()){
					pop.dismiss();
				}
				else{
		             layout = (LinearLayout) getLayoutInflater().inflate(R.layout.popmenu_huodong_evaluate, null);
		             DisplayMetrics dm = new DisplayMetrics();  
		             getWindowManager().getDefaultDisplay().getMetrics(dm);  
		             double screenW = dm.widthPixels /2.2;
		             double screenH = dm.heightPixels /2.2;
		             Double popWidth = new Double(screenW);
		             Double popHeight = new Double(screenH);
		             pop = new PopupWindow(layout,popWidth.intValue(),popHeight.intValue());
		             pop.setFocusable(true);
		             pop.setOutsideTouchable(true);
		             pop.update();
		             pop.setBackgroundDrawable(new BitmapDrawable());
		             pop.setAnimationStyle(R.style.AnimationFade_TopButton);
		             int[] location = new int[2];  
		             v.getLocationOnScreen(location);  
		             pop.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-pop.getHeight());  
		             
		             top_1=(Button)layout.findViewById(R.id.top_1);
		             top_2=(Button)layout.findViewById(R.id.top_2);
		             top_3=(Button)layout.findViewById(R.id.top_3);
		             top_4=(Button)layout.findViewById(R.id.top_4);
		             top_5=(Button)layout.findViewById(R.id.top_5);
		             top_1.setOnClickListener(new PopButtonEvaluate());
		             top_2.setOnClickListener(new PopButtonEvaluate());
		             top_3.setOnClickListener(new PopButtonEvaluate());
		             top_4.setOnClickListener(new PopButtonEvaluate());
		             top_5.setOnClickListener(new PopButtonEvaluate());
				}
			}
			else{
         		if(mDialog == null){
         			mDialog = Dialog_Register_Login.getMyDialog();
         			mDialog.show();
         		}
         		else {
         			mDialog.show();
         		}
			}
		}
	}
	
	class PopButtonEvaluate implements OnClickListener{
		public void onClick(View v) {
			double temp_evaluate = 5.0;
			if(v == top_1) temp_evaluate = 5.0;
			else if(v == top_2) temp_evaluate = 4.0;
			else if(v == top_3) temp_evaluate = 3.0;
			else if(v == top_4) temp_evaluate = 2.0;
			else if(v == top_5) temp_evaluate = 1.0;
			final double evaluate = temp_evaluate;
			
			new Thread()
			{
				public void run()
				{
					try
					{
						String getName="Activity/add_rate";
						NameValuePair param1 = new BasicNameValuePair("activity_id", get_mId);
						NameValuePair param2 = new BasicNameValuePair("rate", evaluate+"");
						String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1,param2);
						if(getResponse.equals("true")){
							Looper.prepare();
							Toast.makeText(HuoDong_PingJia.this,"评分成功", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}.start();
			pop.dismiss();
		}
	}
	
	
    private List<Card_huodong_pingjia> getItems(){
    	mCards=new ArrayList<Card_huodong_pingjia>();
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, jResult + get_mId);
        	JSONArray jsonArray=jsonObject.getJSONArray("actx_comment");
        	temp_arrayLenght = jsonArray.length();
        	for(int i=0;i<temp_arrayLenght;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
        		Card_huodong_pingjia mCard=new Card_huodong_pingjia(jsonpet.getString("nickname"),
        															jsonpet.getString("CommentContent"),
        															jsonpet.getString("CommentTime"));  
        		mCards.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
    	return mCards;
    }

	public void onRefresh() {
	}

	public void onLoadMore() {
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
    			Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, "timeingWork");
			}
		}; 
		timerLoading.schedule(timertask, MyStatic.LoadingTime);
	}
	
	private void changeLayout(){
		if(!layoutChanged){
			if(timeExhausted){
				progressBar.setVisibility(4);
				failToConnect.setVisibility(0);
				Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, "timeExhausted");
			}
			else {
				loading.setVisibility(4);
				connectSuccessLayout.setVisibility(0);
				Log.i(HuoDong_PingJia.ACTIVITY_SERVICE, "loadingWork");
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
