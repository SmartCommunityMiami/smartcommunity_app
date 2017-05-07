package edu.miami.c11173414.smartcommunitydrawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class WelcomeLanding extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_welcome_landing, container,false);
        String username = ((MainActivity)(getActivity())).sessionUser;
        int userID = ((MainActivity)getActivity()).getUserId();
        TextView userDisplay = (TextView)fragmentView.findViewById(R.id.welcome_username_display);
        userDisplay.setText(username);
        if (userID == -1) { //if we're a guest
            Button b = (Button)fragmentView.findViewById(R.id.report_button);
            Button b2 = (Button)fragmentView.findViewById(R.id.vote_button);
            b.setEnabled(false);
            b2.setEnabled(false);
        }
        return(fragmentView);
    }
}
