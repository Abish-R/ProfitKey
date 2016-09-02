package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Fragment to change the email
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
import helix.profitkey.hotelapp.AsyncPasswordChange;
import helix.profitkey.hotelapp.R;


public class EmailChangeFragment extends Fragment {
    /**Global declarations*/
    ApplicationConfigs application;
    Button ok;
    View view;
    EditText cnfm_email,new_email,old_email;
    String uuid,current_email;
    public EmailChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Get stored uuid and old email **/
        SharedPreferences sp=this.getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        uuid = sp.getString("profit_key_uuid", "");
        current_email = sp.getString("profit_key_email", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_email_change, container, false);
        initializeViews();
        return view;
    }

    /**View initialize*/
    private void initializeViews() {
        application = (ApplicationConfigs) getActivity().getApplication();

        ok = (Button) view.findViewById(R.id.ok);
        old_email = (EditText)view.findViewById(R.id.old_email);
        new_email = (EditText)view.findViewById(R.id.new_email);
        cnfm_email = (EditText)view.findViewById(R.id.cnfm_email);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailAndPost();
                //changeFragment();
            }
        });

        /**Set custom font*/
        application.setTypefaceButton(ok);
        application.setTypefaceEditText(old_email);
        application.setTypefaceEditText(new_email);
        application.setTypefaceEditText(cnfm_email);
    }

    /** After changing the email call the settings button fragment*/
    public void changeFragment(String email){
        Toast.makeText(getActivity(), "Email Updated Successfully.", Toast.LENGTH_SHORT).show();
        saveEmail(email);
        SettingsButtonFragment fr = new SettingsButtonFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_communicator, fr);
        fragmentTransaction.commit();
    }

    /** Validation and call Asynctask to update in live DB*/
    private void getEmailAndPost(){
        String email_old = old_email.getText().toString();
        String email_new = new_email.getText().toString();
        String email_cnfm = cnfm_email.getText().toString();
        if(checkEmail(email_old)==1)
            Toast.makeText(getActivity(), "Old Email is not valid", Toast.LENGTH_SHORT).show();
        else if(checkEmail(email_new)==1)
            Toast.makeText(getActivity(), "New Email is not valid", Toast.LENGTH_SHORT).show();

        else if(checkEmail(email_cnfm)==1)
            Toast.makeText(getActivity(), "Confirm Email is not valid", Toast.LENGTH_SHORT).show();

        else if(current_email.equals(email_old) && email_new.equals(email_cnfm)) {
            new AsyncMobileEmailChange(getActivity(), EmailChangeFragment.this,1).execute(uuid, email_new);
        }
        else {
            Toast.makeText(getActivity(), "New Email & Confirm Email not matching", Toast.LENGTH_SHORT).show();
        }
    }

    /**Check email is valid or not*/
    private int checkEmail(String email){
        if(!email.contains("@") || (!email.contains(".co") && !email.contains(".com") && !email.contains(".org") && !email.contains(".net") &&
                !email.contains(".edu") && !email.contains(".info") && !email.contains(".in")) || email.length() < 6 ||
                (email.length() > 50) || email.contains("@@") || email.contains("@.") || email.contains(".@") ||
                email.contains("..") || email.contains("--") || email.contains("__") || email.charAt(0) == '@' ||
                email.charAt(0) == '.' || email.charAt(0) == '_' || email.charAt(0) == '-') {
            return 1;
        }
        else
            return 0;
    }

    /**Set url parameter in webservice link*/
    public String setUrlParams(String uu, String email){
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("uuid", uu)
                .appendQueryParameter("email_id", email);
        String query = builder.build().getEncodedQuery();
        return query;
    }

    /** Save the new email*/
    private void saveEmail(String email){
        SharedPreferences sp=getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString("profit_key_email", email);
        ed.commit();
    }

}
