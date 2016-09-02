package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Display Booking History Fragment Screen in Tab layout(Last Tab)
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import general.AlertMessages;
import general.ApplicationConfigs;
import general.CheckInternet;
import general.StaticConstants;
import helix.profitkey.hotelapp.CustomAdapterBookedHistory;
import helix.profitkey.hotelapp.CustomOffersAdapter;
import helix.profitkey.hotelapp.GetSetBookedHistory;
import helix.profitkey.hotelapp.R;


public class BookedHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    /**Global declarations*/
    View view;
    ApplicationConfigs application;
    private ListView list_history;
    private TextView name,load_more_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GetSetBookedHistory> bookedHistory = new ArrayList<GetSetBookedHistory>();
    CustomAdapterBookedHistory cabh;
    CheckInternet ci;
    AlertMessages am;
    Context context;
    String uuid,name1;
    int page_no=0;
    boolean initiatedOnce = false,calledOncreateView = false,fragmentVisibleHint=false;

    public BookedHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Get stored uuid and name*/
        SharedPreferences sp=this.getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        uuid = sp.getString("profit_key_uuid", "");
        name1 = sp.getString("profit_key_users_name","");
        ci = new CheckInternet(getActivity());
        am = new AlertMessages(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ScrollView scroller = new ScrollView(getActivity());

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.booking_history_fragment, container, false);

        /**View initializer*/
        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);
        list_history = (ListView) view.findViewById(R.id.list_history);
        name = (TextView) view.findViewById(R.id.name);
        load_more_text = (TextView) view.findViewById(R.id.load_more_text);
        name.setText(name1);
        calledOncreateView = true;

        /**Set custom font in views*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceTextViewBold(load_more_text);
        application.setTypefaceTextViewBold(name);

        swipeRefreshLayout.setOnRefreshListener(this);
        if(fragmentVisibleHint)
            callSwipePost();
        list_history.setOnScrollListener(onScrollListener());

        context=view.getContext();

        return view;
    }

    /**While swiping down it will the called*/
    @Override
    public void onRefresh() {
        page_no=0;
        //bookedHistory.clear();
        callAsyncClass();
    }

    /**This function will be used to execute the code only, if the fragment is visible**/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisibleHint=isVisibleToUser;
        if(isVisibleToUser  && !initiatedOnce && calledOncreateView){
            callSwipePost();
            calledOncreateView = false;
            initiatedOnce = true;
        }
    }

    /**Called from onCreateView if view created*/
    public void callSwipePost() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                callAsyncClass();
            }
        });
    }

    /** Pagination implemented with onScroll action*/
    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = list_history.getCount();
                String text = load_more_text.getText().toString();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (list_history.getLastVisiblePosition() >= count - threshold &&
                            load_more_text.getText().toString().equals("Loading...") && count%10==0) {// && pageCount < 2
                        //Log.i(TAG, "loading more data");
                        // Execute LoadMoreDataTask AsyncTask
                        page_no++;
                        load_more_text.setVisibility(View.VISIBLE);
                        callAsyncClass();
                        //Toast.makeText(getActivity(), "You Reached End", Toast.LENGTH_SHORT).show();
                    }
                }//else
                //text_msg.setText("");
                //text_msg.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }

    /** Called before going to invisible*/
    @Override
    public void onDestroyView(){
        initiatedOnce = false;
        super.onDestroyView();
    }

    /**Called everytime when the fragment the invoked*/
    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
//        initiatedOnce = false;
        cabh=new CustomAdapterBookedHistory(context,R.layout.custom_layout_booked_history,
                bookedHistory,"booking_history");
        list_history.setAdapter(cabh);
        bookedHistory.clear();
        page_no=0;
    }

    /**Call the AsyncTask to get booking history*/
    private void callAsyncClass(){
        if(ci.isOnline())
            new AsyncBookingHistory(getActivity()).execute(StaticConstants.hotel_id, uuid, page_no+"");
        else
            am.SingleButtonAlert("Internet Error", "Check the connection and try again", "Ok");
    }

    /**AsyncTask to get the booking history details from live DB*/
    public class AsyncBookingHistory extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        //ProgressDialog progressDialog;
        Context context;
        GetSetBookedHistory gsbh;

        public AsyncBookingHistory(Context conx) {
            context = conx;
        }

        /**Enter first here to execute the preExecute to show loader*/
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
//            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
//            progressDialog.setMessage("Loading. Booking History...");
//            progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//            progressDialog.show();
        }

        /**All background process handled here*/
        @Override
        protected String doInBackground(String... arg0) {
            String result = null;
            try {
                // url where the data will be posted or retrieved
                String postReceiverUrl = StaticConstants.get_booking_history_url;
                Log.v(TAG, "postURL: " + postReceiverUrl);
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient
                HttpPost httpPost = new HttpPost(postReceiverUrl);    // post header

                /**Parameter adding to send with url*/
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[0]));
                nameValuePairs.add(new BasicNameValuePair("uuid", arg0[1]));
                nameValuePairs.add(new BasicNameValuePair("pageno", arg0[2]));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                httpPost.setEntity(entity);

                // execute HTTP post request
                HttpResponse response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                //e.printStackTrace();
                //am.SingleButtonAlert("Server Error","Error, try again","Ok");
                Toast.makeText(getActivity(), "Internet Connection problem", Toast.LENGTH_SHORT).show();
                return null;
            }
            return result;
        }

        /**Called to update the status finally*/
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
            //progressDialog.dismiss();

            swipeRefreshLayout.setRefreshing(false);
            if (result == null)
                Toast.makeText(getActivity(), "Network problem try again", Toast.LENGTH_SHORT).show();
            else {
                String message = null;
                JSONObject root;
                int validation = 0, datacount;
                try {
                    Log.d("Inside Post", "message");
                    root = new JSONObject(result);
                    validation = root.getInt("response");
                    message = root.getString("message");
                    datacount = root.getInt("datacount");
                    Log.d(result, message);

                    /** Pass Received booked history details data to CustomBookedHistoryAdapter **/
                    if (datacount > 0 && validation == 1 && message.equals("Success")) {
                        JSONArray data = root.getJSONArray("data");
                        if (page_no == 0)
                            bookedHistory.clear();
                        for (int i = 0; i < data.length(); i++) {
                            gsbh = new GetSetBookedHistory();
                            gsbh.setHotelId(data.getJSONObject(i).getString("hotel_id"));
                            gsbh.setOfferUsedStatus(data.getJSONObject(i).getString("offer_id"));
                            gsbh.setOfferTitle(data.getJSONObject(i).getString("offer_title"));
                            gsbh.setOfferDescription(data.getJSONObject(i).getString("offer_description"));
                            //gsbh.setOfferPrice(data.getJSONObject(i).getString(""));
                            gsbh.setRoomType(data.getJSONObject(i).getString("room_type_name"));
                            gsbh.setFromDate(data.getJSONObject(i).getString("from_date"));
                            gsbh.setToDate(data.getJSONObject(i).getString("to_date"));
                            gsbh.setTotalDays(data.getJSONObject(i).getString("total_days"));
                            gsbh.setTotalPrice(data.getJSONObject(i).getString("price"));
                            gsbh.setBookedDate(data.getJSONObject(i).getString("booked_date"));

                            bookedHistory.add(gsbh);
                        }
                        cabh.notifyDataSetChanged();
                        /**For loading more validation condition*/
                        if (data.length() < 10) {
                            load_more_text.setVisibility(View.GONE);
                            load_more_text.setText("End Reached");
                        }
                    } /**For loading more validation condition*/
                    else if (datacount > 0 && validation == 1 && message.equals("Success no more data")) {
                        load_more_text.setVisibility(View.GONE);
                        load_more_text.setText("End Reached");
                    } /**For unavailability of hotels*/
                    else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        load_more_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        load_more_text.setVisibility(View.VISIBLE);
                        load_more_text.setTextSize(30);
                        load_more_text.setText("No\nbooking\nhistory\nfor\nyou");
                    }

//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
