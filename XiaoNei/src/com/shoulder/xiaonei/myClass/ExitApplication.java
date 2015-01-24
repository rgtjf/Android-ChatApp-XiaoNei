package com.shoulder.xiaonei.myClass;

import java.util.LinkedList;
import java.util.List;

import com.shoulder.xiaonei.myClass.DragImageView.MyAsyncTask;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Application;

public class ExitApplication extends Application {


	private List<Activity> activityList=new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication(){}
	
	//单例模式中获取唯一的ExitApplication 实例
	public static ExitApplication getInstance()
	{
		if(null == instance){
			instance = new ExitApplication();
		}
	return instance;
	}
	
	
	//添加Activity 到容器中
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	//遍历所有Activity 并finish
	public void exit(){
		try{
			for(Activity activity:activityList){
				activity.finish();
			}
			if(MyVal.session != null)
				MyVal.session.close();//退出应用时关闭session
			MobclickAgent.onKillProcess(MyVal.getApplication());//保存友盟的统计数据
			System.exit(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
