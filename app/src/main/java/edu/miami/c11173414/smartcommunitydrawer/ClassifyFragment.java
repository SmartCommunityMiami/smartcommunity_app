package edu.miami.c11173414.smartcommunitydrawer;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClassifyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner dropDown1, dropDown2, dropDown3;
    Button continueButton;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_classify, container,false);

        if(((MainActivity)getActivity()).currentLocation != null){
            DecodeLocation d = new DecodeLocation(getActivity().getApplicationContext(), getActivity());
            ((TextView)(fragView.findViewById(R.id.current_location))).setText(d.doInBackground(((MainActivity)getActivity()).currentLocation));
        }

        dropDown1 = (Spinner) fragView.findViewById(R.id.classification_spinner1);
        dropDown2 = (Spinner) fragView.findViewById(R.id.classification_spinner2);
        dropDown3 = (Spinner) fragView.findViewById(R.id.classification_spinner3);
        dropDown1.setOnItemSelectedListener(this);
        dropDown2.setOnItemSelectedListener(this);
        dropDown3.setOnItemSelectedListener(this);
        dropDown2.setVisibility(View.GONE);
        dropDown3.setVisibility(View.GONE);
        continueButton = (Button)fragView.findViewById(R.id.classify_continue_button);
        continueButton.setOnClickListener(this);
        setSpinnerArray(dropDown1, R.array.classifications_array);

        return(fragView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.classify_continue_button:
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

                Toast.makeText(getActivity(), selectionText, Toast.LENGTH_LONG).show();
                Log.i("Classification:", selectionText);

                ArrayList<Integer> duplicateIDs = checkDuplicates(selectionText);
                if(duplicateIDs.size() == 0) {
                    // Send directly to new report fragment
                    Log.i("Classify", "no duplicates found, starting new report");
                    Fragment newReport = new ReportFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Classification", selectionText);
                    newReport.setArguments(bundle);
                    ((MainActivity)getActivity()).displayView(newReport);
                }else{
                    Log.i("Classify", "duplicates found, displaying");
                    Fragment duplicateList = new DuplicateListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("reportIDArray", SearchFragment.ArrayConvert(duplicateIDs));
                    bundle.putString("Classification", selectionText);
                    duplicateList.setArguments(bundle);
                    ((MainActivity)getActivity()).displayView(duplicateList);
                }
            default:
                break;
        }
    }

    private ArrayList<Integer> checkDuplicates(String classString){
        // Initialize the report id array we will send in bundle
        ArrayList<Integer> reportIDs = new ArrayList<Integer>();

        // Get the location from main
        Location loc = ((MainActivity)getActivity()).currentLocation;

        // Get our JSON to search through
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = JsonReader.readJsonFromUrl("http://smart-community-dev.us-east-1.elasticbeanstalk.com/api/reports");
            Log.i("output", jsonArray.toString());
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }

        // Get the classification of our new report
        int classification = ReportFragment.parseClassification(classString);

        // Search for report ids that match location, then classification
        ArrayList<Integer> nearbyReportIDs = SearchFragment.searchByRadius(jsonArray, loc, 1500);
        ArrayList<Integer> sameClassIDs = SearchFragment.searchByClassification(jsonArray, classification);

        // Find the report ids that are in both, add them to our output array
        for(int i: nearbyReportIDs){
            for(int j:sameClassIDs){
                if(i == j){
                    reportIDs.add(i);
                }
            }
        }

        return reportIDs;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        continueButton.setClickable(false);
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
                    continueButton.setClickable(true);
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
                    continueButton.setClickable(true);
                    break;
                case "Lights":
                    arrayId = R.array.lights_subarray;
                    break;
                case "Power Lines":
                    arrayId = R.array.powerline_subarray;
                    break;
                case "Outage/Blackout/Surges":
                    arrayId = -1;
                    continueButton.setClickable(true);
                    break;
                case "Lost Animal":
                    arrayId = -1;
                    continueButton.setClickable(true);
                    break;
                case "Dead Animal Removal":
                    arrayId = R.array.dead_animal_subarray;
                    break;
                case "Animal Bite":
                    arrayId = -1;
                    continueButton.setClickable(true);
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
            continueButton.setClickable(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
