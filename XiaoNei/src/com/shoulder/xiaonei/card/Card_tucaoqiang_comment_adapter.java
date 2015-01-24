package com.shoulder.xiaonei.card;

import java.util.List;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.tucao.Text_Full;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Card_tucaoqiang_comment_adapter extends BaseAdapter{

	private List<Card_tucaoqiang_comment> mCards;
	private Context mContext;
	
	private boolean waitDouble = true;
	
	public Card_tucaoqiang_comment_adapter(Context mContext,List<Card_tucaoqiang_comment> mCards){
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
    	mView=LayoutInflater.from(mContext).inflate(R.layout.card_tucao_comment, null);
    	mHolder.Count_Lou=(TextView)mView.findViewById(R.id.count_lou);
    	mHolder.Count_Lou.setText(Index+1 + "¥");
    	mHolder.Content=(TextView)mView.findViewById(R.id.comment_content);
    	mHolder.Content.setText(mCards.get(Index).getContent());
    	mHolder.UserName=(TextView)mView.findViewById(R.id.user_name);
    	mHolder.UserName.setText("@" + mCards.get(Index).getUserName());
    	mHolder.PublicTime=(TextView)mView.findViewById(R.id.pub_time);
    	mHolder.PublicTime.setText(TimeMachining.twoDateDistance(mCards.get(Index).getPubTime()));
    	
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
    	TextView Count_Lou;
    	TextView UserName;
    	TextView PublicTime;
    }
	

}
