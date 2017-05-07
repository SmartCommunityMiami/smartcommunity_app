package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by bmaune on 5/6/17.
 */

public class SCListAdapter extends SimpleAdapter {
    public SCListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        ImageView reportPic = (ImageView) v.findViewById(R.id.listitem_pic);

        // get the url from the data you passed to the `Map`
        String url = (String) ((Map)getItem(position)).get("image");
        // do Picasso
        Picasso.with(v.getContext()).load(url).into(reportPic);

        return v;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
