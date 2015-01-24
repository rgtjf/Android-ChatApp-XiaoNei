package com.shoulder.xiaonei.card;

public class Card_contacts {

	private String id;
	private String avatorUrl;
	private String name;
	private int chatType;
	private String objectId;
	
	public Card_contacts(String id, String avatorUrl, String name, int chatType ,String objectId) {
		this.id = id;
		this.avatorUrl = avatorUrl;
		this.name = name;
		this.chatType = chatType;
		this.objectId = objectId;
	}
	
	public String getId(){
		return id;
	}
	
	public String getAvatorUrl(){
		return avatorUrl;
	}
	
	public String getName(){
		return name;
	}
	
	public int getChatType(){
		return chatType;
	}
	
	public String getObjectId(){
		return objectId;
	}
}
