package com.shoulder.xiaonei.myClass_chat;

import org.json.JSONObject;

import u.aly.av;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewDebug.IntToString;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avospush.notification.NotificationCompat;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myZone.YourZone;


public class PushReceiver extends BroadcastReceiver{

	private String actionName = "";
	private String icon = "";
	private String title = "";
	private String content = "";
	private int notificationId = -1;
	
	public void onReceive(Context context, Intent intent) {
		    try {
		    	
		    	JSONObject json = new JSONObject(intent.getExtras().getString(MyStatic.KEYNAME_LeanCloudData));
		    	actionName = json.getString(MyStatic.KEYNAME_Push_Action);
		    	icon = json.getString(MyStatic.KEYNAME_Push_Icon);
		    	title = json.getString(MyStatic.KEYNAME_Push_Title);
		    	content = json.getString(MyStatic.KEYNAME_Push_Content);
		    	notificationId = json.getInt(MyStatic.KEYNAME_Push_NotificationId);
		    	
		    	//别人关注我了
		        if (actionName.equals(MyStatic.LeanCloud_Push_Action_GuanZhu)){
		        	
		        	String myId = json.getString(MyStatic.KEYNAME_SingleId);
		        	String myName = json.getString(MyStatic.KEYNAME_SingleName);
		        	
			        Intent resultIntent = new Intent(AVOSCloud.applicationContext, YourZone.class);
			        resultIntent.putExtra(MyStatic.KEYNAME_SingleId, myId);
			        
			        PendingIntent pendingIntent =
			            PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
			                PendingIntent.FLAG_UPDATE_CURRENT);
			        NotificationCompat.Builder mBuilder =
			            new NotificationCompat.Builder(AVOSCloud.applicationContext)
			                .setSmallIcon(R.drawable.icon)
			                .setContentTitle(title)
			                .setContentText(content)
			                .setTicker(title)
			                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
			        mBuilder.setContentIntent(pendingIntent);
			        mBuilder.setAutoCancel(true); 
	
			        NotificationManager mNotifyMgr =
			            (NotificationManager) AVOSCloud.applicationContext
			                .getSystemService(
			                Context.NOTIFICATION_SERVICE);
			        mNotifyMgr.notify(notificationId, mBuilder.build());
		        }
		        
		        else if(actionName.equals(MyStatic.LeanCloud_Push_Action_HuoDongTuiJian)){
		        	
		        	String get_name = json.getString("name");
		        	String get_time = json.getString("time");
		        	String get_address = json.getString("address");
		        	int get_myId = json.getInt("mId");
		        	int get_org_id = json.getInt("orgId");
		        	String get_uri = json.getString("uri");
		        	
		        	Intent resultIntent = new Intent(AVOSCloud.applicationContext, HuoDong_Detail.class);
		        	resultIntent.putExtra("name", get_name);
		        	resultIntent.putExtra("time", get_time);
		        	resultIntent.putExtra("address", get_address);
		        	resultIntent.putExtra("mId", get_myId);
		        	resultIntent.putExtra("orgId", get_org_id);
		        	resultIntent.putExtra("uri", get_uri);
		        	
			        PendingIntent pendingIntent =
				            PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
				                PendingIntent.FLAG_UPDATE_CURRENT);
				        NotificationCompat.Builder mBuilder =
				            new NotificationCompat.Builder(AVOSCloud.applicationContext)
				                .setSmallIcon(R.drawable.icon)
				                .setContentTitle(title)
				                .setContentText(content)
				                .setTicker(title)
				                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
				        mBuilder.setContentIntent(pendingIntent);
				        mBuilder.setAutoCancel(true); 
		
				        NotificationManager mNotifyMgr =
				            (NotificationManager) AVOSCloud.applicationContext
				                .getSystemService(
				                Context.NOTIFICATION_SERVICE);
				        mNotifyMgr.notify(notificationId, mBuilder.build());//以活动id为标识符
		        }
		    }
		    
		    catch (Exception e) {
		    	e.printStackTrace();
		    	}
		    
		  }
}
