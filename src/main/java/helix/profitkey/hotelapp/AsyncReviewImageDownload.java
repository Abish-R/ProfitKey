package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Review Image download
 *  Created by : Abish
 *  Created Dt : 3/26/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class AsyncReviewImageDownload extends AsyncTask<String, Void, Bitmap> {
    Context context;
    String from;
    private ProgressDialog simpleWaitDialog;

    /**Constructor*/
    public AsyncReviewImageDownload(Context conx){
        context=conx;
    }

    /** Function to handle background operations*/
    @Override
    protected Bitmap doInBackground(String... param) {
        // TODO Auto-generated method stub
        Bitmap temp = downloadBitmap(param[0]);
        from = param[1];
        return temp;
    }

    /** Loader screen  **/
    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");
        simpleWaitDialog = ProgressDialog.show(context,
                "Wait", "Downloading Image");
    }

    /**Function execute last to update*/
    @Override
    protected void onPostExecute(Bitmap result) {
        Log.i("Async-Example", "onPostExecute Called");
//        ImageView iv=null;
//        iv.setImageBitmap(result);
        simpleWaitDialog.dismiss();
        if(result!=null && from.equals("review_description"))
            ((ReviewDescription)context).setImage(result);
        else if(result!=null && from.equals("edit_review"))
            ((ReviewWriteNew)context).setImage(result);
        else
            Toast.makeText(context,"Image not in URL",Toast.LENGTH_SHORT).show();
    }

    /** Download image from url */
    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownload", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
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
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownload", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return null;
    }
}
