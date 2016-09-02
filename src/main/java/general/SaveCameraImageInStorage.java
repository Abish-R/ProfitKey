package general;

/**
 *  ProfitKey v1.0
 * 	Purpose	   : Save captured image in local memory
 *  Created by : Abish
 *  Created Dt : Old File
 *  Modified on: modified for delete
 *  Verified by:
 *  Verified Dt:
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveCameraImageInStorage {
    private Context context;
    /** class constructor */
    public SaveCameraImageInStorage(Context contex){
        this.context=contex;
    }
    /** call camera event to save the image */
    public File saveImage(String directory,String img_name,ImageView imagereceived) {
        File image;
        ImageView temperory=imagereceived;
        BitmapDrawable drawable = (BitmapDrawable) temperory.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        Log.d("File Path: ", sdCardDirectory.toString());
        File folder = new File(sdCardDirectory+ File.separator + directory);
        if(!folder.exists())
            folder.mkdir();
        image = new File(sdCardDirectory+ File.separator + directory, img_name+".png");
        if(image.exists())
            image.delete();
        boolean success = false;
        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	        /* 100 to keep full quality of the image */
            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            //Toast.makeText(getApplicationContext(), "Image saved with success",
            //       Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Error during image saving", Toast.LENGTH_LONG).show();
        }
        return image;
    }

}
