/**
 *  ProfitKey v1.0
 * 	Purpose	   : Custom List Adapter for showing the offers
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: Modified for new design with image
 *  Verified by:
 *  Verified Dt:
 */

package helix.profitkey.hotelapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fragments.EmailChangeFragment;
import fragments.MobileChangeFragment;
import fragments.OfferFragment;
import general.AlertMessages;
import general.ApplicationConfigs;
import general.StaticConstants;

public class CustomOffersAdapter extends ArrayAdapter<GetSetOffers>{
   /**Global declarations*/
   ApplicationConfigs application;
	ArrayList<GetSetOffers> offerList = new ArrayList<GetSetOffers>();
    OfferFragment frag_context = new OfferFragment(); /**to access the fragment methods*/
    Intent intent;
    Context context;
    private static LayoutInflater inflater=null;
    /**For image loading*/
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    /***  CustomAdapter Constructor, initializer for Offer Fragment entry **/
	public CustomOffersAdapter(Context ofr, int textViewResourceId, ArrayList<GetSetOffers> lists, OfferFragment conx) {
		super(ofr,textViewResourceId, lists);
        offerList=lists;
		context=ofr;
        this.frag_context = conx;
        application = (ApplicationConfigs)ofr.getApplicationContext();
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /**Initializer for image loading*/
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.lock)
                .showImageForEmptyUri(R.drawable.lock)
                .showImageOnFail(R.drawable.lock)
                .cacheInMemory(true)//actual true
                .cacheOnDisk(true)//actual true
                .considerExifParams(true)
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {
                        return Bitmap.createScaledBitmap(bmp, 410, 250, false);
                    }
                })
                //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
	}


	@Override
    public int getCount() {
        return offerList.size();
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
        Button ok;
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        ImageView img;
    }

    /***  Place the values from json into the listview **/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//final Object[] id_obj=h_id.toArray();
        final ViewHolder holder;
        if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_offer_listview, null);

			/** Creates a ViewHolder and store references to the two children to bind data to.*/
			holder=new ViewHolder();
			convertView.setTag(holder);
			/**Custom layout view initialization*/
            holder.tv2=(TextView) convertView.findViewById(R.id.b);
            holder.tv3=(TextView) convertView.findViewById(R.id.c);
            holder.tv6=(TextView) convertView.findViewById(R.id.f);
            holder.img=(ImageView) convertView.findViewById(R.id.img);
            holder.ok=(Button) convertView.findViewById(R.id.ok);

            /**Set custom font*/
            application.setTypefaceTextView(holder.tv2);
            application.setTypefaceTextView(holder.tv3);
            application.setTypefaceTextView(holder.tv6);
            application.setTypefaceButton(holder.ok);

	        }
        else {
			/** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
			holder = (ViewHolder) convertView.getTag();
        }
        if(position%2==0)
            convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
        else
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        /**Set values in view object*/
        holder.tv2.setText(offerList.get(position).getOfferTitle());
        holder.tv3.setText(offerList.get(position).getOfferCategory());
//        holder.tv4.setText(offerList.get(position).getOfferFromDt());
//        holder.tv5.setText(offerList.get(position).getOfferToDt());
        holder.tv6.setText("Rs : "+offerList.get(position).getOfferPrice());
        ImageLoader.getInstance().displayImage(offerList.get(position).getImageUrl(), holder.img, options, animateFirstListener);

        /**Button Click will call booking page and pass the values*/
        holder.ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncGetImageUrls().execute(offerList.get(position).getOfferCategory());
                intent = new Intent().setClass(context, Booking.class);
                intent.putExtra("intent_invoker", "offer_page");
                intent.putExtra("offer_id",offerList.get(position).getOfferId());
                intent.putExtra("hotel_id",offerList.get(position).getHotelId());
                intent.putExtra("room_type_id",offerList.get(position).getRoomTypeId());
                intent.putExtra("offer_title",offerList.get(position).getOfferTitle());
                intent.putExtra("offer_description",offerList.get(position).getOfferDescrip());
                intent.putExtra("offer_from",offerList.get(position).getOfferFromDt());
                intent.putExtra("offer_to",offerList.get(position).getOfferToDt());
                intent.putExtra("offer_price", offerList.get(position).getOfferPrice());
                //intent.putExtra("tax", offerList.get(position).getTax());
                intent.putExtra("room_type_name",offerList.get(position).getOfferCategory());
                intent.putExtra("length_of_stay",offerList.get(position).getLengthOfStay());
                intent.putExtra("inclusions",offerList.get(position).getInclusions());
                intent.putExtra("food_options",offerList.get(position).getFoodOptions());
                intent.putExtra("event_options",offerList.get(position).getEventOptions());
                intent.putExtra("room_description",offerList.get(position).getRoomTypeDescription());
                intent.putExtra("fan",offerList.get(position).getFan());
                intent.putExtra("ac",offerList.get(position).getAC());
                intent.putExtra("fridge",offerList.get(position).getFridge());
                intent.putExtra("tv",offerList.get(position).getTV());
                intent.putExtra("bar",offerList.get(position).getBar());
                intent.putExtra("wifi",offerList.get(position).getWifi());
                intent.putExtra("complement",offerList.get(position).getComplementary());
                intent.putExtra("bed",offerList.get(position).getBedDetail());
                intent.putExtra("allowed_guest",offerList.get(position).getGuestAllowed());
                intent.putExtra("room_service",offerList.get(position).getRoomService());
                intent.putExtra("avail_restaurant",offerList.get(position).getRestaurantAvailability());
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                //Toast.makeText(getContext(),"You clicked: "+position,Toast.LENGTH_SHORT).show();
               // if(string_intent.equals("offers")) {

//                new AsyncGetImageUrls().execute(offerList.get(position).getOfferCategory());
//                    intent = new Intent().setClass(context, Booking.class);
//                    intent.putExtra("intent_invoker","offer_page");
//                    intent.putExtra("offer_id",offerList.get(position).getOfferId());
//                    intent.putExtra("hotel_id",offerList.get(position).getHotelId());
//                    intent.putExtra("room_type_id",offerList.get(position).getRoomTypeId());
//                    intent.putExtra("offer_title",offerList.get(position).getOfferTitle());
//                    intent.putExtra("offer_description",offerList.get(position).getOfferDescrip());
//                    intent.putExtra("offer_from",offerList.get(position).getOfferFromDt());
//                    intent.putExtra("offer_to",offerList.get(position).getOfferToDt());
//                    intent.putExtra("offer_price",offerList.get(position).getOfferPrice());
//                    intent.putExtra("room_type_name",offerList.get(position).getOfferCategory());
//                    context.startActivity(intent);
//                }
        	}
        });
        return convertView;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**Get URLs Asynchronously of clicked offers to show slider*/
    public class AsyncGetImageUrls extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        ProgressDialog progressDialog;
        AlertMessages am;

        /**Enter first here to execute the preExecute to show loader*/
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Loading. Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            progressDialog.show();
        }

        /**All background process handled here*/
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

                /**Parameter setting for url to send values*/
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

        /**Called to update the status finally*/
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
                    if (validation == 1) {
                        data = root.getJSONArray("data");
                        String[] url=new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            url[i] = data.getJSONObject(i).getString("image_url");
                        }
                        /**Starting the booking class with received url*/
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