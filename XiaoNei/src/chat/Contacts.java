package chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.card.Card_contacts;
import com.shoulder.xiaonei.card.Card_contacts_adapter;
import com.shoulder.xiaonei.card.Card_myActivity;
import com.shoulder.xiaonei.card.Card_myActivity_adapter;
import com.shoulder.xiaonei.card.Card_myOrg;
import com.shoulder.xiaonei.card.Card_myOrg_adapter;
import com.shoulder.xiaonei.card.Card_myTucao;
import com.shoulder.xiaonei.card.Card_myTucao_adapter;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.main.Shetuan_Subscribe_Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myZone.MyZone;
import com.shoulder.xiaonei.myZone.MyZone.MyOnPageChangeListener;
import com.shoulder.xiaonei.myZone.MyZone.MyViewPagerAdapter;
import com.shoulder.xiaonei.tucao.TuCaoQiang_Comment_Fabu;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class Contacts extends Activity implements com.shoulder.xiaonei.XListView.XListView.IXListViewListener{
	
	private String jResult ="";
	private String methodName_initMyMessage = "initMyMessage";
	private String methodName_initMyFriend = "initMyFriend";
	private String methodName_initMyShetuan = "initMyShetuan";
	
	public LinearLayout connectSuccessLayout_myMessage,connectSuccessLayout_myFriend,connectSuccessLayout_myShetuan;
	private RelativeLayout loading_myMessage,loading_myFriend,loading_myShetuan;
	private ProgressBar progressBar_myMessage,progressBar_myFriend,progressBar_myShetuan;
	private TextView failToConnect_myMessage,failToConnect_myFriend,failToConnect_myShetuan;
	private Boolean layoutChanged_myMessage = false,layoutChanged_myFriend = false,layoutChanged_myShetuan = false;
	private Boolean timeExhausted_myMessage = false,timeExhausted_myFriend = false,timeExhausted_myShetuan = false;
	
    private RelativeLayout right_rel;	
	private Button left_btn;
    private TextView text;
    private LinearLayout myMenu;
    private Button right_btn;
    
    public RelativeLayout clue_linear_myMessage,clue_linear_myFriend,clue_linear_myShetuan;
	private TextView clue_text1_myMessage,clue_text1_myFriend,clue_text1_myShetuan;
	private TextView clue_text2_myMessage,clue_text2_myFriend,clue_text2_myShetuan;
	private Boolean buildCard_myMessage = false;//当该页面第一次获取信息时，若无数据，则使clue页面可见
	private Boolean buildCard_myFriend = false;
	private Boolean buildCard_myShetuan = false;
    
	private TextView myMessage;
    private TextView myFriend;
    private TextView myShetuan;
    
    private Timer timer;
    private int timeCount = 0;
    
    private ImageView imageView;// 动画图片  
	private ViewPager viewPager;//页卡内容  
    private List<View> views;// Tab页面列表  
    private int currIndex = 0;// 当前页卡编号  
    private View view_myFriend,view_myShetuan;//各个页卡  
    private int offset = 0;// 动画图片偏移量  
    private int bmpW;// 动画图片宽度  
    
    private XListView mListView1,mListView2,mListView3;
    private Card_contacts_adapter mAdapter_myMessage;
    private Card_contacts_adapter mAdapter_myFriend;
    private Card_contacts_adapter mAdapter_myShetuan;
    private List<Card_contacts> mCards_myMessage;
    private List<Card_contacts> mCards_myFriend;
    private List<Card_contacts> mCards_myShetuan;
    private Handler mHandler_myMessage,mHandler_myFriend,mHandler_myShetuan;
    
    private Boolean flag_ListView_myMessage_isInit = false;
    private Boolean flag_ListView_myFriend_isInit = false;
    private Boolean flag_ListView_myShetuan_isInit = false;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.contacts); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        initTitle();
        initImageView();//可切换标题下的滑块
        initTextView();//可切换标题名
        initViewPager();
        
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
		public void handleMessage(Message msg)
		{
			jResult = msg.obj.toString();
			switch(msg.what)
			{
			case MyStatic.MSG_changeLayout:
 				changeLayout();
				break;
			case MyStatic.MSG_buildCard:
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
				if(mtName.equals(methodName_initMyFriend)){
					getMethod = "manage/get_connect_info";
					getParams = "mid=" + MyVal.getApplication().getMyId();
				}else if(mtName.equals(methodName_initMyShetuan)){
					getMethod = "Manage/get_my_org";
				}
				try
				{
					String result = CustomerHttpClient.get(MyVal.getApplication().getIpName() + getMethod + "?" + getParams);
					Message msg = new Message();
					if(mtName.equals(methodName_initMyFriend) || mtName.equals(methodName_initMyShetuan)){
						msg.what = MyStatic.MSG_buildCard;
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
        right_btn=(Button)findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Contacts.this, Contacts_All.class);
				startActivity(intent);
			}
		});
        text=(TextView)findViewById(R.id.text);
        text.setText(R.string.menu_main_buttonText_contacts);
	}
	
    private void initImageView() {  
        imageView= (ImageView) findViewById(R.id.cursor);  
        bmpW = imageView.getWidth();// 获取图片宽度 
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;// 获取分辨率宽度  
        LayoutParams para = new LayoutParams(screenW / 2, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(para);
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);// 设置动画初始位置  
    }  
    
    private void initTextView() {  
        myFriend=(TextView)findViewById(R.id.my_friends);
        myShetuan=(TextView)findViewById(R.id.my_shetuan);
        myFriend.setOnClickListener(new MyOnClickListener(0));
        myShetuan.setOnClickListener(new MyOnClickListener(1));
    }
    
    private void initViewPager() {  
        viewPager=(ViewPager)findViewById(R.id.vPager);  
        views=new ArrayList<View>();  
        LayoutInflater inflater=getLayoutInflater();  
        view_myFriend=inflater.inflate(R.layout.card_mycontacts_friend_listview, null);
        view_myShetuan=inflater.inflate(R.layout.card_mycontacts_friend_listview, null);

        views.add(view_myFriend);  
        views.add(view_myShetuan);
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener()); 
        
        if(flag_ListView_myFriend_isInit == false && currIndex == 0){
        	initListView_myFriend();
        	jGet(methodName_initMyFriend);
        }
    } 
    
    private void initListView_myFriend(){
    	if(!flag_ListView_myFriend_isInit){
		    connectSuccessLayout_myFriend=(LinearLayout)view_myFriend.findViewById(R.id.connectSuccessLayout);
		    loading_myFriend=(RelativeLayout)view_myFriend.findViewById(R.id.loading);
		    progressBar_myFriend=(ProgressBar)view_myFriend.findViewById(R.id.progressBar);
		    failToConnect_myFriend=(TextView)view_myFriend.findViewById(R.id.failToConnect);
		    failToConnect_myFriend.setOnClickListener(new TextViewConnectFail());
		    timing(0);
		    flag_ListView_myFriend_isInit = true;
    	}
    	
    	clue_linear_myFriend = (RelativeLayout)view_myFriend.findViewById(R.id.clue_linear);
        clue_text1_myFriend = (TextView)view_myFriend.findViewById(R.id.clue_text1);
        clue_text2_myFriend = (TextView)view_myFriend.findViewById(R.id.clue_text2);
        clue_linear_myFriend.setOnLongClickListener(new ClueLongClick());
        clue_text1_myFriend.setText(getResources().getString(R.string.clue_contacts_myFriend_text1));
        clue_text2_myFriend.setText(getResources().getString(R.string.clue_contacts_myFriend_text2));
	    
    	mListView2=(XListView)view_myFriend.findViewById(R.id.Lv1);
	    mAdapter_myFriend=new Card_contacts_adapter(this,getItems_myFriend()); 
        mHandler_myFriend= new Handler();
        mListView2.setPullLoadEnable(false);//设置上拉刷新
        mListView2.setPullRefreshEnable(false);//设置下拉刷新
        mListView2.setXListViewListener(this);
        mListView2.setAdapter(mAdapter_myFriend);  
    }
    
    private void initListView_myShetuan(){
    	if(!flag_ListView_myShetuan_isInit){
	        connectSuccessLayout_myShetuan=(LinearLayout)view_myShetuan.findViewById(R.id.connectSuccessLayout);
	        loading_myShetuan=(RelativeLayout)view_myShetuan.findViewById(R.id.loading);
	        progressBar_myShetuan=(ProgressBar)view_myShetuan.findViewById(R.id.progressBar);
	        failToConnect_myShetuan=(TextView)view_myShetuan.findViewById(R.id.failToConnect);
	        failToConnect_myShetuan.setOnClickListener(new TextViewConnectFail());
	        timing(1);
	        flag_ListView_myShetuan_isInit = true;
        }
    	
        mListView3=(XListView)view_myShetuan.findViewById(R.id.Lv1);  
        mAdapter_myShetuan=new Card_contacts_adapter(this,getItems_myShetuan()); 
        mHandler_myShetuan = new Handler();
        mListView3.setPullLoadEnable(false);//设置上拉刷新
        mListView3.setPullRefreshEnable(false);//设置下拉刷新
        mListView3.setXListViewListener(this);
        mListView3.setAdapter(mAdapter_myShetuan);
        
        clue_linear_myShetuan = (RelativeLayout)view_myShetuan.findViewById(R.id.clue_linear);
        clue_text1_myShetuan = (TextView)view_myShetuan.findViewById(R.id.clue_text1);
        clue_text2_myShetuan = (TextView)view_myShetuan.findViewById(R.id.clue_text2);
        clue_linear_myShetuan.setOnLongClickListener(new ClueLongClick());
        clue_text1_myShetuan.setText(getResources().getString(R.string.clue_contacts_myShetuan_text1));
        clue_text2_myShetuan.setText(getResources().getString(R.string.clue_contacts_myShetuan_text2));
    }
    
    
    private List<Card_contacts> getItems_myFriend(){
    	mCards_myFriend=new ArrayList<Card_contacts>();
    	buildMyFriendCard();
        return mCards_myFriend;  
    }
    private void buildMyFriendCard(){
    	try {
          	JSONObject jsonObject;
          	jsonObject = new JSONObject(jResult);
          	JSONArray jsonArray=jsonObject.getJSONArray("connect_info");
          	int temp_arrayLength = jsonArray.length();
          	
          	if(temp_arrayLength == 0 && buildCard_myFriend == false && currIndex == 0){
          		clue_linear_myFriend.setVisibility(0);
    			connectSuccessLayout_myFriend.setVisibility(4);
    			buildCard_myFriend = true;
          	}
          	
          	for(int i=0;i<temp_arrayLength;i++)  
          	{
          		JSONObject jsonpet = jsonArray.getJSONObject(i);
          		Card_contacts mCard=new Card_contacts(jsonpet.getString("loginid"),jsonpet.getString("photo"), 
          										jsonpet.getString("nickname") ,MyStatic.CHATTYPE_SINGLE,
          										jsonpet.getString("loginid")); 
          		mCards_myFriend.add(mCard);
          	}
        } catch (JSONException e) {
          	e.printStackTrace();
        }
    }
    
    private List<Card_contacts> getItems_myShetuan(){
    	mCards_myShetuan = new ArrayList<Card_contacts>();
    	buildMyShetuanCard();
        return mCards_myShetuan;
    }
    private void buildMyShetuanCard(){
        try {
        	JSONObject jsonObject;
        	jsonObject = new JSONObject(jResult);
        	JSONArray jsonArray=jsonObject.getJSONArray("my_org");
        	int temp_arrayCount =jsonArray.length();
        	
          	if(temp_arrayCount == 0 && buildCard_myShetuan == false && currIndex == 1){
          		clue_linear_myShetuan.setVisibility(0);
    			connectSuccessLayout_myShetuan.setVisibility(4);
    			buildCard_myShetuan = true;
          	}
        	
        	for(int i=0;i<temp_arrayCount;i++)  
        	{
        		JSONObject jsonpet = jsonArray.getJSONObject(i);
          		Card_contacts mCard=new Card_contacts(jsonpet.getString("OrganizationID"),jsonpet.getString("Logo"), 
          				jsonpet.getString("OrganizationName") ,MyStatic.CHATTYPE_SHETUAN,
          				jsonpet.getString("GroupID")); 
        		mCards_myShetuan.add(mCard);
        	}
        } catch (JSONException e) {
        	e.printStackTrace();
        }
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
	
	class ClueLongClick implements OnLongClickListener{
		public boolean onLongClick(View v) {
			if(v == clue_linear_myFriend){
			}
			else if(v == clue_linear_myShetuan){
				Intent intent = new Intent(Contacts.this,Shetuan_Subscribe_Main.class);
				startActivity(intent);
			}
			return true;
		}
	}
    
	class TextViewConnectFail implements OnClickListener{
		public void onClick(View v) {
			if(v == failToConnect_myFriend){
				layoutChanged_myFriend = false;
				timeExhausted_myFriend = false;
				failToConnect_myFriend.setVisibility(4);
				progressBar_myFriend.setVisibility(0);
				timing(0);
	        	jGet(methodName_initMyFriend);
			}
			else if(v == failToConnect_myShetuan){
				layoutChanged_myShetuan = false;
				timeExhausted_myShetuan = false;
				failToConnect_myShetuan.setVisibility(4);
				progressBar_myShetuan.setVisibility(0);
				timing(1);
	        	jGet(methodName_initMyShetuan);
			}
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
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
            currIndex = arg0;  
            
            //给tab文字加选中颜色
            myFriend.setTextColor(getResources().getColor(R.color.text_black_tab));
            myShetuan.setTextColor(getResources().getColor(R.color.text_black_tab));
            if(currIndex == 0)
            	myFriend.setTextColor(getResources().getColor(R.color.widget_tab_text));
            else if (currIndex == 1)
            	myShetuan.setTextColor(getResources().getColor(R.color.widget_tab_text));
            
            //当打开该tab时才初始化
            if(flag_ListView_myShetuan_isInit == false && currIndex == 1){
            	initListView_myShetuan();
            	jGet(methodName_initMyShetuan);
            }
            animation.setFillAfter(true);// True:图片停在动画结束位置  
            animation.setDuration(300);  
            imageView.startAnimation(animation);  
        }  
    }
    
    
	private void timing(final int i){
        Timer timerLoading=new Timer();
        TimerTask timertask=new TimerTask() {
			public void run() {
				if(i == 0){
					timeExhausted_myFriend = true;
				}
				else if(i == 1){
					timeExhausted_myShetuan =true;
				}
    			Message msg = new Message();
    			msg.what = MyStatic.MSG_changeLayout;
    			msg.obj = "";
    			handler.sendMessage(msg);
			}
		}; 
		timerLoading.schedule(timertask, MyStatic.LoadingTime);
	}
	
	private void changeLayout(){
		if(currIndex == 0){
			if(!layoutChanged_myFriend){
				if(timeExhausted_myFriend){
					progressBar_myFriend.setVisibility(4);
					failToConnect_myFriend.setVisibility(0);
				}
				else {
					loading_myFriend.setVisibility(4);
					connectSuccessLayout_myFriend.setVisibility(0);
				}
			}
			layoutChanged_myFriend = true;
		}
		else if(currIndex == 1){
			if(!layoutChanged_myShetuan){
				if(timeExhausted_myShetuan){
					progressBar_myShetuan.setVisibility(4);
					failToConnect_myShetuan.setVisibility(0);
				}
				else {
					loading_myShetuan.setVisibility(4);
					connectSuccessLayout_myShetuan.setVisibility(0);
				}
			}
			layoutChanged_myShetuan = true;
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


	public void onRefresh() {
	}
	public void onLoadMore() {
	}
    public void refresh()
    {
    	if(currIndex == 0){
	    	mHandler_myFriend.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_myFriend!=null)
	    				mCards_myFriend.clear();
	    			buildMyFriendCard();
	    			mAdapter_myFriend.notifyDataSetChanged();
	    			mListView2.setAdapter(mAdapter_myFriend);
	    		}
	    	});
    	}
    	else if(currIndex == 1){
	    	mHandler_myShetuan.post(new Runnable()
	    	{
	    		public void run()
	    		{
	    			if(mCards_myShetuan!=null)
	    				mCards_myShetuan.clear();
	    			buildMyShetuanCard();
	    			mAdapter_myShetuan.notifyDataSetChanged();
	    			mListView3.setAdapter(mAdapter_myShetuan);
	    		}
	    	});
    	}
    }
}
