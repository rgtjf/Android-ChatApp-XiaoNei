package com.shoulder.xiaonei.card;

public class Card_shetuan_wangqi {
	   private String mTitle;
	   private String mImageUri;
	   private String mId;
	   private String mRate;
	   private String mCommentCount;
	   public Card_shetuan_wangqi(String mTitle,String mImageUri,String mId,String mRate,String mCommentCount)  
	   {  
		   this.mTitle=mTitle;
		   this.mImageUri=mImageUri;
		   this.mId=mId;
		   this.mRate=mRate;
		   this.mCommentCount=mCommentCount;
	   }  
	     
	   public String getTitle()   
	   {  
	    return mTitle;  
	   }  
	   public void setTitle(String mTitle)   
	   {  
		   this.mTitle=mTitle;
	   }  
	   
	   public String getImageUri(){
		   return mImageUri;
	   }
	   
	   public String getMyId(){
		   return mId;
	   }
	   
	   public String getRate(){
		   return mRate;
	   }
	   
	   public String getCommentCount(){
		   return mCommentCount;
	   }
	   

}
