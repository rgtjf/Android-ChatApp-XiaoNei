package com.shoulder.xiaonei.card;

public class Card_chatMember {
	
	private String mId;
	private String mNickName;
	private String mImageUrl;
	
	public Card_chatMember(String mId, String mNickName, String mImageUrl){
		this.mId = mId;
		this.mNickName = mNickName;
		this.mImageUrl = mImageUrl;
	}
	
	public String getMyNickName(){
		return mNickName;
	}
	
	public String getMyAvator(){
		return mImageUrl;
	}

}
