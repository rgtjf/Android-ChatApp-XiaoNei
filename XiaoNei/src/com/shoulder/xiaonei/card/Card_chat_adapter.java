package com.shoulder.xiaonei.card;

import java.util.List;

import chat.Chat;
import chat.Chat_GourpMember;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.SheTuan_Detail;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.myZone.YourZone;
import com.shoulder.xiaonei.tucao.Text_Full;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Card_chat_adapter extends BaseAdapter{
	
	private List<Card_chat> mCards;
	private Context mContext;
	
	private AsyncImageLoader asyncImageLoader;
	
    private boolean waitDouble = true;
	
	public Card_chat_adapter(Context context , List<Card_chat> cards){
		this.mCards = cards;
		this.mContext = context;
		
		asyncImageLoader = new AsyncImageLoader(this.mContext);
	}
	
    public int getCount()   
    {  
        return mCards.size();  
    }  
  
    public Object getItem(int Index)   
    {  
        return mCards.get(Index);  
    }  
  
    public long getItemId(int Index)   
    {  
        return Index;  
    }  
    
    public Card_chat getCardByContent(String content){
    	for(int i = mCards.size()-1; i>-1;i--){
    		if(mCards.get(i).getContent().equals(content)){
    			return mCards.get(i);
    		}
    	}
		return null;
    }
    
    
	public View getView(final int Index, View mConvertView, ViewGroup mParent) {
		
		ViewHolder mViewHolder = null;
		if(mConvertView == null){
			mConvertView = LayoutInflater.from(mContext).inflate(R.layout.chat_item, null);
			mViewHolder = new ViewHolder();
			mViewHolder.TimeStamp=(TextView)mConvertView.findViewById(R.id.sendTimeView);
			mViewHolder.relativeLeft=(RelativeLayout)mConvertView.findViewById(R.id.relativeLeft);
			mViewHolder.relativeRight=(RelativeLayout)mConvertView.findViewById(R.id.relativeRight);
			mViewHolder.NameLeft=(TextView)mConvertView.findViewById(R.id.chat_name);
			if(mCards.get(Index).getChatType() == MyStatic.CHATTYPE_SINGLE)
				mViewHolder.NameLeft.setVisibility(8);
			mViewHolder.ContentLeft=(TextView)mConvertView.findViewById(R.id.chat_text_left);
			mViewHolder.ContentRight=(TextView)mConvertView.findViewById(R.id.chat_text_Right);
			mViewHolder.ImageLeft=(ImageView)mConvertView.findViewById(R.id.chat_image_left);
			mViewHolder.ImageRight=(ImageView)mConvertView.findViewById(R.id.chat_image_Right);
			mViewHolder.ChatStatus=(ProgressBar)mConvertView.findViewById(R.id.chat_status);
			mConvertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder)mConvertView.getTag();
		}
		
		
		
		//根据时间戳的差判断是否需要显示时间
		if(Index ==0 || TimeMachining.haveTimeGap(mCards.get(Index-1).getTimeStamp(), mCards.get(Index).getTimeStamp())){
			mViewHolder.TimeStamp.setVisibility(0);
			mViewHolder.TimeStamp.setText(TimeMachining.millisecs2DateString(mCards.get(Index).getTimeStamp()));
		}
		else{
			mViewHolder.TimeStamp.setVisibility(8);
		}
		
		
		//chat的状态，发送中还是发送成功、失败
		if(mCards.get(Index).getStatus() == MyStatic.Status_Success){
			mViewHolder.ChatStatus.setVisibility(8);
		}
		else if(mCards.get(Index).getStatus() == MyStatic.Status_Loading){
			mViewHolder.ChatStatus.setVisibility(0);
		}
		
		
		if(mCards.get(Index).getWho() == MyStatic.CHATWHO_ME){
			mViewHolder.relativeLeft.setVisibility(4);
			mViewHolder.relativeRight.setVisibility(0);
			mViewHolder.ContentRight.setText(mCards.get(Index).getContent());
			
	        final String imageUrl = mCards.get(Index).getPhotoUrl();
	        mViewHolder.ImageRight.setTag(imageUrl);
	        mViewHolder.ImageRight.setImageResource(R.drawable.fill_color);
	        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
	        		mViewHolder.ImageRight); 
	        //说明此图片已被下载并缓存
	        if(drawable!=null)
	        	mViewHolder.ImageRight.setImageDrawable(drawable); 
	        
		}
		else if(mCards.get(Index).getWho() == MyStatic.CHATWHO_OTHERS){
			mViewHolder.relativeRight.setVisibility(4);
			mViewHolder.relativeLeft.setVisibility(0);
			mViewHolder.NameLeft.setText(mCards.get(Index).getName());
			mViewHolder.ContentLeft.setText(mCards.get(Index).getContent());
			
	        final String imageUrl = mCards.get(Index).getPhotoUrl();
	        mViewHolder.ImageLeft.setTag(imageUrl);
	        mViewHolder.ImageLeft.setImageResource(R.drawable.fill_color);
	        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
	        		mViewHolder.ImageLeft); 
	        //说明此图片已被下载并缓存
	        if(drawable!=null)
	        	mViewHolder.ImageLeft.setImageDrawable(drawable); 
	        
		}

		
		mViewHolder.ImageLeft.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext,YourZone.class);
				intent.putExtra(MyStatic.KEYNAME_SingleName, mCards.get(Index).getName());
				intent.putExtra(MyStatic.KEYNAME_SingleId, mCards.get(Index).getId());
				intent.putExtra(MyStatic.KEYNAME_Url, mCards.get(Index).getPhotoUrl());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
			}
		});
		
		//还没找到能合并在一起的方法，不雅，先凑合着用吧，以后注意要改
		mViewHolder.ContentLeft.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
		         if(waitDouble == true){
		             waitDouble = false;
		             Thread thread = new Thread(){
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
		             };
		             thread.start();
		          }else{
		             waitDouble = true;
		             Intent intent=new Intent(mContext,Text_Full.class);
		             intent.putExtra(MyStatic.KEYNAME_Content, mCards.get(Index).getContent());
		             mContext.startActivity(intent);
		          }
			}
		});
		
		mViewHolder.ContentRight.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
		         if(waitDouble == true){
		             waitDouble = false;
		             Thread thread = new Thread(){
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
		             };
		             thread.start();
		          }else{
		             waitDouble = true;
		             Intent intent=new Intent(mContext,Text_Full.class);
		             intent.putExtra(MyStatic.KEYNAME_Content, mCards.get(Index).getContent());
		             mContext.startActivity(intent);
		          }
			}
		});
		
		return mConvertView;
	}
	
	
	private static class ViewHolder{
		RelativeLayout relativeLeft;
		RelativeLayout relativeRight;
		TextView NameLeft;
    	TextView ContentLeft;
    	TextView ContentRight;
    	ImageView ImageLeft;
    	ImageView ImageRight;
    	TextView TimeStamp;
    	ProgressBar ChatStatus;
    }

}
