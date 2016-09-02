package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Display available Offers Fragment Screen in Tab layout(First Tab)
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import helix.profitkey.hotelapp.GetSetOffers;
import helix.profitkey.hotelapp.CustomOffersAdapter;
import helix.profitkey.hotelapp.R;
import helix.profitkey.hotelapp.SimpleTabsActivity;


public class OfferFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {//View.OnClickListener
    /**Global Declarations**/
    View view;
    ApplicationConfigs application;
    public ListView offers_list;
    Spinner offer_category_list;
    TextView text_msg;
    CustomOffersAdapter hca;
    ArrayAdapter<String> array_adapter;
    ArrayList<GetSetOffers> offerList = new ArrayList<GetSetOffers>();
    CheckInternet ci;
    AlertMessages am;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean firstTime=true, initiatedOnce = false, viewCreated= false,fragmentVisibleHint=false;
    String categ;
    int page_no=0;
    String[] hotel_category;
    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ci = new CheckInternet(getActivity());
        am = new AlertMessages(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //ScrollView scroller = new ScrollView(getActivity());
        view = inflater.inflate(R.layout.offers_fragment, container, false);
        viewInitialize();
        viewCreated = true;

        offer_category_list.setOnItemSelectedListener(this);
        offers_list.setOnScrollListener(onScrollListener());
        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                callAsyncClass();
//            }
//        });
        //context=view.getContext();
        if(fragmentVisibleHint)
            getCategories();
        return view;
    }

    /**This function will be used to execute the code only, if the fragment is visible**/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisibleHint = isVisibleToUser;
        if(initiatedOnce && ((SimpleTabsActivity)getActivity()).getSelectedOfferFromActivity()!=null)
            initiatedOnce=false;
        if (isVisibleToUser && !initiatedOnce && viewCreated) {
            getCategories();
            initiatedOnce = true;
            //viewCreated = false;
        }

    }

    /**Call the Asynctask class to get the available categories of room**/
    private void getCategories(){
        if (ci.isOnline())
            new AsyncOffers(getActivity(), "category").execute("category", "http://phpws.profitkeyapp.com/RUC_GetOfferCategories");
        else
            am.SingleButtonAlert("Internet Error", "Check the connection and try again", "Ok");
    }

    /** Initialize the views in the layout**/
    private void viewInitialize(){
        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);
        offers_list = (ListView) view.findViewById(R.id.offers_list);
        offer_category_list = (Spinner) view.findViewById(R.id.offer_category_list);
        text_msg = (TextView) view.findViewById(R.id.text_msg);

        /**Set custom font*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceTextViewBold(text_msg);
    }

    /**While swiping down it will the called*/
    @Override
    public void onRefresh() {
        callOfferAsyncTask();
    }

//    @Override
//    public void onPause(){
//        //array_adapter.clear();
//    }

    /** Called before going to invisible*/
    @Override
    public void onDestroyView(){
        viewCreated = false;
        initiatedOnce = false;
        super.onDestroyView();
    }

    /**Called everytime when the fragment the invoked*/
    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        hca = new CustomOffersAdapter(getActivity(), R.layout.custom_offer_listview, offerList,OfferFragment.this);
        offers_list.setAdapter(hca);
        offerList.clear();
        firstTime=true;
        page_no=0;
    }

//    public void getAdapterView(ArrayList<GetSetOffers> hotelList){
//        Log.d("Passing Context",getActivity().toString());
//        temm=hotelList;
//    }

    /**Called from onCreateView or after the Category received*/
    public void callSwipePost(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                page_no=0;
                callOfferAsyncTask();
            }
        });
    }

    /** Pagination implemented with onScroll action*/
    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {
            /**After onScroll*/
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = offers_list.getCount();
                String text = text_msg.getText().toString();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (offers_list.getLastVisiblePosition() >= count - threshold &&
                            text_msg.getText().toString().equals("Loading...") && count%10==0) {// && pageCount < 2
                        //Log.i(TAG, "loading more data");
                        // Execute LoadMoreDataTask AsyncTask
                        page_no++;
                        text_msg.setVisibility(View.VISIBLE);
                        callOfferAsyncTask();
                        //Toast.makeText(getActivity(),"You Reached End", Toast.LENGTH_SHORT).show();
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

    /**Call for AsyncTask to get the Offers*/
    private void callOfferAsyncTask(){
        if(ci.isOnline())
            new AsyncOffers(getActivity(),"offers").execute("offers", StaticConstants.hotel_id, categ, page_no+"");
        else
            am.SingleButtonAlert("Internet Error", "Check the connection and try again", "Ok");
    }

    /**Spinner item selected action is coded inside**/
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        //Toast.makeText(getActivity(),parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        categ = parent.getItemAtPosition(position).toString();

        if(position==0 && firstTime) {
            firstTime=false;
        }
        else {
            page_no=0;
            offerList.clear();
            text_msg.setText("Loading...");
            text_msg.setVisibility(View.GONE);
            callOfferAsyncTask();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    /**Both for Offers Category and Offers retrieval from live DB*/
    public class AsyncOffers extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        ProgressDialog progressDialog;
        Context context;
        GetSetOffers hgs;
        String check_ofr_categ_lst,invokers;

        /**Constructor just coded not required*/
        public AsyncOffers(Context conx,String invoker) {
            context = conx;
            invokers = invoker;
        }

        /**Enter first here to execute the preExecute to show loader*/
        protected void onPreExecute() {
            super.onPreExecute();
            if(invokers.equals("offers")) {
                swipeRefreshLayout.setRefreshing(true);
            }else{
                progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
                progressDialog.setMessage("Loading. Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                progressDialog.show();
            }
        }

        /**All background process handled here*/
        @Override
        protected String doInBackground(String... arg0) {
            String result = null,postReceiverUrl=null;
            check_ofr_categ_lst=arg0[0];
            try {
                // url where the data will be posted or retrieved
                if(check_ofr_categ_lst.equals("offers"))
                    postReceiverUrl = StaticConstants.get_offers_url;//http://ridio.in/profitkey/RUC_GetOffers
                else
                    postReceiverUrl=arg0[1];
                Log.v(TAG, "postURL: " + postReceiverUrl);
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient
                HttpPost httpPost = new HttpPost(postReceiverUrl);    // post header
                List<NameValuePair> nameValuePairs=null;

                /**Parameter adding to send with url*/
                if(check_ofr_categ_lst.equals("offers")) {
                    nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[1]));
                    nameValuePairs.add(new BasicNameValuePair("offer_category", arg0[2]));
                    nameValuePairs.add(new BasicNameValuePair("pageno", arg0[3]));
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                    httpPost.setEntity(entity);
                }

                // execute HTTP post request
                HttpResponse response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                //e.printStackTrace();
                //am.SingleButtonAlert("Server Error","Error, try again","Ok");
                //Toast.makeText(getActivity(), "Internet Connection problem.", Toast.LENGTH_SHORT).show();
                return result;
            }
            return result;
        }

        /**Called to update the status finally*/
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
                if (invokers.equals("offers"))
                    swipeRefreshLayout.setRefreshing(false);
                else
                    progressDialog.dismiss();
            if ( result == null) {//result == null && StaticConstants.alertOn==0
                //am.SingleButtonAlert("Server Error", "Error, try again", "Ok");
                Toast.makeText(getActivity(), "Network problem try again", Toast.LENGTH_SHORT).show();
                //StaticConstants.setAlertInt(1);
            }
            else{
                String message = null;
                JSONObject root;
                int validation = 0, datacount = 0;

                try {
                    Log.d("Inside Post", "message");
                    root = new JSONObject(result);
                    validation = root.getInt("response");
                    message = root.getString("message");
                    if (invokers.equals("offers"))
                        datacount = root.getInt("datacount");
                    Log.d(result, message);

                    if (validation == 1 && check_ofr_categ_lst.equals("offers") && datacount > 0 && message.equals("Success")) {
                        if (page_no == 0)
                            offerList.clear();
                        JSONArray data = null;
//                    if(datacount>0 && message.equals("Success")){//success
                        data = root.getJSONArray("data");
//                        if(datacount>0 && data.length()>0) {
                        /** Pass Received Offers data CustomOffersAdapter **/
                        for (int i = 0; i < data.length(); i++) {
                            hgs = new GetSetOffers();
                            hgs.setOfferId(data.getJSONObject(i).getString("offer_id"));
                            hgs.setHotelId(data.getJSONObject(i).getString("hotel_id"));
                            hgs.setRoomTypeId(data.getJSONObject(i).getString("room_type_id"));
                            hgs.setOfferTitle(data.getJSONObject(i).getString("offer_title"));
                            hgs.setOfferCategory(data.getJSONObject(i).getString("room_type_name"));
                            hgs.setOfferFromDt(data.getJSONObject(i).getString("valid_from_date"));
                            hgs.setOfferToDt(data.getJSONObject(i).getString("valid_to_date"));
                            hgs.setOfferPrice(data.getJSONObject(i).getString("offer_price"));//image_url
                            //hgs.setTax(data.getJSONObject(i).getString("tax"));
                            hgs.setOfferDescrip(data.getJSONObject(i).getString("offer_description"));
                            hgs.setImageUrl(data.getJSONObject(i).getString("image_url"));
                            hgs.setLengthOfStay(data.getJSONObject(i).getInt("length_of_stay"));
                            hgs.setInclusions(data.getJSONObject(i).getString("inclusions"));
                            hgs.setFoodOptions(data.getJSONObject(i).getString("food_options"));
                            hgs.setEventOptions(data.getJSONObject(i).getString("event_options"));
                            //hgs.setCreatedDate(data.getJSONObject(i).getString("created_date"));
                            hgs.setRoomTypeDescription(data.getJSONObject(i).getString("room_type_description"));
                            hgs.setFan(data.getJSONObject(i).getInt("fan"));
                            hgs.setAC(data.getJSONObject(i).getInt("AC"));
                            hgs.setFridge(data.getJSONObject(i).getInt("fridge"));
                            hgs.setTV(data.getJSONObject(i).getInt("TV"));
                            hgs.setBar(data.getJSONObject(i).getInt("bar"));
                            hgs.setWifi(data.getJSONObject(i).getInt("wifi"));
                            hgs.setComplementary(data.getJSONObject(i).getString("complementary_details"));
                            hgs.setBedDetail(data.getJSONObject(i).getString("bed_details"));
                            hgs.setGuestAllowed(data.getJSONObject(i).getString("guest_allowed"));
                            hgs.setRoomService(data.getJSONObject(i).getInt("room_service"));
                            hgs.setRestaurantAvailability(data.getJSONObject(i).getInt("restaurant_availability"));

                            offerList.add(hgs);
                        }
                        /**Tell the adapter that data passed*/
                        hca.notifyDataSetChanged();
                        /**Received data is less than 10 then that is the end of the page. No more data to load */
                        if (data.length() < 10) {
                            text_msg.setText("End Reached");
                            text_msg.setVisibility(View.GONE);
                        }
                    } /**Check this is the end of the page. No more data to load */
                    else if (validation == 1 && check_ofr_categ_lst.equals("offers") && datacount > 0 && message.equals("Success no more data")) {
                        text_msg.setText("End Reached");
                        text_msg.setVisibility(View.GONE);
                    } /** No offers is available condition */
                    else if (validation == 1 && check_ofr_categ_lst.equals("offers") && datacount == 0 && message.equals("Success no more data")) {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        text_msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        text_msg.setVisibility(View.VISIBLE);
                        text_msg.setTextSize(30);
                        text_msg.setText("No\nOffers\nToday");
                    } /** Populate category values to the spinner */
                    else if (validation == 1 && check_ofr_categ_lst.equals("category")) {
                        offerList.clear();
                        int count=0;
                        JSONArray data = root.getJSONArray("data");
                        hotel_category = new String[data.length() + 1];
                        hotel_category[0] = categ = "All";
                        for (int i = 0; i < data.length(); i++) {
                            hotel_category[i + 1] = data.getJSONObject(i).getString("room_type_name");
                            if(((SimpleTabsActivity)getActivity()).getSelectedOfferFromActivity()!=null &&
                                    ((SimpleTabsActivity)getActivity()).getSelectedOfferFromActivity().equals(hotel_category[i + 1])) {
                                ((SimpleTabsActivity)getActivity()).sendToActivity(null);
                                categ = hotel_category[i + 1];
                                count = i+1;
                            }
                        }
                        array_adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_dropdown, hotel_category);
                        offer_category_list.setAdapter(array_adapter);
                        offer_category_list.setSelection(count, true);
                        offerList.clear();
                        hca.notifyDataSetChanged();
                        //new AsyncOffers(context).execute("offers","1", "All", "0");
                        callSwipePost();
                    } /** Last condition to tell your request is not valid*/
                    else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        text_msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        text_msg.setVisibility(View.VISIBLE);
                        text_msg.setTextSize(30);
                        text_msg.setText("This\nHotel\nHas\nNo\nRecords");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
