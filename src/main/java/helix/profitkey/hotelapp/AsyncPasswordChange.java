package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Password Change AsyncTask
 *  Created by : Abish
 *  Created Dt :
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fragments.PasswordResetFragment;
import general.AlertMessages;
import general.StaticConstants;

public class AsyncPasswordChange extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    private Context context;
    PasswordResetFragment context1 = new PasswordResetFragment();
    AlertMessages am;

    /**Constructor*/
    public AsyncPasswordChange(Context conx,PasswordResetFragment con){
        context=conx;
        context1 = con;
        am = new AlertMessages(context);
    }

    /** Loader screen  **/
    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
        progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("Loading. Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        progressDialog.show();
    }

    /** Function to handle background operations*/
    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String response="";
        try {
                /* forming th java.net.URL object */
            URL url = new URL(StaticConstants.password_change_url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            /** Add params to the urls */
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("uuid", params[0])
                    .appendQueryParameter("existing_password", params[1])
                    .appendQueryParameter("new_password", params[2]);
            String query = builder.build().getEncodedQuery();

            //OutputStream os = urlConnection.getOutputStream();
            DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());

            writer.writeBytes(query);
            writer.flush();
            writer.close();
            writer.close();

            int statusCode = urlConnection.getResponseCode();
                /* 200 represents HTTP OK */
            if (statusCode ==  200) {
                String result;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((result=br.readLine()) != null) {
                    response+=result;
                }
            }else{
                response = ""; //"Failed to fetch data!";
            }
        } catch (Exception e) {
            Log.d("", e.getLocalizedMessage());
        }
        return response; //"Failed to fetch data!";
    }

    /**Function execute last to update*/
    @Override
    protected void onPostExecute(String result) {
        // do stuff after posting data
        progressDialog.dismiss();
        if(result==null || result.equals(""))
            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
        else{
            int validation = 0;
            JSONObject root = null;
            String user_id = "", message, uuid, name,email;
            try {
                Log.d("Inside Post", "message");
                root = new JSONObject(result);
                validation = root.getInt("response");
                message = root.getString("message");
                Log.d(result, user_id);

                /** After success response */
                if (validation == 1) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    context1.changeFragment();
                }else{
                    am.SingleButtonAlertNoTile(message,"Ok");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

//    @Override
//    protected String doInBackground(String... params) {
//        InputStream inputStream = null;
//        HttpURLConnection urlConnection = null;
//        String response=null;
//        try {
//                /* forming th java.net.URL object */
//            URL url = new URL(StaticConstants.password_change_url);
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//                 /* optional request header */
//            urlConnection.setRequestProperty("Content-Type", "application/json");
//
//                /* optional request header */
//            urlConnection.setRequestProperty("Accept", "application/json");
//
//                /* for Get request */
//            urlConnection.setRequestMethod("GET");
//            int statusCode = urlConnection.getResponseCode();
//
//                /* 200 represents HTTP OK */
//            if (statusCode ==  200) {
//                inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                response =inputStream.toString();
//                //parseResult(response);
////                result = 1; // Successful
//            }else{
//                response = null; //"Failed to fetch data!";
//            }
//        } catch (Exception e) {
//            Log.d("", e.getLocalizedMessage());
//        }
//        return response; //"Failed to fetch data!";
//    }
