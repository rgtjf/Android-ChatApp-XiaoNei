package com.shoulder.xiaonei.card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.List;  
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.R.id;
import com.shoulder.xiaonei.R.layout;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.myClass.MyVal;

import android.content.Context;  
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;  
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;  
  


public class Card_main_adapter extends BaseAdapter   
{  
    private List<Card_main> mCards;  
    private Context mContext;  
    private MyVal mv;
    private AsyncImageLoader asyncImageLoader;
    
    //线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    public Card_main_adapter(Context mContext,List<Card_main> mCards)  
    {  
        this.mContext=mContext;  
        this.mCards=mCards;  
        mv=(MyVal)mContext.getApplicationContext();
        asyncImageLoader = new AsyncImageLoader(this.mContext);
        
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
    public View getView(final int Index, View mConvertView, ViewGroup mParent)
    {  
    	ViewHolder mHolder = null;
        if (mConvertView == null) {
	        mConvertView=LayoutInflater.from(mContext).inflate(R.layout.card_main, null);
	        mHolder=new ViewHolder();
	        mHolder.Card_Title=(TextView)mConvertView.findViewById(R.id.Card_title);  
	        mHolder.Card_Time=(TextView)mConvertView.findViewById(R.id.Card_time);
	        mHolder.Card_Place=(TextView)mConvertView.findViewById(R.id.Card_place);
	        mHolder.Card_Pic=(ImageView)mConvertView.findViewById(R.id.Card_picture);
	        mHolder.myCard=(ImageView)mConvertView.findViewById(R.id.myCard);
	        //动态将点击button的高度设为同card等高
	        mHolder.myCard.setBackgroundResource(R.drawable.selector_cover_transparent);
	        mConvertView.setTag(mHolder);
        }
        else{
        	mHolder = (ViewHolder)mConvertView.getTag();
        }
        
        mHolder.Card_Title.setText(mCards.get(Index).getTitle());
        mHolder.Card_Title.setSingleLine(true);
        mHolder.Card_Time.setText(TimeMachining.DateTranslator(mCards.get(Index).getTime()));
        mHolder.Card_Place.setText(mCards.get(Index).getPlace());
        mHolder.Card_Place.setSingleLine(true);
        
        
        final String imageUrl = mCards.get(Index).getImageUrl();
        mHolder.Card_Pic.setTag(imageUrl);
        mHolder.Card_Pic.setImageResource(R.drawable.fill_color);
        //因为listview是使用同一个item不断更新其界面显得创建了很多个item，会有下一个图片还没更新时原来的
        //图片还显示着的情况，所以图片出现时现将图片变为默认色，等获取图片后再更新图片
        Drawable drawable = getDrawable(asyncImageLoader, imageUrl, 
        		mHolder.Card_Pic); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	mHolder.Card_Pic.setImageDrawable(drawable); 
        
        
//        //图片缓存
//        final String imageUrl = mCards.get(Index).getImageUrl();
//        final ImageView cp = mHolder.Card_Pic;
//        File file = new File(myDir+mv.getFileName(), imageUrl);
//        if (file.exists()) {
//        	Uri uri = Uri.fromFile(file);
//	        mHolder.Card_Pic.setImageURI(uri);
//        }
//        else
//        {
//        	executorService.submit(new Runnable()
//        	{
//    			public void run()
//    			{
//    				try
//    				{
//    					String u = mv.getImageIpName()+mv.getImageAddress()+imageUrl;
//    					URL url = new URL(u);
//    					InputStream is ;
//    					is = url.openStream();
//    					File file=new File(myDir+mv.getFileName(),imageUrl);
//    					if(!file.exists()){
//    						file.createNewFile();
//    					}
//    					FileOutputStream fos = new FileOutputStream(file);
//    					byte[] buff = new byte[1024];
//    					int hasRead = 0;
//    					while((hasRead = is.read(buff)) > 0)
//    					{
//    						fos.write(buff, 0 , hasRead);
//    					}
//    					is.close();
//    					fos.close();
//    					Uri uri = Uri.fromFile(file);
//    					cp.setImageURI(uri);//http://sheyou.me/
//    				}
//    				catch (Exception e)
//    				{
//    					Log.i(Main.ACTIVITY_SERVICE,  myIpName+"images/"+imageUrl + " fail");
//    					e.printStackTrace();
//    				}
//    			}
//    		});
//        }
        
        
		mHolder.myCard.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent=new Intent(mContext,HuoDong_Detail.class);
				intent.putExtra("name", mCards.get(Index).getTitle());
				intent.putExtra("time", mCards.get(Index).getTime());
				intent.putExtra("address", mCards.get(Index).getPlace());
				intent.putExtra("mId", mCards.get(Index).getMyId());
				intent.putExtra("orgId", mCards.get(Index).getOrgId());
				intent.putExtra("uri", mCards.get(Index).getImageUrl());
				intent.putExtra("girlLimit", mCards.get(Index).getGirlLimit());
				intent.putExtra("boyLimit", mCards.get(Index).getBoyLimit());
				intent.putExtra("girlCount", mCards.get(Index).getGirlCount());
				intent.putExtra("boyCount", mCards.get(Index).getBoyCount());
				mContext.startActivity(intent);
			}
		});
		
        return mConvertView;
    }  
  
    private static class ViewHolder  
    {  
        TextView Card_Title;  
        TextView Card_Time;
        TextView Card_Place;
        ImageView Card_Pic;  
        ImageView myCard;
    }  
    
    
    public Drawable getDrawable(AsyncImageLoader asyncImageLoader, 
            String imageUrl, final ImageView imageView) { 
        Drawable drawable = asyncImageLoader.loadDrawable(imageUrl, 
                new ImageCallback() { 
                    @Override 
                    public void imageLoaded(Drawable imageDrawable, 
                            String imageUrl) { 
//                        if (imageDrawable != null) {
//                        	imageView.setImageDrawable(imageDrawable);
//                        }
                        if (imageDrawable != null && imageView.getTag().equals(imageUrl)) {  
                        	imageView.setImageDrawable(imageDrawable);  
                        } 

                    } 
                }); 
        return drawable; 
    } 
    
}  
