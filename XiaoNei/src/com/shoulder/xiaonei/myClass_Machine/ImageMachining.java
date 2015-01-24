package com.shoulder.xiaonei.myClass_Machine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.java_websocket.util.Base64;
import org.java_websocket.util.Base64.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.avos.avoscloud.signature.Base64Encoder;

public class ImageMachining {

	//��ͼƬתΪbase64
    public static String getBitmapStrBase64(Bitmap bitmap){ 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bitmap.compress(CompressFormat.PNG, 100, baos); 
        byte[] bytes  = baos.toByteArray(); 
        return Base64.encodeBytes(bytes); 
    }
	
}
