package com.shoulder.xiaonei.card;

public class Card_myActivity {
	   
	   private String mTitle;
	   private String mTime;
	   private String mPlace;
	   private String mImageUrl;
	   private String pub_time;
	   private int mId;
	   private int orgId;
	   private int gzstate;
	   private Double activityMark;
	   private String girlLimit;
	   private String boyLimit;
	   private String girlCount;
	   private String boyCount;
	   
	   
	   public Card_myActivity(String mTitle,String mTime,String mPlace,String mImageUrl,
			   				int mId,int orgId,int gzstate,
			   				double activityMark,
			   				String girlLimit,String boyLimit,String girlCount,String boyCount)  
	   {  
	       this.mTitle=mTitle;
	       this.mTime=mTime;
	       this.mPlace=mPlace;
	       this.mImageUrl=mImageUrl;
	       this.mId=mId;
	       this.orgId=orgId;
	       this.gzstate = gzstate;
	       this.activityMark = activityMark;
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
	    this.mTime = mTime;  
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

	   public String getPublishTime(){
		   return pub_time;
	   }
	   public void setPublishTime(String pub_time){
		   this.pub_time=pub_time;
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
	   
	   public int getGuanzhuState(){
		   return gzstate;
	   }
	   public void setGuanzhuState(int gzstate){
		   this.gzstate = gzstate;
	   }
	   
	   public double getActivityMark(){
		   return activityMark;
	   }
	   
	   public String getGirlLimit(){
		   return girlLimit;
	   }
	   
	   public String getBoyLimit(){
		   return boyLimit;
	   }
	   
	   public String getGirlCount(){
		   return girlCount;
	   }
	   
	   public String getBoyCount(){
		   return boyCount;
	   }
}
