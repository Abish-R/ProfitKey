package helix.profitkey.hotelapp;
/** Profit Key 1.0.0
 *  Purpose	   : Tab Layout Activity with
 *  Created by : Abish
 *  Created Dt :
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import fragments.AvailableRoomFragment;
import general.ApplicationConfigs;
import general.CheckInternet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fragments.OfferFragment;
import fragments.ReviewFragment;
import fragments.BookedHistoryFragment;
import general.CommonMethods;
import general.EditStatusBar;
import general.StaticConstants;

public class SimpleTabsActivity extends AppCompatActivity {
    /** Global declarations */
    CheckInternet ci=new CheckInternet(this);
    ApplicationConfigs application;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView toolbar_title;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    String selected_offer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_simple_tabs);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.status_biscuit));

        initializeViews();
        customizeToolbar();

        /** Call to setup a tabs using the viewPager*/
        setupViewPager(viewPager);

        /** Setting up tab layout view from super class*/
        tabLayout.setupWithViewPager(viewPager);

        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int page) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/

        mNavigationView.setItemIconTintList(null);
        /** Navigate to screens from navigation drawer to all the screens*/
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.menuItem1) {
                    navigateToTabs(0);
                }
                else if (menuItem.getItemId() == R.id.menuItem2) {
                    navigateToTabs(1);
                }
                else if (menuItem.getItemId() == R.id.menuItem4) {
                    navigateToTabs(2);
                }
                else if (menuItem.getItemId() == R.id.menuItem3) {
                    navigateToTabs(3);
                }
                else if (menuItem.getItemId() == R.id.menuItem5) {
                    callInvokingClass(ReviewWriteNew.class);
                }
                else if (menuItem.getItemId() == R.id.menuItem6) {
                    callInvokingClass(Settings.class);
                }
                else if (menuItem.getItemId() == R.id.menuItem7) {
                    callInvokingClass(About.class);
                }
                else if (menuItem.getItemId() == R.id.menuItem8) {
                    callInvokingClass(Help.class);
                }
                else if (menuItem.getItemId() == R.id.menuItem9) {
                    /** Clear all the stored values from shared preferences*/
                    SharedPreferences sharedPref = getSharedPreferences("profit_key", Context.MODE_PRIVATE);
                    sharedPref.edit().clear().commit();
                    Toast.makeText(getApplicationContext(), "You logged out.", Toast.LENGTH_SHORT).show();
                    finish();
                    callInvokingClass(Login.class);
                }

                return false;
            }

        });

        /*** Setup Drawer Toggle of the Toolbar */
        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void onStart() {
        super.onStart();
        /** Get users name and email */
        SharedPreferences sp = getSharedPreferences("profit_key", Context.MODE_PRIVATE);
        String name = sp.getString("profit_key_users_name", "");
        String email = sp.getString("profit_key_email", "");
        if(counter!=0)
            nav_header.setVisibility(View.GONE);
        setNavigationViewHeader(name, email);
    }

    /** Initialize view objects*/
    private void initializeViews(){
        application = (ApplicationConfigs)getApplicationContext();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.drawerstuff) ;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    /** Toolbar customization*/
    private void customizeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title =(TextView)toolbar.findViewById(R.id.toolbar_title);
        application.setTypefaceTextView(toolbar_title);
        toolbar_title.setText(R.string.hotel_name);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getString(R.string.hotel_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /** Navigation drawer custom font set, Got better soln so hided.
     *  set some default font and replace when application starts. */
//    public boolean onCreateOptionsMenu(Menu menu) {
//        final ArrayList<View> mMenuItems = new ArrayList<>(9);
//        final Menu navMenu = mNavigationView.getMenu();
//        final Typeface regular = Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat-Bold.ttf");
//        // Install an OnGlobalLayoutListener and wait for the NavigationMenu to fully initialize
//        mNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Remember to remove the installed OnGlobalLayoutListener
//                mNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                // Loop through and find each MenuItem View
//                for (int i = 0, length = 9; i < length; i++) {
//                    final String id = "menuItem" + (i + 1);
//                    final MenuItem item = navMenu.findItem(getResources().getIdentifier(id, "id", getPackageName()));
//                    mNavigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
//                }
//                // Loop through each MenuItem View and apply your custom Typeface
//                for (final View menuItem : mMenuItems) {
//                    ((TextView) menuItem).setTypeface(regular, Typeface.BOLD);
//                }
//            }
//        });

 //       return true;
 //'   }


    /** Call of setting ViewPageAdapter and pass the fragments which we want to add in Tab layout*/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OfferFragment(), getString(R.string.offers));
        adapter.addFragment(new AvailableRoomFragment(), getString(R.string.rooms));
        adapter.addFragment(new ReviewFragment(), getString(R.string.reviews));
        adapter.addFragment(new BookedHistoryFragment(), getString(R.string.booked_history));
        viewPager.setAdapter(adapter);
    }

    /** Class to set all fragment in Tab Layout.*/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    View nav_header; int counter=0;
    /** Set navigation drawer header with Name, Email, Profile image, we can */
    private void setNavigationViewHeader(String nam, String email){
        counter++;
        nav_header = LayoutInflater.from(this).inflate(R.layout.navigation_view_header, null);
        TextView user = ((TextView) nav_header.findViewById(R.id.username));
        TextView mail = ((TextView) nav_header.findViewById(R.id.email));
        application.setTypefaceTextView(user);
        application.setTypefaceTextView(mail);
        user.setText(nam);
        mail.setText(email);
        ImageView iview = (ImageView) nav_header.findViewById(R.id.profile_image);

        /** Check for profile image is there in local memory and set, if not set default image*/
        File f= new File(StaticConstants.prof_img+nam+".png");
        if(f.exists()) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(StaticConstants.prof_img+nam+".png"));
            iview.setImageBitmap(thumbnail);
        }else
            iview.setImageResource(R.drawable.avatar_nav_bar);

        mNavigationView.removeHeaderView(nav_header);
        mNavigationView.addHeaderView(nav_header);
    }

    /**Set color to statusbar*/
    public void setStatusBarColor(View statusBar,int color){
        EditStatusBar esb = new EditStatusBar(this);
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

    /** Invoke passed class */
    private void callInvokingClass(Class in){
        Intent intent = new Intent(getApplicationContext(), in);
        startActivity(intent);
    }

    /** Called from navigation drawer to go for navigation within tabs*/
    public void navigateToTabs(int tabcount){
        TabLayout.Tab tab = tabLayout.getTabAt(tabcount);
        tab.select();
    }

    /** set Selected category from Available Rooms custom Adapter to get filtered output from offers page */
    public void sendToActivity(String selectedCategory){
        selected_offer = selectedCategory;
    }

    /** get Selected category from Available Rooms custom Adapter to get filtered output from offers page */
    public String getSelectedOfferFromActivity(){
        return selected_offer;
    }
}
