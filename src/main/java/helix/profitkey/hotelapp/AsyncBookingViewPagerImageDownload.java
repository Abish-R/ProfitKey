package helix.profitkey.hotelapp;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : AsyncTask for Booking ViewPager images (Not Used)
 *  Created by : Abish
 *  Created Dt : old file
 *  Modified on: modified related to ViewPager images
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class AsyncBookingViewPagerImageDownload extends AsyncTask<String, Void, Bitmap> {
    private ProgressDialog simpleWaitDialog;
    Context context;

    /**Constructor*/
    AsyncBookingViewPagerImageDownload(Context conx){
        context = conx;
    }
    @Override
    protected Bitmap doInBackground(String... param) {
        Bitmap temp = downloadBitmap(param[0]);
        return temp;
    }

    /** Loader screen  **/
    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");
        //simpleWaitDialog = ProgressDialog.show(context, "Wait", "Downloading Image");
    }

    /**Update finally*/
    @Override
    protected void onPostExecute(Bitmap result) {
        Log.i("Async-Example", "onPostExecute Called");
        //simpleWaitDialog.dismiss();
        if(result!=null) {
//            imageView.setImageBitmap(result);
//            ((ViewPager) contain).addView(viewItem);
        }
    }

    /** Image download */
    private Bitmap downloadBitmap(String url) {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownload", "Error " + statusCode +" while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            getRequest.abort();
            Log.e("ImageDownload", "Something went wrong while retrieving bitmap from " + url + e.toString());
        }
        return null;
    }
}
