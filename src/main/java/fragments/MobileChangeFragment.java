package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Fragment to change the mobile
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import general.ApplicationConfigs;
import helix.profitkey.hotelapp.AsyncMobileEmailChange;
import helix.profitkey.hotelapp.R;


public class MobileChangeFragment extends Fragment {
    /**Global declarations*/
    ApplicationConfigs application;
    Button ok;
    View view;
    EditText cnfm_mobile,new_mobile,old_mobile;
    String uuid;
    public MobileChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=this.getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        uuid = sp.getString("profit_key_uuid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mobile_change, container, false);
        initializeViews();
        return view;
    }

    /**View initialize*/
    private void initializeViews() {
        application = (ApplicationConfigs) getActivity().getApplication();
        ok = (Button)view.findViewById(R.id.ok);
        old_mobile = (EditText)view.findViewById(R.id.old_mobile);
        new_mobile = (EditText)view.findViewById(R.id.new_mobile);
        cnfm_mobile = (EditText)view.findViewById(R.id.cnfm_mobile);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMobileAndPost();
                //changeFragment();
            }
        });

        /**Set custom font*/
        application.setTypefaceButton(ok);
        application.setTypefaceEditText(old_mobile);
        application.setTypefaceEditText(new_mobile);
        application.setTypefaceEditText(cnfm_mobile);
    }

    /** After changing the password call the settings button fragment*/
    public void changeFragment(){
        Toast.makeText(getActivity(), "Mobile Updated Successfully.", Toast.LENGTH_SHORT).show();
        SettingsButtonFragment fr = new SettingsButtonFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_communicator, fr);
        fragmentTransaction.commit();
    }

    /** Validation and call Asynctask to update the mobile in live DB*/
    private void getMobileAndPost(){
        String mobile_old = old_mobile.getText().toString();
        String mobile_new = new_mobile.getText().toString();
        String mobile_cnfm = cnfm_mobile.getText().toString();

        if(!privateCheckMobile(mobile_old))
            Toast.makeText(getActivity(), "Old Mobile is not valid", Toast.LENGTH_SHORT).show();
        else if(!privateCheckMobile(mobile_new))
            Toast.makeText(getActivity(), "New Mobile is not valid", Toast.LENGTH_SHORT).show();

        else if(!privateCheckMobile(mobile_cnfm))
            Toast.makeText(getActivity(), "Confirm Mobile is not valid", Toast.LENGTH_SHORT).show();

        else if(mobile_new.equals(mobile_cnfm)) {
            new AsyncMobileEmailChange(getActivity(), MobileChangeFragment.this,2,0).execute(uuid, mobile_new);
        }
        else {
            Toast.makeText(getActivity(), "New Mobile & Confirm Mobile not matching", Toast.LENGTH_SHORT).show();
        }
    }

    /**Condition for mobiles*/
    private boolean privateCheckMobile(String mobil){
        if (mobil.length() > 9) {
            char c = mobil.charAt(0);
            return (c >= '7' && c <= '9');
        }else
            return false;
    }

    /**Set url parameter in webservice link*/
    public String setUrlParams(String uu, String mob){
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("uuid", uu)
                .appendQueryParameter("mobile_number", mob);
        String query = builder.build().getEncodedQuery();
        return query;
    }

}
