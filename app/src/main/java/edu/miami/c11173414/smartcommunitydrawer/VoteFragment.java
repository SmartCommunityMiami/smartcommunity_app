package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class VoteFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return(inflater.inflate(R.layout.fragment_vote, container,false));
    }
}
