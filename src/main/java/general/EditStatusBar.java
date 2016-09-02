package general;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Statusbar edit
 *  Created by : Abish
 *  Created Dt : 4/19/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
public class EditStatusBar {
    Context context;
    public EditStatusBar(Context contex){
        context = contex;
    }

//    /**Set color to statusbar*/
//    public void setStatusBarColor(View statusBar,int color){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();  //it is must
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //status bar height
//            //int actionBarHeight = getActionBarHeight();
//            int statusBarHeight = getStatusBarHeight();
//            //action bar height
//            statusBar.getLayoutParams().height = statusBarHeight;// + actionBarHeight ;
//            statusBar.setBackgroundColor(color);
//        }
//    }

    /**Find statusbar height*/
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //    public int getActionBarHeight() {
//        int actionBarHeight = 0;
//        TypedValue tv = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//        {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }
//        return actionBarHeight;
//    }
}
