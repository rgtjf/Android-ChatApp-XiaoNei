package com.shoulder.xiaonei.card;

public class Card_myTucao {

	private String mContent;
	private String mUserName;
	private String mZanCount;
	private String mCommentCount;
	private int mColor;
	private int mId;
	private String pub_time;
	private int zan_state;
	
	public Card_myTucao(String mContent,String mUserName,String mZanCount,String mCommentCount,int mColor,int mId,String pub_time,int zan_state) {
		this.mContent=mContent;
		this.mUserName=mUserName;
		this.mZanCount=mZanCount;
		this.mCommentCount=mCommentCount;
		this.mColor=mColor;
		this.mId=mId;
		this.pub_time=pub_time;
		this.zan_state=zan_state;
	}

	public String getContent(){
		return mContent;
	}
	public void setContent(String mContent) {
		this.mContent=mContent;
	}
	
	public String getUserName(){
		return mUserName;
	}
	public void setUserName(String mUserName){
		this.mUserName=mUserName;
	}
	
	
	public String getZanCount(){
		return mZanCount;
	}
	public void setZanCount(String mZanCount){
		this.mZanCount=mZanCount;
	}
	
	public String getCommentCount(){
		return mCommentCount;
	}
	public void setCommentCoutn(String mCommentCount){
		this.mCommentCount=mCommentCount;
	}
	
	public int getMyColor(){
		return mColor;
	}
	public void setMyColor(int mColor){
		this.mColor=mColor;
	}
	
	public int getMyId(){
		return mId;
	}
	public void setMyId(int mId){
		this.mId=mId;
	}
	
	public String getPublishTime(){
		return pub_time;
	}
	public void setPublishTime(String pub_time){
		this.pub_time=pub_time;
	}
	
	public int getZanState(){
		return zan_state;
	}
	public void changeZanState(){
		this.zan_state = (this.zan_state+1)%2;
	}

}
