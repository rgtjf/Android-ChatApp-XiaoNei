package com.shoulder.xiaonei.myClass_Machine;

import com.shoulder.xiaonei.R;
import com.shoulder.xiaonei.main.HuoDong_Detail;
import com.shoulder.xiaonei.others.Login;
import com.shoulder.xiaonei.others.Register_Page1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Dialog_Register_Login {

	private static Dialog myDialog_login_and_register_inclass;
	private static Button login;
	private static Button register;
	
	public static void Set_Dialog_Register_Login(final Context ac,final String where_come_from){
		myDialog_login_and_register_inclass=new Dialog(ac, R.style.dialog_login);
		myDialog_login_and_register_inclass.setContentView(R.layout.dialog_login_register);
		login=(Button)myDialog_login_and_register_inclass.findViewById(R.id.login);
		register=(Button)myDialog_login_and_register_inclass.findViewById(R.id.register);

		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
	    		Intent intent = null;
				if(v == login){
					intent = new Intent(ac, Login.class);
				}else if(v == register){
					intent = new Intent(ac, Register_Page1.class);
				}
				intent.putExtra("where_come_from", where_come_from);
				ac.startActivity(intent);
				myDialog_login_and_register_inclass.dismiss();
				
			}
		};
		login.setOnClickListener(listener);
		register.setOnClickListener(listener);
	}
	
	public static Dialog getMyDialog(){
		return myDialog_login_and_register_inclass;
	}
	
}
