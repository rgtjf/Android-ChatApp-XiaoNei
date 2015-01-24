package com.shoulder.xiaonei.card;

import java.util.List;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Card_group_member_adapter extends BaseAdapter{

	private List<Card_group_member> mCards;
	private Context mContext;
	
	private AsyncImageLoader asyncImageLoader;
	
	public Card_group_member_adapter(Context mContext,List<Card_group_member> mCards){
		this.mCards = mCards;
		this.mContext = mContext;
		
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
    
    
	public View getView(int Index, View mConvertView, ViewGroup mParent) {
		mConvertView = LayoutInflater.from(mContext).inflate(R.layout.chat_group_member, null);
		ViewHolder mViewHolder = new ViewHolder();
		mViewHolder.Photo = (ImageView)mConvertView.findViewById(R.id.chat_group_member_image);
		mViewHolder.Name = (TextView)mConvertView.findViewById(R.id.chat_group_member_text);
		mViewHolder.Name.setText(mCards.get(Index).getName());
		mViewHolder.Name.setSingleLine(true);
		
		//缓存图片
        final String imageUrl = mCards.get(Index).getPhotoUrl();
        mViewHolder.Photo.setTag(imageUrl);
        mViewHolder.Photo.setImageResource(R.drawable.white);
        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
        		mViewHolder.Photo); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	mViewHolder.Photo.setImageDrawable(drawable); 
        
		return mConvertView;
	}
    
    
	private static class ViewHolder{
		ImageView Photo;
		TextView Name;
    }
}
