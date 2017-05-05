package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private final String URL = "http://smart-community-dev.us-east-1.elasticbeanstalk.com/api/reports";
    private JSONArray jsonArray;
    private ArrayList<Integer> reportIDs;
    private Bundle toDisplay;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container,false);
        Button searchButton = (Button)fragmentView.findViewById(R.id.search_go_button);
        searchButton.setOnClickListener(this);
        reportIDs = new ArrayList<Integer>();
        toDisplay = new Bundle();

        try {
            jsonArray = JsonReader.readJsonFromUrl(URL);
            Log.i("output", jsonArray.toString());
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }

        return(fragmentView);
    }

    public String searchByRadius(float radius){
        String outputJSON = "";
        Location current = ((MainActivity)getActivity()).currentLocation;
        for (int x = 0; x < jsonArray.length(); x++) {
            try {
                JSONObject jo = jsonArray.getJSONObject(x);
                double lat = jo.getDouble("latitude");
                double lon = jo.getDouble("longitude");
                Location reportLoc = new Location("");
                reportLoc.setLatitude(lat);
                reportLoc.setLongitude(lon);
                float dist = current.distanceTo(reportLoc);
                if(dist < radius){
                    Log.i("searchByRadius", "found report in radius");
                    reportIDs.add(jo.getInt("id"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return outputJSON;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_go_button:
                // TODO: get radius of search from user input
                searchByRadius(153); // 500 feet
                Log.i("SearchFragment", "found " + reportIDs.size() + " in radius");
                int[] primIDs = new int[reportIDs.size()];
                for(int i = 0; i < reportIDs.size(); i++){
                    primIDs[i] = reportIDs.get(i);
                    Log.i("", primIDs[i] + "");
                }
                Log.i("SearchFragment", "Putting primitive array of length: " + primIDs.length);
                toDisplay.putIntArray("reportIDArray", primIDs);
                Fragment list = new ReportListFragment();
                list.setArguments(toDisplay);
                ((MainActivity)getActivity()).displayView(list);
                break;
            default:
                break;
        }
    }
}
