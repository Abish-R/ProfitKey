/** Profit Key v1.0.0
 *  Purpose	   : Custom Available room Adapter to get view
 *  Created by : Abish
 *  Created Dt :
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.profitkey.hotelapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import general.AlertMessages;
import general.ApplicationConfigs;
import general.StaticConstants;

public class CustomAvailableRoomAdapter extends ArrayAdapter<GetSetAvailableRooms>{
    Intent intent;
    ApplicationConfigs application;
	ArrayList<GetSetAvailableRooms> availList = new ArrayList<GetSetAvailableRooms>();
    Context context;
    private static LayoutInflater inflater=null;

    /***  CustomAdapter Constructor, initializer for Hotel Master entry **/
	public CustomAvailableRoomAdapter(Context room, int textViewResourceId, ArrayList<GetSetAvailableRooms> lists) {
		super(room,textViewResourceId, lists);
        availList=lists;
		context=room;
        application = (ApplicationConfigs)room.getApplicationContext();
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
    public int getCount() {
        return availList.size();
    }

//    @Override
//    public Object getItem(int position) {
//        return position;
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView tv2,tv3,tv4;
        Button book,offer_avail;
    }

    /***  Place the values from json into the listview **/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//final Object[] id_obj=h_id.toArray();
        final ViewHolder holder;
        if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_available_roomlist, null);

			/** Creates a ViewHolder and store references to the two children to bind data to.*/
			holder=new ViewHolder();
			convertView.setTag(holder);

            /** Initialize custom view*/
			//holder.tv1=(TextView) convertView.findViewById(R.id.a);
            holder.tv2=(TextView) convertView.findViewById(R.id.room_type);
            //holder.tv3=(TextView) convertView.findViewById(R.id.offer_avail);
            holder.tv4=(TextView) convertView.findViewById(R.id.room_price);
            holder.book=(Button) convertView.findViewById(R.id.book);
            holder.offer_avail=(Button) convertView.findViewById(R.id.offer_avail);

            /** Set custom font*/
            application.setTypefaceTextView(holder.tv2);
            application.setTypefaceTextView(holder.tv4);
            application.setTypefaceButton(holder.book);
            application.setTypefaceButton(holder.offer_avail);

            intent = new Intent().setClass(context, Booking.class);
	        }
        else {
			/** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
			holder = (ViewHolder) convertView.getTag();
        }
        if(position%2==0)
        	convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
        else
        	convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        /**  set values and enabling views*/
        holder.tv2.setText(availList.get(position).getRoomType());
        if(availList.get(position).getOfferStatus()==1)
            holder.offer_avail.setVisibility(View.VISIBLE);

        holder.tv4.setText("Price/ day: "+availList.get(position).getPrice());

        /** Book button call Async Get Image Url class and get the current category all images.
         * send below details with received url to booking screen.*/
        holder.book.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncGetImageUrls().execute(availList.get(position).getRoomType());

                intent.putExtra("intent_invoker", "available_page");
                intent.putExtra("room_type_id", availList.get(position).getRoomTypeId());
                intent.putExtra("room_type", availList.get(position).getRoomType());
                intent.putExtra("available", availList.get(position).getAvailableRooms());
                intent.putExtra("price_per_day", availList.get(position).getPrice());
                //intent.putExtra("tax", availList.get(position).getTax());
                intent.putExtra("room_description",availList.get(position).getRoomTypeDescription());
                intent.putExtra("fan",availList.get(position).getFan());
                intent.putExtra("ac",availList.get(position).getAC());
                intent.putExtra("fridge",availList.get(position).getFridge());
                intent.putExtra("tv",availList.get(position).getTV());
                intent.putExtra("bar",availList.get(position).getBar());
                intent.putExtra("wifi",availList.get(position).getWifi());
                intent.putExtra("complement",availList.get(position).getComplementary());
                intent.putExtra("bed",availList.get(position).getBedDetail());
                intent.putExtra("allowed_guest",availList.get(position).getGuestAllowed());
                intent.putExtra("room_service",availList.get(position).getRoomService());
                intent.putExtra("avail_restaurant", availList.get(position).getRestaurantAvailability());
                intent.putExtra("avail_room_today",availList.get(position).getRommAvailabilityToday());

                //context.startActivity(intent);
            }
        });

        /** Offer button take you to the offer fragment with filter of selected category*/
        holder.offer_avail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((SimpleTabsActivity) context).sendToActivity(availList.get(position).getRoomType());
                    ((SimpleTabsActivity) context).navigateToTabs(0);
            }
        });

        /** Book button call Async Get Image Url class and get the current category all images.
         * send below details with received url to booking screen.*/
        convertView.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
                new AsyncGetImageUrls().execute(availList.get(position).getRoomType());

                intent.putExtra("intent_invoker","available_page");
                intent.putExtra("room_type_id",availList.get(position).getRoomTypeId());
                intent.putExtra("room_type",availList.get(position).getRoomType());
                intent.putExtra("price_per_day",availList.get(position).getPrice());
                intent.putExtra("room_description",availList.get(position).getRoomTypeDescription());
                intent.putExtra("fan",availList.get(position).getFan());
                intent.putExtra("ac",availList.get(position).getAC());
                intent.putExtra("fridge",availList.get(position).getFridge());
                intent.putExtra("tv",availList.get(position).getTV());
                intent.putExtra("bar",availList.get(position).getBar());
                intent.putExtra("wifi",availList.get(position).getWifi());
                intent.putExtra("complement",availList.get(position).getComplementary());
                intent.putExtra("bed",availList.get(position).getBedDetail());
                intent.putExtra("allowed_guest",availList.get(position).getGuestAllowed());
                intent.putExtra("room_service", availList.get(position).getRoomService());
                intent.putExtra("avail_restaurant",availList.get(position).getRestaurantAvailability());
                //context.startActivity(intent);


//                Intent intent = new Intent().setClass(context, Booking.class);
//                intent.putExtra("intent_invoker","available_page");
//                intent.putExtra("room_type_id",availList.get(position).getRoomTypeId());
//                intent.putExtra("room_type",availList.get(position).getRoomType());
//                intent.putExtra("available",availList.get(position).getAvailableRooms());
//                intent.putExtra("price_per_day",availList.get(position).getPrice());
//
//                context.startActivity(intent);
        	}
        });
        return convertView;
    }

    /** Async class to get the selected category Url from DB*/
    public class AsyncGetImageUrls extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        ProgressDialog progressDialog;
        AlertMessages am;

        /** First function with loader screen*/
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Loading. Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            progressDialog.show();
        }

        /** Function to be performed in background **/
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String response="";
            try {
                /* forming th java.net.URL object */
                URL url = new URL(StaticConstants.room_type_slider_image_getting_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("room_type_name", params[0]);
                //.appendQueryParameter("room_type_name", params[1])
                //.appendQueryParameter("new_password", params[2]);
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

        /** Last function to be executed **/
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
            progressDialog.dismiss();
            if(result==null || result.equals(""))
                Toast.makeText(context,"Server Error or Network Error!",Toast.LENGTH_SHORT);
            else {
                String message = null;
                JSONObject root;
                JSONArray data = null;
                int validation = 0;

                try {
                    Log.d("Inside Post", "message");
                    root = new JSONObject(result);
                    validation = root.getInt("response");
                    message = root.getString("message");
                    Log.d(result, message);

                    /** On success store the url and start the Booking screen activity*/
                    if (validation == 1) {
                        data = root.getJSONArray("data");
                        String[] url=new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            url[i] = data.getJSONObject(i).getString("image_url");
                        }
                        intent.putExtra("url_array",url);
                        context.startActivity(intent);
                    }
                    else
                        am.SingleButtonAlertNoTile(message,"Ok");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}