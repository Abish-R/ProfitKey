package helix.profitkey.hotelapp;
/** Profit Key v1.0.0
 *  Purpose	   : Custom Adapter Image Slider (Not Used)
 *  Created by : Abish
 *  Created Dt : 4/26/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.AsyncTask;
    import android.support.v4.view.PagerAdapter;
    import android.support.v4.view.ViewPager;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.apache.http.HttpEntity;
    import org.apache.http.HttpResponse;
    import org.apache.http.HttpStatus;
    import org.apache.http.client.methods.HttpGet;
    import org.apache.http.impl.client.DefaultHttpClient;

    import java.io.InputStream;

public class CustomBookingPageImageSliderAdapter extends PagerAdapter {
    ImageView imageView;
    View viewItem;
    ViewGroup contain;
        Context context;
        String[] url ={"http://icons.iconarchive.com/icons/fasticon/essential-toolbar/32/small-icons-icon.png",
                        "http://icons.iconarchive.com/icons/robinweatherall/recycling/128/bin-small-icon.png",
                        "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/128/Emotes-face-smile-icon.png",
                        "https://www.creativefreedom.co.uk/wp-content/uploads/2013/03/00-android-4-0_icons.png"};
        //int[] imageId = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};

        public CustomBookingPageImageSliderAdapter(Context context){
            this.context = context;

        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            contain=container;
            viewItem = inflater.inflate(R.layout.custom_booking_viewpager_image, container, false);
            imageView = (ImageView) viewItem.findViewById(R.id.image);
            for(int i=0;i<url.length;i++) {
                new AsyncBookingViewPagerImageDownload(context).execute(url[i]);
            }
            //imageView.setImageBitmap(result);
                ((ViewPager) container).addView(viewItem);


//            TextView textView1 = (TextView) viewItem.findViewById(R.id.textView1);
//            textView1.setText("hi");

            return viewItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return url.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub

            return view == ((View)object);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }


    /**
     * Created by HelixTech-Admin on 3/26/2016.
     */
//    public class AsyncBookingViewPagerImageDownload extends AsyncTask<String, Void, Bitmap> {
//        private ProgressDialog simpleWaitDialog;
//        @Override
//        protected Bitmap doInBackground(String... param) {
//            Bitmap temp = downloadBitmap(param[0]);
//            return temp;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.i("Async-Example", "onPreExecute Called");
//            //simpleWaitDialog = ProgressDialog.show(context, "Wait", "Downloading Image");
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            Log.i("Async-Example", "onPostExecute Called");
//            //simpleWaitDialog.dismiss();
//            if(result!=null) {
//                imageView.setImageBitmap(result);
//                ((ViewPager) contain).addView(viewItem);
//            }
//        }
//
//        private Bitmap downloadBitmap(String url) {
//            final DefaultHttpClient client = new DefaultHttpClient();
//            final HttpGet getRequest = new HttpGet(url);
//            try {
//                HttpResponse response = client.execute(getRequest);
//                //check 200 OK for success
//                final int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode != HttpStatus.SC_OK) {
//                    Log.w("ImageDownload", "Error " + statusCode +" while retrieving bitmap from " + url);
//                    return null;
//                }
//
//                final HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    InputStream inputStream = null;
//                    try {
//                        inputStream = entity.getContent();
//                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        return bitmap;
//                    } finally {
//                        if (inputStream != null) {
//                            inputStream.close();
//                        }
//                        entity.consumeContent();
//                    }
//                }
//            } catch (Exception e) {
//                getRequest.abort();
//                Log.e("ImageDownload", "Something went wrong while retrieving bitmap from " + url + e.toString());
//            }
//            return null;
//        }
//    }

    }
