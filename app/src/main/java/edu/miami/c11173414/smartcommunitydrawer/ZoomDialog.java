package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ZoomDialog extends DialogFragment {
    View dialogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.fragment_zoom_dialog,container);
        dialogView.findViewById(R.id.zoom_dismiss_button).setOnClickListener(myClickHandler);
        ((ImageView)dialogView.findViewById(R.id.image_zoomed)).setImageResource(getArguments().getInt("picture_id"));
        return(dialogView);
    }
    private View.OnClickListener myClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };
}
