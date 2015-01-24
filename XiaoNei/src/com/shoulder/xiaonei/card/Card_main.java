package com.shoulder.xiaonei.card;

import com.shoulder.xiaonei.myClass_Machine.TimeMachining;

public class Card_main {
	   
	   private String mTitle;
	   private String mTime;
	   private String mPlace;
	   private String mImageUrl;
	   private int mId;
	   private int orgId;
	   private int girlLimit;
	   private int boyLimit;
	   private int girlCount;
	   private int boyCount;
	   
	   
	   public Card_main(String mTitle,String mTime,String mPlace,String mImageUrl,int mId,int orgId,
			   			int girlLimit,int boyLimit,int girlCount,int boyCount)  
	   {  
	       this.mTitle=mTitle;
	       this.mTime=mTime;
	       this.mPlace=mPlace;
	       this.mImageUrl=mImageUrl;
	       this.mId=mId;
	       this.orgId=orgId;
	       this.girlLimit = girlLimit;
	       this.boyLimit = boyLimit;
	       this.girlCount = girlCount;
	       this.boyCount = boyCount;
	   } 
	   
	   public String getTitle()   
	   {  
	    return mTitle;  
	   }  
	   public void setTitle(String mTitle)   
	   {  
	    this.mTitle = mTitle;  
	   }  
	   
	   public String getTime()   
	   {  
	    return mTime;  
	   }  
	   public void setTime(String mTime)   
	   {  
		   this.mTime=mTime;
	   }  
	   
	   public String getPlace()   
	   {  
	    return mPlace;  
	   }  
	   public void setPlace(String mPlace)   
	   {  
	    this.mPlace = mPlace;  
	   }  
	     

	   public String getImageUrl()   
	   {  
	    return mImageUrl;  
	   }  
	     
	   public void setImageUrl(String mImageUrl)   
	   {  
	    this.mImageUrl = mImageUrl;  
	   }  

	   
	   public int getMyId(){
		   return mId;
	   }
	   public void setMyId(int mId){
		   this.mId=mId;
	   }

	   public int getOrgId(){
		   return orgId;
	   }
	   public void setOrgId(int orgId){
		   this.orgId=orgId;
	   }
	   
	   public int getGirlLimit(){
		   return girlLimit;
	   }
	   
	   public int getBoyLimit(){
		   return boyLimit;
	   }
	   
	   public int getGirlCount(){
		   return girlCount;
	   }
	   
	   public int getBoyCount(){
		   return boyCount;
	   }
}
