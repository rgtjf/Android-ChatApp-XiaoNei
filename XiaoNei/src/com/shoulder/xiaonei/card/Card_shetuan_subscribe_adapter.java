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
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.SheTuan_Detail;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Card_shetuan_subscribe_adapter extends BaseAdapter{
    private List<Card_shetuan_subscribe> mCards;  
    private Context mContext;  
    
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private AsyncImageLoader asyncImageLoader;
    
    private String myDir="";
    private String myIpName="";
    private MyVal mv;
    
    private String getName="";
    
    private Dialog myDialog_register_login;
    
    public Card_shetuan_subscribe_adapter(Context mContext,List<Card_shetuan_subscribe> mCards)  
    {  
        this.mContext=mContext;  
        this.mCards=mCards;  
        asyncImageLoader = new AsyncImageLoader(this.mContext);
        
        mv=(MyVal)mContext.getApplicationContext();
        myDir=mv.getMyDir();
        myIpName=mv.getIpName();
        
        Dialog_Register_Login.Set_Dialog_Register_Login(mContext,"shetuan_subscribe");
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
        ViewHolder mHolder = null;
        if(mView == null){
	        mView=LayoutInflater.from(mContext).inflate(R.layout.card_shetuan_subscribe, null);
	        mHolder = new ViewHolder();
	        mHolder.Card_Title=(TextView)mView.findViewById(R.id.org_name);  
	        mHolder.Card_Title.setText(mCards.get(Index).getTitle());
	        mHolder.Card_Title.setSingleLine();
	        mHolder.Card_Logo=(ImageView)mView.findViewById(R.id.logo);
	        mHolder.Card_OrgMark=(TextView)mView.findViewById(R.id.org_mark);
	        mHolder.Card_OrgMark.setText(mCards.get(Index).getOrgMark() + "·Ö");
	        mHolder.CardSelected=(ImageView)mView.findViewById(R.id.Card_select);
	        mHolder.CardSelected.setBackgroundResource(R.drawable.selector_cover_transparent);
	        mHolder.Card_DingYue=(RelativeLayout)mView.findViewById(R.id.dingyue);
	//        mHolder.Card_DingYue_Image=(ImageView)mView.findViewById(R.id.dingyue_image);
	        mHolder.Card_DingYue_Text=(TextView)mView.findViewById(R.id.dingyue_text);
	        mView.setTag(mHolder);
        }
        else{
        	mHolder = (ViewHolder)mView.getTag();
        }
        
        if(mCards.get(Index).getDingYueState() == 1){
        	mHolder.Card_DingYue_Text.setText("ÒÑ¶©ÔÄ");
        	mHolder.Card_DingYue_Text.setTextColor(mContext.getResources().getColor(R.color.text_gray_light));
        }
        else if(mCards.get(Index).getDingYueState() == 0){
        	mHolder.Card_DingYue_Text.setText("¶©ÔÄ");
        	mHolder.Card_DingYue_Text.setTextColor(mContext.getResources().getColor(R.color.text_blue_dark));
        }
        
        //Í¼Æ¬»º´æ
        final String imageUrl = mCards.get(Index).getImageUri();
        mHolder.Card_Logo.setTag(imageUrl);
        mHolder.Card_Logo.setImageResource(R.drawable.fill_color);
        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
        		mHolder.Card_Logo); 
        //ËµÃ÷´ËÍ¼Æ¬ÒÑ±»ÏÂÔØ²¢»º´æ
        if(drawable!=null)
        	mHolder.Card_Logo.setImageDrawable(drawable); 
        
        mHolder.CardSelected.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext,SheTuan_Detail.class);
				intent.putExtra("where_come_from", "shetuan_subscribe_second");
				intent.putExtra("org_name", mCards.get(Index).getTitle());
				intent.putExtra("org_logo", mCards.get(Index).getImageUri());
				intent.putExtra("org_id", mCards.get(Index).getOrgId());
				intent.putExtra("org_rate", mCards.get(Index).getOrgMark());
				mContext.startActivity(intent);
			}
		});
        
        final TextView temp_DingYue_Text = mHolder.Card_DingYue_Text;
        mHolder.Card_DingYue.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ( mv.getLoginState() == 0){
		    		 if (myDialog_register_login == null) 
			      		{
			    			 myDialog_register_login = Dialog_Register_Login.getMyDialog();
			    			 myDialog_register_login.show();
			      		}
			      	else 
			     		{
			      			myDialog_register_login.show();
			     		}
					}
				else {
					if (mCards.get(Index).getDingYueState()==0){
						temp_DingYue_Text.setText("ÒÑ¶©ÔÄ");
						temp_DingYue_Text.setTextColor(mContext.getResources().getColor(R.color.text_gray_light));
						mCards.get(Index).setDingYueState(1);
						getName = "Organization/add_gz";
					}
					else {
						temp_DingYue_Text.setText("¶©ÔÄ");
						temp_DingYue_Text.setTextColor(mContext.getResources().getColor(R.color.text_blue_dark));
						mCards.get(Index).setDingYueState(0);
						getName = "Organization/delete_gz_org";
					}
					new Thread()
					{
						public void run()
						{
							try
							{
								NameValuePair param1 = new BasicNameValuePair("org_id", mCards.get(Index).getOrgId()+"");
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
		});
		
        return mView;  
    }
    
    private static class ViewHolder{
    	TextView Card_Title;
    	ImageView Card_Logo;
    	TextView Card_OrgMark;
    	ImageView CardSelected;
    	RelativeLayout Card_DingYue;
    	TextView Card_DingYue_Text;
//    	ImageView Card_DingYue_Image;
    }
}
