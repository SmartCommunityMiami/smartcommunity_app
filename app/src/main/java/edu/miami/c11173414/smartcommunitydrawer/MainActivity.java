package edu.miami.c11173414.smartcommunitydrawer;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int ACTIVITY_SELECT_PICTURE = 1; // Activity ID for picture selection
    private boolean viewIsAtHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        displayView(R.id.nav_user_info);
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
                displayView((Fragment) (new ReportListFragment()));
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
                displayView((Fragment) (new ReportListFragment()));
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
                fragment = new ReportFragment();
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
                finish();
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
                    ImageView pictureView = (ImageView)findViewById(R.id.report_photo);
                    Uri selectedURI = data.getData();
                    pictureView.setImageURI(selectedURI);
                }
                break;
        }
    }
}
