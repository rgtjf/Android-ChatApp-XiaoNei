package chat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.Group;
import com.avos.avoscloud.GroupMemberQueryCallback;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.card.Card_group_member;
import com.shoulder.xiaonei.card.Card_group_member_adapter;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_myView.MyGridView;
import com.shoulder.xiaonei.myZone.YourZone;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Chat_GourpMember extends Activity{
	
	private Button leftButton;
	private TextView text;
	private Button rightButton;
	private ImageView rightImageView;
	
	private MyGridView mGridView;
	private List<Card_group_member> mCards;
	private Card_group_member_adapter mAdapter;
	
	private Group group;
	
	private int get_chatType;
	private String get_objectId;//groupId in leanCloud
	private String get_thisId;//orgId or huodongId in our database
	private String get_thisName;
	
	private String jResult = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.chat_group_member_grid);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
		
		Bundle extras = getIntent().getExtras();
		get_chatType = extras.getInt(MyStatic.KEYNAME_ChatType);
		get_objectId = extras.getString(MyStatic.KEYNAME_GroupId);
		if(get_chatType == MyStatic.CHATTYPE_SHETUAN){
			get_thisId = extras.getString(MyStatic.KEYNAME_OrgId);
			get_thisName = extras.getString(MyStatic.KEYNAME_OrgName);
		}
		else if(get_chatType == MyStatic.CHATTYPE_HUODONG){
			get_thisId = extras.getString(MyStatic.KEYNAME_HuoDongId);
			get_thisName = extras.getString(MyStatic.KEYNAME_HuoDongName);
		}
		
		initTitle();
		initGridView();
	}
	
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			jResult = msg.obj.toString();
			switch(msg.what){
			case MyStatic.MSG_buildCard:
				buildCards();
			}
		}
	};
	
	public void jGet(final String mtName){
		new Thread(){
			public void run(){
				String getMethod="";
				String getParams="";
				if(mtName.equals("getMember") && get_chatType == MyStatic.CHATTYPE_SHETUAN){
					getMethod = "organization/get_org_allmember";
					getParams = "orgid=" + get_thisId;
				}
				else if(mtName.equals("getMember") && get_chatType == MyStatic.CHATTYPE_HUODONG){
					getMethod = "activity/get_activity_allmember";
					getParams = "actid=" + get_thisId;
				}
				try{
					String result = CustomerHttpClient.get(MyVal.getApplication().getIpName()+getMethod+"?"+getParams);
					Message msg = new Message();
					if(mtName.equals("getMember") && (get_chatType == MyStatic.CHATTYPE_SHETUAN || get_chatType == MyStatic.CHATTYPE_HUODONG)){
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
		rightButton = (Button)findViewById(R.id.right_btn);
		rightImageView = (ImageView)findViewById(R.id.function_image);
		leftButton.setOnClickListener(new ButtonOnClickListenerL());
		text.setText(get_thisName);
		rightImageView.setVisibility(4);
		rightButton.setVisibility(4);
	}
	
	private void initGridView(){
		mGridView = (MyGridView)findViewById(R.id.chat_group_member_grid);
		mCards = new ArrayList<Card_group_member>();
		mAdapter = new Card_group_member_adapter(this, mCards);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new GridItemClickListener());
		
		jGet("getMember");
	}
	
	private void buildCards(){
		int[] tempPhotoUrl = {R.drawable.test_1,R.drawable.test_2}; 
		
		try {
	    	JSONObject jsonObject;
	    	jsonObject = new JSONObject(jResult);
	    	JSONArray jsonArray = new JSONArray();
	    	if(get_chatType == MyStatic.CHATTYPE_SHETUAN)
	    		jsonArray=jsonObject.getJSONArray("org_allmember");
	    	else if(get_chatType == MyStatic.CHATTYPE_HUODONG)
	    		jsonArray = jsonObject.getJSONArray("activity_allmember");
	    	
	    	text.setText(get_thisName + "(" + jsonArray.length() + ")");//标题中显示群人数 
			
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonpet = jsonArray.getJSONObject(i);
				String thisId = jsonpet.getString("LoginID");
				Card_group_member card = new Card_group_member(thisId, 
																jsonpet.getString("nickname"), 
																jsonpet.getString("photo"));
				mCards.add(card);
			}
			mAdapter.notifyDataSetChanged();
		} 
		catch (Exception e) {
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
	
	class GridItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Intent intent = new Intent(Chat_GourpMember.this,YourZone.class);
			intent.putExtra(MyStatic.KEYNAME_SingleId, mCards.get(position).getId());
			startActivity(intent);
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
