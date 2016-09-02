package general;

/**
 *  ProfitKey v1.0
 * 	Purpose	   : General Alert Messages for all Screens
 *  Created by : Abish
 *  Created Dt : Old File
 *  Modified on: Modified
 *  Verified by:
 *  Verified Dt:
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import helix.profitkey.hotelapp.BookingConfirm;
import helix.profitkey.hotelapp.SignUp;

public class AlertMessages {

    private Context context;

    /**Constructor*/
    public AlertMessages(Context contex){
        this.context = contex;
    }

    /**Alert with single button with title*/
    public void SingleButtonAlert(String title,String message,String but_txt){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but_txt,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Single button alert without title*/
    public void SingleButtonAlertNoTile(String message,String but_txt){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Single button Alert without title with some responsibility(Invoking some methods)
     * used in SignUp Screen, BookingConfirm Screen*/
    public void SingleButtonAlertWithResponsibility(String message,String but_txt, final String purpose){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        //alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (purpose.equals("go_login"))
                    ((SignUp) context).startLogin();
                else if (purpose.equals("call_cal_dialog"))
                    ((SignUp) context).showDialog(0);
                else if (purpose.equals("book_cnfm_to_date"))
                    ((BookingConfirm) context).callDateTimePicker(2);
                else if (purpose.equals("book_cnfm_from_date"))
                    ((BookingConfirm) context).callDateTimePicker(1);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Alert with Double button and title*/
    public void alertDoubleButton(String title,String message,String but1_txt,String but2_txt){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but1_txt,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });
        alertDialogBuilder.setNeutralButton(but2_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Alert with title and three buttons in it*/
    public void alertThreeButtonWarning(String tit,String msg,final String b1,final String b2,final String b3) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(tit);
        alertDialogBuilder.setMessage(msg);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(b1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialogBuilder.setNeutralButton(b2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialogBuilder.setNegativeButton(b3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
