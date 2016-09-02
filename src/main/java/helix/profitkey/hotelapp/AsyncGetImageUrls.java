//package helix.profitkey.hotelapp;
//
//import android.app.ActionBar;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import general.AlertMessages;
//import general.StaticConstants;
//
///**
// * Created by HelixTech-Admin on 4/29/2016.
// */
//public class AsyncGetImageUrls extends AsyncTask<String, Void, String> {
//    private static final String TAG = "Inside Thread HotelAdd";
//    ProgressDialog progressDialog;
//    Context context;
//    AlertMessages am;
//
//    AsyncGetImageUrls(Context conx){
//        context = conx;
//    }
//
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
//        progressDialog.setMessage("Loading. Please Wait...");
//        progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        progressDialog.show();
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        HttpURLConnection urlConnection = null;
//        String response="";
//        try {
//                /* forming th java.net.URL object */
//            URL url = new URL(StaticConstants.room_type_slider_image_getting_url);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//
//            Uri.Builder builder = new Uri.Builder().appendQueryParameter("room_type_name", params[0]);
//            //.appendQueryParameter("room_type_name", params[1])
//            //.appendQueryParameter("new_password", params[2]);
//            String query = builder.build().getEncodedQuery();
//
//            //OutputStream os = urlConnection.getOutputStream();
//            DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());
//            writer.writeBytes(query);
//            writer.flush();
//            writer.close();
//            writer.close();
//
//            int statusCode = urlConnection.getResponseCode();
//                /* 200 represents HTTP OK */
//            if (statusCode ==  200) {
//                String result;
//                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                while ((result=br.readLine()) != null) {
//                    response+=result;
//                }
//            }else{
//                response = ""; //"Failed to fetch data!";
//            }
//        } catch (Exception e) {
//            Log.d("", e.getLocalizedMessage());
//        }
//        return response; //"Failed to fetch data!";
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // stuff after posting data
//        progressDialog.dismiss();
//        if(result==null || result.equals(""))
//            Toast.makeText(context, "Server Error or Network Error!", Toast.LENGTH_SHORT);
//        else {
//            String message = null;
//            JSONObject root;
//            JSONArray data = null;
//            int validation = 0;
//
//            try {
//                Log.d("Inside Post", "message");
//                root = new JSONObject(result);
//                validation = root.getInt("response");
//                message = root.getString("message");
//                Log.d(result, message);
//                if (validation == 1) {
//                    data = root.getJSONArray("data");
//                    String[] url=new String[data.length()];
//                    for (int i = 0; i < data.length(); i++) {
//                        url[i] = data.getJSONObject(i).getString("image_url");
//                    }
//                    ((AvailableRoomDescription)context).gotUrls(url);
//                    //AsyncGetImageUrls().
////                    intent.putExtra("url_array",url);
////                    context.startActivity(intent);
//                }
//                else
//                    am.SingleButtonAlertNoTile(message,"Ok");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}