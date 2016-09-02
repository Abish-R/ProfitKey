package general;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Custom TextView
 *  Created by : Abish
 *  Created Dt : 5/4/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
public class CustomTextview extends TextView {

    public CustomTextview(Context context, AttributeSet attrs){
        super(context,attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Regular.ttf"));//Montserrat-Regular.ttf
    }
}
