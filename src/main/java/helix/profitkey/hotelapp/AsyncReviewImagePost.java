package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Post review image
 *  Created by : Abish
 *  Created Dt : 3/15/2016
 *  Modified on:
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import general.StaticConstants;

public class AsyncReviewImagePost extends AsyncTask<String, Void, String> {
    private static final String TAG = "Inside Thread HotelAdd";
    ProgressDialog progressDialog;
    Context context;
    File image_path;

    /**Constructor*/
    public AsyncReviewImagePost(Context conx, File img_path) {
       context = conx;
        image_path=img_path;
    }

    /** Loader screen  **/
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("Posting Review. Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        progressDialog.show();
    }

    /** Function to handle background operations*/
    @Override
    protected String doInBackground(String... arg0) {
        String result = null,requestURL=null, charset = "UTF-8";
        if(arg0[0].equals("edit"))
            requestURL=StaticConstants.review_edit_image_post_url;
        else
        //requestURL = "http://phpws.betterthegame.com/BetterTheGame/WS/fileupload";
            requestURL=StaticConstants.review_image_post_url;

        /** Pass image and data to multipart*/
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            if(image_path!=null)
                multipart.addFilePart("picture", image_path);
            if(arg0[0].equals("edit")){
                multipart.addFormField("review_id", arg0[1]);
                multipart.addFormField("review_title", arg0[2]);
                multipart.addFormField("review_description", arg0[3]);
            }else {
                multipart.addFormField("hotel_id", arg0[1]);
                multipart.addFormField("uuid", arg0[2]);
                multipart.addFormField("review_title", arg0[3]);
                multipart.addFormField("review_description", arg0[4]);
            }

            /** Response which I received */
            List<String> response = multipart.finish();
            //result=multipart.finish();
            System.out.println("SERVER REPLIED:");
            if(image_path==null)
                result=response.get(response.size()-1);
            else
                result=response.get(0);
            for (String line : response) {
                System.out.println(line);
                //Toast.makeText(context, "txt " + line, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ex) {
        System.err.println(ex);
        }
        return result;
    }

    /**Function execute last to update*/
    @Override
    protected void onPostExecute(String result) {
        // stuff after posting data
        progressDialog.dismiss();
        if(result==null)
            Toast.makeText(context, "Server Error or Network Error!", Toast.LENGTH_SHORT);
        else {
            int validation = 0;
            JSONObject root = null;
            String message;
            try {
                Log.d("Inside Post", "message");
                root = new JSONObject(result);
                validation = root.getInt("response");
                message = root.getString("message");

                /** After successfull image upload */
                if (validation == 1)
                    ((ReviewWriteNew) context).imageUploaded(image_path);
                else
                    ((ReviewWriteNew) context).imageUploaded(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
