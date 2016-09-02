/** Profit Key 1.0.0
 * 	Purpose	   : Sign Up for users, sends info to server
 *  Created by : Abish 
 *  Created Dt : 05-03-2016
 *  Modified on: 
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.profitkey.hotelapp;

import java.util.ArrayList;
import java.util.List;

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

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import general.AlertMessages;

public class AsyncSignUp extends AsyncTask<String, Void, String> {
	/**Class Variables*/
	Context context;
	String TAG="Async SignUp";
	int validation=0;
	JSONObject root;
	ProgressDialog progressDialog;
	AlertMessages am;

	/**Constructor*/
	public AsyncSignUp(SignUp signUp) {
		//this.activity = activity;
		context = signUp;
		am = new AlertMessages(context);
	}

	/**Before Sending Data*/
	protected void onPreExecute() {
		super.onPreExecute();
		// do stuff before posting data
		progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Loading. Please Wait...");
		progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		progressDialog.show();
	}

	/**While sending datas to DB, do in background*/
	@Override
	protected String doInBackground(String... arg0) {
		String result=null;
		try{
			// url where the data will be posted                	
			String postReceiverUrl = "http://phpws.profitkeyapp.com/RUC_CustomreSignUp";
			Log.v(TAG, "postURL: " + postReceiverUrl);                   
			HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
			HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
                    
			/** our data for sending - parameters*/
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(12);
			//Log.d(uname.getText().toString(), pass.getText().toString());
			nameValuePairs.add(new BasicNameValuePair("first_name", arg0[0]));
			nameValuePairs.add(new BasicNameValuePair("last_name", arg0[1]));
			nameValuePairs.add(new BasicNameValuePair("user_name", arg0[2]));
			nameValuePairs.add(new BasicNameValuePair("password", arg0[3]));
			nameValuePairs.add(new BasicNameValuePair("gender", arg0[4]));
			nameValuePairs.add(new BasicNameValuePair("dob", arg0[5]));
			nameValuePairs.add(new BasicNameValuePair("mobile_number", arg0[6]));
			nameValuePairs.add(new BasicNameValuePair("email_id", arg0[7]));
			nameValuePairs.add(new BasicNameValuePair("city", arg0[8]));
			nameValuePairs.add(new BasicNameValuePair("country", arg0[9]));
			nameValuePairs.add(new BasicNameValuePair("zip_code", arg0[10]));
			nameValuePairs.add(new BasicNameValuePair("device_token", arg0[11]));//
			nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[12]));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
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

	/**After Data passed, returned datas are below*/
	@Override
	protected void onPostExecute(String result) {
		// do stuff after posting data
		progressDialog.dismiss();
		if(result==null)
			Toast.makeText(context,"Server Error or Network Error!",Toast.LENGTH_SHORT);
		else {
			String message = null, user_id = null;
			try {
				Log.d("Inside Post", "message");
				root = new JSONObject(result);
				validation = root.getInt("response");
				user_id = root.getString("userId");
				message = root.getString("message");
				Log.d(result, user_id);

				/** On success */
				if (validation == 1) {
					if (message.equals("This username is already registered"))
						am.SingleButtonAlertNoTile("Already Registered.!", "Ok");
					else {
						((SignUp) context).saveCustomerDetails();
					}
				} else {
					am.SingleButtonAlert("SignUp Error",message + ".!","Retry");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
