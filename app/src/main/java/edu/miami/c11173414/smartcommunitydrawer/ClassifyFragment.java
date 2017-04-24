package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Switch;
import android.widget.Toast;

public class ClassifyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner dropDown1, dropDown2, dropDown3;
    Button continueButton;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_classify, container,false);

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

                Toast.makeText(getActivity(),
                        selectionText,
                        Toast.LENGTH_LONG).show();
                Log.i("Classification:", selectionText);
            default:
                break;
        }
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
