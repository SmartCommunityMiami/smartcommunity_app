package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ReportFragment extends Fragment {

    private static final int ACTIVITY_SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 1888;

    OnClickListener myClickHandlerReport = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.upload_existing_pic:
                    chooseAPicture();
                    break;
                case R.id.take_new_photo:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    break;
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button upload = null, take = null;
        View fragmentView = inflater.inflate(R.layout.fragment_report, container, false);


        upload = (Button) fragmentView.findViewById(R.id.upload_existing_pic);
        take = (Button) fragmentView.findViewById(R.id.take_new_photo);
        upload.setOnClickListener(myClickHandlerReport);
        take.setOnClickListener(myClickHandlerReport);
        return (fragmentView);
    }



    public void chooseAPicture() {
        // Opens picture gallery for user to select a picture, opens for result
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE);
    }

}