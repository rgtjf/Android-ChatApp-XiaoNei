package com.shoulder.xiaonei.card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;

public class Card_shetuan_yugao_adapter extends BaseAdapter{
    private List<Card_shetuan_yugao> mCards;  
    private Context mContext;  
    private MyVal mv;
    
  //线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
      
    public Card_shetuan_yugao_adapter(Context mContext,List<Card_shetuan_yugao> mCards)  
    {  
        this.mContext=mContext;  
        this.mCards=mCards;  
        
        mv = (MyVal)mContext.getApplicationContext();
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
    public View getView(int Index, View mView, ViewGroup mParent)   
    {  
        ViewHolder mHolder=new ViewHolder();  
        mView=LayoutInflater.from(mContext).inflate(R.layout.card_shetuan_yugao, null);  
        mHolder.Card_Name=(TextView)mView.findViewById(R.id.huodong_name);  
        mHolder.Card_Name.setText(mCards.get(Index).getTitle());
        mHolder.Card_Time=(TextView)mView.findViewById(R.id.huodong_time);
        mHolder.Card_Time.setText(TimeMachining.DateTranslator(mCards.get(Index).getTime()));
        mHolder.Card_Address=(TextView)mView.findViewById(R.id.huodong_address);
        mHolder.Card_Address.setText(mCards.get(Index).getPlace());
        mHolder.Card_Like=(TextView)mView.findViewById(R.id.huodong_guanzhu);
        int countNumber = mCards.get(Index).getBoyCount()+mCards.get(Index).getGirlCount();
        mHolder.Card_Like.setText(countNumber + "人已关注");
        mHolder.Card_Pic=(ImageView)mView.findViewById(R.id.huodong_image);  
        
        //图片缓存
        final String imageUrl = mCards.get(Index).getImageUrl();
        final ImageView cp = mHolder.Card_Pic;
        File file = new File(mv.getMyDir()+mv.getFileName(), imageUrl);
        if (file.exists()) {
        	Uri uri = Uri.fromFile(file);
	        mHolder.Card_Pic.setImageURI(uri);
        }
        else
        {
        	executorService.submit(new Runnable()
        	{
    			public void run()
    			{
    				try
    				{
    					String u = mv.getImageIpName()+mv.getImageAddress()+imageUrl;
    					URL url = new URL(u);
    					InputStream is ;
    					is = url.openStream();
    					File file=new File(mv.getMyDir()+mv.getFileName(),imageUrl);
    					if(!file.exists()){
    						file.createNewFile();
    					}
    					FileOutputStream fos = new FileOutputStream(file);
    					byte[] buff = new byte[1024];
    					int hasRead = 0;
    					while((hasRead = is.read(buff)) > 0)
    					{
    						fos.write(buff, 0 , hasRead);
    					}
    					is.close();
    					fos.close();
    					Uri uri = Uri.fromFile(file);
    					cp.setImageURI(uri);//http://sheyou.me/
    				}
    				catch (Exception e)
    				{
    					e.printStackTrace();
    				}
    			}
    		});
        }
        
        
        return mView;  
    }  
  
    private static class ViewHolder  
    {  
        TextView Card_Name;  
        TextView Card_Time;
        TextView Card_Address;
        TextView Card_Like;
        ImageView Card_Pic;  
    }  
}
