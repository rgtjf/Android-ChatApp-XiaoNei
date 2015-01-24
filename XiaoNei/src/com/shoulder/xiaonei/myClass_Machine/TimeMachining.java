package com.shoulder.xiaonei.myClass_Machine;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.tucao.TuCaoQiang_Comment;

import android.text.format.DateFormat;
import android.util.Log;

public class TimeMachining {
	
	public static String twoDateDistance(String temp_startDate1){  
    	SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	java.util.Date d1 = null;
    	java.util.Date d2 = new java.util.Date(System.currentTimeMillis());
    	try
    	{
    	d1 = df.parse(temp_startDate1);
    	}
    	catch(Exception e){
    	}
    	if(d2==null) {
			return "d2wrong";
		} else if(d1==null){
    		return "d1wrong";
    	}
    	else{
        long timeLong = d2.getTime() - d1.getTime();  
        if (timeLong < 3*60*1000)
        	return "刚刚";
        else if (timeLong<60*60*1000){  
            timeLong = timeLong/1000 /60;  
            return timeLong + "分钟前";  
        }  
        else if (timeLong<60*60*24*1000){  
            timeLong = timeLong/60/60/1000;  
            return timeLong+"小时前";  
        }  
        else{  
            timeLong = timeLong/1000/ 60 / 60 / 24;  
            return timeLong + "天前";  
        } 
    	}
    }
	
	public static Boolean TimeTOEvaluate(String tempDate){
		SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	java.util.Date d1 = null;
    	java.util.Date d2 = new java.util.Date(System.currentTimeMillis());
    	try
    	{
    	d1 = df.parse(tempDate);
    	}
    	catch(Exception e){
    	}
    	if(d2.getTime() - d1.getTime() >0){
    		return true; //当前时间比活动时间晚了，所以活动已经过去了
    	}
    	else {
    		return false;
    	}
    	
	}
	
	public static long BeforeHand(String temp_Date){
    	SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	java.util.Date d1 = null;
    	try
    	{
    	d1 = df.parse(temp_Date);
    	}
    	catch(Exception e){
    	}
    	long timeBefore = d1.getTime() - 3*60*60*1000;
    	return timeBefore;
	}
	
	public static int getTimeRelatedId(){
		java.util.Date d2 = new java.util.Date(System.currentTimeMillis());
		return (d2.getDate()*1000000 + d2.getHours()*10000 + d2.getMinutes()*100 +d2.getSeconds());
	}
	
	public static String DateTranslator(String temp_Date){
    	SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	java.util.Date d1 = null;
    	try
    	{
    	d1 = df.parse(temp_Date);
    	}
    	catch(Exception e){
    	}
    	
    	String result="";
    	String day = "";
    	switch (d1.getDay()){
    	case 0:
    		day = "周日";break;
    	case 1:
    		day = "周一";break;
    	case 2:
    		day = "周二";break;
    	case 3:
    		day = "周三";break;
    	case 4:
    		day = "周四";break;
    	case 5:
    		day = "周五";break;
    	case 6:
    		day = "周六";break;
    	}
    	DecimalFormat mFormat= new DecimalFormat("00"); // 不足两位数用0补齐
    	result = (d1.getMonth()+1) + "月" +d1.getDate() +"日(" + day + ") " + mFormat.format(Double.valueOf(d1.getHours())) + ":" + mFormat.format(Double.valueOf(d1.getMinutes()));
    	return result;
	}
	
	
	public static int getMonth(){
		java.util.Date d1 = new java.util.Date(System.currentTimeMillis());
		return d1.getMonth();
	}
	
	
	//将服务器传来的时间变成中间有%的时间，才能get到数据
	public static String TimeTranslatorToGetJson(String temp_Date){
		
		//将服务器时间转换成date
		SimpleDateFormat df =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	java.util.Date d1 = null;
    	try
    	{
    	d1 = df.parse(temp_Date);
    	}
    	catch(Exception e){
    	}
    	
    	//将date时间转换成有%的时间
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd%20HH:mm:ss");
    	String dateString = formatter.format(d1);
    	
    	return dateString;
	}
	
	//获取当前时间，以string并且中间有%的形式
	public static String getCurrentTime(long minutes){//参数表示提前几分钟，若为0，则为当前时间
		
		//将当前时间转换成date
    	java.util.Date d1 = new java.util.Date(System.currentTimeMillis() - minutes*60*1000);
    	
    	//将date时间转换成有%的时间
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd%20HH:mm:ss");
    	String dateString = formatter.format(d1);
    	
    	return dateString;
	}
	
	
	 public static boolean haveTimeGap(long lastTime, long time) {
		    long gap = MyStatic.Chat_TimeGap;
		    return time - lastTime > gap;
	}
	 
	 public static String millisecs2DateString(long timestamp) {
		    long gap=System.currentTimeMillis()-timestamp;
		    java.util.Date d1 = new java.util.Date(timestamp);
		    DecimalFormat mFormat= new DecimalFormat("00"); // 不足两位数用0补齐
		    if(gap<1000*60*60*24){
		    	return mFormat.format(Double.valueOf(d1.getHours())) + ":" + d1.getMinutes();
		    }
		    else if(gap<1000*60*60*24*15){
		      return d1.getDate() + "日" + mFormat.format(Double.valueOf(d1.getHours())) + ":" + mFormat.format(Double.valueOf(d1.getMinutes()));
		    }
		    else{
		    	return d1.getMonth() + "月" + d1.getDate() + "日" + mFormat.format(Double.valueOf(d1.getHours())) + ":" + mFormat.format(Double.valueOf(d1.getMinutes()));
		    }
	 }
}
