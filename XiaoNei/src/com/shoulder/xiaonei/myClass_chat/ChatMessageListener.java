package com.shoulder.xiaonei.myClass_chat;

import com.avos.avoscloud.AVMessage;


public interface ChatMessageListener {
	
	public void onMessage(AVMessage av);
	
	public void onMessageFailure(AVMessage av);

	public void onMessageSent(AVMessage av);

}
