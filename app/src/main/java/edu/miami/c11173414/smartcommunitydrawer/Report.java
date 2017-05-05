package edu.miami.c11173414.smartcommunitydrawer;

import android.support.annotation.NonNull;

/**
 * Created by bmaune on 5/5/17.
 */

public class Report implements Comparable {
    private String description;
    private double latitude;
    private double longitude;

    public Report(String d, double lat, double lon){
        description = d;
        latitude = lat;
        longitude = lon;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
