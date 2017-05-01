package edu.miami.c11173414.smartcommunitydrawer;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private static final int ACTIVITY_SELECT_PICTURE = 1; // Activity ID for picture selection
    private static final int ACTIVITY_SELECT_LOCATION = 2;
    private static final int CAMERA_REQUEST = 1888;
    private boolean viewIsAtHome = true;
    protected String sessionUser;
    protected String userFullName;
    public LocationManager locationManager;
    public Location currentLocation;
    private Boolean gpsWorking = false;
    private Boolean netWorking = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)(getSystemService(LOCATION_SERVICE));
        detectLocators();

        sessionUser = this.getIntent().getStringExtra(getPackageName()+".username");
        userFullName = this.getIntent().getStringExtra(getPackageName()+".fullname");


        //TODO this better. As of now it is only workaround for NetworkOnMainThread Exception
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_fullname_display);
        nav_user.setText(userFullName);
        TextView nav_email = (TextView)hView.findViewById(R.id.user_email_textview);
        nav_email.setText(sessionUser + "@smartcommunity.com");

        //displayView(R.id.nav_user_info);
        displayView(new WelcomeLanding());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.nav_user_info); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
    }

    public void myClickHandler(View view) {
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
        switch (view.getId()){
            case R.id.report_button:
                displayView(R.id.nav_report);
                viewIsAtHome = false;
                break;
            case R.id.vote_button:
                displayView(new ReportListFragment());
                viewIsAtHome = false;
                break;
            case R.id.search_button:
                displayView(R.id.nav_search);
                viewIsAtHome = false;
                break;
            case R.id.report_photo:
                break;
            case R.id.listitem_pic:
                // Consider adding a zoom function, using zoomdialog
                break;
            case R.id.search_go_button:
                displayView(new ReportListFragment());
                viewIsAtHome = false;
                break;
            case R.id.upvote_icon:
                ImageView theImage = (ImageView)view;
                LinearLayout parent = (LinearLayout)view.getParent();
                parent.setBackgroundColor(Color.parseColor("#00FF00"));
                break;
            case R.id.downvote_icon:
                theImage = (ImageView)view;
                parent = (LinearLayout)view.getParent();
                parent.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
            case R.id.location_change_button:
                // TODO: create map interface to select new location
                Intent nextIntent = new Intent();
                nextIntent.setClassName(getPackageName(), getPackageName()+".LocationSelect");
                startActivityForResult(nextIntent, ACTIVITY_SELECT_LOCATION);
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i("onNavItemSelect:", item.getItemId()+"");
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_report:
                title  = "Report";
                Log.i("displayView:", "opening report fragment");
                // Todo: figure out reporting & classifying
                fragment = new ClassifyFragment();
                viewIsAtHome = false;
                break;
            case R.id.nav_search:
                title = "Search";
                Log.i("displayView:", "opening search fragment");
                fragment = new SearchFragment();
                viewIsAtHome = false;
                break;
            case R.id.nav_vote:
                title = "Vote";
                Log.i("displayView:", "opening search fragment");
                fragment = new ReportListFragment();
                viewIsAtHome = false;
                break;
            case R.id.nav_user_info:
                title = "About me";
                Log.i("displayView:", "opening about me fragment");
                fragment = new WelcomeLanding();
                viewIsAtHome = true;
                break;
            case R.id.nav_my_reports:
                title = "My Reports";
                Log.i("displayView:", "opening my reports fragment");
                fragment = new WelcomeLanding();
                viewIsAtHome = true;
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Bye!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
            default:
                fragment = new WelcomeLanding();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            Log.i("displayView: ", "Fragment isn't null!");
        }else{
            Log.i("displayView: ", "fragment is null!");
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    /**
     * @param nextFrag
     * Overloaded version of the displayView method,
     * takes a fragment rather than an id
     */
    public void displayView(Fragment nextFrag) {
        String title = getString(R.string.app_name);
        if (nextFrag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, nextFrag);
            ft.commit();
            Log.i("displayView: ", "Fragment isn't null!");
        }else{
            Log.i("displayView: ", "fragment is null!");
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void chooseAPicture(){
        // Opens picture gallery for user to select a picture, opens for result
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,ACTIVITY_SELECT_PICTURE);
    }


    private void detectLocators() {

        List<String> locators;

        locators = locationManager.getProviders(true);
        for (String aProvider : locators) {
            if (aProvider.equals(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this,"GPS available",Toast.LENGTH_LONG).show();
                gpsWorking = true;
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, getResources().getInteger(
                                    R.integer.time_between_location_updates_ms), 0, this);
                }catch (SecurityException e){
                    Log.i("HAHA ", "Should handle this but not");
                    e.printStackTrace();
                }
            }
            if (aProvider.equals(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(this,"Network available",Toast.LENGTH_LONG).show();
                netWorking = true;
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, getResources().getInteger(
                                    R.integer.time_between_location_updates_ms), 0, this);
                }catch (SecurityException e){
                    Log.i("HAHA ", "Should handle this but not");
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onResume() {
        String[] providers = {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER};
        int providerIndex;
        Location lastLocation;
        String errorMessage = "";

        super.onResume();
        currentLocation = null;
        providerIndex = 0;

        while (currentLocation == null && providerIndex < providers.length) {
            try {
                if ((lastLocation =
                        locationManager.getLastKnownLocation(providers[providerIndex])) != null &&
                        (System.currentTimeMillis() - lastLocation.getTime()) < getResources().getInteger(R.integer.threshold_for_last_location_ms)) {
                    currentLocation = lastLocation;
                    onLocationChanged(currentLocation);
                    return;
                }
                providerIndex++;
            } catch (SecurityException e) {
                errorMessage = e.getMessage();
            }
        }
        Toast.makeText(this,"No previous location available" + errorMessage, Toast.LENGTH_LONG).show();
    }
    /**
     * @param newLocation
     * Handles when a location has changed
     * Use this method to repopulate textviews,
     * Also get info to send to API
     */
    public void onLocationChanged(Location newLocation) {
        Log.i("onLocationChanged: ", "New location\n("+newLocation.getLatitude()+", "+newLocation.getLongitude());
        TextView locationView = (TextView)findViewById(R.id.current_location);
        String whereAmI="";

        DecodeLocation d = new DecodeLocation(getApplicationContext(),this);
        String decoded = d.doInBackground(newLocation);
        String verbose = d.doInBackground(newLocation);
        Log.i("onLocationChanged: ", "decoded location is " + verbose);

        whereAmI+= "(" + newLocation.getLatitude() + ", " + newLocation.getLongitude() + ")";
        try {
            locationView.setText(whereAmI);
            locationView.setText(decoded);
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Toast.makeText(this,"Cannot removeUpdates" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    /**
     * Handles results from gallery picture selection and picture evaluation
     * @param requestCode Identifier to identify which activity is sending the intent back here
     * @param resultCode Exit code for previous activity
     * @param data Intent passed from previous activity back to this activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case ACTIVITY_SELECT_PICTURE: // To handle when a user has selected a picture
                if(resultCode== Activity.RESULT_OK){
                    Log.i("MainAct", "we set image we pick form gallery");
                    ImageView pictureView = (ImageView)findViewById(R.id.report_photo);
                    Uri selectedURI = data.getData();
                    pictureView.setImageURI(selectedURI);
                }
                break;
            case ACTIVITY_SELECT_LOCATION:
                if(resultCode == RESULT_OK) {
                    double lat, lon;
                    lat = data.getDoubleExtra(getPackageName() + "LocationSelect.latitude", 0);
                    lon = data.getDoubleExtra(getPackageName() + "LocationSelect.longitude", 0);
                    Location newLocation = new Location("");
                    newLocation.setLatitude(lat);
                    newLocation.setLongitude(lon);
                    onLocationChanged(newLocation);
                }else {
                    Toast.makeText(this, "No new location found", Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_REQUEST:
                if(resultCode== Activity.RESULT_OK){
                    Log.i("MainAct", "we set image we took from camera");
                    ImageView pictureView = (ImageView)findViewById(R.id.report_photo);
                    Uri selectedURI = data.getData();
                    pictureView.setImageURI(selectedURI);
                }
                break;
        }
    }
}
