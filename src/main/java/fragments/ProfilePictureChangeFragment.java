package fragments;
/**
 *  ProfitKey v1.0
 * 	Purpose	   : Fragment to change the profile pic
 *  Created by : Abish
 *  Created Dt :
 *  Modified on: modified to run only when the page is visible
 *  Verified by:
 *  Verified Dt:
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import general.ApplicationConfigs;
import general.SaveCameraImageInStorage;
import general.StaticConstants;
import helix.profitkey.hotelapp.R;


public class ProfilePictureChangeFragment extends Fragment {
    /**Global Declarations*/
    ApplicationConfigs application;
    Button ok;
    ImageView prof_img;
    View view;
    int SELECT_PICTURE=1,CAMERA_REQUEST=2;
    private String selectedImagePath,name;
    boolean phototaken = false;

    public ProfilePictureChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Get the name of the user*/
        SharedPreferences sp=getActivity().getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        name = sp.getString("profit_key_users_name","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile_pic_change, container, false);
        initializeViews();
        checkOldProfImage();
        return view;
    }

    /**View initialize*/
    private void initializeViews(){
        ok = (Button)view.findViewById(R.id.ok);
        prof_img =(ImageView)view.findViewById(R.id.prof_img);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        prof_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        /** Change custom font*/
        application = (ApplicationConfigs)getActivity().getApplication();
        application.setTypefaceButton(ok);
    }

    /**Check the old profile image to set in imageview as default*/
    private void checkOldProfImage(){
        File f= new File(StaticConstants.prof_img+name+".png");
        if(f.exists()) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(StaticConstants.prof_img+name+".png"));
            prof_img.setImageBitmap(thumbnail);
        }
//        else
//            prof_img.setImageResource(R.drawable.tyre);
    }

    /**Invoker to capture the image and take from gallery*/
    private void changeImage(){
        final CharSequence[] items = { "Take Picture", "Take from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Picture")) {
                    imageFromCamera();
                } else if (items[item].equals("Take from Gallery")) {
                    imageFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        //Toast.makeText(getActivity(), "Gonna Set Image", Toast.LENGTH_SHORT).show();
    }

    /**Action to take image from gallery*/
    private void imageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /**Action to capture image from camera*/
    private void imageFromCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    /**Called to process the camera and gallery image*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            //Toast.makeText(getActivity(), "Image "+selectedImagePath, Toast.LENGTH_SHORT).show();
            Bitmap thumbnail = (BitmapFactory.decodeFile(selectedImagePath));
            prof_img.setImageBitmap(thumbnail);
            phototaken =true;
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            prof_img.setImageBitmap(photo);
            phototaken=true;
        }
    }

    /**Save the the image*/
    private void saveImage(){
        if(phototaken) {
            SaveCameraImageInStorage sciis = new SaveCameraImageInStorage(getActivity());
            sciis.saveImage("ProfitKey/profile", name, prof_img);
            changeFragment();
        }else
            Toast.makeText(getActivity(), "Photos not taken to save", Toast.LENGTH_SHORT).show();
    }

    /** helper to retrieve the path of an image URI */
    public String getPath(Uri uri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /** After changing the image call the settings button fragment*/
    public void changeFragment(){
        Toast.makeText(getActivity(), "Profile Image Saved.", Toast.LENGTH_SHORT).show();
        SettingsButtonFragment fr = new SettingsButtonFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_communicator, fr);
        fragmentTransaction.commit();
    }

}
