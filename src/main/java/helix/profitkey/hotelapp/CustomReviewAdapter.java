/** ProfitKey v1.0.0
 *  Purpose	   : Custom Review Adapter to get view
 *  Created by : Abish
 *  Created Dt :
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.profitkey.hotelapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragments.OfferFragment;
import fragments.ReviewFragment;
import general.ApplicationConfigs;

public class CustomReviewAdapter extends ArrayAdapter<GetSetReview>{
   ApplicationConfigs application;
	ArrayList<GetSetReview> reviewList = new ArrayList<GetSetReview>();
    Context context;
    ReviewFragment frag_context = new ReviewFragment();
    String name;
    private static LayoutInflater inflater=null;

    /***  CustomAdapter Constructor, initializer for Hotel Master entry **/
	public CustomReviewAdapter(Context review, int textViewResourceId, ArrayList<GetSetReview> hotelLists, ReviewFragment conx) {
		super(review,textViewResourceId, hotelLists);
        reviewList=hotelLists;
		context=review;
        this.frag_context = conx;
        application = (ApplicationConfigs)review.getApplicationContext();
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
    public int getCount() {
        return reviewList.size();
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
       // TextView tv;
        TextView tv1,tv2,tv3,tv4,a,cc,dd,edit;
        //Button edit;
    }

    /***  Place the values from json into the listview **/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//final Object[] id_obj=h_id.toArray();
        final ViewHolder holder;
        if (convertView == null) {
			convertView = inflater.inflate(R.layout.review_custom_listview, null);

			/** Creates a ViewHolder and store references to the two children to bind data to.*/
			holder=new ViewHolder();
			convertView.setTag(holder);

            /** Initialize views */
			//holder.tv1=(TextView) convertView.findViewById(R.id.a);
            holder.tv2=(TextView) convertView.findViewById(R.id.b);
            holder.tv3=(TextView) convertView.findViewById(R.id.c);
            holder.tv4=(TextView) convertView.findViewById(R.id.d);
            holder.a=(TextView) convertView.findViewById(R.id.a);
            holder.cc=(TextView) convertView.findViewById(R.id.cc);
            holder.dd=(TextView) convertView.findViewById(R.id.dd);
            holder.edit=(TextView) convertView.findViewById(R.id.edit);

            /** Set custom font */
            application.setTypefaceTextView(holder.tv2);
            application.setTypefaceTextView(holder.tv3);
            application.setTypefaceTextView(holder.tv4);
            application.setTypefaceTextView(holder.a);
            application.setTypefaceTextView(holder.cc);
            application.setTypefaceTextView(holder.dd);
            application.setTypefaceTextView(holder.edit);

            holder.cc.setPaintFlags(holder.cc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.dd.setPaintFlags(holder.dd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
	        }
        else {
			/** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
			holder = (ViewHolder) convertView.getTag();
        }
        if(position%2==0)
        	convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_bg));
        else
        	convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        /** Set values in values */
        //holder.tv1.setText(hotelList.get(position).getOfferName());
        holder.tv2.setText(reviewList.get(position).getReviewDate());
        holder.tv3.setText(reviewList.get(position).getReviewTitle());
        holder.tv4.setText(reviewList.get(position).getReviewDescription());
        if(frag_context.checkName(reviewList.get(position).getReviewName())==1) { /** Check the reviewer name*/
            holder.edit.setVisibility(View.VISIBLE);
            name = "Me";
        }
        else {
            holder.edit.setVisibility(View.GONE);
            name = reviewList.get(position).getReviewName();
        }
        holder.a.setText(name);
        //holder.tv7.setText(hotelList.get(position).getOfferDescrip());
        //holder.tv3.setText((CharSequence) date_obj[position]);
        //holder.check_status.setTag(hgs);

        /** On Click the textView it open the write review with editing option*/
        holder.edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "You clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent().setClass(context, ReviewWriteNew.class);
//                intent.putExtra("review_dt", hotelList.get(position).getReviewDate());
                intent.putExtra("review_id", reviewList.get(position).getReviewId());
                intent.putExtra("review_tit", reviewList.get(position).getReviewTitle());
                intent.putExtra("review_descrip", reviewList.get(position).getReviewDescription());
                intent.putExtra("review_img_url", reviewList.get(position).getReviewImageurl());
//                intent.putExtra("reviewer_name",name);
                context.startActivity(intent);
            }
        });

        /** Listview click shows the detailed explanation**/
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "You clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent().setClass(context, ReviewDescription.class);
                //intent.putExtra("intent_invoker","offer_page");
                intent.putExtra("review_dt", reviewList.get(position).getReviewDate());
                intent.putExtra("review_tit", reviewList.get(position).getReviewTitle());
                intent.putExtra("review_descrip", reviewList.get(position).getReviewDescription());
                intent.putExtra("review_img_url", reviewList.get(position).getReviewImageurl());
                intent.putExtra("reviewer_name",name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}