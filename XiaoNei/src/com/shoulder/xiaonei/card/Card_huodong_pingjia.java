package com.shoulder.xiaonei.card;

public class Card_huodong_pingjia {
	
	private String user_name;
	private String comment_content;
	private String comment_time;
	
	public Card_huodong_pingjia(String user_name,String comment_content,String comment_time){
		this.user_name=user_name.trim();
		this.comment_content=comment_content.trim();
		this.comment_time = comment_time;
	}
	
	public String getUserName(){
		return user_name;
	}
	public void setUserName(String user_name){
		this.user_name=user_name;
	}
	
	public String getContent(){
		return comment_content;
	}
	public void setContent(String comment_content){
		this.comment_content=comment_content;
	}
	
	public String getCommentTime(){
		return comment_time;
	}
	public void setCommentTime(String comment_time){
		this.comment_time = comment_time;
	}
}
