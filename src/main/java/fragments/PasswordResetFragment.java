package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Fragment to change the password
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
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import general.ApplicationConfigs;
import helix.profitkey.hotelapp.AsyncPasswordChange;
import helix.profitkey.hotelapp.R;


public class PasswordResetFragment extends Fragment {
    /**Global declarations*/
    ApplicationConfigs application;
    Button ok;
    View view;
    EditText old_pass,new_pass,cnfm_pass;
    String uuid;
    public PasswordResetFragment() {
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
        view= inflater.inflate(R.layout.fragment_password_reset, container, false);
        initializeViews();
        return view;
    }

    /**View initialize*/
    private void initializeViews() {
        ok = (Button)view.findViewById(R.id.ok);
        old_pass = (EditText)view.findViewById(R.id.old_pass);
        new_pass = (EditText)view.findViewById(R.id.new_pass);
        cnfm_pass = (EditText)view.findViewById(R.id.cnfm_pass);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPasswordAndPost();
                //changeFragment();
            }
        });

        /**Set custom font*/
        application = (ApplicationConfigs) getActivity().getApplication();
        application.setTypefaceButton(ok);
        application.setTypefaceEditText(old_pass);
        application.setTypefaceEditText(new_pass);
        application.setTypefaceEditText(cnfm_pass);
    }

    /** After changing the password call the settings button fragment*/
    public void changeFragment(){
        SettingsButtonFragment fr = new SettingsButtonFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_communicator, fr);
        fragmentTransaction.commit();
    }

    /**Validation for password change*/
    private void getPasswordAndPost(){
        if(old_pass.getText().toString().length()<3 || old_pass.getText().toString().contains(" "))
            Toast.makeText(getActivity(), "Old Password wrong", Toast.LENGTH_SHORT).show();
        else if(new_pass.getText().toString().length()<3 || new_pass.getText().toString().contains(" "))
            Toast.makeText(getActivity(), "Password must have length greater than 3 & should not contain Space' '", Toast.LENGTH_SHORT).show();
        else if(cnfm_pass.getText().toString().length()<3 || cnfm_pass.getText().toString().contains(" "))
            Toast.makeText(getActivity(),"Password must have length greater than 3 & should not contain Space' '",Toast.LENGTH_SHORT).show();
        else if(cnfm_pass.getText().toString().equals(new_pass.getText().toString())){
            //Toast.makeText(getActivity(), "Perfect", Toast.LENGTH_SHORT);
            new AsyncPasswordChange(getActivity(),PasswordResetFragment.this).execute(uuid,old_pass.getText().toString(),cnfm_pass.getText().toString());
        }
        else {
            Toast.makeText(getActivity(), "New password & Confirm password not matching", Toast.LENGTH_SHORT).show();
        }
    }

}
