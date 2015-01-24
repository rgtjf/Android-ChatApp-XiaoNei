package com.shoulder.xiaonei.card;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.SheTuan_Detail;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;

public class Card_search_shetuan_adapter extends BaseAdapter{
	   
		private List<Card_search_shetuan> mCards;  
	    private Context mContext; 
	    private Boolean isOut = false;
	    
	      
	    public Card_search_shetuan_adapter(Context mContext,List<Card_search_shetuan> mCards)  
	    {  
	        this.mContext=mContext;  
	        this.mCards=mCards;  
	        MyVal mv=(MyVal)mContext.getApplicationContext();
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
	    
	    public View getView(final int Index, View mConvertView, ViewGroup mParent)
	    {  
	    	final ViewHolder mHolder; 
	        if (mConvertView == null) {
		        mConvertView=LayoutInflater.from(mContext).inflate(R.layout.card_search, null);
		        mHolder=new ViewHolder();
		        mHolder.Card_Title=(TextView)mConvertView.findViewById(R.id.title);  
		        mHolder.Card_Describe=(TextView)mConvertView.findViewById(R.id.describe);
		        mHolder.myCard=(ImageView)mConvertView.findViewById(R.id.myCard);
		        mHolder.myCard.setBackgroundResource(R.drawable.selector_cover_transparent);
		        mConvertView.setTag(mHolder);
	        }
	        else{
	            mHolder = (ViewHolder)mConvertView.getTag();
	        }
	        
	        OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if(v==mHolder.myCard){
						Intent intent = new Intent(mContext,SheTuan_Detail.class);
						intent.putExtra("where_come_from", "shetuan_subscribe_second");
						intent.putExtra("org_name", mCards.get(Index).getTitle());
						intent.putExtra("org_logo", mCards.get(Index).getImageUri());
						intent.putExtra("org_id", mCards.get(Index).getOrgId());
						intent.putExtra("org_rate", mCards.get(Index).getOrgMark());
						mContext.startActivity(intent);
//						FinishOrNot();
					}
				}
			};
			mHolder.myCard.setOnClickListener(listener);
	        
	        mHolder.Card_Title.setText(mCards.get(Index).getTitle());
	        mHolder.Card_Title.setSingleLine(true);
	        mHolder.Card_Describe.setText(mCards.get(Index).getOrgMark());
	        
	        return mConvertView;
	    }
	    
//	    private void FinishOrNot(){
//			new Thread(){
//			public void run() {
//				try{
//					sleep(MyStatic.FinishSearchTime);
//					((Activity) mContext).finish();
//				}	
//				catch (Exception e)
//				{
//						e.printStackTrace();
//				}
//				
//			}
//		}.start();
//	    }
	    
	    private static class ViewHolder{
	    	TextView Card_Title;
	    	TextView Card_Describe;
	    	ImageView myCard;
	    }
}
