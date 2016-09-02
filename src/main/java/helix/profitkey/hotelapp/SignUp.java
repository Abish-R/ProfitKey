package helix.profitkey.hotelapp;

/** Profit Key 1.0.0
 *  Purpose	   : Sign Up for Users
 *  Created by : Abish
 *  Created Dt : 07-03-2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */

import general.ApplicationConfigs;
import general.CheckInternet;
import general.AlertMessages;
import general.EditStatusBar;
import general.StaticConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUp extends Activity implements View.OnClickListener {
    /**Global Class Variables*/
    ApplicationConfigs application;
    TextView sign_up_txt;
    CheckInternet ci=new CheckInternet(this);
    AlertMessages am=new AlertMessages(this);
    EditText f_name,l_name,uname,pass,dob,mobile,email,city,country,zipcode;
    LinearLayout first_layout,second_layout;
    RadioGroup genderradiobtn;
    Button btn_signup,back,btn_next,btn_signin;
    //String text = "<font color=#cc0029>*</font>";
    String f_nam,l_nam,unam,pas,gende,dobb,mobil,emai,cit=null,countr=null,zipcod=null;
    private int day,month,year,hour,minute;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));
        initializeAllViews();
        changeTextViewText();
        defaultDateCal();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btn_signup:
                /** Get gender*/
                RadioButton selectRadio = (RadioButton) findViewById(genderradiobtn.getCheckedRadioButtonId());
                gende = selectRadio.getText().toString();

                getViewValuesOnSignUp();
                //Toast.makeText(this, "Your Opinion is : " + gende, Toast.LENGTH_LONG).show();
                if(validationOnSignUp()==1){
                    if(ci.isOnline()){
                        new AsyncSignUp(SignUp.this).execute(f_nam,l_nam,unam,pas,gende,dobb,mobil,
                                emai,cit,countr,zipcod, "device_id",StaticConstants.hotel_id);
                    }
                    else
                        am.SingleButtonAlertNoTile("Check the internet connection", "Ok");
                }
                break;
            case R.id.btn_next:
                getViewValuesonNext();
                if(validationOnNextButton()==1) {
                    first_layout.setVisibility(View.GONE);
                    second_layout.setVisibility(View.VISIBLE);
                    sign_up_txt.setText("");
                    back.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.back:
                back.setVisibility(View.INVISIBLE);
                first_layout.setVisibility(View.VISIBLE);
                sign_up_txt.setText("Sign Up for a free Account");
                second_layout.setVisibility(View.GONE);
                break;
            case R.id.btn_signin:
                Intent callSignIn = new Intent(this,Login.class);
                startActivity(callSignIn);
                finish();
                break;
            case R.id.dob:
                showDialog(0);
                break;
        }
    }

    /**Once sign Up done take you to the login Screen*/
    public void startLogin(){
        Intent gologin=new Intent(this,Login.class);
        startActivity(gologin);
        finish();
    }

    /**After sign up it will be called to show the alert for success */
    public void saveCustomerDetails(){
        am.SingleButtonAlertWithResponsibility("Registration Success.", "Ok", "go_login");
    }

    /**View initialization*/
    private void initializeAllViews() {
        application = (ApplicationConfigs)getApplication();

        sign_up_txt=(TextView)findViewById(R.id.sign_up_txt);
        TextView textView2=(TextView)findViewById(R.id.textView2);
        TextView textView=(TextView)findViewById(R.id.textView);
        TextView dob_txt=(TextView)findViewById(R.id.dob_txt);
        TextView gndr_txt=(TextView)findViewById(R.id.gndr_txt);
        f_name=(EditText)findViewById(R.id.f_name);
        l_name=(EditText)findViewById(R.id.l_name);
        uname=(EditText)findViewById(R.id.uname);
        pass=(EditText)findViewById(R.id.pass);
        email=(EditText)findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        dob=(EditText)findViewById(R.id.dob);
        city=(EditText)findViewById(R.id.city);
        country=(EditText)findViewById(R.id.country);
        zipcode=(EditText)findViewById(R.id.zipcode);
        //gender=(Spinner)findViewById(R.id.gender);
        btn_signup=(Button)findViewById(R.id.btn_signup);
        back = (Button)findViewById(R.id.back);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_signin = (Button)findViewById(R.id.btn_signin);

        genderradiobtn = (RadioGroup)findViewById(R.id.gender);
        RadioButton male = (RadioButton)findViewById(R.id.male);
        RadioButton female = (RadioButton)findViewById(R.id.male);

        first_layout = (LinearLayout)findViewById(R.id.first_layout);
        second_layout = (LinearLayout)findViewById(R.id.second_layout);

        btn_signup.setOnClickListener(this);
        dob.setOnClickListener(this);
        back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_signin.setOnClickListener(this);

        btn_signin.setPaintFlags(btn_signin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        /** Set custom font*/
        application.setTypefaceEditText(f_name);
        application.setTypefaceEditText(l_name);
        application.setTypefaceEditText(uname);
        application.setTypefaceEditText(pass);
        application.setTypefaceEditText(email);
        application.setTypefaceEditText(mobile);
        application.setTypefaceEditText(dob);
        application.setTypefaceEditText(city);
        application.setTypefaceEditText(country);
        application.setTypefaceEditText(zipcode);
        application.setTypefaceButton(btn_signup);
        application.setTypefaceButton(back);
        application.setTypefaceButton(btn_next);
        application.setTypefaceButton(btn_signin);
        application.setTypefaceTextViewBold(sign_up_txt);
        application.setTypefaceTextView(gndr_txt);
        application.setTypefaceTextView(dob_txt);
        application.setTypefaceTextView(textView);
        application.setTypefaceTextView(textView2);
        application.setTypefaceRadio(male);
        application.setTypefaceRadio(female);
    }

    /**Get all the values I entered in editext on press of next button */
    private void getViewValuesonNext() {
        f_nam = f_name.getText().toString();
        l_nam = l_name.getText().toString();
        unam = uname.getText().toString();
        pas = pass.getText().toString();
        mobil = mobile.getText().toString();
        emai = email.getText().toString();
    }

    /** Get the values of remaining fields on pree of signUp button*/
    private void getViewValuesOnSignUp(){
        dobb = dob.getText().toString();
        cit = city.getText().toString();
        countr = country.getText().toString();
        zipcod = zipcode.getText().toString();
    }

    /** Validation process before doing action of next button*/
    private int validationOnNextButton(){
        boolean isDigit = false;
        int count = 0;
        if (emai.length() > 5)
            for (int i = 0; i < emai.length(); i++) {
                if (emai.charAt(i) == '@')
                    count++;
            }
        if (mobil.length() > 9) {
            char c = mobil.charAt(0);
            isDigit = (c >= '7' && c <= '9');
        }

        if (f_nam.length() < 1 || f_nam.contains("  ") || f_nam.charAt(0) == ' ' || f_nam.charAt(f_nam.length()-1) == ' ') {
            am.SingleButtonAlertNoTile("Check First Name", "Ok");
            return 0;
        }
        else if (l_nam.length() < 1 || l_nam.contains("  ") || l_nam.charAt(0) == ' ' || l_nam.charAt(l_nam.length()-1) == ' ') {
            am.SingleButtonAlertNoTile("Check Last Name", "Ok");
            return 0;
        }
        else if (!emai.contains("@") || (!emai.contains(".co") && !emai.contains(".com") && !emai.contains(".org") && !emai.contains(".net") &&
                !emai.contains(".edu") && !emai.contains(".info") && !emai.contains(".in")) || emai.length() < 6 ||
                (emai.length() > 50) || emai.contains("@@") || emai.contains("@.") || emai.contains(".@") ||
                emai.contains("..") || emai.contains("--") || emai.contains("__") || emai.charAt(0) == '@' ||
                emai.charAt(0) == '.' || emai.charAt(0) == '_' || emai.charAt(0) == '-' || count > 1){
            am.SingleButtonAlertNoTile("Check Email ID","Ok");
            return 0;
        }
        else if(!isDigit){
            am.SingleButtonAlertNoTile("Check Mobile","Ok");
            return 0;
        }
        else if (unam.length() < 3 || unam.contains("__") || unam.contains("..") || unam.charAt(0) == '_' ||
                unam.charAt(0) == '.' || unam.charAt(unam.length()-1) == '.') {// || unam.charAt(unam.length())=='_')
            am.SingleButtonAlertNoTile("Check Username", "Ok");
            return 0;
        }
        else if (pas.length() < 3 || pas.contains(" ")) {
            am.SingleButtonAlertNoTile("Check Password", "Ok");
            return 0;
        }
        else
            return 1;
    }

    /** Remaining field validation on preess of signup button*/
    private int validationOnSignUp(){
        if(dobb.length()<10){
            am.SingleButtonAlertNoTile("Enter DOB","Ok");
            return 0;
        }
        else if(cit.length()<3 || f_nam.contains("  ") || f_nam.charAt(0) == ' ' || f_nam.charAt(f_nam.length()-1) == ' '){
            am.SingleButtonAlertNoTile("Check City","Ok");
            return 0;
        }
        else if(countr.length()<3 || countr.contains("  ") || countr.charAt(0) == ' ' || countr.charAt(countr.length()-1) == ' '){
            am.SingleButtonAlertNoTile("Check Country","Ok");
            return 0;
        }
        else if(zipcod.length()<6){
            am.SingleButtonAlertNoTile("Check Zipcode","Ok");
            return 0;
        }
        else
            return 1;
    }

    /**Change textField with '*' */
    private void changeTextViewText() {
        //textView1.append(Html.fromHtml(text));
        //textView2.append(Html.fromHtml(text));
        //textView3.append(Html.fromHtml(text));
        //textView4.append(Html.fromHtml(text));
        //textView5.append(Html.fromHtml(text));
        //textView6.append(Html.fromHtml(text));
        //textView8.append(Html.fromHtml(text));
        //textView9.append(Html.fromHtml(text));
        //textView10.append(Html.fromHtml(text));
        //textView11.append(Html.fromHtml(text));
        //textView12.append(Html.fromHtml(text));
        //textView13.append(Html.fromHtml(text));
        //textView14.append(Html.fromHtml(text));
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

    /**Default Date Sets*/
    @SuppressLint("SimpleDateFormat")
    public void defaultDateCal(){
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        //hour  = c.get(Calendar.HOUR_OF_DAY);
        //minute = c.get(Calendar.MINUTE);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        //getDateDiffString();
    }

    /**Date Difference Calculation*/
    public Long getDateDiffString(){
        String today=df.format(c.getTime());
        long diff=0;
        Date inputDate,nowdate;
        try {
            inputDate=df.parse(dob.getText().toString());
            inputDate=df.parse(df.format(inputDate));
            nowdate=df.parse(today);
            nowdate=df.parse(df.format(nowdate));
            diff=nowdate.getTime()-inputDate.getTime();
            diff=diff/86400000;
            if(diff >= 1)
                return diff;
            else{
                am.SingleButtonAlertWithResponsibility("DOB you selected may be Wrong","Ok","call_cal_dialog");
                diff=0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    /** Date picker action listeners*/
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int i) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            if(selectedMonth<10 && selectedDay<10)
                dob.setText(selectedYear + "-0" + (selectedMonth + 1) + "-0"  + selectedDay);
            else if(selectedMonth<10)
                dob.setText(selectedYear + "-0" + (selectedMonth + 1) + "-"  + selectedDay);
            else if(selectedDay<10)
                dob.setText(selectedYear + "-" + (selectedMonth + 1) + "-0"  + selectedDay);
            if(view.isShown())
                getDateDiffString();
        }
    };
}
