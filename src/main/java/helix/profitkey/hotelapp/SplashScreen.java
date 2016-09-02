package helix.profitkey.hotelapp;

/** Profit Key 1.0.0
 *  Purpose	   : Splash Screen
 *  Created by : Abish
 *  Created Dt : 05-03-2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import general.EditStatusBar;

public class SplashScreen extends AppCompatActivity {
EditStatusBar esb = new EditStatusBar(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = this.getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.green));
//        }
        setContentView(R.layout.splash_screen);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        /**Getting Username*/
        SharedPreferences sp=getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        final String username = sp.getString("profit_key_user_id", "");
        Log.e("Username", username);
//        Log.d(status_login, status_user);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);    /** Thread will sleep for 3 seconds*/

                    if(username!=""){
                        /**Starts Main Screen*/
                        Intent i=new Intent(getBaseContext(),SimpleTabsActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        /**Starts SignIn&UP Controller Screen*/
                        Intent i=new Intent(getBaseContext(),Login.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        };
        background.start();     /** start thread*/

    }

    /**Set color to statusbar*/
    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            //int actionBarHeight = getActionBarHeight();
            int statusBarHeight = esb.getStatusBarHeight();
            //action bar height
            statusBar.getLayoutParams().height = statusBarHeight;// + actionBarHeight ;
            statusBar.setBackgroundColor(color);
        }
    }


}
