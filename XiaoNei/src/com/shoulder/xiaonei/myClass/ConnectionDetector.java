package com.shoulder.xiaonei.myClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionDetector {
 
    private Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();  
              if (mNetworkInfo != null) {  
                  return mNetworkInfo.isAvailable();  
              }  
          }
          return false;
    }
}
