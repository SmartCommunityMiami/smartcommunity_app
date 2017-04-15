package edu.miami.c11173414.smartcommunitydrawer;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportListFragment extends ListFragment {
    // ListView theList;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_report_list, container,false);
        //theList = (ListView) (fragmentView.findViewById(R.id.list));

        String[] fromHashMapFieldNames = {"name", "picture"};
        int[] toListRowFieldIds = {R.id.listitem_description,R.id.listitem_pic};
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < 5; i++) {
            HashMap<String, Object> oneItem = new HashMap<String, Object>();
            oneItem.put("name", "Report #"+i);
            oneItem.put("picture",R.drawable.ic_menu_camera);
            listItems.add(oneItem);
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
