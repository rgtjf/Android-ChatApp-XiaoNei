package com.shoulder.xiaonei.card;

public class Card_chat {

	private String content;
	private int who;//是我还是别人的发言
	private String name;//发言人的名字
	private String id;//发言人的id
	private String photoUrl;
	private int chatType;
	private long timeStamp;
	private int status;//0表示发送中，1表示成功，2表示失败
	
	public Card_chat(String content , int who , String name , String id ,String photoUrl ,int chatType, long timeStamp, int status){
		this.content = content.trim();
		this.who = who;
		this.name = name;
		this.id = id;
		this.photoUrl = photoUrl;
		this.chatType = chatType;
		this.timeStamp = timeStamp;
		this.status = status;
	}
	
	public String getContent(){
		return content;
	}
	
	public int getWho(){
		return who;
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
	
	public String getPhotoUrl(){
		return photoUrl;
	}
	
	public int getChatType(){
		return chatType;
	}
	
	public long getTimeStamp(){
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp){
		this.timeStamp = timeStamp;
	}
	
	public int getStatus(){
		return status;
	}
	public void setStatus(int status){
		this.status = status;
	}
}
