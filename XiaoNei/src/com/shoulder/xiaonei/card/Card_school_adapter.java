package com.shoulder.xiaonei.card;

import java.util.List;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.MyVal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Card_school_adapter extends BaseAdapter{

	private List<Card_school> mCards;  
    private Context mContext; 
    
    private MyVal mv;
    
    public Card_school_adapter(Context mContext,List<Card_school> mCards)  
    {  
        this.mContext=mContext;  
        this.mCards=mCards;  
        
        mv = (MyVal)mContext.getApplicationContext();
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
    	ViewHolder mHolder = new ViewHolder(); 
	    mConvertView=LayoutInflater.from(mContext).inflate(R.layout.card_school, null);
	    mHolder=new ViewHolder();
	    mHolder.MyCard = (TextView)mConvertView.findViewById(R.id.school_name);
	    String schoolName = mCards.get(Index).getSchoolName();
	    String schoolBranch = mCards.get(Index).getSchoolBranch();
	    mHolder.MyCard.setText(schoolName + "_" + schoolBranch);
        
        return mConvertView;
    }
    
    private static class ViewHolder{
    	TextView MyCard;
    }
}
