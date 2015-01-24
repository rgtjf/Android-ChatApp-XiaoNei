package com.shoulder.xiaonei.card;

public class Card_shetuan_subscribe {
	
		private String org_name;
		private String image_uri;
		private String org_mark;
		private int org_id;
		private String class_name;
		private int dingyue_state;
		
		public Card_shetuan_subscribe(String org_name,String image_uri,String org_mark,int org_id,String class_name,int dingyue_state){
			this.org_name=org_name;
			this.image_uri=image_uri;
			this.org_mark=org_mark;
			this.org_id=org_id;
			this.class_name=class_name;
			this.dingyue_state = dingyue_state;
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
		
		public String getClassName(){
			return class_name;
		}
		public void setClassName(String class_name){
			this.class_name=class_name;
		}
		
		public int getDingYueState(){
			return dingyue_state;
		}
		public void setDingYueState(int dingyue_state){
			this.dingyue_state = dingyue_state;
		}
}

