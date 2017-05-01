package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ReportFragment extends Fragment {

    private static final int ACTIVITY_SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String classificationString;
    private Location curLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button upload = null, take = null, submit = null;
        View fragmentView = inflater.inflate(R.layout.fragment_report, container, false);
        TextView classText;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            classText = (TextView) fragmentView.findViewById(R.id.reportClassText);
            classificationString = bundle.getString("Classification");
            Log.i("ReportFragment: ", classificationString);
            curLoc = ((MainActivity)(getActivity())).currentLocation;
            String locString = "(" + curLoc.getLongitude() + ", " + curLoc.getLatitude() + ")";
            classText.setText(classificationString + " at " + locString);
        }else{
            Log.i("ReportFragment: ", "bundle is null!");
        }

        submit = (Button) fragmentView.findViewById(R.id.send_report_button);
        submit.setOnClickListener(myClickHandlerReport);
        upload = (Button) fragmentView.findViewById(R.id.upload_existing_pic);
        take = (Button) fragmentView.findViewById(R.id.take_new_photo);
        upload.setOnClickListener(myClickHandlerReport);
        take.setOnClickListener(myClickHandlerReport);
        return (fragmentView);
    }

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
                case R.id.send_report_button:
                    Log.i("ReportFragment: " , "clicked report submit button");
                    String desc = ((TextView)(getView().findViewById(R.id.report_desc_textbox))).getText().toString();
                    sendReport(desc, parseClassification(classificationString), curLoc);
                    break;
            }

        }
    };

    private boolean sendReport(String description, int classification, Location curLoc) {
        float lat = (float) curLoc.getLatitude();
        float lng = (float) curLoc.getLongitude();
        int reportID;

        HttpURLConnection http = null;
        try {
            JSONObject details = new JSONObject();
            JSONObject report = new JSONObject();
            details.put("description", description);
            details.put("classification", classification);
            details.put("latitude", lat);
            details.put("longitude", lng);
            report.put("report", details);
            String urlParameters = report.toString();

            URL url = new URL("http://smartcommunity-dev2.us-east-1.elasticbeanstalk.com/api/reports");
            URLConnection con = url.openConnection();
            http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            http.setInstanceFollowRedirects(false);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("charset", "utf-8");
            http.setRequestProperty("Accept", "application/json");
            http.setUseCaches(false);

            try (OutputStream wr = http.getOutputStream()) {
                wr.write(urlParameters.getBytes());
            }
            if (http.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : " + http.getResponseCode());
            }
            if (http.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + http.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            Toast.makeText(getActivity(), "Account created!", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.i("sendCreateAcc: ", "Something went wrong"); //saying something is wrong isnt
            e.printStackTrace();                                 // helpful if you don't print stack trace doofus ;)
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return false;
    }

    private int parseClassification(String classification){
        return 0;
    }

    public void chooseAPicture() {
        // Opens picture gallery for user to select a picture, opens for result
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE);
    }
}