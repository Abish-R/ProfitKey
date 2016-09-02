package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Fragment to show all the settings button
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import general.ApplicationConfigs;
import helix.profitkey.hotelapp.R;
import helix.profitkey.hotelapp.Settings;


public class SettingsButtonFragment extends Fragment implements View.OnClickListener {
    /**Global Declarations*/
    View view;
    ApplicationConfigs application;
    Button password_change,mobile_change,email_change,prof_img_change;
    public SettingsButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ScrollView scroller = new ScrollView(getActivity());
        view= inflater.inflate(R.layout.settings_all_widgets, container, false);
        initializeViews();

        return view;
    }

    /**View initialize*/
    private void initializeViews() {
        password_change = (Button) view.findViewById(R.id.password_change);
        mobile_change = (Button) view.findViewById(R.id.mobile_change);
        email_change = (Button) view.findViewById(R.id.email_change);
        prof_img_change = (Button) view.findViewById(R.id.prof_img_change);
        password_change.setOnClickListener(this);
        mobile_change.setOnClickListener(this);
        email_change.setOnClickListener(this);
        prof_img_change.setOnClickListener(this);

        /** Change custom font*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceButton(password_change);
        application.setTypefaceButton(mobile_change);
        application.setTypefaceButton(email_change);
        application.setTypefaceButton(prof_img_change);
    }

    /**Action perform invoker for all the buttons. Function is in Settings Activity class*/
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.password_change:
                ((Settings)getActivity()).callToChangeFragments(1);
                break;
            case R.id.mobile_change:
                ((Settings)getActivity()).callToChangeFragments(2);
                break;
            case R.id.email_change:
                ((Settings)getActivity()).callToChangeFragments(3);
                break;
            case R.id.prof_img_change:
                ((Settings)getActivity()).callToChangeFragments(4);
                break;
        }
    }
}
