//package helix.profitkey.hotelapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//
//import general.AlertMessages;
//import general.CheckInternet;
//
///**
// * Created by HelixTech-Admin on 4/29/2016.
// */
//public class AvailableRoomDescription extends AppCompatActivity implements View.OnClickListener {
//    CheckInternet ci = new CheckInternet(this);
//    AlertMessages am = new AlertMessages(this);
//    ListView avail_descrip_list;
//    Button ok;
//
//    String intent_invoker,room_type,price_per_day;
//
//    @Override
//    public void onCreate(Bundle onSavedInstance){
//        super.onCreate(onSavedInstance);
//        setContentView(R.layout.available_room_description);
//        Bundle extras = getIntent().getExtras();
//        if(extras!=null)
//            intent_invoker=extras.getString("intent_invoker");
//        else
//            intent_invoker="";
//        initializeViews();
//
//        if(intent_invoker.equals("available_page")){
//            //room_type_id=extras.getString("room_type_id");
//            room_type=extras.getString("room_type");
//            price_per_day=extras.getString("price_per_day");
//        }
//        if(ci.isOnline())
//            new AsyncGetImageUrls(this).execute(room_type);
//        else
//            am.SingleButtonAlertNoTile("Check Internet Connection.","Ok");
//    }
//    private void initializeViews(){
//        avail_descrip_list = (ListView)findViewById(R.id.avail_descrip_list);
//        ok = (Button)findViewById(R.id.ok);
//
//        ok.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ok:
//                break;
//        }
//    }
//
//    public void gotUrls(String[] url){
//        CustomAvailableRoomDescriptionAdapter carda = new CustomAvailableRoomDescriptionAdapter(this,url);
//        avail_descrip_list.setAdapter(carda);
//    }
//
//    private void goBooking(){
//        onBackPressed();
//        Intent intent = new Intent(this,Booking.class);
//        startActivity(intent);
//    }
//}
