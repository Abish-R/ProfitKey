package helix.profitkey.hotelapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelixTech-Admin on 3/11/2016.
 */
public class DatabaseHandlerToSaveDetails extends SQLiteOpenHelper {

    /** All Static variables*/
    private static final int DATABASE_VERSION = 1; 				  // Database Version
    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory()+
             File.separator + "/DataBase/" + File.separator+"profit_key";  	   	  // Database Name
    private static final String TABLE_CUSTOMER = "profit_key_customer";  // Contacts table name
    /** Contacts Table Columns names*/
    private static final String KEY_ID = "cust_id";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String KEY_UNAME = "username";
    private static final String KEY_PASS = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "dob";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_ZIP = "zipcode";
    private static final String KEY_HOTEL_ID = "hotel_id";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_CREATED_DATE = "uuid";

    public DatabaseHandlerToSaveDetails(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Creating Tables*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement not null,"+ KEY_FNAME+" TEXT,"
                + KEY_LNAME + " TEXT,"+ KEY_UNAME + " TEXT,"+KEY_PASS+" TEXT," +
                KEY_PHONE+ " TEXT,"+KEY_MOBILE+ " TEXT,"+KEY_EMAIL+ " TEXT,"+KEY_DOB+ " TEXT,"+
                KEY_GENDER+ " TEXT,"+KEY_CITY+ " TEXT,"+KEY_COUNTRY+ " TEXT,"+KEY_ZIP+ " TEXT,"+
                KEY_HOTEL_ID+ " TEXT,"+KEY_UUID+ " TEXT,"+KEY_CREATED_DATE+" DATETIME DEFAULT CURRENT_DATE)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /** Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    /** Adding new rate*/
    void addContact(GetSetMyDetails contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, contact.getFirstName());                    // Get Rate
        values.put(KEY_LNAME, contact.getLastName());                  // Get Rate
        values.put(KEY_UNAME, contact.getUsername());                  // Get Rate
        values.put(KEY_PASS,contact.getPassword());
        values.put(KEY_PHONE,contact.getPhone());				// Get Created date
        values.put(KEY_MOBILE,contact.getMobile());                  // Get Up State
        values.put(KEY_EMAIL, contact.getEmail());                  // Get Rate
        values.put(KEY_DOB, contact.getDOB());                  // Get Rate
        values.put(KEY_GENDER,contact.getGender());
        values.put(KEY_CITY,contact.getCity());				// Get Created date
        values.put(KEY_COUNTRY,contact.getCountry());
        values.put(KEY_ZIP, contact.getZipcode());                  // Get Rate
        values.put(KEY_HOTEL_ID,contact.getHotel_id());
        values.put(KEY_UUID,contact.getUUID());				// Get Created date

        /** Inserting Row*/
        db.insert(TABLE_CUSTOMER, null, values);
        Log.e(values.getAsString(KEY_ID), "Done");
        db.close(); // Closing database connection
    }

    /** Update rate of a date */
    void updateRates(String rt1,String rt2,String rt3,String dt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(KEY_RATE, rt1);
//        values.put(KEY_RATE1, rt2);
//        values.put(KEY_RATE2, rt3);
//        values.put(KEY_RATE_UP_STATE, "N");
//        /** updating row*/
//        int temp= db.update(TABLE_RIDE_RATE, values, KEY_RATE_DATE + " = ?",
               // new String[] {dt});
        db.close();
    }

    /** Getting Rates for a particular Date*/
    public List<GetSetMyDetails> getCustomerDetails() {
        List<GetSetMyDetails> contactList = new ArrayList<GetSetMyDetails>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                GetSetMyDetails contact = new GetSetMyDetails();
                //contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setRate(cursor.getString(1));
//                contact.setRate1(cursor.getString(2));
//                contact.setRate2(cursor.getString(3));
//                contact.setRateDate(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    // Getting contacts Count
    public int getRateCount() {
        //String countQuery = "SELECT  * FROM " + TABLE_RIDE_RATE;
        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor cursor = db.rawQuery(countQuery, null);
        //int temp=cursor.getCount();
        //cursor.close();
        db.close();
        return 1;
    }
    /**Update the upload status*/
    public int updateRatesUploadStatus(String dt, String st){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_RATE_UP_STATE, st);

        /** updating row*/
        return 1;//db.update(TABLE_RIDE_RATE, values, KEY_RATE_DATE + " = ?",
                //new String[] {dt});
    }

}