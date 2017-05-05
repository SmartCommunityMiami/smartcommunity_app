package edu.miami.c11173414.smartcommunitydrawer;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.R.id.list;

public class ReportListFragment extends ListFragment {
    // ListView theList;
    private JSONArray jsonArray;
    private final String URL = "http://smart-community-dev.us-east-1.elasticbeanstalk.com/api/reports";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_report_list, container, false);
        //theList = (ListView) (fragmentView.findViewById(R.id.list));

        // Try to get any JSON passed into the list fragment

        String[] fromHashMapFieldNames = {"name", "picture", "id"};
        int[] toListRowFieldIds = {R.id.listitem_description, R.id.listitem_pic, R.id.listitem_id};
        ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Bundle bundle = this.getArguments();

        try {
            jsonArray = JsonReader.readJsonFromUrl(URL);
            Log.i("output", jsonArray.toString());
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }
        if (bundle == null) {
            Log.i("ReportList", "no bundle sent, displaying all reports");
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.i("ReportList", "displaying report with index " + i);
                try {
                    map = new HashMap<String, Object>();
                    JSONObject jo = jsonArray.getJSONObject(i);
                    map.put("name", jo.getString("description"));
                    map.put("id", jo.getInt("id"));
                    map.put("picture", R.drawable.ic_menu_camera);
                    listItems.add(map);
                } catch (Exception e) {
                    Log.e("ReportListView", "failure retrieving username from json array/object. Stack trace:\n");
                    e.printStackTrace();
                }


            }
        }
        else {
            Log.i("ReportList", "bundle received, displaying reports ");
            try {
                int[] indices = bundle.getIntArray("reportIDArray");
                Log.i("ReportList", "received primitive array of length" + indices.length);
                ArrayList<Integer> indexList = new ArrayList<Integer>();
                for(int x=0; x < indices.length; x++){
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
                        listItems.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i("Reportlist", "list is " + listItems.toString());

        if(!listItems.isEmpty()){
            SimpleAdapter listAdapter = new SimpleAdapter(getActivity(),
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

}
