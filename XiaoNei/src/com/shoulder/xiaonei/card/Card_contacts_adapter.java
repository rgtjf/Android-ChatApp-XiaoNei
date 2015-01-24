package com.shoulder.xiaonei.card;

import java.util.List;

import chat.Chat;
import chat.Contacts;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Card_contacts_adapter extends BaseAdapter{
	private List<Card_contacts> mCards;
	private Context mContext;
	
	private AsyncImageLoader asyncImageLoader;
	
	public Card_contacts_adapter(Context context , List<Card_contacts> cards){
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
    
    
	public View getView(final int Index, View mConvertView, ViewGroup mParent) {
		
		ViewHolder mViewHolder = null;
		if(mConvertView == null){
			mConvertView = LayoutInflater.from(mContext).inflate(R.layout.card_mycontacts_friend, null);
			mViewHolder = new ViewHolder();
			mViewHolder.mCard=(RelativeLayout)mConvertView.findViewById(R.id.myCard);
			mViewHolder.avator=(ImageView)mConvertView.findViewById(R.id.contacts_avatar);
			mViewHolder.name=(TextView)mConvertView.findViewById(R.id.contacts_name);
			mConvertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder)mConvertView.getTag();
		}
		
		mViewHolder.name.setText(mCards.get(Index).getName());
		
        final String imageUrl = mCards.get(Index).getAvatorUrl();
        mViewHolder.avator.setTag(imageUrl);
        mViewHolder.avator.setImageResource(R.drawable.fill_color);
        Drawable drawable = getDrawable(asyncImageLoader, imageUrl, 
        		mViewHolder.avator); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	mViewHolder.avator.setImageDrawable(drawable); 
		
		mViewHolder.mCard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//当点击到自己头像时的动作
				if(mCards.get(Index).getId().equals(MyVal.getApplication().getMyId())){
					
				}
				
				//点击别人头像时的操作
				else{
					Intent intent = new Intent(mContext, Chat.class);
					if(mCards.get(Index).getChatType() == MyStatic.CHATTYPE_SINGLE){
						intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_SINGLE);
						intent.putExtra(MyStatic.KEYNAME_SingleName,  mCards.get(Index).getName());
						intent.putExtra(MyStatic.KEYNAME_SingleId, mCards.get(Index).getId());
						intent.putExtra(MyStatic.KEYNAME_Avator, mCards.get(Index).getAvatorUrl());
						intent.putExtra(MyStatic.KEYNAME_ObjectId, mCards.get(Index).getObjectId());
						mContext.startActivity(intent);
					}
					else if(mCards.get(Index).getChatType() == MyStatic.CHATTYPE_SHETUAN){
						intent.putExtra(MyStatic.KEYNAME_ChatType, MyStatic.CHATTYPE_SHETUAN);
						intent.putExtra(MyStatic.KEYNAME_OrgName, mCards.get(Index).getName());
						intent.putExtra(MyStatic.KEYNAME_OrgId, mCards.get(Index).getId());
						intent.putExtra(MyStatic.KEYNAME_ObjectId, mCards.get(Index).getObjectId());
						intent.putExtra(MyStatic.KEYNAME_Avator, mCards.get(Index).getAvatorUrl());
						mContext.startActivity(intent);
					}
				}
			}
		});
		
		return mConvertView;
	}
    
    
	private static class ViewHolder{
		RelativeLayout mCard;
		ImageView avator;
		TextView name;
    }
	
    public Drawable getDrawable(AsyncImageLoader asyncImageLoader, 
            String imageUrl, final ImageView imageView) { 
        Drawable drawable = asyncImageLoader.loadDrawable(imageUrl, 
                new ImageCallback() { 
                    @Override 
                    public void imageLoaded(Drawable imageDrawable, 
                            String imageUrl) { 
                        if (imageView != null && imageView.getTag().equals(imageUrl)) {  
                        	imageView.setImageDrawable(imageDrawable);  
                        } 
                    } 
                }); 
        return drawable; 
    } 


}
