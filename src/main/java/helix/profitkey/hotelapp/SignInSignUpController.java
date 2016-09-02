///** Profit Key 1.0.0
// * 	Purpose	   : SignIn and SignUp Controller
// *  Created by : Abish
// *  Created Dt : 15-01-2016
// *  Modified on:
// *  Verified by: Srinivas
// *  Verified Dt:
// * **/
//
//package helix.profitkey.hotelapp;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//public class SignInSignUpController extends Activity {
//	Button signin,signup;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.signin_signup_control);
//		signin=(Button)findViewById(R.id.signin);
//		signup=(Button)findViewById(R.id.signup);
//		/**Navigate to login page*/
//		signin.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent start_signin=new Intent(getApplicationContext(),Login.class);
//				startActivity(start_signin);
//				finish();
//			}
//		});
//		/**Navigate to sign Up page*/
//		signup.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent start_signup=new Intent(getApplicationContext(),SignUp.class);
//				startActivity(start_signup);
//				finish();
//			}
//		});
//	}
//}
