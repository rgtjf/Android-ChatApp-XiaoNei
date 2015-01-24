package com.shoulder.xiaonei.others;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.Image_HuoDong;
import com.shoulder.xiaonei.main.Main;
import com.shoulder.xiaonei.myClass.AsyncImageLoader;
import com.shoulder.xiaonei.myClass.CustomerHttpClient;
import com.shoulder.xiaonei.myClass.MyStatic;
import com.shoulder.xiaonei.myClass.MyVal;
import com.shoulder.xiaonei.myClass_Machine.ImageMachining;
import com.shoulder.xiaonei.myClass_myView.CircularImage;
import com.shoulder.xiaonei.others.Set_Up.ButtonBack;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInformation extends Activity{
	
	private Button left_btn;
	private RelativeLayout right_rel;
	private TextView text;
	
    private CircularImage myAvatar;
    private Button changeAvatar;
    private AsyncImageLoader asyncImageLoader;
    
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.my_information);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2model);
        
        initTitle();
        initWidgets();
	}
	
	
	private void initTitle(){
        left_btn=(Button)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new ButtonBack());
        right_rel=(RelativeLayout)findViewById(R.id.right_rel);
        right_rel.setVisibility(4);
        text=(TextView)findViewById(R.id.text);
        text.setText("个人设置");
	}
	
	private void initWidgets(){
		asyncImageLoader = new AsyncImageLoader(MyInformation.this);
		
		myAvatar = (CircularImage)findViewById(R.id.my_avator);
		changeAvatar = (Button)findViewById(R.id.change_avator);
		
        //图片缓存
		final String imageUrl = MyVal.getApplication().getMyAvator();
		myAvatar.setTag(imageUrl);
        myAvatar.setImageResource(R.drawable.fill_color);
        Drawable drawable = AsyncImageLoader.getImageViewDrawable(asyncImageLoader, imageUrl, 
        		myAvatar); 
        //说明此图片已被下载并缓存
        if(drawable!=null)
        	myAvatar.setImageDrawable(drawable); 
        
        myAvatar.setOnClickListener(new ImageGoFull());//点击头像可以看大图
        changeAvatar.setOnClickListener(new ButtonChangeAvatar());//更换头像
	}
	
	class ImageGoFull implements OnClickListener{
		public void onClick(View v) {
			Intent intent = new Intent(MyInformation.this, Image_HuoDong.class);
			intent.putExtra(MyStatic.KEYNAME_TitleText, MyVal.getApplication().getMyName());
			intent.putExtra(MyStatic.KEYNAME_Url, MyVal.getApplication().getMyAvator());
			startActivity(intent);
		}
	}
	
	class ButtonChangeAvatar implements OnClickListener{
		public void onClick(View v) {
            Intent intent = new Intent();    
            /* 开启Pictures画面Type设定为image */    
            intent.setType("image/*");    
            /* 使用Intent.ACTION_GET_CONTENT这个Action */    
            intent.setAction(Intent.ACTION_GET_CONTENT);     
            /* 取得相片后返回本画面 */    
            startActivityForResult(intent, 1);    
		}
	}
	
	private void GoBack(){
		finish();
	}
	class ButtonBack implements OnClickListener{
		public void onClick(View arg0) {
			GoBack();
		}
	}
	public void onBackPressed(){
		GoBack();
	}
	
	
	//用户选择好头像后返回的信息
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
        if (requestCode == 1 && resultCode == RESULT_OK) {    
            final Uri uri = data.getData();    
            Log.e("kkk", uri.toString());    
            final ContentResolver cr = this.getContentResolver();
			try {
				final Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				/* 将Bitmap设定到ImageView */  
				myAvatar.setImageBitmap(bitmap);
				
	            new Thread(new Runnable() {
					public void run() {
						try{
							String image64 = ImageMachining.getBitmapStrBase64(bitmap);
							Log.i("kkk", image64);
							
							String getName="manage/upload_photo";
							NameValuePair param1 = new BasicNameValuePair("img", image64);
							String getResponse = CustomerHttpClient.post(MyVal.getApplication().getIpName()+getName, param1);
							Log.i("kkk", getResponse);
							Looper.prepare();
							Toast.makeText(MyInformation.this, getResponse, Toast.LENGTH_SHORT).show();
							Looper.loop();
						}
						catch (Exception e){
							e.printStackTrace();
						}
					}
				}).start();
			}
			catch (Exception e){
				e.printStackTrace();
			}
            
        }    
        super.onActivityResult(requestCode, resultCode, data);    
    }    
    
	
}
