package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Write new review and post, edit review.
 *  Created by : Abish
 *  Created Dt : 3/11/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import general.AlertMessages;
import general.ApplicationConfigs;
import general.CheckInternet;
import general.EditStatusBar;
import general.SaveCameraImageInStorage;
import general.StaticConstants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReviewWriteNew extends AppCompatActivity implements View.OnClickListener {
    /** Global Declarations*/
    SaveCameraImageInStorage sciis = new SaveCameraImageInStorage(this);
    CheckInternet ci = new CheckInternet(this);
    AlertMessages am = new AlertMessages(this);
    ApplicationConfigs application;
    private Toolbar toolbar;
    Button review_add_image,review_ok_enter;
    ImageView review_image_captured;
    EditText review_title_enter,review_description_enter;
    TextView toolbar_title,review_name;
    int CAMERA_REQUEST=1;
    boolean photo_taken=false;
    String imagePath="", image_name,uuid,name,rev_tit,rev_descrip,review_id;
    SimpleDateFormat dateformat;
    Calendar cal;
    Bundle extras;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.review_write);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        intializeViews();
        setCustomToolbar();

        dateformat= new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);

        /**Get uuid and user's name*/
        SharedPreferences sp=getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        uuid = sp.getString("profit_key_uuid", "");
        name = sp.getString("profit_key_users_name","");
        review_name.setText(name);

        /**Process bundle value*/
        extras = getIntent().getExtras();
        if(extras!=null)
            getValueFromBundle();
    }

    public void onStart(){
        super.onStart();

    }

    /** View initialize */
    private void intializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        review_add_image=(Button)findViewById(R.id.review_add_image);
        review_ok_enter=(Button)findViewById(R.id.review_ok_enter);
        review_image_captured=(ImageView)findViewById(R.id.review_image_captured);
        review_title_enter=(EditText)findViewById(R.id.review_title_enter);
        review_description_enter=(EditText)findViewById(R.id.review_description_enter);
        review_name=(TextView)findViewById(R.id.review_name);

        review_add_image.setOnClickListener(this);
        review_ok_enter.setOnClickListener(this);

        /** Set custom font */
        application = (ApplicationConfigs)getApplication();
        application.setTypefaceTextView(toolbar_title);
        application.setTypefaceTextView(review_name);
        application.setTypefaceEditText(review_title_enter);
        application.setTypefaceEditText(review_description_enter);
        application.setTypefaceButton(review_add_image);
        application.setTypefaceButton(review_ok_enter);
    }

    /** Set custom toolbar*/
    private void setCustomToolbar(){
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.write_review);
//        getSupportActionBar().setTitle("Write Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.write_review) + "</font>"));
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

    /** Onclick action handler */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.review_add_image:
                /*DatabaseHandlerToSaveDetails db = new DatabaseHandlerToSaveDetails(this);
                db.addContact(new GetSetMyDetails("first_name", "last_name", "username", "password","phone",
                        "mobile", "email","dob","gender","city", "country", "zipcode", "hid","uu"));*/

                /**Request Camera Activity*/
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            case R.id.review_ok_enter: /** Review posting*/
                if(review_title_enter.getText().toString().length()<1)
                    Log.d("Error","Check Review Title");
                else if(review_description_enter.getText().toString().length()<1)
                    Log.d("Error","Check Review Description");
//                else if(!photo_taken)
//                    Log.d("Error","Photo not taken");
                else {/** Save image if captured*/
                    File imagepath=null;
                    if(photo_taken) {
                        cal = Calendar.getInstance();
                        image_name = name + dateformat.format(cal.getTime());
                        imagepath = sciis.saveImage("ProfitKey", image_name, review_image_captured);
                    }

                    /** Review posting call to live DB */
                    if(extras==null)
                        new AsyncReviewImagePost(this, imagepath).execute("post",StaticConstants.hotel_id,uuid,
                                review_title_enter.getText().toString(),review_description_enter.getText().toString());
                    else
                        new AsyncReviewImagePost(this, imagepath).execute("edit",review_id,
                                review_title_enter.getText().toString(),review_description_enter.getText().toString());

                }
                break;
        }
    }

    /**Camera activity set image*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            review_image_captured.setImageBitmap(photo);
            photo_taken=true;
        }
    }

    /** Set values from bundle which is got from edit Review call */
    private void getValueFromBundle() {
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.edit_review) + "</font>"));
        review_id = extras.getString("review_id");
        rev_tit = extras.getString("review_tit");
        rev_descrip = extras.getString("review_descrip");
        review_title_enter.setText(rev_tit);
        review_description_enter.setText(rev_descrip);
        review_add_image.setText("Capture Again");
        review_ok_enter.setText("Post Edited");
        new AsyncReviewImageDownload(this).execute(extras.getString("review_img_url"),"edit_review");
    }

    /** Set the image from edit review download image call*/
    public void setImage(Bitmap image){
        review_image_captured.setImageBitmap(image);
    }

    /** After posting review success it will be called*/
    public void imageUploaded(File img_pth){
        if(img_pth==null) {
            finish();
        }
        else if(img_pth.exists()) {
            img_pth.delete();
            finish();
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
