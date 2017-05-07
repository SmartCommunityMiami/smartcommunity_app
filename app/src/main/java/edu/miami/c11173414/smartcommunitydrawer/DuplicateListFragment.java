package edu.miami.c11173414.smartcommunitydrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DuplicateListFragment extends ListFragment implements View.OnClickListener {
    // ListView theList;
    private JSONArray jsonArray;
    private final String URL = "http://smart-community-dev.us-east-1.elasticbeanstalk.com/api/reports";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_duplicate_list, container, false);
        fragmentView.findViewById(R.id.duplicate_override_button).setOnClickListener(this);
        fragmentView.findViewById(R.id.back_home_button).setOnClickListener(this);
        //theList = (ListView) (fragmentView.findViewById(R.id.list));

        // Try to get any JSON passed into the list fragment

        String[] fromHashMapFieldNames = {"name", "picture", "id", "image"};
        int[] toListRowFieldIds = {R.id.listitem_description, R.id.listitem_pic, R.id.listitem_id, R.id.listitem_pic};
        ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Bundle bundle = this.getArguments();

        try {
            jsonArray = JsonReader.readJsonFromUrl(URL);
            Log.i("output", jsonArray.toString());
            jsonArray = JsonReader.sortJSONbyVotes(jsonArray);
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }

        if(bundle != null) {
            Log.i("ReportList", "bundle received, displaying reports ");
            try {
                int[] indices = bundle.getIntArray("reportIDArray");
                Log.i("ReportList", "received primitive array of length" + indices.length);
                ArrayList<Integer> indexList = new ArrayList<Integer>();
                for (int x = 0; x < indices.length; x++) {
                    Log.i("ReportList", "adding " + indices[x] + " to ID arraylist");
                    indexList.add(indices[x]);
                }

                Log.i("ReportList", "displaying reports in indexList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<String, Object>();
                    JSONObject jo = jsonArray.getJSONObject(i);
                    if (indexList.contains(jo.getInt("id"))) {
                        Log.i("ReportList", "report id matching bundle list");
                        map.put("name", jo.getString("description"));
                        map.put("id", jo.getInt("id"));
                        map.put("picture", R.drawable.ic_menu_camera);
                        map.put("image", ReportListFragment.buildPicURL(jo.getInt("id")));
                        listItems.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i("Reportlist", "list is " + listItems.toString());

        if(!listItems.isEmpty()){
            SCListAdapter listAdapter = new SCListAdapter(getActivity(),
                    listItems,
                    R.layout.report_list_item,
                    fromHashMapFieldNames,
                    toListRowFieldIds);

            setListAdapter(listAdapter);
        }else{
            Log.i("reportlist", "report list is empty");
        }
        return (fragmentView);
    }

    public void refreshList(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.duplicate_override_button:
                Fragment newReport = new ReportFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Classification", this.getArguments().getString("Classification"));
                newReport.setArguments(bundle);
                ((MainActivity) getActivity()).displayView(newReport);
                break;
            case R.id.back_home_button:
                ((MainActivity) getActivity()).displayView(new WelcomeLanding());
                break;
        }
    }
}
