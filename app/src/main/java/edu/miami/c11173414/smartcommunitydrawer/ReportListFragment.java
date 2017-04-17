package edu.miami.c11173414.smartcommunitydrawer;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportListFragment extends ListFragment {
    // ListView theList;
    private JSONArray jsonArray;
    private final String URL = "http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/reports.json";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_report_list, container,false);
        //theList = (ListView) (fragmentView.findViewById(R.id.list));
        try {
            jsonArray = JsonReader.readJsonFromUrl(URL);
            Log.i("output", jsonArray.toString());
        } catch (Exception e) {
            Log.e("exceptions", "are annoying");
            e.printStackTrace();
        }
        String[] fromHashMapFieldNames = {"name", "picture"};
        int[] toListRowFieldIds = {R.id.listitem_description,R.id.listitem_pic};
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            try {
                map.put("name", jsonArray.getJSONObject(i).getJSONObject("user").getString("username"));
            } catch (Exception e) {
                Log.e("ReportListView", "failure retriving username from json array/object. Stack trace:\n");
                e.printStackTrace();
            }
            map.put("picture",R.drawable.ic_menu_camera);
            listItems.add(map);
        }

        SimpleAdapter listAdapter = new SimpleAdapter(getActivity(),
                listItems,
                R.layout.report_list_item,
                fromHashMapFieldNames,
                toListRowFieldIds);

        setListAdapter(listAdapter);
        return(fragmentView);
    }
}
