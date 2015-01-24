package chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import chat.Contacts.ButtonOnClickListener;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.XListView.XListView;
import com.shoulder.xiaonei.XListView.XListView.IXListViewListener;
import com.shoulder.xiaonei.card.Card_contacts;
import com.shoulder.xiaonei.card.Card_contacts_adapter;
import com.shoulder.xiaonei.db.DB_ChatLast_Helper;
import com.shoulder.xiaonei.myClass.MyStatic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Contacts_All extends Activity implements IXListViewListener{

    private RelativeLayout right_rel;	
	private Button left_btn;
    private TextView text;
    
    private XListView mListView;
    private Card_contacts_adapter mAdapter;
    private List<Card_contacts> mCards;
    
    private RelativeLayout loading;
    private LinearLayout connect_success;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.card_mycontacts_friend_listview); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        initLoading();
        initTitle();
        initListView();
	}
	
	private void initLoading(){
        loading = (RelativeLayout)findViewById(R.id.loading);
        connect_success = (LinearLayout)findViewById(R.id.connectSuccessLayout);
        loading.setVisibility(4);
        connect_success.setVisibility(0);
	}
	
	private void initTitle(){
		left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonOnClickListener());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText(R.string.menu_main_buttonText_contacts);
	}

	private void initListView(){
    	mListView=(XListView)findViewById(R.id.Lv1);
	    mAdapter=new Card_contacts_adapter(this,getItems()); 
        mListView.setPullLoadEnable(false);//设置上拉刷新
        mListView.setPullRefreshEnable(false);//设置下拉刷新
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);  
	}
	
	
    private List<Card_contacts> getItems(){
    	mCards=new ArrayList<Card_contacts>();
    	buildCards();
        return mCards;  
    }
    
    
    private void buildCards(){
    	DB_ChatLast_Helper db = new DB_ChatLast_Helper(Contacts_All.this);
    	Log.i("kkk", "contactsAll_01");
    	Cursor cursor = db.query();
    	Log.i("kkk", "contactsAll_02");
    	while(cursor.moveToNext()){
    		
    		Log.i("kkk", "contactsAll_02_2");
    		int readState = cursor.getInt(cursor.getColumnIndex(MyStatic.DB_fieldHaveRead));
    		if(readState!= MyStatic.DB_HaveNotRead)
    			continue;
    		
    		Log.i("kkk", "contactsAll_02_5");
    		String objectId = cursor.getString(cursor.getColumnIndex(MyStatic.DB_fieldObjectId));
    		Log.i("kkk", "contactsAll_03");
    		String lastMessge = cursor.getString(cursor.getColumnIndex(MyStatic.DB_fieldLastMessage));
    		Log.i("kkk", "contactsAll_04");
      		Card_contacts mCard=new Card_contacts("13400889943","tidai.jpg", 
						lastMessge,MyStatic.CHATTYPE_SINGLE,
						objectId); 
      		mCards.add(mCard);
    	}
    	Log.i("kkk", "contactsAll");
    	cursor.close();
    	db.close();
    }
	
	
    private void GoBack(){
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

	public void onRefresh() {
	}

	public void onLoadMore() {
	}    

}
