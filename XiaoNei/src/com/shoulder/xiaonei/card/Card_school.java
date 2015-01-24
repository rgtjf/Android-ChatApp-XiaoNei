package com.shoulder.xiaonei.card;

public class Card_school {
	
	private String schoolId;
	private String schoolName;
	private String schoolBranch;
	
	public Card_school(String schoolId,String schoolName,String schoolBranch){
		this.schoolId = schoolId.trim();
		this.schoolName = schoolName.trim();
		this.schoolBranch = schoolBranch.trim();
	}
	
	public String getSchoolId(){
		return schoolId;
	}
	
	public String getSchoolName(){
		return schoolName;
	}
	
	public String getSchoolBranch(){
		return schoolBranch;
	}
}
