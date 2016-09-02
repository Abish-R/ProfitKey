package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Review Detailed description with image
 *  Created by : Abish
 *  Created on :
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import general.ApplicationConfigs;
import general.EditStatusBar;

public class ReviewDescription extends AppCompatActivity {
    /** Global declaration*/
    ApplicationConfigs application;
    private Toolbar toolbar;
    private TextView toolbar_title,review_date,review_descrip_title,review_descrip_display,reviewer_name;
    ImageView review_image_captured;
    Button review_desrip_ok;
    String rev_date,rev_tit,rev_descrip,rev_img_url,rev_name;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.review_description);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        initializeViews();
        getValueFromBundle();
        new AsyncReviewImageDownload(this).execute(rev_img_url, "review_description");

        setCustomToolbar();
        setValuesInTextView();
    }

    /** View Initializer*/
    private void initializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        review_date = (TextView)findViewById(R.id.review_date);
        review_descrip_title = (TextView)findViewById(R.id.review_descrip_title);
        review_descrip_display = (TextView)findViewById(R.id.review_descrip_display);
        reviewer_name = (TextView)findViewById(R.id.reviewer_name);

        review_image_captured = (ImageView)findViewById(R.id.review_image_captured);;
        review_desrip_ok = (Button)findViewById(R.id.review_desrip_ok);

        /** Set Custom font*/
        application = (ApplicationConfigs)getApplication();
        application.setTypefaceTextView(toolbar_title);
        application.setTypefaceTextView(review_date);
        application.setTypefaceTextView(review_descrip_title);
        application.setTypefaceTextView(review_descrip_display);
        application.setTypefaceTextView(reviewer_name);
        application.setTypefaceButton(review_desrip_ok);

        review_desrip_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** Customize toolbar*/
    private void setCustomToolbar(){
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.review_detail);
        //getSupportActionBar().setTitle("Review");
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.review_detail) + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /** Get value from bundle which is passed by review fragment*/
    private void getValueFromBundle(){
        Bundle extras = getIntent().getExtras();
        rev_date = extras.getString("review_dt");
        rev_tit = extras.getString("review_tit");
        rev_descrip = extras.getString("review_descrip");
        rev_img_url = extras.getString("review_img_url");
        rev_name = extras.getString("reviewer_name");
    }

    /** Set values in view */
    private void setValuesInTextView(){
        review_date.setText(rev_date);
        review_descrip_title.setText(rev_tit);
        review_descrip_display.setText(rev_descrip);
        reviewer_name.setText(rev_name);
    }

    /** Set downloaded image */
    public void setImage(Bitmap image){
        review_image_captured.setImageBitmap(image);
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
