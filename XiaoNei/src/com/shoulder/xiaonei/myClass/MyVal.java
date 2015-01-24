package com.shoulder.xiaonei.myClass;

import javax.xml.transform.Templates;

import org.apache.http.HttpResponse;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.Session;
import com.avos.avoscloud.SessionManager;
import com.shoulder.xiaonei.card.Card_myActivity_adapter;
import com.shoulder.xiaonei.card.Card_myOrg_adapter;
import com.shoulder.xiaonei.card.Card_myTucao_adapter;

import android.app.Application;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyVal extends Application{
	
	public static MyVal mv;
	public static SessionManager session;
	
	private String ipName="http://m.sheyou.me/";
	private String imageIpName="http://sheyou.me/";
	private String imageAddress = "admin/images/";
	private String myDir="mnt/sdcard/";
	private String fileName=".sheyouApp/";
	private String fileName_ho=".sheyouApp";
	private int login_state=0; //标记登陆状态，0为未登录，1为登陆
	
	//记录个人信息
	private String schoolId = "1";
	private int mySex = 0;//0表示女，1表示男
	private String myName = "";
	private String myId = "";
	private String myIntroduce = "";
	private String myAvator = "";
	
	private Boolean initPageIsInit = false;
	
	private Card_myTucao_adapter mAdapter_myTucao;//使这两个adapter能在适配器内部
	private Card_myOrg_adapter mAdapter_myOrg;//很奇怪的用法，但没想到其它办法了..
	private Card_myActivity_adapter mAdapter_myAct;
	//受不了啊用这么不雅的方式实现，但是用public实现不了啊，以后再改吧，好难受
	private RelativeLayout clue_linear_myOrg,clue_linear_myAct,clue_linear_myTucao;
	private LinearLayout connectSuccessLayout_myOrg,connectSuccessLayout_myAct,connectSuccessLayout_myTucao;
	
	public void onCreate() {
		super.onCreate();
		
		AVOSCloud.initialize(this,"rs9i47ahgspg803e5k9zzj3l4ikdwb33d9y8txb5lsh61ctg",//appId,
				"ys1xa7pinmizf50ijf06r6wist93cnznatij2l7myxw5wb85"//appKey
				);
		
		AVOSCloud.setDebugLogEnabled(true);
		
		mv=this;//将自己赋值给static的MyVal
	}
	
	
	public static MyVal getApplication(){
		return mv;
	}

	
	
	
	
	
	
	
	
	
	//获得各种全局变量
	public String getIpName(){
		return ipName;
	}
	
	public String getImageIpName(){
		return imageIpName;
	}
	
	public String getImageAddress(){
		return imageAddress;
	}
	
	public String getFileName(){
		return fileName;
	}
	public String getFileNameHo(){
		return fileName_ho;
	}
	
	public String getMyDir(){
		return myDir;
	}
	
	public int getLoginState(){
		return login_state;
	}
	public void setLoginState(int state){
		this.login_state = state;
	}
	
	public String getSchoolId(){
		return schoolId;
	}
	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}
	
	public int getMySex(){
		return mySex;
	}
	public void setMySex(int mySex){
		this.mySex = mySex;
	}
	
	public String getMyName(){
		return myName;
	}
	public void setMyName(String myName){
		this.myName = myName;
	}
	
	public String getMyId(){
		return myId;
	}
	public void setMyId(String myId){
		this.myId = myId;
	}
	
	public String getMyIntroduce(){
		return myIntroduce;
	}
	public void setMyIntroduce(String myIntroduce){
		this.myIntroduce = myIntroduce;
	}
	
	public Boolean getInitPageIsInit(){
		return initPageIsInit;
	}
	public void setInitPageIsInit(Boolean initPageIsInit){
		this.initPageIsInit = initPageIsInit;
	}
	
	public String getMyAvator(){
		return myAvator;
	}
	public void setMyAvator(String myAvator){
		this.myAvator = myAvator;
	}
	
	
	
	
	
	
	
	
	
	
	//关于个人中心那些长按操作的不优雅的解决方法
	public Card_myTucao_adapter getMyTucaoAdapter(){
		return mAdapter_myTucao;
	}
	public void setMyTucaoAdapter(Card_myTucao_adapter temp_adapter){
		this.mAdapter_myTucao = temp_adapter;
	}
	
	public Card_myOrg_adapter getMyOrgAdapter(){
		return mAdapter_myOrg;
	}
	public void setMyOrgAdapter(Card_myOrg_adapter temp_org_adapter){
		this.mAdapter_myOrg = temp_org_adapter;
	}
	
	public Card_myActivity_adapter getMyActAdapter(){
		return mAdapter_myAct;
	}
	public void setMyActAdapter(Card_myActivity_adapter temp_act_dapter){
		this.mAdapter_myAct = temp_act_dapter;
	}
	
	public RelativeLayout getClue(int pageIndex){
		if(pageIndex == 0) return clue_linear_myOrg;
		if(pageIndex == 1) return clue_linear_myAct;
		if(pageIndex == 2) return clue_linear_myTucao;
		return clue_linear_myOrg;
	}
	public void setClue(int pageIndex , RelativeLayout clue){
		if(pageIndex == 0)
			this.clue_linear_myOrg = clue;
		else if(pageIndex == 1)
			this.clue_linear_myAct = clue;
		else if(pageIndex == 2)
			this.clue_linear_myTucao = clue;
	}
	
	public LinearLayout getConnectSuccessLayout(int pageIndex){
		if(pageIndex == 0)
			return connectSuccessLayout_myOrg;
		if(pageIndex == 1)
			return connectSuccessLayout_myAct;
		if(pageIndex == 2)
			return connectSuccessLayout_myTucao;
		return connectSuccessLayout_myOrg;
	}
	public void setConnectSuccessLayoutMyOrg(int pageIndex , LinearLayout connectSuccessLayout){
		if(pageIndex == 0)
			this.connectSuccessLayout_myOrg = connectSuccessLayout;
		else if(pageIndex == 1)
			this.connectSuccessLayout_myAct = connectSuccessLayout;
		else if(pageIndex == 2)
			this.connectSuccessLayout_myTucao = connectSuccessLayout;
	}
	
}
