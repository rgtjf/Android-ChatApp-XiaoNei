package com.shoulder.xiaonei.card;

import java.util.List;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.tucao.Text_Full;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Card_huodong_pingjia_adapter extends BaseAdapter{
	private List<Card_huodong_pingjia> mCards;
	private Context mContext;
	
	private boolean waitDouble = true;
	
	public Card_huodong_pingjia_adapter(Context mContext,List<Card_huodong_pingjia> mCards)
	{
		this.mCards=mCards;
		this.mContext=mContext;
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
    	ViewHolder mHolder=new ViewHolder();
    	mView=LayoutInflater.from(mContext).inflate(R.layout.card_huodong_pingjia, null);
    	mHolder.Content=(TextView)mView.findViewById(R.id.comment_content);
    	mHolder.Content.setText(mCards.get(Index).getContent());
    	mHolder.UserName=(TextView)mView.findViewById(R.id.user_name);
    	mHolder.UserName.setText("from  " + mCards.get(Index).getUserName());
    	
    	mHolder.Content.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
		         if(waitDouble == true){
		             waitDouble = false;
		             new Thread(){
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
		             }.start();;
		          }else{
		             waitDouble = true;
		             Intent intent=new Intent(mContext,Text_Full.class);
		             intent.putExtra("content", mCards.get(Index).getContent());
		             mContext.startActivity(intent);
		          }
			}
		});
    	
    	return mView;
    	
    }
    
    private static class ViewHolder{
    	TextView Content;
    	TextView UserName;
    }
}
