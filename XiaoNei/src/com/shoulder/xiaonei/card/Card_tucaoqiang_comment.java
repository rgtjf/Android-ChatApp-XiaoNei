package com.shoulder.xiaonei.card;

public class Card_tucaoqiang_comment {
	
	private String user_name;
	private String pub_time;
	private String comment_content;
		
	public Card_tucaoqiang_comment(String user_name,String pub_time,String comment_content){
		this.user_name=user_name.trim();
		this.pub_time=pub_time;
		this.comment_content=comment_content.trim();
	}
	
	public String getUserName(){
		return user_name;
	}
	public void setUserName(String user_name){
		this.user_name=user_name;
	}
	
	public String getPubTime(){
		return pub_time;
	}
	public void setPubTime(String pub_time){
		this.pub_time=pub_time;
	}
	
	public String getContent(){
		return comment_content;
	}
	public void setContent(String comment_content){
		this.comment_content=comment_content;
	}
}
