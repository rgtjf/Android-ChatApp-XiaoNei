package com.shoulder.xiaonei.myClass_chat;

import java.util.HashMap;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import chat.Chat;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVMessageReceiver;
import com.avos.avoscloud.Session;
import com.avos.avospush.notification.NotificationCompat;
import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.db.DB_ChatLast_Helper;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.tencent.a.b.f;

public class ChatMsgReceiver extends AVMessageReceiver{
	
	public static HashMap<String, ChatMessageListener> singleMessageDispatchers =
		      new HashMap<String, ChatMessageListener>();
	
	@Override
	public void onError(Context arg0, Session arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context context, Session session, AVMessage avMessage) {
		ChatMessageListener listener = singleMessageDispatchers.get(avMessage.getFromPeerId());
		HashMap<String, Object> params = JSON.parseObject(avMessage.getMessage(), HashMap.class);
		final String objectId = avMessage.getFromPeerId();
		final String content = (String)params.get(MyStatic.KEYNAME_Content);
		final String name = (String)params.get(MyStatic.KEYNAME_MESSAGE_NAME);
		
		if (listener == null){
			//加入数据库，定义为未读
			try{
				Log.i("kkk", "db_01");
				DB_ChatLast_Helper dbHelper = new DB_ChatLast_Helper(MyVal.getApplication());
				Log.i("kkk", "db_02");
				ContentValues cv = new ContentValues();
				Log.i("kkk", "db_03");
				cv.put(MyStatic.DB_fieldObjectId, objectId);
				Log.i("kkk", "db_04");
				cv.put(MyStatic.DB_fieldHaveRead, MyStatic.DB_HaveNotRead);
				Log.i("kkk", "db_05");
				cv.put(MyStatic.DB_fieldLastMessage, content);
				Log.i("kkk", "db_06");
				//更新操作，如果影响的数据只有0条，说明数据库中没有，则进行插入操作
//				int affect = dbHelper.updataLastMessage(objectId, cv);
//				Log.i("kkk", "db_07");
//				if(affect == 0){
					dbHelper.insert(cv);
//					Log.i("kkk", "insertSuccess");
//				}
				Log.i("kkk", "updateSuccess");
				dbHelper.close();
			}
			catch(Exception e){
				Log.i("kkk", "ChatMsgMReceiver_" + e.getMessage());
			}
			
			//聊天的推送
	        NotificationManager nm =
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	        Intent resultIntent = new Intent(context, Chat.class);
	        resultIntent.putExtra(MyStatic.KEYNAME_ChatType,MyStatic.CHATTYPE_SINGLE);
	        resultIntent.putExtra(MyStatic.KEYNAME_SingleName, name);
	        resultIntent.putExtra(MyStatic.KEYNAME_SingleId, avMessage.getFromPeerId());
	        resultIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

	        PendingIntent pi =
	            PendingIntent.getActivity(context, -1, resultIntent, PendingIntent.FLAG_ONE_SHOT);

	        Notification notification =
	            new NotificationCompat.Builder(context)
	                .setContentTitle(name)
	                .setContentText(content)
	                .setContentIntent(pi)
	                .setSmallIcon(R.drawable.icon)
//	                .setLargeIcon(
//	                		BitmapFactory.decodeResource(context.getResources(), R.drawable.test_2))
	                .setAutoCancel(true).build();
	        nm.notify(233, notification);
		}
		else{
			Log.i("kkk","onMessage");
			listener.onMessage(avMessage);
		}
	}

	@Override
	public void onMessageDelivered(Context arg0, Session arg1, AVMessage arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMessageFailure(Context arg0, Session arg1, AVMessage avMessage) {
		// TODO Auto-generated method stub
		ChatMessageListener listener = singleMessageDispatchers.get(avMessage.getToPeerIds().get(0));
		if(listener != null){
			listener.onMessageFailure(avMessage);
		}
	}

	@Override
	public void onMessageSent(Context context, Session session, AVMessage avMessage) {
		Log.i("kkk","chat_sent");
		ChatMessageListener listener = singleMessageDispatchers.get(avMessage.getToPeerIds().get(0));
		if(listener != null){
			listener.onMessageSent(avMessage);
		}
		else{
		}
	}

	@Override
	public void onPeersUnwatched(Context arg0, Session arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeersWatched(Context context, Session session, List<String> list) {
//	    Intent i = new Intent(context, Chat.class);
//	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//	    context.startActivity(i);
		Log.i("kkk","chat_watched");
	}

	@Override
	public void onSessionOpen(Context arg0, Session arg1) {
		// TODO Auto-generated method stub
		Log.i("kkk", "sessionOpen");
	}

	@Override
	public void onSessionPaused(Context arg0, Session arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSessionResumed(Context arg0, Session arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusOffline(Context arg0, Session arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusOnline(Context arg0, Session arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void registerSingleListener(String fromPeerId, ChatMessageListener listener) {
		singleMessageDispatchers.put(fromPeerId, listener);
	}	

	public static void unregisterSingleListener(String fromPeerId) {
		singleMessageDispatchers.remove(fromPeerId);
	}
}
