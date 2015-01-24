package com.shoulder.xiaonei.card;

public class Card_group_member {

	private String id;
	private String name;
	private String photoUrl;
	
	public Card_group_member(String id,String name,String photoUrl){
		this.id = id;
		this.name = name;
		this.photoUrl = photoUrl;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPhotoUrl(){
		return photoUrl;
	}
}
