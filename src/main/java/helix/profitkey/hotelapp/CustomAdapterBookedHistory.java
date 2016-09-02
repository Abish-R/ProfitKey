package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Custom Listview Adapter for booking history
 *  Created by : Abish
 *  Created Dt : 3/12/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import general.ApplicationConfigs;

public class CustomAdapterBookedHistory extends ArrayAdapter<GetSetBookedHistory> {
    /** Global declarations*/
    ApplicationConfigs application;
    ArrayList<GetSetBookedHistory> bookHistory = new ArrayList<GetSetBookedHistory>();
    String string_intent;
    Context context;
    private static LayoutInflater inflater=null;

    /***  CustomAdapter Constructor, initializer for booking history fragment **/
    public CustomAdapterBookedHistory(Context bk_hist, int textViewResourceId,
                                      ArrayList<GetSetBookedHistory> bk_history, String strg_intnt) {
        super(bk_hist,textViewResourceId, bk_history);
        bookHistory=bk_history;
        context=bk_hist;
        string_intent=strg_intnt;
        application = (ApplicationConfigs)bk_hist.getApplicationContext();
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return bookHistory.size();
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
        TextView tv1,tv2,tv3,aa,bb,cc;
    }

    /***  Place the values from json into the listview **/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final Object[] id_obj=h_id.toArray();
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_layout_booked_history, null);

            /** Creates a ViewHolder and store references to the two children to bind data to.*/
            holder=new ViewHolder();

            /** Initialize view*/
            convertView.setTag(holder);
            holder.tv1=(TextView) convertView.findViewById(R.id.a);
            holder.tv2=(TextView) convertView.findViewById(R.id.b);
            holder.tv3=(TextView) convertView.findViewById(R.id.c);
            holder.aa=(TextView) convertView.findViewById(R.id.aa);
            holder.bb=(TextView) convertView.findViewById(R.id.bb);
            holder.cc=(TextView) convertView.findViewById(R.id.cc);

            /** Set custom fonts*/
            application.setTypefaceTextView(holder.tv1);
            application.setTypefaceTextView(holder.tv2);
            application.setTypefaceTextView(holder.tv3);
            application.setTypefaceTextView(holder.aa);
            application.setTypefaceTextView(holder.bb);
            application.setTypefaceTextView(holder.cc);
        }
        else {
            /** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
            holder = (ViewHolder) convertView.getTag();
        }
        if(position%2==0)
            convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
        else
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        /** Set values to views */
        holder.tv1.setText(bookHistory.get(position).getBookedDate());
        holder.tv2.setText(bookHistory.get(position).getRoomType());
        holder.tv3.setText(bookHistory.get(position).getOfferTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** On click in the list, open Booking description with below details*/
                if(string_intent.equals("booking_history")) {
                    Intent intent = new Intent().setClass(context, BookingHistoryDescription.class);
                    intent.putExtra("intent_invoker","booking_history");
                    intent.putExtra("offer_title",bookHistory.get(position).getOfferTitle());
                    intent.putExtra("offer_description",bookHistory.get(position).getOfferDescription());
                    intent.putExtra("room_type_name",bookHistory.get(position).getRoomType());
                    intent.putExtra("from_date",bookHistory.get(position).getFromDate());
                    intent.putExtra("to_date",bookHistory.get(position).getToDate());
                    intent.putExtra("total_days",bookHistory.get(position).getTotalDays());
                    intent.putExtra("price",bookHistory.get(position).getTotalPrice());
                    intent.putExtra("booked_date",bookHistory.get(position).getBookedDate());
                    context.startActivity(intent);
                }
                else if(string_intent.equals("review_description")) {
                    Intent intent = new Intent().setClass(context, ReviewDescription.class);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
