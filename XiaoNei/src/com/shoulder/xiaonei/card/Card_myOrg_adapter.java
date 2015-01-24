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

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.SheTuan_Detail;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass.AsyncImageLoader.ImageCallback;
import com.shoulder.xiaonei.myZone.MyZone;

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

public class Card_myOrg_adapter extends BaseAdapter{
	
    private List<Card_myOrg> mCards;  
    private Context mContext;  
    private AsyncImageLoader asyncImageLoader;
    
    private String myDir="";
    private String myIpName="";
    private MyVal mv;
//    private MyZone mz;
    
    private Dialog dialogMyOrgAction;
    private Button yes_unfollow;
    private TextView myorg_action_text;
    
    private int temp_index;//作为中介，将longclick方法中的值传出，变为final后传入yes_delete方法中
    private View temp_view;//很不优雅，但是怎么都搞不定...只能先这么用着了...
      
    public Card_myOrg_adapter(Context mContext,List<Card_myOrg> mCards) {
    	
        this.mContext=mContext;  
        this.mCards=mCards;  
        asyncImageLoader = new AsyncImageLoader(this.mContext);
        
        mv=(MyVal)mContext.getApplicationContext();
        myDir=mv.getMyDir();
        myIpName=mv.getIpName();
        
//        mz = new MyZone();
        
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
        	ViewHolder mHolder=null;
        	if (mView == null) {
		        mView=LayoutInflater.from(mContext).inflate(R.layout.card_myorg, null);
		        mHolder = new ViewHolder();
		        mHolder.Card_Title=(TextView)mView.findViewById(R.id.name);  
		        mHolder.Card_Mark=(TextView)mView.findViewById(R.id.mark);
		        mHolder.Card_Logo=(ImageView)mView.findViewById(R.id.logo);
		        mHolder.MyCard=(ImageView)mView.findViewById(R.id.myCard);
		        mHolder.MyCard.setBackgroundResource(R.drawable.selector_cover_transparent);
		        mView.setTag(mHolder);
        	}
        	else{
            	mHolder = (ViewHolder)mView.getTag();
            }
        	mHolder.Card_Title.setText(mCards.get(Index).getTitle());
        	mHolder.Card_Mark.setText(mCards.get(Index).getOrgMark() + "分");
        	
            final String imageUrl = mCards.get(Index).getImageUri();
            mHolder.Card_Logo.setTag(imageUrl);
            mHolder.Card_Logo.setImageResource(R.drawable.fill_color);
            Drawable drawable = getDrawable(asyncImageLoader, imageUrl, 
            		mHolder.Card_Logo); 
            //说明此图片已被下载并缓存
            if(drawable!=null)
            	mHolder.Card_Logo.setImageDrawable(drawable); 
	        
            mHolder.MyCard.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,SheTuan_Detail.class);
				intent.putExtra("where_come_from", "myZone");
				intent.putExtra("org_id", mCards.get(Index).getOrgId());
				mContext.startActivity(intent);
				}
            });
		
        final View temp_temp_view = mView;
        mHolder.MyCard.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				
				temp_index = Index;
				temp_view = temp_temp_view;
				
				dialogMyOrgAction = new Dialog(mContext, R.style.dialog_huodong_tixing);
		        dialogMyOrgAction.setContentView(R.layout.dialog_myorg_action);
		        yes_unfollow = (Button)dialogMyOrgAction.findViewById(R.id.yes_unfollow);
		        myorg_action_text=(TextView)dialogMyOrgAction.findViewById(R.id.my_org_action_text);
		        yes_unfollow.setOnClickListener(new ButtonYesUnfollow());
		        myorg_action_text.setSingleLine(true);
				
				myorg_action_text.setText(mCards.get(Index).getTitle());
				
				if (dialogMyOrgAction == null) 
				{
					dialogMyOrgAction = new Dialog(mContext,R.style.dialog);
				}
				
				dialogMyOrgAction.show();
				
				return true;
			}
		});
        
        return mView;  
    }  
    
    
    private class ButtonYesUnfollow implements OnClickListener{
		public void onClick(View arg0) {
			mCards.get(temp_index).setGuanZhuState(0);
			dialogMyOrgAction.dismiss();
			removeListItem(temp_view,temp_index);
			
			final String getName = "Organization/delete_gz_org";
			new Thread()
			{
				public void run()
				{
					try
					{
						NameValuePair param1 = new BasicNameValuePair("org_id", mCards.get(temp_index).getOrgId()+"");
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
    
//    item的飞出及动画
    private void removeListItem(View mView,final int index) {          
        final Animation animation = (Animation)AnimationUtils.loadAnimation(mContext, R.anim.out_item_alpha); 
        animation.setAnimationListener(new AnimationListener() {   
            public void onAnimationStart(Animation animation) {}   
            public void onAnimationRepeat(Animation animation) {}   
            public void onAnimationEnd(Animation animation) {   
                mCards.remove(index);   
                
              if(mCards.size() == 0){
//                	mz.clue_linear_myOrg.setVisibility(0);
//                  mz.connectSuccessLayout_myOrg.setVisibility(4);
            	  mv.getClue(0).setVisibility(0);
            	  mv.getConnectSuccessLayout(0).setVisibility(4);
                }
                
                mv.getMyOrgAdapter().notifyDataSetChanged();   
                animation.cancel(); 
                
            }   
        });   
           
        mView.startAnimation(animation);   
        
    }  
    
    private static class ViewHolder{
    	TextView Card_Title;
    	TextView Card_Mark;
    	ImageView Card_Logo;
    	ImageView MyCard;
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
