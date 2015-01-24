package com.shoulder.xiaonei.card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.HuoDong_PingJia;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;

public class Card_myActivity_adapter extends BaseAdapter{
    private List<Card_myActivity> mCards;  
    private Context mContext;  
    
    private AsyncImageLoader asyncImageLoader;
    
//    private  final ViewHolder mHolder;
    
    //线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    private String myDir="";
    private String myIpName="";
    private MyVal mv;
    
    private Dialog dialogMyActAction;
    private Button yes_unfollow;
    private TextView myact_action_text;
    
    private int temp_index;//作为中介，将longclick方法中的值传出，变为final后传入yes_delete方法中
    private View temp_view;//很不优雅，但是怎么都搞不定...只能先这么用着了...
      
    public Card_myActivity_adapter(Context mContext,List<Card_myActivity> mCards)  
    {  
        this.mContext=mContext;  
        this.mCards=mCards;  
        
        mv=(MyVal)mContext.getApplicationContext();
        myDir=mv.getMyDir();
        myIpName=mv.getIpName();
        
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
  
    
    public View getView(final int Index, View mConvertView, ViewGroup mParent)
    {  
    	ViewHolder mHolder = null;
    	if (mConvertView == null) {
		    mConvertView=LayoutInflater.from(mContext).inflate(R.layout.card_myactivity, null);
		    mHolder=new ViewHolder();
		    mHolder.Card_Title=(TextView)mConvertView.findViewById(R.id.name);  
		    mHolder.Card_Time=(TextView)mConvertView.findViewById(R.id.time);
		    mHolder.Card_Place=(TextView)mConvertView.findViewById(R.id.address);
		    mHolder.Card_Pic=(ImageView)mConvertView.findViewById(R.id.image);
		    mHolder.myCard=(ImageView)mConvertView.findViewById(R.id.myCard);
		    mHolder.goToEvaluate=(Button)mConvertView.findViewById(R.id.goToEvaluate);
		    mConvertView.setTag(mHolder);
    	}
    	else{
        	mHolder = (ViewHolder)mConvertView.getTag();
        }

	    mHolder.Card_Title.setText(mCards.get(Index).getTitle());
        mHolder.Card_Title.setSingleLine(true);
        mHolder.Card_Time.setText(TimeMachining.DateTranslator(mCards.get(Index).getTime()));
        mHolder.Card_Place.setText(mCards.get(Index).getPlace());
        if(TimeMachining.TimeTOEvaluate(mCards.get(Index).getTime())){
        	mHolder.goToEvaluate.setText(mContext.getString(R.string.myZone_myActivity_activityIsDone));
        	mHolder.goToEvaluate.setTextColor(mContext.getResources().getColor(R.color.text_blue_dark));
        }
        else{
        	mHolder.goToEvaluate.setText(mContext.getString(R.string.myZone_myActivity_activityIsComing));
        	mHolder.goToEvaluate.setTextColor(mContext.getResources().getColor(R.color.text_gray_clue));
        }
        
        final String imageUrl = mCards.get(Index).getImageUrl();
        mHolder.Card_Pic.setTag(imageUrl);
        mHolder.Card_Pic.setImageResource(R.drawable.fill_color);
        Drawable drawable = getDrawable(asyncImageLoader, imageUrl, 
        		mHolder.Card_Pic); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	mHolder.Card_Pic.setImageDrawable(drawable); 
        
        
        OnClickListener listenerMyCard=new OnClickListener() {
			public void onClick(View v) {
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
		};
        OnClickListener listenerGoToEvaluate=new OnClickListener() {
			public void onClick(View v) {
					if(TimeMachining.TimeTOEvaluate(mCards.get(Index).getTime())){
						Intent intent = new Intent(mContext,HuoDong_PingJia.class);
						intent.putExtra("name", mCards.get(Index).getTitle());
						intent.putExtra("uri", mCards.get(Index).getImageUrl());
						intent.putExtra("rate", "4.4");
						intent.putExtra("mId", mCards.get(Index).getMyId() + "");
						mContext.startActivity(intent);
					}
			}
		};
		mHolder.myCard.setOnClickListener(listenerMyCard);
		mHolder.goToEvaluate.setOnClickListener(listenerGoToEvaluate);
		
        final View temp_temp_view = mConvertView;
        mHolder.myCard.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				
				temp_index = Index;
				temp_view = temp_temp_view;
				
				dialogMyActAction = new Dialog(mContext, R.style.dialog_huodong_tixing);
		        dialogMyActAction.setContentView(R.layout.dialog_myact_action);
		        yes_unfollow = (Button)dialogMyActAction.findViewById(R.id.yes_unfollow);
		        myact_action_text=(TextView)dialogMyActAction.findViewById(R.id.my_act_action_text);
		        yes_unfollow.setOnClickListener(new ButtonYesUnfollow());
		        myact_action_text.setSingleLine(true);
				
				myact_action_text.setText(mCards.get(Index).getTitle());
				
				if (dialogMyActAction == null) 
				{
					dialogMyActAction = new Dialog(mContext,R.style.dialog);
				}
				
				dialogMyActAction.show();
				
				return true;
			}
		});
		
        return mConvertView;
    }  
    
    
    private class ButtonYesUnfollow implements OnClickListener{
		public void onClick(View arg0) {
			mCards.get(temp_index).setGuanzhuState(0);
			dialogMyActAction.dismiss();
			removeListItem(temp_view,temp_index);
			
			final String getName = "Activity/delete_cj_activity";
			new Thread()
			{
				public void run()
				{
					try
					{
						NameValuePair param1 = new BasicNameValuePair("activity_id", mCards.get(temp_index).getMyId()+"");
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
    
	//  item的飞出及动画
	private void removeListItem(View mView,final int index) {          
	      final Animation animation = (Animation) AnimationUtils.loadAnimation(mContext, R.anim.out_item_alpha); 
	      animation.setAnimationListener(new AnimationListener() {   
	          public void onAnimationStart(Animation animation) {}   
	          public void onAnimationRepeat(Animation animation) {}   
	          public void onAnimationEnd(Animation animation) {   
	              mCards.remove(index);   
	              
	              if(mCards.size() == 0){
	            	  mv.getClue(1).setVisibility(0);
	            	  mv.getConnectSuccessLayout(1).setVisibility(4);
	                }
	              
	              mv.getMyActAdapter().notifyDataSetChanged();   
	              animation.cancel();   
	          }   
	      });   
         
      mView.startAnimation(animation);   
  }  
	
	
    private static class ViewHolder  
    {  
        TextView Card_Title;  
        TextView Card_Time;
        TextView Card_Place;
        ImageView Card_Pic;  
        ImageView myCard;
        Button goToEvaluate;
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
