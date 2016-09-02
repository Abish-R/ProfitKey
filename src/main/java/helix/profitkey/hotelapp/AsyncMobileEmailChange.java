package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Mobile and Email change AsyncTask
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fragments.EmailChangeFragment;
import fragments.MobileChangeFragment;
import general.AlertMessages;
import general.StaticConstants;

public class AsyncMobileEmailChange extends AsyncTask<String, Void, String> {
    private static final String TAG = "Inside Thread HotelAdd";
    ProgressDialog progressDialog;
    Context context;
    AlertMessages am;
    EmailChangeFragment context1 =  new EmailChangeFragment();
    MobileChangeFragment context2 = new MobileChangeFragment();
    int invoker;

    /**Constructors*/
    public AsyncMobileEmailChange(Context conx, EmailChangeFragment ecf,int val) {
       context = conx;
        context1 = ecf;
        invoker=val;
        am = new AlertMessages(context);
    }
    public AsyncMobileEmailChange(Context conx, MobileChangeFragment mcf,int val,int dummy) {
        context = conx;
        context2 = mcf;
        invoker=val;
        am = new AlertMessages(context);
    }

    /*** Loader screen **/
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
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String response="";
        try {
                /* forming th java.net.URL object */
            URL url = new URL(StaticConstants.email_mobile_change_url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

//            Uri.Builder builder = new Uri.Builder().appendQueryParameter("uuid", params[0])
//                    .appendQueryParameter("existing_password", params[1])
//                    .appendQueryParameter("new_password", params[2]);
//            String query = builder.build().getEncodedQuery();

            //OutputStream os = urlConnection.getOutputStream();

            /** Add params to the urls */
            DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());
            if(invoker==1)
                writer.writeBytes(context1.setUrlParams(params[0],params[1]));
            else if(invoker==2)
                writer.writeBytes(context2.setUrlParams(params[0],params[1]));
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
        // stuff after posting data
        progressDialog.dismiss();
        if(result==null || result.equals(""))
            Toast.makeText(context,"Server Error or Network Error!",Toast.LENGTH_SHORT);
        else {
            String message = null,email;
            JSONObject root;
            int validation = 0;

            try {
                Log.d("Inside Post", "message");
                root = new JSONObject(result);
                validation = root.getInt("response");
                message = root.getString("message");
                email = root.getString("updated_email_id");
                Log.d(result, message);

                /** On Success change fragment call */
                if (validation == 1 && invoker==1)
                    context1.changeFragment(email);
                else if (validation == 1 && invoker==2)
                    context2.changeFragment();
                else
                    am.SingleButtonAlertNoTile(message,"Ok");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
