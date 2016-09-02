package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Room Availability Displaying Fragment Screen in Tab layout
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import general.AlertMessages;
import general.ApplicationConfigs;
import general.CheckInternet;
import general.StaticConstants;
import helix.profitkey.hotelapp.Booking;
import helix.profitkey.hotelapp.CustomAvailableRoomAdapter;
import helix.profitkey.hotelapp.CustomOffersAdapter;
import helix.profitkey.hotelapp.GetSetAvailableRooms;
import helix.profitkey.hotelapp.R;

public class AvailableRoomFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    /**Global Declarations**/
    View view;
    ApplicationConfigs application;
    TextView category_title,category_room_available,error_text;
    ListView available_room_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GetSetAvailableRooms> availableRoomList = new ArrayList<GetSetAvailableRooms>();
    CheckInternet ci;
    AlertMessages am;
    Calendar c;
    SimpleDateFormat sdf;
    boolean initiatedOnce = false, viewCreated= false,fragmentVisibleHint=false;

    public AvailableRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ci = new CheckInternet(getActivity());
        am = new AlertMessages(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ScrollView scroller = new ScrollView(getActivity());
        view = inflater.inflate(R.layout.available_room_fragment, container, false);
        viewCreated = true;
        initializeViews();
        if(fragmentVisibleHint)
            callSwipePost();
        return view;
    }

    /**This function will be used to execute the code only, if the fragment is visible**/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisibleHint = isVisibleToUser;
        if(isVisibleToUser && !initiatedOnce && viewCreated) {
            callSwipePost();
            initiatedOnce = true;
            viewCreated = false;
        }
    }

    /**Called from onCreateView if view created*/
    private void callSwipePost(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                callAsyncClass();
            }
        });
    }

    /** Called before going to invisible*/
    @Override
    public void onDestroyView(){
        viewCreated =false;
        initiatedOnce = false;
        super.onDestroyView();
    }

    /**Called everytime when the fragment the invoked*/
    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
    }

    /** Initialize the views in the layout**/
    private void initializeViews(){
        available_room_list = (ListView)view.findViewById(R.id.available_room_list);
        category_title = (TextView)view.findViewById(R.id.category_title);
        error_text = (TextView)view.findViewById(R.id.error_text);
        category_room_available = (TextView)view.findViewById(R.id.category_room_available);
        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**Set custom font for views*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceTextViewBold(error_text);
        application.setTypefaceTextViewBold(category_title);
    }

    /**While swiping down it will the called*/
    @Override
    public void onRefresh() {
        callAsyncClass();
    }

    /**Call the AsyncTask to get the Room Details*/
    private void callAsyncClass() {
        c = Calendar.getInstance();
        sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if(ci.isOnline())
            new AsyncGetAvailableRooms(getActivity()).execute(StaticConstants.hotel_id, sdf.format(c.getTime()));
        else
            am.SingleButtonAlert("Internet Error", "Check the connection and try again", "Ok");
    }

    /**AsyncTask to to the rooms available with categories*/
    public class AsyncGetAvailableRooms extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        //ProgressDialog progressDialog;
        Context context;
        GetSetAvailableRooms hgar;
        String check_ofr_categ_lst;

        public AsyncGetAvailableRooms(Context conx) {
            context = conx;
        }

        /**Enter first here to execute the preExecute to show loader*/
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
//            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
//            progressDialog.setMessage("Loading. Please Wait...");
//            progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//            progressDialog.show();
        }

        /**All background process handled here*/
        @Override
        protected String doInBackground(String... arg0) {
            String result = null,postReceiverUrl=null;
            try {
                // url where the data will be posted or retrieved
                    postReceiverUrl = StaticConstants.get_available_rooms_url;
                Log.v(TAG, "postURL: " + postReceiverUrl);
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient
                HttpPost httpPost = new HttpPost(postReceiverUrl);    // post header
                List<NameValuePair> nameValuePairs=null;

                /**Parameter adding to send with url*/
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[0]));
                    nameValuePairs.add(new BasicNameValuePair("today_date", arg0[1]));
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                    httpPost.setEntity(entity);

                // execute HTTP post request
                HttpResponse response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getActivity(),"Server error or Network error",Toast.LENGTH_SHORT).show();
                //am.SingleButtonAlert("Internet Error","Check the connection and try again","Ok");
            }
            return result;
        }

        /**Called to update the status finally*/
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
            swipeRefreshLayout.setRefreshing(false);
            if (result == null)
                Toast.makeText(getActivity(), "Network problem try again", Toast.LENGTH_SHORT).show();
            else {
                //progressDialog.dismiss();
                String message = null;
                JSONObject root;
                int validation = 0, datacount = 0;

                try {
                    Log.d("Inside Post", "message");
                    root = new JSONObject(result);
                    validation = root.getInt("response");
                    message = root.getString("message");
                    Log.d(result, message);

                    /** Pass Received Room details data to CustomAvailableRoomAdapter **/
                    if (validation == 1) {
                        availableRoomList.clear();
                        JSONArray data = root.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            hgar = new GetSetAvailableRooms();
                            hgar.setRoomTypeId(data.getJSONObject(i).getString("room_type_id"));
                            hgar.setRoomType(data.getJSONObject(i).getString("room_type_name"));
                            //hgar.setAvailableRooms(data.getJSONObject(i).getString("available_rooms"));
                            hgar.setPrice(data.getJSONObject(i).getString("price"));
                            //hgar.setTax(data.getJSONObject(i).getString("tax"));
                            hgar.setOfferStatus(data.getJSONObject(i).getInt("offeravailablestatus"));
                            hgar.setRoomTypeDescription(data.getJSONObject(i).getString("room_type_description"));
                            hgar.setFan(data.getJSONObject(i).getInt("fan"));
                            hgar.setAC(data.getJSONObject(i).getInt("AC"));
                            hgar.setFridge(data.getJSONObject(i).getInt("fridge"));
                            hgar.setTV(data.getJSONObject(i).getInt("TV"));
                            hgar.setBar(data.getJSONObject(i).getInt("bar"));
                            hgar.setWifi(data.getJSONObject(i).getInt("wifi"));
                            hgar.setComplementary(data.getJSONObject(i).getString("complementary_details"));
                            hgar.setBedDetail(data.getJSONObject(i).getString("bed_details"));
                            hgar.setGuestAllowed(data.getJSONObject(i).getString("guest_allowed"));
                            hgar.setRoomService(data.getJSONObject(i).getInt("room_service"));
                            hgar.setRestaurantAvailability(data.getJSONObject(i).getInt("restaurant_availability"));
                            hgar.setRommAvailabilityToday(data.getJSONObject(i).getInt("restaurant_availability"));

                            availableRoomList.add(hgar);
                        }
                        CustomAvailableRoomAdapter hca;
                        hca = new CustomAvailableRoomAdapter(context, R.layout.custom_available_roomlist, availableRoomList);
                        available_room_list.setAdapter(hca);
                    } else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        error_text.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, "End Reached/ No data found", Toast.LENGTH_SHORT).show();
                        //text_msg.setText("End Reached/ No data found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
