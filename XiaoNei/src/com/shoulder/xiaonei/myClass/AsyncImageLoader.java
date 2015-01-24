package com.shoulder.xiaonei.myClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import com.shoulder.xiaonei.myClass_myView.CircularImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/***
 * 异步加载图片 缓存的实现
 */ 
public class AsyncImageLoader { 
    // 软引用 
    private HashMap<String, SoftReference<Drawable>> imageCache; 
    
    private static MyVal mv;
    private Context mContext;
    
    public AsyncImageLoader(Context context) { 
        imageCache = new HashMap<String, SoftReference<Drawable>>(); 
        mContext = context;
        mv = (MyVal)mContext.getApplicationContext();
    } 
 
    /***
     * 下载图片
     * 
     * @param imageUrl
     *            图片地址
     * @param imageCallback
     *            回调接口
     * @return
     */ 
    public Drawable loadDrawable(final String imageUrl, 
            final ImageCallback imageCallback) { 
        if (imageCache.containsKey(imageUrl)) { 
            SoftReference<Drawable> softReference = imageCache.get(imageUrl); 
            Drawable drawable = softReference.get(); 
            if (drawable != null) { 
                return drawable; 
            } 
        } 
        final Handler handler = new Handler() { 
            public void handleMessage(Message message) { 
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl); 
            } 
        }; 
        // 开启线程下载图片 
        new Thread() { 
            public void run() { 
                Drawable drawable = loadImageFromUrl(imageUrl); 
                // 将下载的图片保存至缓存中 
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable)); 
                Message message = handler.obtainMessage(0, drawable); 
                handler.sendMessage(message); 
            } 
        }.start(); 
        return null; 
    } 
 
    /***
     * 根据URL下载图片（这里要进行判断，先去本地sd中查找，没有则根据URL下载，有则返回该drawable）
     * 
     * @param url
     * @return
     */ 
    public static Drawable loadImageFromUrl(String imageURL) { 
        Drawable drawable = getDrawable(imageURL);
        return drawable; 
    } 
    
    public static Drawable getDrawable(String imageURL){
    	File file = new File(mv.getMyDir()+mv.getFileName(), imageURL);
    	if(file.exists()){
    		return Drawable.createFromPath(file.getAbsolutePath());
    	}
    	else{
	    	try {
	    		String u = mv.getImageIpName()+mv.getImageAddress()+imageURL;
		    	URL url = new URL(u);
		    	InputStream is = url.openStream();
		    	if(!file.exists()) file.createNewFile();
		    	FileOutputStream fos = new FileOutputStream(file);
				byte[] buff = new byte[1024];
				int hasRead = 0;
				while((hasRead = is.read(buff)) > 0)
					{
						fos.write(buff, 0 , hasRead);
					}
				is.close();
				fos.close();
//				return Drawable.createFromStream(is, "src");
				return Drawable.createFromPath(file.getAbsolutePath());
		    	}
	    	catch (Exception e) {
	    	}
    	}
    	return null;
    }

    // 回调接口 
    public interface ImageCallback { 
        public void imageLoaded(Drawable imageDrawable, String imageUrl); 
    } 
    
    
    public static Drawable getImageViewDrawable(AsyncImageLoader asyncImageLoader, 
            String imageUrl, final ImageView imageView){
        Drawable drawable = asyncImageLoader.loadDrawable(imageUrl, 
                new ImageCallback() { 
                    public void imageLoaded(Drawable imageDrawable, 
                            String imageUrl) { 
                        if (imageDrawable != null && imageView.getTag().equals(imageUrl)) {  
                        	imageView.setImageDrawable(imageDrawable);  
                        } 
                    } 
                }); 
        return drawable; 
    }
    public static Drawable getImageViewDrawable(AsyncImageLoader asyncImageLoader, 
            String imageUrl, final CircularImage imageView){
        Drawable drawable = asyncImageLoader.loadDrawable(imageUrl, 
                new ImageCallback() { 
                    public void imageLoaded(Drawable imageDrawable, 
                            String imageUrl) { 
                        if (imageDrawable != null && imageView.getTag().equals(imageUrl)) {  
                        	imageView.setImageDrawable(imageDrawable);  
                        } 
                    } 
                }); 
        return drawable; 
    }
    
} 