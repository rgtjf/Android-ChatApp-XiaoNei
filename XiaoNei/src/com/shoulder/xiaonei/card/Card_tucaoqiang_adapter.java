package com.shoulder.xiaonei.card;

import java.util.ArrayList;
import java.util.List;  

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.actionbarsherlock.R.color;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.tucao.Text_Full;
import com.shoulder.xiaonei.tucao.TuCaoQiang;
import com.shoulder.xiaonei.tucao.TuCaoQiang_Comment;

import android.app.Dialog;
import android.content.Context;  
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.sax.StartElementListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;  
import android.widget.RelativeLayout;
import android.widget.TextView;  
import android.widget.Toast;
  
public class Card_tucaoqiang_adapter extends BaseAdapter   
{  
    private List<Card_tucaoqiang> mCards;  
    private Context mContext;  
    
    private boolean waitDouble = true;
    
    private String ipName;
    private MyVal mv;
    
    private String getName="";
    
    private Dialog myDialog_register_login;
    
    public Card_tucaoqiang_adapter(Context mContext,List<Card_tucaoqiang> list)  
    {  
        this.mContext=mContext;  
        this.mCards=list;  
        mv = (MyVal)mContext.getApplicationContext();
        ipName = mv.getIpName();
        Dialog_Register_Login.Set_Dialog_Register_Login(mContext,"tucaoqiang");
    } 
	@Override  
    public int getCount()   
    {
		return mCards.size();  
    }  
  
    @Override  
    public Object getItem(int Index)   
    {  
        return mCards.get(Index);  
    }  
  
    @Override  
    public long getItemId(int Index)   
    {  
        return Index;  
    }  
  
    
    @Override  
    public View getView(final int Index, View mView, ViewGroup mParent)   
    {  
        final ViewHolder mHolder=new ViewHolder();  
        mView=LayoutInflater.from(mContext).inflate(R.layout.card_tucaoqiang, null);  
        mHolder.Zan=(ImageButton)mView.findViewById(R.id.zan);
		mHolder.PublishTime=(TextView)mView.findViewById(R.id.pub_time);
		mHolder.PublishTime.setText(TimeMachining.twoDateDistance(mCards.get(Index).getPublicTime()));
        mHolder.Content=(TextView)mView.findViewById(R.id.content);  
        mHolder.Content.setText(mCards.get(Index).getContent());
        mHolder.Content.setBackgroundResource(mCards.get(Index).getMyColor());
        mHolder.UserName=(TextView)mView.findViewById(R.id.user_id);
        mHolder.UserName.setText(mCards.get(Index).getUserName());
        mHolder.ZanCount=(TextView)mView.findViewById(R.id.zan_cnt);
        mHolder.ZanCount.setText(mCards.get(Index).getZanCount() + "个赞");
        mHolder.CommentCount=(Button)mView.findViewById(R.id.comment_cnt);
        mHolder.CommentCount.setText(mCards.get(Index).getCommentCount());
        if (mCards.get(Index).getZanState()==0){
			mHolder.Zan.setImageResource(R.drawable.zan_no);
		}
		else {
			mHolder.Zan.setImageResource(R.drawable.zan_yes);
		}
        
        
        //监听button事件
        OnClickListener listener =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v==mHolder.CommentCount){
					Intent intent=new Intent(mContext, TuCaoQiang_Comment.class);
					intent.putExtra("color", mCards.get(Index).getMyColor());
					intent.putExtra("content", mCards.get(Index).getContent());
					intent.putExtra("mId", mCards.get(Index).getMyId());
					mContext.startActivity(intent);
				}
				else if (v==mHolder.Zan){
					if ( mv.getLoginState() == 0){
			    		 if (myDialog_register_login == null) 
			      		{
			    			 myDialog_register_login = Dialog_Register_Login.getMyDialog();
			    			 myDialog_register_login.show();
			      		}
			      		else 
			     		{
			      			myDialog_register_login.show();
			     		}
					}else {
						mCards.get(Index).changeZanState();
						if (mCards.get(Index).getZanState()==0){
							mHolder.Zan.setImageResource(R.drawable.zan_no);
							mCards.get(Index).changeZanCount(-1);
							getName = "Tucao/tucao_delete_zan";
						}
						else {
							mHolder.Zan.setImageResource(R.drawable.zan_yes);
							mCards.get(Index).changeZanCount(1);
							getName = "Tucao/tucao_add_zan";
						}
						mHolder.ZanCount.setText(mCards.get(Index).getZanCount() + "个赞");
						new Thread()
						{
							public void run()
							{
								try
								{
									NameValuePair param1 = new BasicNameValuePair("tucaoid", mCards.get(Index).getMyId()+"");
									String getResponse = CustomerHttpClient.post(mv.getIpName()+getName, param1);
									if(getResponse.equals("true")){
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
				else if(v==mHolder.Content){
			         if(waitDouble == true){
			             waitDouble = false;
			             Thread thread = new Thread(){
			                @Override
			                public void run(){
			                   try {
			                      sleep(MyStatic.DOUBLE_CLICK_TIME);
			                      if(waitDouble == false){
			                         waitDouble = true;
			                      }
			                   } catch (InterruptedException e) {
			                      // TODO Auto-generated catch block
			                      e.printStackTrace();
			                   }
			                }
			             };
			             thread.start();
			          }else{
			             waitDouble = true;
			             Intent intent=new Intent(mContext,Text_Full.class);
			             intent.putExtra(MyStatic.KEYNAME_Content, mCards.get(Index).getContent());
			             mContext.startActivity(intent);
			          }
				}
			}
		};
		mHolder.CommentCount.setOnClickListener(listener);
		mHolder.Content.setOnClickListener(listener);
		mHolder.Zan.setOnClickListener(listener);
		
        return mView;  
    }  
    
    
  
    private static class ViewHolder  
    {  
    	TextView PublishTime;
    	ImageButton Zan;
    	TextView Content;
    	TextView UserName;
    	TextView ZanCount;
    	Button CommentCount;
    }  
    
}  