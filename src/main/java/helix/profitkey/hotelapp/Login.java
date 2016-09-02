/** Profit Key 1.0.0
 *  Purpose	   : Login for users
 *  Created by : Abish
 *  Created on : 05-03-2016
 *  Modified on: 07-03-2016 (Async Class Split)
 *  Verified by:
 *  Verified Dt:
 */

package helix.profitkey.hotelapp;
import general.ApplicationConfigs;
import general.CheckInternet;
import general.AlertMessages;
import general.EditStatusBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity{
	/** Global declarations **/
	ApplicationConfigs application;
	EditStatusBar esb = new EditStatusBar(this);
	CheckInternet ci=new CheckInternet(this);
	AlertMessages am=new AlertMessages(this);
	Button btn_login,btn_signup;
	EditText uname,pass;

	/**Activity created now*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

		viewInitialize();

		btn_login.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (uname.getText().toString().length() < 3 || pass.getText().toString().length() < 3)
					am.SingleButtonAlert("Login Error", "Username/ Password not valid", "Ok");
				else {
					if (ci.isOnline()) {
						new AsyncLogin(Login.this).execute(uname.getText().toString(), pass.getText().toString());
					} else
						am.SingleButtonAlert("Internet Problem", "Check Internet and try again.!", "Ok");
				}
			}
		});

		/** Start the signUp Activity*/
		btn_signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			Intent signup=new Intent(getApplicationContext(),SignUp.class);
			startActivity(signup);
			finish();
			}
		});
	}

	/** View Initialization  */
	private void viewInitialize(){
		application = (ApplicationConfigs)getApplication();

		btn_login=(Button)findViewById(R.id.btn_login);
		btn_signup=(Button)findViewById(R.id.btn_signup);
		uname =(EditText)findViewById(R.id.uname);
		pass=(EditText)findViewById(R.id.pass);

		TextView textView1 = (TextView)findViewById(R.id.textView1);
		TextView textView2 = (TextView)findViewById(R.id.textView2);

		btn_signup.setPaintFlags(btn_signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		/** Set custom font */
		application.setTypefaceEditText(uname);
		application.setTypefaceEditText(pass);
		application.setTypefaceButton(btn_login);
		application.setTypefaceButton(btn_signup);
		application.setTypefaceTextViewBold(textView1);
		application.setTypefaceTextView(textView2);
	}

	/** save the user in session starts here **/
	public void saveSession(String user_id,String uuid, String name, String email) {
		SharedPreferences sp=getSharedPreferences("profit_key", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed=sp.edit();
		ed.putString("profit_key_user_id", user_id);
		ed.putString("profit_key_uuid", uuid);
		ed.putString("profit_key_users_name", name);
		ed.putString("profit_key_email", email);
		ed.commit();
		loadMainscreen();
	}

	/**Alert for wrong username or password*/
	public void alertInvoker(){
		am.SingleButtonAlert("Login Error", "Username/Password wrong.!", "Retry");
	}

	/** Load MainScreen*/
	public void loadMainscreen(){
		finish();
		Intent intent= new Intent(getApplicationContext(), SimpleTabsActivity.class);
		startActivity(intent);

	}

	/**Set color to statusbar*/
	public void setStatusBarColor(View statusBar,int color){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window w = getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//status bar height
			//int actionBarHeight = getActionBarHeight();
			int statusBarHeight = esb.getStatusBarHeight();
			//action bar height
			statusBar.getLayoutParams().height = statusBarHeight;// + actionBarHeight ;
			statusBar.setBackgroundColor(color);
		}
	}

}
