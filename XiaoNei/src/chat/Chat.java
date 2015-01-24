package chat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGroup;
import com.avos.avoscloud.AVHistoryMessage;
import com.avos.avoscloud.AVHistoryMessageQuery;
import com.avos.avoscloud.AVHistoryMessageQuery.HistoryMessageCallback;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.Group;
import com.avos.avoscloud.GroupListener;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SessionManager;
import com.avos.avoscloud.Session;


import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.card.Card_chat;
import com.shoulder.xiaonei.card.Card_chatMember;
import com.shoulder.xiaonei.card.Card_chat_adapter;
import com.shoulder.xiaonei.card.Card_group_member;
import com.shoulder.xiaonei.card.Card_school;
import com.shoulder.xiaonei.card.Card_school_adapter;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.LoginMachine;
import com.shoulder.xiaonei.myClass_chat.ChatMessageListener;
import com.shoulder.xiaonei.myClass_chat.GroupMsgReceiver;
import com.shoulder.xiaonei.myClass_chat.ChatMsgReceiver;
import com.tencent.utils.Util;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.JetPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Chat extends Activity implements com.shoulder.xiaonei.XListView.XListView.IXListViewListener,ChatMessageListener{

	private Button leftButton;
	private TextView text;
	private Button rightButton;
	private ImageView rightImageView;
	
	private EditText chatBottomEdit;
	private Button chatBottomSend;
	
	private int getChatType = 0;
	private String getName = "";
	private String getId = "";
	private String getAvatar = "";
	
    private com.shoulder.xiaonei.XListView.XListView mListView;
	private List<Card_chat> mCards;//0表示“我”（右），1表示“其他人”（左）
	private Card_chat_adapter mAdapter;
	
	private Group group;
	
	public static Chat ctx;
	
	private InputMethodManager imm;
	
	private String selfId;//leanCould的id
	private String objectId;//groupId in leanCloud
	
	private String jResult = "";
	private HashMap<String, Card_chatMember> memberMap;
	
	private long timeStamp;//聊天记录的时间戳
	private long testTime = 0;
	
	private Boolean cardFirstBuild = true;//如果listview是第一次生成，则滑动到最后一行
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list);
		
		rightImageView = (ImageView)findViewById(R.id.function_image);//后面要根据type修改title，所以提前初始化
		rightButton = (Button)findViewById(R.id.right_btn);
		
		Bundle extras = getIntent().getExtras();
		getChatType = extras.getInt(MyStatic.KEYNAME_ChatType);
		if(getChatType == MyStatic.CHATTYPE_SHETUAN){
			getName = extras.getString(MyStatic.KEYNAME_OrgName);
			getId = extras.getString(MyStatic.KEYNAME_OrgId);
			getAvatar = extras.getString(MyStatic.KEYNAME_Avator);
			rightImageView.setImageResource(R.drawable.chat_group);
			objectId = extras.getString(MyStatic.KEYNAME_ObjectId);
		}
		else if(getChatType == MyStatic.CHATTYPE_HUODONG){
			getName = extras.getString(MyStatic.KEYNAME_HuoDongName);
			getId = extras.getString(MyStatic.KEYNAME_HuoDongId);
			getAvatar = extras.getString(MyStatic.KEYNAME_Avator);
			rightImageView.setImageResource(R.drawable.chat_group);
			objectId = extras.getString(MyStatic.KEYNAME_ObjectId);
		}
		else if(getChatType == MyStatic.CHATTYPE_SINGLE){
			getName = extras.getString(MyStatic.KEYNAME_SingleName);
			getId = extras.getString(MyStatic.KEYNAME_SingleId);
			getAvatar = extras.getString(MyStatic.KEYNAME_Avator);
			rightImageView.setVisibility(4);
			rightButton.setVisibility(4);
		}
		
		ctx = this;//关闭该页面使用，暂时的因为聊天很多回调的bug正在测试是哪里发生的
		
		//监听虚拟键盘状态
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//设置当开启软键盘时将页面顶上，避免虚拟键盘挡住下面的文字；不能写在xml中，会没有效果
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		timeStamp = new Long(System.currentTimeMillis() + 24*60*60*1000);//初始化时间戳为现在的时间+24小时，避免有人手机本地的时间较慢
		
		rigisterChatDispatchersListener();
		initTitle();
		init();//初始化对话，包括加入群、关注对方等操作
		initBottom();//下面的输入框
		initCard();//聊天界面
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			jResult = msg.obj.toString();
			switch(msg.what){
			case MyStatic.MSG_buildCard:
				buildChatMemberMap();
				getHistoryMessage();//成员列表获取成功后再获取聊天记录
			}
		}
	};
	
	public void jGet(final String mtName){
		new Thread(){
			public void run(){
				String getMethod="";
				String getParams="";
				if(mtName.equals("getMember") && getChatType == MyStatic.CHATTYPE_SHETUAN){
					getMethod = "organization/get_org_allmember";
					getParams = "orgid=" + getId;
				}
				else if(mtName.equals("getMember") && getChatType == MyStatic.CHATTYPE_HUODONG){
					getMethod = "activity/get_activity_allmember";
					getParams = "actid=" + getId;
				}
				try{
					String result = CustomerHttpClient.get(MyVal.getApplication().getIpName()+getMethod+"?"+getParams);
					Message msg = new Message();
					if(mtName.equals("getMember") && (getChatType == MyStatic.CHATTYPE_SHETUAN || getChatType == MyStatic.CHATTYPE_HUODONG)){
						msg.what = MyStatic.MSG_buildCard;
					}
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch(Exception e){
				}
			}
		}.start();
	}
	
	
	private void initTitle(){
		leftButton = (Button)findViewById(R.id.left_btn);
		text = (TextView)findViewById(R.id.text);
		
		leftButton.setOnClickListener(new ButtonOnClickListenerL());
		text.setText(getName);
		rightButton.setOnClickListener(new goMemberDetail());
	}
	
	private void initBottom(){
		chatBottomEdit=(EditText)findViewById(R.id.chatBottomEdit);
		chatBottomSend=(Button)findViewById(R.id.chatBottomSend);
		chatBottomSend.setOnClickListener(new chatBottomSend());
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
	
	class chatBottomSend implements OnClickListener{
		public void onClick(View v) {
			String chatText = chatBottomEdit.getText().toString().trim();
			if(!TextUtils.isEmpty(chatText)){
				
				testTime = System.currentTimeMillis();
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				
				params.put(MyStatic.KEYNAME_ChatType, getChatType);
				params.put(MyStatic.KEYNAME_Content,chatText);
				params.put(MyStatic.KEYNAME_MESSAGE_NAME, getName);
				params.put(MyStatic.KEYNAME_MESSAGE_AVATAR,getAvatar);
				
				//ios的所需字段
				params.put(MyStatic.IOS_KEYNAME_GROUPID, getId);
				params.put(MyStatic.IOS_KEYNAME_MYNAME, MyVal.getApplication().getMyName());
				params.put(MyStatic.IOS_KEYNAME_MYPHONE, MyVal.getApplication().getMyId());
				params.put(MyStatic.IOS_KEYNAME_MYURL, MyVal.getApplication().getMyAvator());
				
				AVMessage avMessage = new AVMessage(JSON.toJSONString(params));
				avMessage.setFromPeerId(MyVal.getApplication().getMyId());
				
				//开启对话message
				switch (getChatType){
				case MyStatic.CHATTYPE_SHETUAN:
					group.sendMessage(avMessage);
					break;
					
				case MyStatic.CHATTYPE_HUODONG:
					group.sendMessage(avMessage);
					break;
					
				case MyStatic.CHATTYPE_SINGLE:
					avMessage.setToPeerIds(Arrays.asList(getId));
					MyVal.session.sendMessage(avMessage);
					break;
				}
				
				Card_chat mCard = new Card_chat(
									chatText,
									MyStatic.CHATWHO_ME,
									MyVal.getApplication().getMyName(),
									MyVal.getApplication().getMyId(),
									MyVal.getApplication().getMyAvator(),
									getChatType,
									0,//先置为0，待发送成功后由leancloud的timeStamp设置该项
									MyStatic.Status_Loading
								);
				mCards.add(mCard);
				mAdapter.notifyDataSetChanged();
				chatBottomEdit.getEditableText().clear();
				mListView.setSelection(mListView.getCount() - 1);
			}
		}
	}
	
	class goMemberDetail implements OnClickListener{
		public void onClick(View v) {
			if(getChatType == MyStatic.CHATTYPE_SHETUAN){
				Intent intent = new Intent(Chat.this,Chat_GourpMember.class);
				intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_SHETUAN);
				intent.putExtra(MyStatic.KEYNAME_GroupId,objectId);
				intent.putExtra(MyStatic.KEYNAME_OrgId, getId);
				intent.putExtra(MyStatic.KEYNAME_OrgName, getName);
				startActivity(intent);
			}
			else if(getChatType == MyStatic.CHATTYPE_HUODONG){
				Intent intent = new Intent(Chat.this,Chat_GourpMember.class);
				intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_HUODONG);
				intent.putExtra(MyStatic.KEYNAME_GroupId,objectId);
				intent.putExtra(MyStatic.KEYNAME_HuoDongId, getId);
				intent.putExtra(MyStatic.KEYNAME_HuoDongName, getName);
				startActivity(intent);
			}
		}
	}
	
	
	private void init(){
		
//		if(getChatType == MyStatic.CHATTYPE_SHETUAN || getChatType == MyStatic.CHATTYPE_HUODONG){
//			//根据groupNameAndId字段查询是否有该社团群组了，若无，创建一个;有则加入
//			AVQuery<AVObject> query = new AVQuery<AVObject>("AVOSRealtimeGroups");
//			query.whereEqualTo(MyStatic.KEYNAME_GroupNameAndId, getName + getId);
//			query.findInBackground(new FindCallback<AVObject>() {
//				public void done(List<AVObject> avObjects, AVException e) {
//					if (e == null){
//						objectId = avObjects.get(0).getObjectId();
//						initGroup();
//					}
//					else {//○这里需要修改，存在不是因为查询不到，而是其它错误导致的报错，此时不应该新建一个新的群
//						final AVObject groupObject = AVObject.createWithoutData("AVOSRealtimeGroups", null);
//						groupObject.put(MyStatic.KEYNAME_GroupNameAndId, getName + getId);
//						groupObject.saveInBackground(new SaveCallback() {
//							public void done(AVException e) {
//								if(e == null){
//									objectId = groupObject.getObjectId();
//									initGroup();
//								}
//								else {
//								}
//							}
//						});
//					}
//				}
//			});
//		}
		if(!MyVal.session.isOpen()){
			LoginMachine.openSession(MyVal.getApplication().getMyId());
			Log.i("kkk","sessionFail");
		}
		else{
			if(getChatType == MyStatic.CHATTYPE_SHETUAN || getChatType == MyStatic.CHATTYPE_HUODONG){
				initGroup();
			}
			
			else if(getChatType == MyStatic.CHATTYPE_SINGLE){
				MyVal.session.watchPeers(Arrays.asList(getId));
				getHistoryMessage();//加入社团/个人聊天后开始获取历史记录
			}
		}
	}
	
	private void initGroup(){
		group = MyVal.session.getGroup(objectId);
		group.join();
		jGet("getMember");//加入社团/个人聊天后开始成员列表
//		getHistoryMessage();//加入社团/个人聊天后开始获取历史记录
	}
	
	private void rigisterChatDispatchersListener(){
	    switch (getChatType){
	    case MyStatic.CHATTYPE_SHETUAN:
	    	Log.i("kkk","register_shetuan");
	    	GroupMsgReceiver.registerGroupListener(objectId, this);
	    	break;
	    case MyStatic.CHATTYPE_HUODONG:
	    	Log.i("kkk","register_huodong");
	    	GroupMsgReceiver.registerGroupListener(objectId, this);
	    	break;
	    case MyStatic.CHATTYPE_SINGLE:
	    	Log.i("kkk","register_single");
	    	ChatMsgReceiver.registerSingleListener(getId, this);
	    	break;
	    }
	}
	
	public void initCard(){
    	mListView=(XListView)findViewById(R.id.chat_listView);  
    	mAdapter=new Card_chat_adapter(this, getItems());  
        mListView.setPullRefreshEnable(true);//设置下拉加载更多聊天记录
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setXListViewListener(this);
        mListView.setType(MyStatic.XListViewType_Chat);
        
        mListView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					if(imm.isActive()){
						imm.hideSoftInputFromWindow(Chat.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(imm.isActive()){
						imm.hideSoftInputFromWindow(Chat.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					}
					break;
				}
				return false;
			}
		});
        
        mListView.setAdapter(mAdapter);
	}
	
	
	private List<Card_chat> getItems(){
		mCards = new ArrayList<Card_chat>();
        return mCards;
	}
	
	private void getHistoryMessage(){
		
		testTime = System.currentTimeMillis();
		
		AVHistoryMessageQuery historyQuery = new AVHistoryMessageQuery();
		switch (getChatType){//初始化聊天记录查询
		case MyStatic.CHATTYPE_SHETUAN:
			historyQuery = group.getHistoryMessageQuery();
			break;
		case MyStatic.CHATTYPE_HUODONG:
			historyQuery = group.getHistoryMessageQuery();
			break;
		case MyStatic.CHATTYPE_SINGLE:
			historyQuery = MyVal.session.getHistoryMessageQuery();
			historyQuery.setPeerIds(Arrays.asList(MyVal.getApplication().getMyId(),getId));
			break;
		}
		historyQuery.setTimestamp(timeStamp);
		historyQuery.setLimit(10);
		historyQuery.findInBackground(new HistoryMessageCallback(){
			public void done(List<AVHistoryMessage> historyMessageList, AVException e) {
				if(e == null){
					long temp = System.currentTimeMillis() - testTime;
					Log.i("kkk","time_history_received" + temp);
					buildCards(historyMessageList);
				}
				else {
				}
			}
		});
	}
	
	public void buildCards(List<AVHistoryMessage> historyMessageList){
		try{
			for(int i=0;i<10;i++){
				Card_chat mcard = null;
				HashMap<String, Object> params = JSON.parseObject(historyMessageList.get(i).getMessage(), HashMap.class);
				String thisId = historyMessageList.get(i).getFromPeerId();
				String content = (String)params.get(MyStatic.KEYNAME_Content);
				if(thisId.equals(MyVal.getApplication().getMyId())){
					mcard = new Card_chat(
								content,
								MyStatic.CHATWHO_ME,
								MyVal.getApplication().getMyName(),
								MyVal.getApplication().getMyId(),
								MyVal.getApplication().getMyAvator(),
								getChatType,
								historyMessageList.get(i).getTimestamp(),
								MyStatic.Status_Success
							);
				}
				else{
					String yourName = "";
					String yourAvator = "";
					if(getChatType == MyStatic.CHATTYPE_SHETUAN) {
						yourName =  memberMap.get(thisId).getMyNickName();
						yourAvator = memberMap.get(thisId).getMyAvator();
					}
					else if(getChatType == MyStatic.CHATTYPE_SINGLE){
						yourName = getName;
						yourAvator = getAvatar;
					}
					else if(getChatType == MyStatic.CHATTYPE_HUODONG){
						yourName = memberMap.get(thisId).getMyNickName();
						yourAvator = memberMap.get(thisId).getMyAvator();
					}
					
					mcard = new Card_chat(
								content,
								MyStatic.CHATWHO_OTHERS,
								yourName,
								thisId,
								yourAvator,
								getChatType,
								historyMessageList.get(i).getTimestamp(),
								MyStatic.Status_Success
							);
				}
				timeStamp = historyMessageList.get(i).getTimestamp() - 1;//更新时间戳,加1避免实际没有消息但是一直请求到最后一条
				mCards.add(0,mcard);
				mAdapter.notifyDataSetChanged();
			}
			scrollToBottom();
		}
		catch (Exception e){
			scrollToBottom();
			mListView.setPullRefreshEnable(false);
		}
	}
	
	private void scrollToBottom(){//第一次获取聊天记录时，滑动到最底下
		if(cardFirstBuild == true){
			mListView.setSelection(mListView.getCount() - 1);
			cardFirstBuild = false;
		}
	}
	
	private void buildChatMemberMap(){
		memberMap = new HashMap<String, Card_chatMember>();
		try {
	    	JSONObject jsonObject;
	    	jsonObject = new JSONObject(jResult);
	    	JSONArray jsonArray = new JSONArray();
	    	if(getChatType == MyStatic.CHATTYPE_SHETUAN)
	    		jsonArray=jsonObject.getJSONArray("org_allmember");
	    	else if(getChatType == MyStatic.CHATTYPE_HUODONG)
	    		jsonArray = jsonObject.getJSONArray("activity_allmember");
			
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonpet = jsonArray.getJSONObject(i);
				Card_chatMember card = new Card_chatMember(jsonpet.getString("LoginID"), 
																jsonpet.getString("nickname"), 
																jsonpet.getString("photo"));
				memberMap.put(jsonpet.getString("LoginID"), card);
			}
		} 
		catch (Exception e) {
		}
	}


	private void onLoad(){
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("");
	}
	public void onRefresh() {
		getHistoryMessage();
		mAdapter.notifyDataSetChanged();
		onLoad();
	}
	public void onLoadMore() {
	}
	

	//接受到聊天信息时
	public void onMessage(AVMessage avMessage) {
		if (!AVUtils.isBlankString(avMessage.getMessage())){
			HashMap<String, Object> params = JSON.parseObject(avMessage.getMessage(), HashMap.class);
			String yourName = "";
			String yourAvator = "";
			String yourContent = (String)params.get(MyStatic.KEYNAME_Content);
			String yourId = avMessage.getFromPeerId();
			if(getChatType == MyStatic.CHATTYPE_SHETUAN){ 
				yourName =  memberMap.get(yourId).getMyNickName();
				yourAvator = memberMap.get(yourId).getMyAvator();
			}
			else if(getChatType == MyStatic.CHATTYPE_SINGLE){
				yourName = getName;
				yourAvator = getAvatar;
			}
			else if(getChatType == MyStatic.CHATTYPE_HUODONG){
				yourName = memberMap.get(yourId).getMyNickName();
				yourAvator = memberMap.get(yourId).getMyAvator();
			}
			
			Card_chat mCard = new Card_chat(
										yourContent,
										MyStatic.CHATWHO_OTHERS,
										yourName,
										yourId,
										yourAvator,
										getChatType,
										avMessage.getTimestamp(),
										MyStatic.Status_Success
								);
			mCards.add(mCard);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
	//聊天发失败时
	public void onMessageFailure(AVMessage av) {
	}

	//聊天发送成功时
	public void onMessageSent(AVMessage av){
		long temp = System.currentTimeMillis() - testTime;
		Log.i("kkk","time_sent" + temp);
		
		HashMap<String, Object> params = JSON.parseObject(av.getMessage(), HashMap.class);
		String content = (String)params.get(MyStatic.KEYNAME_Content);
		timeStamp = av.getTimestamp();
		
		Card_chat mCard = mAdapter.getCardByContent(content);
		if(mCard!=null){
			mCard.setTimeStamp(timeStamp);
			mCard.setStatus(MyStatic.Status_Success);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	
	public void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}

	public void onPause() {
	    switch (getChatType){
	    case MyStatic.CHATTYPE_SHETUAN:
	    	GroupMsgReceiver.unregisterGroupListener(objectId);
	    	break;
	    case MyStatic.CHATTYPE_HUODONG:
	    	GroupMsgReceiver.unregisterGroupListener(objectId);
	    	break;
	    case MyStatic.CHATTYPE_SINGLE:
	    	ChatMsgReceiver.unregisterSingleListener(getId);
	    	break;
	    }
	    super.onPause();
		MobclickAgent.onPause(this);
	}
	
	protected void onDestroy() {
	    ctx=null;
	    super.onDestroy();
	}


}
