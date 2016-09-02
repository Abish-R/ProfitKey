package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Password Change AsyncTask
 *  Created by : Abish
 *  Created Dt : 3/8/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
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
import android.widget.TextView;

import general.ApplicationConfigs;
import general.EditStatusBar;

public class BookingHistoryDescription extends AppCompatActivity {
    /** Global declarations **/
    ApplicationConfigs application;
    TextView booked_date,booked_from,booked_to,booked_days,booked_amount,booked_category,booked_offer,booked_description,toolbar_title;
    Button seen_booked_history;
    private Toolbar toolbar;
    String intent_invoker,offer_title,offer_description,room_type_name,from_date,to_date,total_days,price,book_date;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.booking_history_description);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        //intent_invoker=extras.getString("intent_invoker");
        initializeViews();
        getAndSetStringFromIntent();

        seen_booked_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        customizeToolbar();
    }

    /** Initialize views*/
    private void initializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);

        booked_date=(TextView)findViewById(R.id.booked_date);
        booked_from=(TextView)findViewById(R.id.booked_from);
        booked_to=(TextView)findViewById(R.id.booked_to);
        booked_days=(TextView)findViewById(R.id.booked_days);
        booked_amount=(TextView)findViewById(R.id.booked_amount);
        booked_category=(TextView)findViewById(R.id.booked_category);
        booked_offer=(TextView)findViewById(R.id.booked_offer);
        booked_description=(TextView)findViewById(R.id.booked_description);
        seen_booked_history=(Button)findViewById(R.id.seen_booked_history);

        /** Set custom font **/
        application = (ApplicationConfigs)getApplication();
        application.setTypefaceTextViewBold(booked_date);
        application.setTypefaceTextView(booked_from);
        application.setTypefaceTextView(booked_to);
        application.setTypefaceTextView(booked_days);
        application.setTypefaceTextView(booked_amount);
        application.setTypefaceTextView(booked_category);
        application.setTypefaceTextView(booked_offer);
        application.setTypefaceTextView(booked_description);
        application.setTypefaceButton(seen_booked_history);

    }

    /** Customize toolbar*/
    private void customizeToolbar() {
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.booking_history);
        //        getSupportActionBar().setTitle("BOOKINGS HISTORY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.booking_history) + "</font>"));
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

    /** Set bundles values from booking history fragment*/
    private void getAndSetStringFromIntent(){
        Bundle extras = getIntent().getExtras();
        offer_title=extras.getString("offer_title");
        offer_description=extras.getString("offer_description");
        room_type_name=extras.getString("room_type_name");
        from_date=extras.getString("from_date");
        to_date=extras.getString("to_date");
        total_days=extras.getString("total_days");
        price=extras.getString("price");
        book_date=extras.getString("booked_date");

        if(offer_title.equals(null) || offer_title.equals("null")) {
            booked_offer.setText("Booked Offer: No Offer Selected");
            booked_description.setVisibility(View.INVISIBLE);
            //booked_description.setText("Booked Descrip: No Offer Selected");
        }
        else {
            booked_offer.setText("Booked Offer: " + offer_title);
            booked_description.setText("Booked Descrip: " + offer_description);
        }
        booked_date.setText("Booked: " + book_date);
        booked_from.setText("From:\n " + from_date);
        booked_to.setText("To:\n " + to_date);
        booked_days.setText("Days Stayed:\n " + total_days);
        booked_amount.setText("Paid Amount:\n " + price);
        booked_category.setText("Stayed In: " + room_type_name);
    }

    public void onBackPressed(){
        finish();
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
