package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Review Handling fragment page
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
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
import helix.profitkey.hotelapp.CustomReviewAdapter;
import helix.profitkey.hotelapp.GetSetReview;
import helix.profitkey.hotelapp.R;
import helix.profitkey.hotelapp.ReviewWriteNew;


public class ReviewFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    /**Global Declarations**/
    View view;
    ApplicationConfigs application;
    Button add_review;
    public ListView list_review;
    private TextView load_more_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    CustomReviewAdapter hca;
    CheckInternet ci;
    AlertMessages am;
    Context context;
    int page_no=0;
    String name;
    boolean initiatedOnce = false,calledOncreateView=false,fragmentVisibleHint=false;
    ArrayList<GetSetReview> reviewList = new ArrayList<GetSetReview>();

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ci = new CheckInternet(getActivity());
        am = new AlertMessages(getActivity());

        /**Get stored name **/
        SharedPreferences sp=getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        name = sp.getString("profit_key_users_name", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ScrollView scroller = new ScrollView(getActivity());
        calledOncreateView = true;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.review_fragment, container, false);

        /**Initialize Views*/
        add_review = (Button) view.findViewById(R.id.add_review);
        list_review = (ListView) view.findViewById(R.id.list_review);
        load_more_text = (TextView) view.findViewById(R.id.load_more_text);
        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        /**Set custom font to the views*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceTextViewBold(load_more_text);
        application.setTypefaceButton(add_review);

        if(fragmentVisibleHint) {
            callSwipePost();
        }

        add_review.setOnClickListener(this);
        list_review.setOnScrollListener(onScrollListener());
        context=view.getContext();
        return view;
    }

    @Override
    public void onClick(View v) {
        /**Take you to write review Screen*/
        switch (v.getId()) {
            case R.id.add_review:
                Intent intent= new Intent(getActivity(), ReviewWriteNew.class);
                startActivity(intent);
                break;
        }
    }

    /**This function will be used to execute the code only, if the fragment is visible**/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisibleHint = isVisibleToUser;
        if(isVisibleToUser && !initiatedOnce && calledOncreateView){
            callSwipePost();
            initiatedOnce = true;
            calledOncreateView = false;
        }
    }

    /**Called from onCreateView if view created*/
    public void callSwipePost(){
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
        initiatedOnce = false;
        super.onDestroyView();
    }

    /**While swiping down it will the called*/
    @Override
    public void onRefresh() {
        page_no=0;
        //reviewList.clear();
        callAsyncClass();
    }

    /**Call the AsyncTask to get the all reviews*/
    private void callAsyncClass(){
        if(ci.isOnline())
            new AsyncHotel(getActivity()).execute(StaticConstants.hotel_id, page_no + "");
        else
            am.SingleButtonAlert("Internet Error", "Check the connection and try again", "Ok");
    }

    /**Called everytime when the fragment the invoked*/
    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
//        initiatedOnce = false;
        hca=new CustomReviewAdapter(context,R.layout.review_custom_listview,reviewList, ReviewFragment.this);
        list_review.setAdapter(hca);
        reviewList.clear();
        page_no=0;
    }

    /** Pagination implemented with onScroll action*/
    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = list_review.getCount();
                String text = load_more_text.getText().toString();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (list_review.getLastVisiblePosition() >= count - threshold &&
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

    /**Called from CustomReviewAdapter to set name in all the reviews*/
    public int checkName(String nam){
        if(name.equals(nam))
            return 1;
        else
            return 0;
    }

    /**AsyncTask to get the reviews*/
    public class AsyncHotel extends AsyncTask<String, Void, String> {
        private static final String TAG = "Inside Thread HotelAdd";
        //ProgressDialog progressDialog;
        Context context;
        GetSetReview gsr;

        /**Constructor just coded not required*/
        public AsyncHotel(Context conx) {
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
            String result = null;
            try {
                // url where the data will be posted or retrieved
                String postReceiverUrl = StaticConstants.get_reviews_url;
                Log.v(TAG, "postURL: " + postReceiverUrl);
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient
                HttpPost httpPost = new HttpPost(postReceiverUrl);    // post header

                /**Parameter adding to send with url*/
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[0]));
                nameValuePairs.add(new BasicNameValuePair("pageno", arg0[1]));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                httpPost.setEntity(entity);

                // execute HTTP post request
                HttpResponse response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                //e.printStackTrace();
                am.SingleButtonAlert("Server Error","Error, try again","Ok");
            }
            return result;
        }

        /**Called to update the status finally*/
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
            swipeRefreshLayout.setRefreshing(false);
            if (result == null)
                Toast.makeText(context, "Network problem try again", Toast.LENGTH_SHORT).show();
            else {
                //progressDialog.dismiss();
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

                    /** Pass Received review data to CustomReviewAdapter **/
                    if (validation == 1 && datacount > 0 && message.equals("Success")) {
                        JSONArray data = root.getJSONArray("data");
                        if (page_no == 0)
                            reviewList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            gsr = new GetSetReview();
                            gsr.setReviewId(data.getJSONObject(i).getString("review_id"));
                            gsr.setReviewDate(data.getJSONObject(i).getString("review_posted_date"));
                            gsr.setReviewTitle(data.getJSONObject(i).getString("review_title"));//
                            gsr.setReviewDescription(data.getJSONObject(i).getString("review_description"));//
                            gsr.setReviewImageurl(data.getJSONObject(i).getString("review_picture_url"));
                            gsr.setReviewName(data.getJSONObject(i).getString("reviewed_customer_firstname")+" "+
                                    data.getJSONObject(i).getString("reviewed_customer_lastname"));

                            reviewList.add(gsr);
                        }
                        hca.notifyDataSetChanged();
                        if (data.length() < 10) {
                            load_more_text.setVisibility(View.GONE);
                            load_more_text.setText("End Reached");
                        }
                    } else if (validation == 1 && datacount > 0 && message.equals("Success no more data")) {
                        load_more_text.setVisibility(View.GONE);
                        load_more_text.setText("End Reached");
                    }
                    else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        load_more_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        load_more_text.setVisibility(View.VISIBLE);
                        load_more_text.setTextSize(30);
                        load_more_text.setText("No\nreview\nfor\nthis\nhotel");
                    }
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
