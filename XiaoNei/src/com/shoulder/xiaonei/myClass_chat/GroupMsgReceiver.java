package com.shoulder.xiaonei.myClass_chat;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import chat.Chat;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVGroupMessageReceiver;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.Group;
import com.avos.avoscloud.LogUtil;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;

public class GroupMsgReceiver extends AVGroupMessageReceiver{
	
	public static HashMap<String, ChatMessageListener> groupMessageDispatchers =
		      new HashMap<String, ChatMessageListener>();

	@Override
	public void onError(Context arg0, Group arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInviteToGroup(Context arg0, Group arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvited(Context arg0, Group arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoined(Context context, Group group) {
		// TODO Auto-generated method stub
//	    Intent i = new Intent(context, Chat.class);
//	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//	    context.startActivity(i);
		Log.i("kkk", "group_joined");
	}

	@Override
	public void onKicked(Context arg0, Group arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMemberJoin(Context arg0, Group arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMemberLeft(Context arg0, Group arg1, List<String> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context context, Group group, AVMessage message) {
		ChatMessageListener listener = groupMessageDispatchers.get(group.getGroupId());
		if(listener==null){
		}
		else {
			listener.onMessage(message);
			Log.i("kkk", "group_onMessage");
		}
	}

	@Override
	public void onMessageFailure(Context context, Group group, AVMessage avMessage) {
		ChatMessageListener listener = groupMessageDispatchers.get(group.getGroupId());
		if(listener != null){
			listener.onMessageFailure(avMessage);
		}
	}

	@Override
	public void onMessageSent(Context context, Group group, AVMessage avMessage) {
		ChatMessageListener listener = groupMessageDispatchers.get(group.getGroupId());
		if(listener != null){
			listener.onMessageSent(avMessage);
		}
		Log.i("kkk", "group_messageSent"); 
	}

	@Override
	public void onQuit(Context arg0, Group arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject(Context arg0, Group arg1, String arg2,
			List<String> arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void registerGroupListener(String groupId, ChatMessageListener listener) {
		    groupMessageDispatchers.put(groupId, listener);
	}

	public static void unregisterGroupListener(String groupId) {
		    groupMessageDispatchers.remove(groupId);
	}
}
