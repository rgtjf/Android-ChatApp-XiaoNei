package com.shoulder.xiaonei.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.myClass.BitmapUtil;
import com.shoulder.xiaonei.myClass.DragImageView;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Image_HuoDong extends Activity {
	private int window_width, window_height;// 控件宽度
	private DragImageView dragImageView;// 自定义控件
	private int state_height;// 状态栏的高度
	private ViewTreeObserver viewTreeObserver;
	
	private Button left_btn;
	private ImageView right_image;
	private Button right_btn;
	private TextView text;
	
	private String myDir="";
	
	private MyVal mv;
	
	private String get_titleText = "";
	private String get_imageUrl = "";
	private String thisUri = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.image_full);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        Bundle extras=getIntent().getExtras();
        get_titleText=extras.getString(MyStatic.KEYNAME_TitleText);
        get_imageUrl=extras.getString(MyStatic.KEYNAME_Url);
        text.setText(get_titleText);
        thisUri=myDir+mv.getFileName()+get_imageUrl;
        
        mv=((MyVal)getApplicationContext());
        myDir=mv.getMyDir();
        
        initTitle();
        initImage();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        right_image=(ImageView)findViewById(R.id.function_image);
        right_btn=(Button)findViewById(R.id.right_btn);
        right_image.setImageResource(R.drawable.download);
        left_btn.setOnClickListener(new ButtonBack());
        right_btn.setOnClickListener(new ButtonDownLoad());
        text=(TextView)findViewById(R.id.text);
	}
	
	private void initImage(){
        WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		dragImageView = (DragImageView)findViewById(R.id.image);
		Bitmap bmp = null;
		bmp = BitmapUtil.ReadBitmapByUri(thisUri, window_width, window_height);
		// 设置图片
		dragImageView.setImageBitmap(bmp);
		dragImageView.setmActivity(this);//注入Activity.
		/** 测量状态栏高度 **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							dragImageView.setScreen_H(window_height-state_height);
							dragImageView.setScreen_W(window_width);
						}

					}
				});
	}
	
	
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	
	private void GoBack(){
		finish();
	}
	class ButtonBack implements OnClickListener{
		public void onClick(View arg0) {
			GoBack();
		}
	}
	public void onBackPressed() {
		GoBack();
	}
	
	class ButtonDownLoad implements OnClickListener{
		public void onClick(View v) {
			try{
		    	File fileOld = new File(mv.getMyDir()+mv.getFileName(), get_imageUrl);
		    	File fileNew = new File("mnt/sdcard/DCIM/100MEDIA/","sheyouImage"+ get_imageUrl);
		    	if(!fileNew.exists()) fileNew.createNewFile();
		    	else {
		    		Toast.makeText(Image_HuoDong.this, getString(R.string.image_HuoDong_download_hasSave), Toast.LENGTH_SHORT).show();
		    		return;
		    	}
		    	
		    	FileInputStream is = new FileInputStream(fileOld);
		    	FileOutputStream fos = new FileOutputStream(fileNew);
				byte[] buff = new byte[1024];
				int hasRead = 0;
				while((hasRead = is.read(buff)) > 0)
					{
						fos.write(buff, 0 , hasRead);
					}
				is.close();
				fos.close();
				Toast.makeText(Image_HuoDong.this, getString(R.string.image_HuoDong_download_done), Toast.LENGTH_SHORT).show();
			}
			catch (Exception e){
			}
		}
	}
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	

}
