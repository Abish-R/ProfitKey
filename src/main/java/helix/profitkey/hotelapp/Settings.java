package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Settings Screen
 *  Created by : Abish
 *  Created on : 4/23/2016
 *  Modified on: on back button press handled
 *  Verified by:
 *  Verified Dt:
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import fragments.EmailChangeFragment;
import fragments.MobileChangeFragment;
import fragments.PasswordResetFragment;
import fragments.ProfilePictureChangeFragment;
import fragments.SettingsButtonFragment;
import general.ApplicationConfigs;
import general.EditStatusBar;

public class Settings extends AppCompatActivity {
    /** Global Declarations*/
    //Fragment fragment_communicator;
    ApplicationConfigs application;
    private Toolbar toolbar;
    private TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.settings);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        initializeViews();
        editToolBar();
        callToChangeFragments(0);

    }

    /** Initialize views*/
    private void initializeViews(){
        application = (ApplicationConfigs)getApplication();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar_title =(TextView)toolbar.findViewById(R.id.toolbar_title);

        application.setTypefaceTextView(toolbar_title);
    }

    /** Customize Toolbar*/
    private void editToolBar(){
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.settings);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.settings) + "</font>"));
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

    /** Handle the fragment to view and hide and close the activity on back button press*/
    public void onBackPressed(){
//        int fragments = getFragmentManager().getBackStackEntryCount();
        SettingsButtonFragment myFragment = (SettingsButtonFragment)getFragmentManager().findFragmentByTag("Main_Fragment");
        if (myFragment != null && myFragment.isVisible()) {
            super.onBackPressed();
        }else
            callToChangeFragments(0);
//        if(fragments<1)
//            super.onBackPressed();
//        else {
//            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_communicator)).commit();
//            callToChangeFragments(0);
//        }
    }

    /** Change the fragment when it called*/
    public void callToChangeFragments(int v){
        Fragment fr=null;
        SettingsButtonFragment fr1 = new SettingsButtonFragment();
        if(v==0){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_communicator, fr1, "Main_Fragment");
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(v == 1) {
            fr = new PasswordResetFragment();
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.password_change) + "</font>"));
        }
        else if(v==2) {
            fr = new MobileChangeFragment();
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.mobile_change) + "</font>"));
        }
        else if(v==3) {
            fr = new EmailChangeFragment();
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.email_change) + "</font>"));
        }
        else if(v==4) {
            fr = new ProfilePictureChangeFragment();
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.prof_pic_change) + "</font>"));
        }
        if(v!=0) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //fragmentTransaction.remove(fr1);
            fragmentTransaction.replace(R.id.fragment_communicator, fr);
            fragmentTransaction.addToBackStack("Main_Fragment");
            //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.commit();
        }
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
