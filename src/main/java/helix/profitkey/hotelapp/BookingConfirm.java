package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Booking confirm
 *  Created by : Abish
 *  Created Dt : 26/04/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import general.AlertMessages;
import general.ApplicationConfigs;
import general.EditStatusBar;
import general.StaticConstants;

public class BookingConfirm extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /** Global declarations **/
    AlertMessages am = new AlertMessages(this);
    ApplicationConfigs application;
    private Toolbar toolbar;
    String intent_invoker,offer_valid_from,offer_valid_to,offered_price,tax1,hotel_id,tot_days,room_type,offer_id,room_type_id;
    TextView toolbar_title,title,description,offer_price,validy,validity_from,validity_to,total_days,tax;
    EditText from_date,to_date;
    TextView confirm_booking,inr_text,confirm_booking1;
    View confirm_booking_layout;
    int date_invoker,tot_price,length_of_stay,today_room_availability;
    StringBuilder from_to_date_time;
    String from_date_frm_strng_bldr,uuid;
    SimpleDateFormat dateformat;
    Calendar cal;
    static String hotel_id_value=StaticConstants.hotel_id;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.booking_confirm);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        cal=Calendar.getInstance();
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        initializeViews();

        /** Get uuid which is stored*/
        SharedPreferences sp=getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        uuid = sp.getString("profit_key_uuid", "");

        /** Bundle values from offer page and available rooms page common values **/
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            intent_invoker = extras.getString("intent_invoker");
            offered_price=extras.getString("day_price");
            //tax1 = extras.getString("tax");
            room_type=extras.getString("room_type");
            room_type_id = extras.getString("room_type_id");
            hotel_id=hotel_id_value;
        }
        else
            intent_invoker="";

        /** Bundle values from offer page **/
        if(intent_invoker.equals("offer_page")){
            offer_valid_from=extras.getString("offer_from");
            offer_valid_to=extras.getString("offer_to");
            length_of_stay=extras.getInt("minimum_days");
            offer_id = extras.getString("offer_id");
            offer_valid_from+=" 00:01";
            offer_valid_to+=" 23:59";
            String descrip_text = "*The offer is valid from "+offer_valid_from+" to "+offer_valid_to+".\n\n"+
                    "*This offer is applicable only if you select minimum of "+length_of_stay+" days.";
            description.setText(descrip_text);
            defaultDateCal(dateformat.format(cal.getTime()), length_of_stay);
        }/** Bundle values from available rooms page **/
        else if(intent_invoker.equals("available_page")){
            //initializeViewsForRegularBooking();
            today_room_availability = extras.getInt("today_room_availability");
            offer_id = "00";
            length_of_stay=0;
            defaultDateCal(dateformat.format(cal.getTime()), length_of_stay);
        }else {
            offer_valid_from = "2000-01-01 00:01";
            offer_valid_to="2000-01-01 00:01";
        }

        customizeToolbar();
    }

    /** View Initialize */
    private void initializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        title=(TextView)findViewById(R.id.room_type);
        description=(TextView)findViewById(R.id.description);
        tax=(TextView)findViewById(R.id.tax);
        //validy = (TextView)findViewById(R.id.validy);
        //validity_from=(TextView)findViewById(R.id.validity_from);
        //validity_to=(TextView)findViewById(R.id.validity_to);
        total_days=(TextView)findViewById(R.id.total_days);
        //total_price=(TextView)findViewById(R.id.total_price);
        from_date=(EditText)findViewById(R.id.from_date);
        to_date=(EditText)findViewById(R.id.to_date);
        confirm_booking=(TextView)findViewById(R.id.confirm_booking);
        inr_text = (TextView)findViewById(R.id.inr_text);
        confirm_booking1 = (TextView)findViewById(R.id.confirm_booking1);
        confirm_booking_layout = findViewById(R.id.confirm_booking_layout);
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        //confirm_booking.setOnClickListener(this);
        confirm_booking_layout.setOnClickListener(this);

        application = (ApplicationConfigs)getApplication();
        application.setTypefaceTextView(toolbar_title);
        application.setTypefaceTextViewBold(title);
        application.setTypefaceTextView(description);
        application.setTypefaceTextView(total_days);
        application.setTypefaceTextView(confirm_booking);
        application.setTypefaceTextView(inr_text);
        application.setTypefaceTextView(confirm_booking1);
        application.setTypefaceTextView(tax);
        application.setTypefaceEditText(from_date);
        application.setTypefaceEditText(to_date);

        title.setText(room_type);
    }

    /** OnClick action handler **/
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.from_date:
                callDateTimePicker(1);
                break;
            case R.id.to_date:
                if(from_date.getText().toString().length()<1)
                    Toast.makeText(this, "Enter From First.!", Toast.LENGTH_SHORT).show();
                else
                    callDateTimePicker(2);
                break;
            case R.id.confirm_booking_layout:/** confirm_booking  */
                double days = findDateDifference();
                //Toast.makeText(this,from_to_date_time.toString(),Toast.LENGTH_SHORT).show();
                if(confirm_booking.getText().equals("Back"))
                    onBackPressed();
                else if(from_date.getText().length()<1 || from_date.getText().length()<1)
                    Toast.makeText(this,"Enter From/To Date.",Toast.LENGTH_SHORT).show();
                else if(days>0) {
                    daysPriceSet(days);

                    /** Call AsyncTask to send the booking details*/
                    new AsyncBooking(this).execute(hotel_id, uuid, room_type_id, from_date.getText().toString(),
                            to_date.getText().toString(), tot_days, tot_price + "", offer_id);
                }
                break;
        }
    }

    public void onStart(){
        super.onStart();
    }
    public void onBackPressed(){
        super.onBackPressed();
    }

    /** While starting the screen default date will be set as from and to date**/
    public void defaultDateCal(String frm, int min_days) {
        Log.i("Initial Time", "" + cal.getTime());
        if(intent_invoker.equals("offer_page")) {
//            checkOfferValidity(offer_valid_from,offer_valid_to);
            //findTodate(frm,min_days);
            checkOfferValidity(frm,findTodate(frm,min_days));
        }
        else {
            from_date.setText(frm);
            to_date.setText(findTodate(frm,min_days));
            double days = findDateDifference();
            if(days>0)
                daysPriceSet(days);
        }
    }

    /** Calculate to date the minimum length of stay value*/
    private String findTodate(String frm,int min_days){
        Calendar cal_to = Calendar.getInstance();
        cal_to.add(Calendar.DATE, min_days);
        String to_date = dateformat.format(cal_to.getTime());
        return to_date;
    }

    /** From and to date validation based on min length of stay*/
    public Double findDateDifference(){
        long frm_to_diff=0,nw_frm_diff=0;
        Date fromdt,todt,nowdt;
        double return_value=0.0,return_value1=0.0;
        try {
            fromdt = dateformat.parse(from_date.getText().toString());
            todt = dateformat.parse(to_date.getText().toString());
            nowdt = dateformat.parse(dateformat.format(cal.getTime()));
            fromdt = dateformat.parse(dateformat.format(fromdt));
            todt = dateformat.parse(dateformat.format(todt));
            nowdt = dateformat.parse(dateformat.format(nowdt));

            frm_to_diff = todt.getTime() - fromdt.getTime();
            nw_frm_diff = fromdt.getTime() - nowdt.getTime();

            return_value = frm_to_diff / 86400000;
            return_value1 = nw_frm_diff / 86400000;
            if (return_value < length_of_stay){
                am.SingleButtonAlertWithResponsibility("You have to select atleast "+length_of_stay+" days","Ok","book_cnfm_to_date");
                //callDateTimePicker(2);
            }
            else if(return_value1<0) {
                am.SingleButtonAlertWithResponsibility("From date you selected is wrong. Please adjust the from date","Ok","book_cnfm_from_date");
                //callDateTimePicker(1);
            }
            else {
                if (return_value >= 0 && return_value <= 1)
                    return 1.0;
                else if (return_value > 1)
                    return return_value;
                else {
                    setInvalidDateValues();
                    return 0.0;
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return return_value;
    }

    /** Check the offer validity. Selected date falls under the offer validity period or not*/
    private void checkOfferValidity(String fdate, String tdate){
        Date ofromdt,otodt,fromdt,todt,nowdt;
        double return_value=0.0,return_value1=0.0,now_validity=0;
        try {
            ofromdt = dateformat.parse(offer_valid_from);
            otodt = dateformat.parse(offer_valid_to);
            fromdt = dateformat.parse(fdate);
            todt = dateformat.parse(tdate);
            nowdt = dateformat.parse(dateformat.format(cal.getTime()));

            ofromdt= dateformat.parse(dateformat.format(ofromdt));
            otodt= dateformat.parse(dateformat.format(otodt));
            fromdt= dateformat.parse(dateformat.format(fromdt));
            todt= dateformat.parse(dateformat.format(todt));
            nowdt= dateformat.parse(dateformat.format(nowdt));

            now_validity = nowdt.getTime() - fromdt.getTime();
            return_value = ofromdt.getTime() - fromdt.getTime();
            return_value1 = otodt.getTime() - todt.getTime();
            return_value /= 86400000;
            return_value1 /= 86400000;

            if(return_value>0) {
                setInvalidDateValues();
                am.SingleButtonAlertNoTile("Offer only starts from '"+offer_valid_from+"' please adjust the from date","Ok");
            }
            else if(return_value1<0) {
                setInvalidDateValues();
                am.SingleButtonAlertNoTile("Offer ended by '" + offer_valid_to + "' please adjust the to date","Ok");
            }
            else if(now_validity>0) {
                setInvalidDateValues();
                am.SingleButtonAlertNoTile("From date you selected is wrong. Please adjust the from date","Ok");
            } else {
                from_date.setText(fdate);
                to_date.setText(tdate);
                daysPriceSet(findDateDifference());
                confirm_booking.setText("Book Now");
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /** Set values if the date is invalid*/
    private void setInvalidDateValues(){
        from_date.setText("");
        to_date.setText("");
        confirm_booking.setText("Back");
        confirm_booking1.setVisibility(View.INVISIBLE);
    }

//    private void checkDateValidity(String fdate, String tdate){
//        Date ofromdt,otodt,fromdt,todt;
//        double return_value=0.0,return_value1=0.0;
//        try {
//            ofromdt = dateformat.parse(offer_valid_from);
//            otodt = dateformat.parse(offer_valid_to);
//            fromdt = dateformat.parse(fdate);
//            todt = dateformat.parse(tdate);
//            ofromdt= dateformat.parse(dateformat.format(ofromdt));
//            otodt= dateformat.parse(dateformat.format(otodt));
//            fromdt= dateformat.parse(dateformat.format(fromdt));
//            todt= dateformat.parse(dateformat.format(todt));
//
//            return_value = ofromdt.getTime() - fromdt.getTime();
//            return_value1 = otodt.getTime() - todt.getTime();
//            return_value /= 86400000;
//            return_value1 /= 86400000;
//
//            if(return_value<=0 && return_value1>=0) {
//                from_date.setText(fdate);
//                to_date.setText(tdate);
//                daysPriceSet(findDateDifference());
//                confirm_booking.setText("Confirm Booking");
//            }
//            else{
//                from_date.setText("Date not valid");
//                to_date.setText("Date not valid");
//                confirm_booking.setText("Back");
//            }
//        }catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }


    /** Date Time Picker invoker*/
    public void callDateTimePicker(int val) {
        date_invoker = val;
        DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).
                show(getFragmentManager(), "datePicker");
    }

    /**  Date Set */
    @Override
    public void onDateSet(DatePickerDialog dialog, int yearselected, int monthOfYear, int dayOfMonth) {

        monthOfYear+=1;
        from_to_date_time = new StringBuilder();
        if(monthOfYear<10 && dayOfMonth<10)
            from_to_date_time.append(yearselected).append("-0" + monthOfYear).append("-0"+dayOfMonth);
        else if(monthOfYear<10)
            from_to_date_time.append(yearselected).append("-0"+monthOfYear).append("-" + dayOfMonth);
        else if(dayOfMonth<10)
            from_to_date_time.append(yearselected).append("-"+monthOfYear).append("-0"+dayOfMonth);
        else
            from_to_date_time.append(yearselected).append("-"+monthOfYear).append("-"+dayOfMonth);

        TimePickerDialog.newInstance(this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).
                show(getFragmentManager(), "timePicker");
    }

    /** Time set */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (hourOfDay < 10 && minute < 10)
            from_to_date_time.append(" 0" + hourOfDay).append(":0" + minute);
        else if (hourOfDay < 10)
            from_to_date_time.append(" 0" + hourOfDay).append(":" + minute);
        else if (minute < 10)
            from_to_date_time.append(" " + hourOfDay).append(":0" + minute);
        else
            from_to_date_time.append(" " + hourOfDay).append(":" + minute);
        //if(intent_invoker.equals("offer_page"))

        if (date_invoker == 1){ // && intent_invoker.equals("offer_page")
            from_date_frm_strng_bldr=from_to_date_time.toString();
            //checkOfferValidity(from_to_date_time.toString());
            from_date.setText(from_to_date_time);
            //daysPriceSet(findDateDifference());
        }
        else if(date_invoker==2  && intent_invoker.equals("available_page")) {
            //from_date_frm_strng_bldr = from_date.getText().toString();
            to_date.setText(from_to_date_time);
            double days = findDateDifference();
            if(days>0)
                daysPriceSet(days);
        }
        else if(date_invoker==2) {
            from_date_frm_strng_bldr = from_date.getText().toString();
            checkOfferValidity(from_date_frm_strng_bldr,from_to_date_time.toString());
            //to_date.setText(from_to_date_time);
            //findDateDifference();
            //daysPriceSet(findDateDifference());
        }
    }

//    private void setTextFromOffers(String offer_title,String offer_description,String offer_valid_from,
//                                   String offer_valid_to,String offered_price){
//        title.setText(offer_title);
//        description.setText(offer_description);
//        offer_price.setText("Offer Price : "+offered_price);
//        validity_from.setText("From :" + offer_valid_from);
//        validity_to.setText("To : " + offer_valid_to);
//    }
//
//    private void setTextFromOffers(String type,String avail,String price){
//        title.setText("Booking");
//        offer_price.setText("Category: " + type);
//        validy.setText("Rooms Available: " + avail);
//        description.setText("Price: "+price);
//        validity_from.setVisibility(View.GONE);
//        validity_to.setVisibility(View.GONE);
//    }

    /** Calculate price */
    private void daysPriceSet(double days){
        DecimalFormat decimal = new DecimalFormat("0.0");
        String pricee= offered_price.replace(",", "");
        tot_days = decimal.format(days);
        double _price = Double.parseDouble(pricee) * days;
        tot_price = (int)_price;
        //Toast.makeText(this,days+" "+ tot_price,Toast.LENGTH_SHORT).show();
        total_days.setText("Total Days: " + decimal.format(days));
        //tax.setText(tax1+"%");
        //int tax = (Integer.parseInt(tax1)/100) * tot_price;
        //tot_price += tax;
        inr_text.setText(tot_price+"");
    }

    private void customizeToolbar() {
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.new_booking);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.new_booking) + "</font>"));
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
