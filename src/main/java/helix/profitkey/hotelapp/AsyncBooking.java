package helix.profitkey.hotelapp;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : AsyncTask for Booking
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified related to booking
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import general.StaticConstants;

public class AsyncBooking extends AsyncTask<String, Void, String> {
    private static final String TAG = "Inside Thread HotelAdd";
    ProgressDialog progressDialog;
    Context context;

    /**Constructor*/
    public AsyncBooking(Context conx) {
       context = conx;
    }

    /** Loader screen  **/
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("Loading. Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        progressDialog.show();
    }

    /** Function to handle background operations*/
    @Override
    protected String doInBackground(String... arg0) {
        String result = null;
        try {
            // url where the data will be posted or retrieved
            String postReceiverUrl = StaticConstants.insert_booking_url;
            Log.v(TAG, "postURL: " + postReceiverUrl);
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient
            HttpPost httpPost = new HttpPost(postReceiverUrl);    // post header

            /** Add params to the urls */
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("uuid", arg0[1]));
            nameValuePairs.add(new BasicNameValuePair("room_type_id", arg0[2]));
            nameValuePairs.add(new BasicNameValuePair("from_date", arg0[3]));
            nameValuePairs.add(new BasicNameValuePair("to_date", arg0[4]));
            nameValuePairs.add(new BasicNameValuePair("total_days", arg0[5]));
            nameValuePairs.add(new BasicNameValuePair("price", arg0[6]));
            nameValuePairs.add(new BasicNameValuePair("offer_id", arg0[7]));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            httpPost.setEntity(entity);

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT);
        }
        return result;
    }

    /**Function execute last to update*/
    @Override
    protected void onPostExecute(String result) {
        // stuff after posting data
        progressDialog.dismiss();
        if(result==null)
            Toast.makeText(context,"Server Error or Network Error!",Toast.LENGTH_SHORT);
        else {
            String message = null;
            JSONObject root;
            int validation = 0;

            try {
                Log.d("Inside Post", "message");
                root = new JSONObject(result);
                validation = root.getInt("response");
                message = root.getString("message");
                Log.d(result, message);

                /**After booking confirm this will execute*/
                if (validation == 1) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    ((Booking) context).onBackPressed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
