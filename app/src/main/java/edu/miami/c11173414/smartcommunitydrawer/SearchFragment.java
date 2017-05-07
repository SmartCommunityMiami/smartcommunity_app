package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private final String URL = "http://smart-community-dev.us-east-1.elasticbeanstalk.com/api/reports";
    private JSONArray jsonArray;
    private ArrayList<Integer> reportIDs;
    private Bundle toDisplay;
    private Button searchButton;
    private Spinner dropDown1, dropDown2, dropDown3;
    private RadioGroup rg;
    private SeekBar radiusSeeker;
    private EditText minScoreEntry;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container,false);
        searchButton = (Button)fragmentView.findViewById(R.id.search_go_button);
        searchButton.setOnClickListener(this);
        reportIDs = new ArrayList<Integer>();
        toDisplay = new Bundle();

        fragmentView.findViewById(R.id.classifyView).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.radiusView).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.scoreView).setVisibility(View.GONE);
        rg = (RadioGroup) fragmentView.findViewById(R.id.filterSelect);
        rg.setOnCheckedChangeListener(this);
        radiusSeeker = (SeekBar) fragmentView.findViewById(R.id.radiusSeeker);
        radiusSeeker.setMax(1610);
        radiusSeeker.setOnSeekBarChangeListener(this);
        minScoreEntry = (EditText) fragmentView.findViewById(R.id.minScoreEntry);
        dropDown1 = (Spinner) fragmentView.findViewById(R.id.classification_spinner1);
        dropDown2 = (Spinner) fragmentView.findViewById(R.id.classification_spinner2);
        dropDown3 = (Spinner) fragmentView.findViewById(R.id.classification_spinner3);
        dropDown1.setOnItemSelectedListener(this);
        dropDown2.setOnItemSelectedListener(this);
        dropDown3.setOnItemSelectedListener(this);
        dropDown2.setVisibility(View.GONE);
        dropDown3.setVisibility(View.GONE);
        setSpinnerArray(dropDown1, R.array.classifications_array);

        try {
            jsonArray = JsonReader.readJsonFromUrl(URL);
            Log.i("output", jsonArray.toString());
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }

        return(fragmentView);
    }

    public static ArrayList<Integer> searchByRadius(JSONArray jsonIn, Location currentLocation, float radius){
        ArrayList<Integer> idsOut = new ArrayList<Integer>();
        for (int x = 0; x < jsonIn.length(); x++) {
            try {
                JSONObject jo = jsonIn.getJSONObject(x);
                double lat = jo.getDouble("latitude");
                double lon = jo.getDouble("longitude");
                Location reportLoc = new Location("");
                reportLoc.setLatitude(lat);
                reportLoc.setLongitude(lon);
                float dist = currentLocation.distanceTo(reportLoc);
                if(dist < radius){
                    Log.i("searchByRadius", "found report in radius");
                    idsOut.add(jo.getInt("id"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return idsOut;
    }

    public static ArrayList<Integer> searchByScore(JSONArray jsonIn, int scorethreshold){
        ArrayList<Integer> idsOut = new ArrayList<Integer>();
        for (int x = 0; x < jsonIn.length(); x++) {
            try {
                JSONObject jo = jsonIn.getJSONObject(x);
                int curScore = jo.getInt("votes");
                if(curScore >= scorethreshold){
                    Log.i("searchByClassification", "found report meeting minimum score threshold");
                    idsOut.add(jo.getInt("id"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return idsOut;
    }

    public static ArrayList<Integer> searchByClassification(JSONArray jsonIn, int classification){
        ArrayList<Integer> idsOut = new ArrayList<Integer>();
        for (int x = 0; x < jsonIn.length(); x++) {
            try {
                JSONObject jo = jsonIn.getJSONObject(x);
                JSONObject issue = jo.getJSONObject("issue");
                int curClass = issue.getInt("id");
                if(curClass == classification){
                    Log.i("searchByClassification", "found report matching classification");
                    idsOut.add(jo.getInt("id"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return idsOut;
    }

    public static int[] ArrayConvert(ArrayList<Integer> a){
        int[] out = new int[a.size()];
        for(int i = 0; i < a.size(); i++){
            out[i] = a.get(i);
        }
        return out;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_go_button:
                switch (rg.getCheckedRadioButtonId()){
                    case R.id.nearMeRadio:
                        // If user is searching by radius around them
                        int radius = radiusSeeker.getProgress();
                        reportIDs = searchByRadius(jsonArray, ((MainActivity)getActivity()).currentLocation, radius); // 500 feet
                        Log.i("SearchFragment", "found " + reportIDs.size() + " in radius");
                        break;
                    case R.id.scoreRadio:
                        if(minScoreEntry.getText() != null) {
                            int minScore = Integer.parseInt(minScoreEntry.getText().toString());
                            reportIDs = searchByScore(jsonArray, minScore);
                            Log.i("SearchFragment", "found " + reportIDs.size() + "meeting minimum score");
                        }
                        break;
                    case R.id.issueTypeRadio:
                        String selectionText = "";
                        if(dropDown1.getSelectedItem() != null){
                            selectionText += dropDown1.getSelectedItem().toString()+"/";
                        }
                        if(dropDown2.getSelectedItem() != null){
                            selectionText += dropDown2.getSelectedItem().toString()+"/";
                        }
                        if(dropDown3.getSelectedItem() != null){
                            selectionText += dropDown3.getSelectedItem().toString();
                        }
                        int classificationID = ReportFragment.parseClassification(selectionText);
                        reportIDs = searchByClassification(jsonArray, classificationID);
                        Log.i("SearchFragment", "found " + reportIDs.size() + " with selected classification");
                        break;
                }

                int[] primIDs = ArrayConvert(reportIDs);
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.nearMeRadio:
                searchButton.setClickable(true);
                getView().findViewById(R.id.radiusView).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.scoreView).setVisibility(View.GONE);
                getView().findViewById(R.id.classifyView).setVisibility(View.GONE);
                break;
            case R.id.scoreRadio:
                searchButton.setClickable(true);
                getView().findViewById(R.id.radiusView).setVisibility(View.GONE);
                getView().findViewById(R.id.scoreView).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.classifyView).setVisibility(View.GONE);
                break;
            case R.id.issueTypeRadio:
                searchButton.setClickable(false);
                getView().findViewById(R.id.radiusView).setVisibility(View.GONE);
                getView().findViewById(R.id.scoreView).setVisibility(View.GONE);
                getView().findViewById(R.id.classifyView).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        TextView progDisplay = (TextView) getView().findViewById(R.id.radiusDisplay);
        DecimalFormat format = new DecimalFormat("0.00");
        if(progress >= 402.336){
            // Display radius in miles
            progDisplay.setText("Radius: " + format.format(metersToMiles(progress)) + " miles");
        }else{
            progDisplay.setText("Radius: " + format.format(metersToFeet(progress)) + " feet");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i("onItemSelected: ", "Button set unclickable");
        String selection = adapterView.getItemAtPosition(position).toString();
        int arrayId = -1;
        if(adapterView.equals(dropDown1)){
            // If the spinner triggering this is dropdown1
            Log.i("DropDown1: ", selection);
            // Set the array for dropdown2
            switch (selection) {
                case "Water":
                    // Dropdown 1 selection is water
                    arrayId = R.array.water_subarray;
                    break;
                case "Transportation":
                    // Dropdown 1 selection is transportation
                    arrayId = R.array.transportation_subarray;
                    break;
                case "Electricity":
                    // Dropdown 1 selection is electricity
                    arrayId = R.array.electricity_subarray;
                    break;
                case "Wildlife":
                    // Dropdown 1 selection is wildlife
                    arrayId = R.array.wildlife_subarray;
                    break;
                default:
                    break;
            }
            setSpinnerArray(dropDown2, arrayId);
        }
        else if(adapterView.equals(dropDown2)){
            switch (selection){
                case "Broken/Missing Water Cover":
                    arrayId = -1;
                    searchButton.setClickable(true);
                    break;
                case "Flooding":
                    // Dropdown 2 selection is flooding
                    arrayId = R.array.flooding_subarray;
                    break;
                case "Water Quality":
                    // Dropdown 2 selection is water quality
                    arrayId = R.array.quality_subarray;
                    break;
                case "Waste Water":
                    // Dropdown 2 selection is waste water
                    arrayId = R.array.waste_subarray;
                    break;
                case "Canals":
                    // Dropdown 2 selection is canals
                    arrayId = R.array.canals_subarray;
                    break;
                case "Signs":
                    // Dropdown 2 selection is signs
                    arrayId = R.array.signs_subarray;
                    break;
                case "Roads":
                    // Dropdown 2 selection is roads
                    arrayId = R.array.roads_subarray;
                    break;
                case "Traffic Lights":
                    arrayId = -1;
                    searchButton.setClickable(true);
                    break;
                case "Lights":
                    arrayId = R.array.lights_subarray;
                    break;
                case "Power Lines":
                    arrayId = R.array.powerline_subarray;
                    break;
                case "Outage/Blackout/Surges":
                    arrayId = -1;
                    searchButton.setClickable(true);
                    break;
                case "Lost Animal":
                    arrayId = -1;
                    searchButton.setClickable(true);
                    break;
                case "Dead Animal Removal":
                    arrayId = R.array.dead_animal_subarray;
                    break;
                case "Animal Bite":
                    arrayId = -1;
                    searchButton.setClickable(true);
                    break;
                case "Animal Infestation":
                    arrayId = R.array.animal_infestation_subarray;
                    break;
                default:
                    break;
            }
            setSpinnerArray(dropDown3, arrayId);
        }
        else if(adapterView.equals(dropDown3)){
            // If the spinner triggering this is dropdown3
            Log.i("DropDown3: ", selection);
        }

        if((dropDown3.getSelectedItem() != null) && !(dropDown3.getSelectedItem().toString().equals("Select one..."))){
            Log.i("onItemSelected: ","Button set clickable");
            searchButton.setClickable(true);
        }
    }

    public float metersToFeet(int meters){
        return meters * 3.28084f;
    }

    public float metersToMiles(int meters){
        return meters * 0.000621371f;
    }

//------------------------------------------------------------------------------------------

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setSpinnerArray(Spinner s, int i){
        if (i == -1){
            s.setVisibility(View.GONE);
            s.setAdapter(null);
        }else {
            s.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    i,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);
        }
    }
}
