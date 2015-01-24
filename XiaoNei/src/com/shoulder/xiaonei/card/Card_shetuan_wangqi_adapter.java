package com.shoulder.xiaonei.card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.HuoDong_PingJia;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Card_shetuan_wangqi_adapter extends BaseAdapter{

	private List<Card_shetuan_wangqi> mCards;
	private Context mContext;
	private AsyncImageLoader asyncImageLoader;
	
    //线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private String myDir;
	private String myIpName;
	private MyVal mv;
	
	public Card_shetuan_wangqi_adapter(Context mContext,List<Card_shetuan_wangqi> mCards){
		this.mCards=mCards;
		this.mContext=mContext;
		asyncImageLoader = new AsyncImageLoader(this.mContext);
		
		mv = (MyVal)mContext.getApplicationContext();
		myDir = mv.getMyDir();
		myIpName = mv.getIpName();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCards.size();
	}

	@Override
	public Object getItem(int Index) {
		// TODO Auto-generated method stub
		return mCards.get(Index);
	}

	@Override
	public long getItemId(int Index) {
		// TODO Auto-generated method stub
		return Index;
	}

	@Override
	public View getView(final int Index, View mView, ViewGroup mParent) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder=null;
		if (mView == null) {
			mView=LayoutInflater.from(mContext).inflate(R.layout.card_shetuan_wangqi, null);
			mViewHolder=new ViewHolder();
			mViewHolder.Title=(TextView)mView.findViewById(R.id.title);
			mViewHolder.Evaluation=(TextView)mView.findViewById(R.id.evaluation);
			mViewHolder.Image=(ImageView)mView.findViewById(R.id.image);
			mViewHolder.myCard=(ImageView)mView.findViewById(R.id.myCard);
			mViewHolder.myCard.setBackgroundResource(R.drawable.selector_cover_transparent);
			mView.setTag(mViewHolder);
		}
		else{
        	mViewHolder = (ViewHolder)mView.getTag();
        }
		
		mViewHolder.Title.setText(mCards.get(Index).getTitle());
		mViewHolder.Evaluation.setText("已有"+mCards.get(Index).getCommentCount()+"条评论"
				+ ","+mCards.get(Index).getRate()+"分");
		
        final String imageUrl = mCards.get(Index).getImageUri();
        mViewHolder.Image.setTag(imageUrl);
        mViewHolder.Image.setImageResource(R.drawable.fill_color);
        Drawable drawable = getDrawable(asyncImageLoader, imageUrl, 
        		mViewHolder.Image); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	mViewHolder.Image.setImageDrawable(drawable); 
		
		mViewHolder.myCard.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,HuoDong_PingJia.class);
				intent.putExtra("name", mCards.get(Index).getTitle());
				intent.putExtra("uri", mCards.get(Index).getImageUri());
				intent.putExtra("rate", mCards.get(Index).getRate());
				intent.putExtra("mId", mCards.get(Index).getMyId());
				mContext.startActivity(intent);
			}
		});
		
		return mView;
	}
	
	private static class ViewHolder{
		TextView Title;
		TextView Evaluation;
		ImageView Image;
		ImageView myCard;
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
