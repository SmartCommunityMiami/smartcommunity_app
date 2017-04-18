package edu.miami.c11173414.smartcommunitydrawer;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class WelcomeLanding extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_welcome_landing, container,false);
        String username = ((MainActivity)(getActivity())).sessionUser;
        TextView userDisplay = (TextView)fragmentView.findViewById(R.id.welcome_username_display);
        userDisplay.setText(username);
        return(fragmentView);
    }
}
