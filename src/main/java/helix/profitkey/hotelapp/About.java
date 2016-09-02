package helix.profitkey.hotelapp;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : About Page
 *  Created by : Abish
 *  Created Dt : 4/23/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import general.ApplicationConfigs;
import general.EditStatusBar;

public class About extends AppCompatActivity {
    /** Global declarations*/
    ApplicationConfigs application;
    private Toolbar toolbar;
    private TextView toolbar_title,text1,text2,text3,text4,text5,text6;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.about);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        initializeViews();
        editToolBar();
    }

    /**View Initialize*/
    private void initializeViews(){
        application = (ApplicationConfigs)getApplication();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar_title =(TextView)toolbar.findViewById(R.id.toolbar_title);
        text1 =(TextView)findViewById(R.id.text1);
        text2 =(TextView)findViewById(R.id.text2);
        text3 =(TextView)findViewById(R.id.text3);
        text4 =(TextView)findViewById(R.id.text4);
        text5 =(TextView)findViewById(R.id.text5);
        text6 =(TextView)findViewById(R.id.text6);

        application.setTypefaceTextView(toolbar_title);
        application.setTypefaceTextView(text1);
        application.setTypefaceTextView(text2);
        application.setTypefaceTextView(text3);
        application.setTypefaceTextView(text4);
        application.setTypefaceTextView(text5);
        application.setTypefaceTextView(text6);
    }

    /**Customize toolbar*/
    private void editToolBar(){
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.about);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.about) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**Set color to statusbar*/
    public void setStatusBarColor(View statusBar,int color){
        EditStatusBar esb = new EditStatusBar(this);
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
