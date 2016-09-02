package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Booking Details display Screen
 *  Created by : Abish
 *  Created Dt : 3/10/2016
 *  Modified on: Changed from confirm booking to booking details
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import fragments.ImagePagerFragment;
import general.AlertMessages;
import general.ApplicationConfigs;
import general.EditStatusBar;
import general.StaticConstants;

public class Booking extends AppCompatActivity implements View.OnClickListener{
        //DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    /** Global declarations **/
    AlertMessages am = new AlertMessages(this);
    ApplicationConfigs application;
    private Toolbar toolbar;
    TextView room_type,description,validy,validity_from,validity_to,inclusion,food_option,event_option,min_los;// for offers
    TextView toolbar_title,title,offer_price,tax,room_description,fan,ac,fridge,tv,bar1,wifi,complement,bed,guest,rm_srvc,restaurant;//Common
    Button confirm_booking;     //Common
    LinearLayout offer_layout,offer_spl_layout,event_option_layout,food_option_layout,inclusion_layout;//Specially for offers
    LinearLayout fan_layout,ac_layout,fridge_layout,tv_layout,bar_layout,wifi_layout,complement_layout,//Common
            bed_layout,guest_layout,rm_srvc_layout,restaurant_layout;      //Common
    ViewPager viewPager;        //Common
    PageIndicator mIndicator;

    String offer_valid_from,offer_valid_to,offer_id,room_type_name,room_type_id;
    String intent_invoker,price_per_day,room_description1,complement1,bed1,allowed_guest;
    int length_of_stay,avail_room_today,avail_restaurant,fan1,ac1,fridge1,tv1,bar11,wifi1,room_service,tax1;
    String urlArray[];

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.booking);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        /** Bundle values from offer page and available rooms page common values **/
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            intent_invoker = extras.getString("intent_invoker");
            //tax1 = extras.getInt("tax");
            room_description1=extras.getString("room_description");
            complement1=extras.getString("complement");
            bed1=extras.getString("bed");
            allowed_guest=extras.getString("allowed_guest");
            avail_restaurant = extras.getInt("avail_restaurant");
            fan1=extras.getInt("fan");
            ac1=extras.getInt("ac");
            fridge1=extras.getInt("fridge");
            tv1=extras.getInt("tv");
            bar11=extras.getInt("bar");
            wifi1=extras.getInt("wifi");
            room_service=extras.getInt("room_service");
        }
        else
            intent_invoker="";

        /** Bundle values from offer page **/
        if(intent_invoker.equals("offer_page")){
            initializeViews();
            initializeViewsForOfferBooking();
            String offer_title,offer_description,inclusions,food_options,event_options;

            offer_title = extras.getString("offer_title");
            offer_description = extras.getString("offer_description");
            offer_valid_from = extras.getString("offer_from");
            offer_valid_to = extras.getString("offer_to");
            price_per_day = extras.getString("offer_price");  //offer price
            room_type_name = extras.getString("room_type_name");
            offer_id = extras.getString("offer_id");
            length_of_stay = extras.getInt("length_of_stay");
            //hotel_id=extras.getString("hotel_id");
            room_type_id = extras.getString("room_type_id");
            urlArray = extras.getStringArray("url_array");
            inclusions = extras.getString("inclusions");
            food_options = extras.getString("food_options");
            event_options = extras.getString("event_options");

            setTextFromOffers(offer_title,offer_description,inclusions,food_options,event_options);

        }/** Bundle values from available rooms page **/
        else if(intent_invoker.equals("available_page")) {
            initializeViews();
            initializeViewsForRegularBooking();
            //String available;
            room_type_id = extras.getString("room_type_id");
            urlArray = extras.getStringArray("url_array");
            room_type_name = extras.getString("room_type");
            room_type_id = extras.getString("room_type_id");
            //available = extras.getString("available");
            avail_room_today = extras.getInt("available");
            price_per_day = extras.getString("price_per_day");
            //setTextFromOffers(room_type_name, available, price_per_day);
            setTextFromBooking();
        }

        /** For image slider calls ImagePagerFragment **/
        PagerAdapter adapter = new ImagePagerFragment(Booking.this,urlArray);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        /** Circle indicator for images counts **/
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(viewPager);

        customizeToolbar();
    }

    /** Button to pass the values to confirm booking **/
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.confirm_booking:
                //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
                sendToConfirmBooking();
                break;
        }
    }

    public void onStart(){
        super.onStart();
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    /** Send values which needed for confirm booking screen */
    private void sendToConfirmBooking(){
        Intent intent = new Intent(getApplicationContext(),BookingConfirm.class);
        intent.putExtra("intent_invoker",intent_invoker);
        intent.putExtra("day_price",price_per_day);
        intent.putExtra("",tax1);
        intent.putExtra("room_type",room_type_name);
        intent.putExtra("room_type_id", room_type_id);
        if(intent_invoker.equals("offer_page")) {
            intent.putExtra("offer_from", offer_valid_from);
            intent.putExtra("offer_to", offer_valid_to);
            intent.putExtra("offer_id",offer_id);
            intent.putExtra("minimum_days",length_of_stay); //integer
            startActivity(intent);
        }else{
            if(price_per_day==null || price_per_day.equals("null"))//(avail_room_today==0)
                am.SingleButtonAlertNoTile("This Category Room is not available","Ok");
            else if(avail_room_today==0) {
                intent.putExtra("today_room_availability",0);
                finish();
                startActivity(intent);
            }
        }

    }

    /** Set the values to views which got from offer screen **/
    private void setTextFromOffers(String offer_title,String offer_description,String inclusions,
                                   String food,String event){
        title.setText(offer_title);
        description.setText(offer_description);
        validity_from.setText("From :" + offer_valid_from);
        validity_to.setText("To : " + offer_valid_to);
        room_type.setText("Room Type: " + room_type_name);
        min_los.setText("Minimum Length of stay: " + length_of_stay + " Nights");
        if(checkText(inclusions,inclusion_layout))
            inclusion.setText(inclusions);
        if(checkText(inclusions,food_option_layout))
            food_option.setText(food);
        if(checkText(inclusions, event_option_layout))
            event_option.setText(event);

        setAdditionalTexts();
    }

    /** Set the values to views which got from available room screen **/
    private void setTextFromBooking(){
        offer_layout.setVisibility(View.GONE);
        offer_spl_layout.setVisibility(View.GONE);
        title.setText(room_type_name);
        //validy.setText("Rooms Available: " + avail);
        //description.setText("Price/day : "+price_per_day);
        setAdditionalTexts();
    }

    /** Check bundle value is null, then not to display that in views*/
    private boolean checkText(String text,LinearLayout get){
        if(text.equals("") || text==null || text.equals("null")){
            get.setVisibility(View.GONE);
            return false;
        }else
            return true;
    }

    /** Check extra features provided by the hotel is available or not*/
    private boolean checkExtraAvailability(int val,LinearLayout get){
        if(val==0){
            get.setVisibility(View.GONE);
            return false;
        }else
            return true;
    }

    /** Set extra features availability in the hotel for the booking*/
    private void setAdditionalTexts(){
        offer_price.setText("Price : " + price_per_day);
        tax.setText("Tax   : Not handled yet" + tax1);
        if(room_description1==null || room_description1.equals("") || room_description1.equals("null"))
            room_description.setVisibility(View.GONE);
        else
            room_description.setText(room_description1);
        if(checkExtraAvailability(fan1, fan_layout))
            fan.setText("Fan Available");
        if(checkExtraAvailability(ac1,ac_layout))
            ac.setText("Fully Air-conditioned Room");
        if(checkExtraAvailability(fridge1,fridge_layout))
            fridge.setText("Seperate Fridge must be provided");
        if(checkExtraAvailability(tv1,tv_layout))
            tv.setText("LED TV available in center of the room");
        if(checkExtraAvailability(bar11,bar_layout))
            bar1.setText("Bar available with quality hot & soft drinks");
        if(checkExtraAvailability(fan1,fan_layout))
            wifi.setText("24/7 wifi usage is included");
        if(checkExtraAvailability(fan1,fan_layout))
            fan.setText("Fan Available");
        if(checkText(complement1, complement_layout))
            complement.setText(complement1);
        if(checkText(bed1,bed_layout))
            bed.setText(bed1);
        if(checkText(allowed_guest,guest_layout))
            guest.setText(allowed_guest);
        if(checkExtraAvailability(room_service, rm_srvc_layout))
            rm_srvc.setText("Room service provided for 24/7");
        if(checkExtraAvailability(avail_restaurant, restaurant_layout))
            restaurant.setText("High quality restaurant is attached with us");
    }

    /** Customize the toolbar */
    private void customizeToolbar(){
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.room_details);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.room_details) + "</font>"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//getSupportActionBar().setTitle("NEW BOOKINGS");
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

    /** Initialize views which are common from offers and rooms screen */
    private void initializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        title = (TextView)findViewById(R.id.title);
        offer_price=(TextView)findViewById(R.id.offer_price);
        tax = (TextView)findViewById(R.id.tax);
        room_description = (TextView)findViewById(R.id.room_description);
        fan = (TextView)findViewById(R.id.fan);
        ac = (TextView)findViewById(R.id.ac);
        fridge = (TextView)findViewById(R.id.fridge);
        tv = (TextView)findViewById(R.id.tv);
        bar1 = (TextView)findViewById(R.id.bar1);
        wifi = (TextView)findViewById(R.id.wifi);
        complement = (TextView)findViewById(R.id.complement);
        bed = (TextView)findViewById(R.id.bed);
        guest = (TextView)findViewById(R.id.guest);
        rm_srvc = (TextView)findViewById(R.id.rm_srvc);
        restaurant = (TextView)findViewById(R.id.restaurant);

        fan_layout = (LinearLayout)findViewById(R.id.fan_layout);
        ac_layout = (LinearLayout)findViewById(R.id.ac_layout);
        fridge_layout = (LinearLayout)findViewById(R.id.fridge_layout);
        tv_layout = (LinearLayout)findViewById(R.id.tv_layout);
        bar_layout = (LinearLayout)findViewById(R.id.bar_layout);
        wifi_layout = (LinearLayout)findViewById(R.id.wifi_layout);
        complement_layout = (LinearLayout)findViewById(R.id.complement_layout);
        bed_layout = (LinearLayout)findViewById(R.id.bed_layout);
        guest_layout = (LinearLayout)findViewById(R.id.guest_layout);
        rm_srvc_layout = (LinearLayout)findViewById(R.id.rm_srvc_layout);
        restaurant_layout = (LinearLayout)findViewById(R.id.restaurant_layout);

        confirm_booking=(Button)findViewById(R.id.confirm_booking);
        viewPager = (ViewPager) findViewById(R.id.viewPagers);

        confirm_booking.setOnClickListener(this);
        /*scrollview = (ScrollView)findViewById(R.id.scrollview);
        confirm_booking.setOnTouchListener(new View.OnTouchListener() {
            Handler mHandler;
            long mInitialDelay = 300;
            long mRepeatDelay = 100;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, mInitialDelay);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null)
                            return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    scrollview.scrollTo((int) scrollview.getScrollX(), (int) scrollview.getScrollY() + 10);
                    mHandler.postDelayed(mAction, mRepeatDelay);
                }
            };
        });*/

        /** Set custom font **/
        application = (ApplicationConfigs)getApplicationContext();
        application.setTypefaceTextView(toolbar_title);
        application.setTypefaceTextViewBold(title);
        application.setTypefaceTextView(offer_price);
        application.setTypefaceTextView(tax);
        application.setTypefaceTextView(room_description);
        application.setTypefaceTextView(fan);
        application.setTypefaceTextView(ac);
        application.setTypefaceTextView(fridge);
        application.setTypefaceTextView(tv);
        application.setTypefaceTextView(bar1);
        application.setTypefaceTextView(wifi);
        application.setTypefaceTextView(complement);
        application.setTypefaceTextView(bed);
        application.setTypefaceTextView(guest);
        application.setTypefaceTextView(rm_srvc);
        application.setTypefaceTextView(restaurant);
        application.setTypefaceButton(confirm_booking);
    }

    /** Initialize views only for offer screen **/
    private void initializeViewsForOfferBooking(){
        room_type = (TextView)findViewById(R.id.room_type);
        description=(TextView)findViewById(R.id.description);
        validy = (TextView)findViewById(R.id.validy);
        validity_from=(TextView)findViewById(R.id.validity_from);
        validity_to=(TextView)findViewById(R.id.validity_to);
        min_los = (TextView)findViewById(R.id.min_los);
        inclusion =(TextView)findViewById(R.id.inclusion);
        food_option =(TextView)findViewById(R.id.food_option);
        event_option =(TextView)findViewById(R.id.event_option);

        event_option_layout = (LinearLayout)findViewById(R.id.event_option_layout);
        food_option_layout = (LinearLayout)findViewById(R.id.food_option_layout);
        inclusion_layout = (LinearLayout)findViewById(R.id.inclusion_layout);

        /** Set custom font **/
        application.setTypefaceTextView(room_type);
        application.setTypefaceTextView(description);
        application.setTypefaceTextView(validy);
        application.setTypefaceTextView(validity_from);
        application.setTypefaceTextView(validity_to);
        application.setTypefaceTextView(min_los);
        application.setTypefaceTextView(inclusion);
        application.setTypefaceTextView(food_option);
        application.setTypefaceTextView(event_option);
    }

    /** Initialize views for room screen */
    private void initializeViewsForRegularBooking(){
        offer_layout = (LinearLayout)findViewById(R.id.offer_layout);
        offer_spl_layout = (LinearLayout)findViewById(R.id.offer_spl_layout);
    }
}
