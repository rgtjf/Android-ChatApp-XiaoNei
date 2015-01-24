package com.shoulder.xiaonei.card;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.Dialog_Register_Login;
import com.shoulder.xiaonei.myClass_Machine.TimeMachining;
import com.shoulder.xiaonei.myZone.MyZone;
import com.shoulder.xiaonei.tucao.Text_Full;
import com.shoulder.xiaonei.tucao.TuCaoQiang_Comment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Card_myTucao_adapter extends BaseAdapter{
    private List<Card_myTucao> mCards;  
    private Context mContext;  
    private int clickCount = 0;
    
    private String getName="";
    
    private Dialog dialogMytucaoDelete;
    private Button mytucao_yes_delete;
    
    private MyVal mv;
    
    private int temp_index;//作为中介，将longclick方法中的值传出，变为final后传入yes_delete方法中
    private View temp_view;//很不优雅，但是怎么都搞不定...只能先这么用着了...
    
    public Card_myTucao_adapter(Context mContext,List<Card_myTucao> list)  
    {  
        this.mContext=mContext;  
        this.mCards=list;  
        
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
    public View getView(final int Index, View mView, ViewGroup mParent)   
    {  
//        ViewHolder mHolder =null;
//        if(mView == null){
        	ViewHolder mHolder=new ViewHolder(); 
	        mView=LayoutInflater.from(mContext).inflate(R.layout.card_mytucao, null);  
	        mHolder.Content=(TextView)mView.findViewById(R.id.content);  
	        mHolder.Content.setText(mCards.get(Index).getContent());
	        mHolder.Count=(TextView)mView.findViewById(R.id.count);
	        mHolder.Count.setText(mCards.get(Index).getZanCount()+"人赞"+
	        					  " ， " + 
	        					  mCards.get(Index).getCommentCount() + "条评论");
	        mHolder.More=(Button)mView.findViewById(R.id.more);
	        
	        
//	        mView.setTag(mHolder);
//        }
//        else {
//        	mHolder = (ViewHolder)mView.getTag();
//        }
        
	        dialogMytucaoDelete = new Dialog(mContext, R.style.dialog_huodong_tixing);
	        dialogMytucaoDelete.setContentView(R.layout.dialog_mytucao_delete);
	        mytucao_yes_delete=(Button)dialogMytucaoDelete.findViewById(R.id.yes_delete);
	        
        //button点击事件
        mHolder.More.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				clickCount ++ ;
				new Thread(){
					public void run(){
						try {
							sleep(MyStatic.DOUBLE_CLICK_TIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(clickCount == 1){
							Intent intent=new Intent(mContext, TuCaoQiang_Comment.class);
							intent.putExtra("color", mCards.get(Index).getMyColor());
							intent.putExtra("content", mCards.get(Index).getContent());
							intent.putExtra("mId", mCards.get(Index).getMyId());
							mContext.startActivity(intent);
						}
						else if(clickCount > 1){
				             Intent intent=new Intent(mContext,Text_Full.class);
				             intent.putExtra("content", mCards.get(Index).getContent());
				             mContext.startActivity(intent);
						}
						clickCount = 0;
					}
				}.start();
			}
		});
        
        final View temp_temp_view = mView;
        mHolder.More.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				
				temp_index = Index;
				temp_view = temp_temp_view;
				if (dialogMytucaoDelete == null) 
				{
					dialogMytucaoDelete = new Dialog(mContext,R.style.dialog);
					dialogMytucaoDelete.show();
				}
				else
				{
					dialogMytucaoDelete.show();
				}
				return true;
			}
		});
        
        mytucao_yes_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				removeListItem(temp_view,temp_index);
				dialogMytucaoDelete.dismiss();
				getName = "Tucao/delete_tocao";
				
				new Thread()
				{
					public void run()
					{
						try
						{
							NameValuePair param1 = new BasicNameValuePair("tucaoid", mCards.get(temp_index).getMyId()+"");
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
		});
        
        return mView;  
        
    }  
    
    
    //item的飞出及动画
    private void removeListItem(View mView,final int index) {          
        final Animation animation = (Animation) AnimationUtils.loadAnimation(mContext, R.anim.out_item_alpha); 
        animation.setAnimationListener(new AnimationListener() {   
            public void onAnimationStart(Animation animation) {}   
            public void onAnimationRepeat(Animation animation) {}   
            public void onAnimationEnd(Animation animation) {   
                mCards.remove(index);   
                
	              if(mCards.size() == 0){
	            	  mv.getClue(2).setVisibility(0);
	            	  mv.getConnectSuccessLayout(2).setVisibility(4);
	              }
                
                mv.getMyTucaoAdapter().notifyDataSetChanged();   
                animation.cancel();   
            }   
        });   
           
        mView.startAnimation(animation);   
    }  
    
    
    private static class ViewHolder  
    {  
    	TextView Content;
    	TextView Count;
    	Button More;
    }  
}
