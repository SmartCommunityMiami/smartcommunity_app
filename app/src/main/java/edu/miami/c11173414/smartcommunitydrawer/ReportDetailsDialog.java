package edu.miami.c11173414.smartcommunitydrawer;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.attr.defaultHeight;
import static android.R.attr.defaultWidth;
import static android.R.attr.width;
import static edu.miami.c11173414.smartcommunitydrawer.R.attr.height;


public class ReportDetailsDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_report_details_dialog, container, false);





        TextView issueTypeView, authorView, locationView, dateView, scoreView, bodyView;
        ImageView reportPicView;
        Button closeButton;

        issueTypeView = (TextView) fragmentView.findViewById(R.id.dialog_issue_type);
        authorView = (TextView) fragmentView.findViewById(R.id.dialog_author);
        locationView = (TextView) fragmentView.findViewById(R.id.dialog_location);
        dateView = (TextView) fragmentView.findViewById(R.id.dialog_date);
        scoreView = (TextView) fragmentView.findViewById(R.id.dialog_score);
        bodyView = (TextView) fragmentView.findViewById(R.id.dialog_description);
        reportPicView = (ImageView) fragmentView.findViewById(R.id.dialog_image);
        closeButton = (Button) fragmentView.findViewById(R.id.dialog_close_button);
        closeButton.setOnClickListener(this);

        Bundle args = getArguments();
        if(args != null) {
            String issueType, reportDescription, username, location, createdAt, picURL;
            int voteScore;

            issueType = args.getString("issueType");
            reportDescription = args.getString("reportDescription");
            username = args.getString("username");
            location = args.getString("location");
            createdAt = args.getString("createdAt");
            picURL = args.getString("picURL");
            voteScore = args.getInt("votes");

            issueTypeView.setText(issueType);
            bodyView.setText("Description: " + reportDescription);
            authorView.setText("Created by\n" + username);
            locationView.setText("At: " + location);
            dateView.setText("On: " + createdAt);
            scoreView.setText("Vote score: " + voteScore + " points");
            Picasso.with(getActivity()).load(picURL).into(reportPicView);
        }else{
            Log.i("ReportDetailsDialog", "arguments null!");
        }
        return fragmentView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(1000, 1500);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
