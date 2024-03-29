package com.shoulder.xiaonei.card;

public class Card_myOrg {
	private String org_name;
	private String image_uri;
	private String org_mark;
	private int org_id;
	private int guanzhu_state;
	
	public Card_myOrg(String org_name,String image_uri,String org_mark,int org_id,int guanzhu_state){
		this.org_name=org_name;
		this.image_uri=image_uri;
		this.org_mark=org_mark;
		this.org_id=org_id;
		this.guanzhu_state = guanzhu_state;
	}
	
	public String getTitle(){
		return org_name;
	}
	public void setTitle(String short_title){
		this.org_name=short_title;
	}
	
	public String getImageUri(){
		return image_uri;
	}
	public void setImageUri(String image_uri){
		this.image_uri=image_uri;
	}
	
	public String getOrgMark(){
		return org_mark;
	}
	public void setOrgMark(String org_mark){
		this.org_mark=org_mark;
	}
	
	public int getOrgId(){
		return org_id;
	}
	public void setOrgId(int org_id){
		this.org_id=org_id;
	}
	
	public int getGuanZhuState(){
		return guanzhu_state;
	}
	public void setGuanZhuState(int guanzhu_state){
		this.guanzhu_state = guanzhu_state;
	}
	
}
