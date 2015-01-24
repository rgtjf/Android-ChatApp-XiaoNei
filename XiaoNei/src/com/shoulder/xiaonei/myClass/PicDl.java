package com.shoulder.xiaonei.myClass;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.shoulder.xiaonei.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class PicDl extends Activity{
	ImageView show;
	// 代表从网络下载得到的图片
	Bitmap bitmap;
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 0x123)
			{
				// 使用ImageView显示该图片
				show.setImageBitmap(bitmap);
			}
		}
	};
	public void pic()
	{
		show = (ImageView) findViewById(R.id.Card_picture);
		new Thread()
		{
			public void run()
			{
				try
				{
					// 定义一个URL对象
					URL url = new URL("http://www.baidu.com/img/" + "baidu_sylogo1.gif");
					// 打开该URL对应的资源的输入流
					InputStream is = url.openStream();
					// 从InputStream中解析出图片
					bitmap = BitmapFactory.decodeStream(is);
					// 发送消息、通知UI组件显示该图片
					handler.sendEmptyMessage(0x123);
					is.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

}
