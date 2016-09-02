package general;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.Toolbar;

import helix.profitkey.hotelapp.R;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Common Methods
 *  Created by : Abish
 *  Created Dt : 4/21/2016
 *  Modified on: Not used
 *  Verified by:
 *  Verified Dt:
 */
public class CommonMethods {
    Context context;
    String uuid,name,user_id,email;
    SharedPreferences sp;

    public CommonMethods(Context conx){
        context = conx;
        sp=context.getSharedPreferences("profit_key", Context.MODE_PRIVATE);
    }


    public String getUuid() {
        return sp.getString("profit_key_uuid", "");
    }
    public String getName() {
        return sp.getString("profit_key_users_name", "");
    }
    public String getUserId() {
        return sp.getString("profit_key_user_id", "");
    }
    public String getEmail() {
        return sp.getString("profit_key_email", "");
    }
}
