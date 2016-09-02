package helix.profitkey.hotelapp;

/** Profit Key 1.0.0
 *  Purpose	   : Passing login details to server
 *  Created by : Abish
 *  Created Dt : 07-03-2016
 *  Modified on: To get email from live DB
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import general.StaticConstants;

public class AsyncLogin extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    private Context context;

    /**Constructor*/
    public AsyncLogin(Login conx){
        context=conx;
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
    protected String doInBackground(String... arg0) {
        String result=null;
        try{
            // url where the data will be posted
            String postReceiverUrl = StaticConstants.login_url;
            Log.v("Login Url:", "postURL: " + postReceiverUrl);
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient
            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header

            /** Add params to the urls */
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //Log.d(uname.getText().toString(), pass.getText().toString());
            nameValuePairs.add(new BasicNameValuePair("user_name", arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("password", arg0[1]));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            httpPost.setEntity(entity);

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(context,"Server Error!",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    /**Function execute last to update*/
    @Override
    protected void onPostExecute(String result) {
        // do stuff after posting data
        progressDialog.dismiss();
        if(result==null)
            Toast.makeText(context, "Server Error or Network Error!", Toast.LENGTH_SHORT).show();
        else {
            int validation = 0;
            JSONObject root = null;
            String user_id = "", message, uuid, name,email;
            try {
                Log.d("Inside Post", "message");
                root = new JSONObject(result);
                validation = root.getInt("response");
                message = root.getString("message");
                Log.d(result, user_id);

                /** After login Success pass values to the login page*/
                if (validation == 1) {
                    user_id = root.getString("userId");
                    uuid = root.getString("uuid");
                    name = root.getString("firstName") + " " + root.getString("lastName");
                    email =root.getString("emailId");
                    ((Login) context).saveSession(user_id, uuid, name,email);
                } else
                    ((Login) context).alertInvoker();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
