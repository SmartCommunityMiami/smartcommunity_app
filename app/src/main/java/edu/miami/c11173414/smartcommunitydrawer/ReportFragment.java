package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
    private ImageView iv;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button upload = null, take = null, submit = null;
        View fragmentView = inflater.inflate(R.layout.fragment_report, container, false);
        TextView classText;
        iv = (ImageView) getView().findViewById(R.id.report_photo);
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
                    sendReport(desc, parseClassification(classificationString), curLoc, ((MainActivity)getActivity()).getUserId());
                    break;
            }

        }
    };

    private boolean sendReport(String description, int classification, Location curLoc, int user_id) {
        float lat = (float) curLoc.getLatitude();
        float lng = (float) curLoc.getLongitude();
        Toast.makeText(getActivity(), "Issue ID is " + classification, Toast.LENGTH_SHORT).show();
        int reportID;

        HttpURLConnection http = null;
        try {
            JSONObject details = new JSONObject();
            JSONObject report = new JSONObject();
            details.put("description", description);
            details.put("issue_id", classification);
            details.put("user_id", user_id);
            details.put("latitude", lat);
            details.put("longitude", lng);
            report.put("report", details);
            String urlParameters = report.toString();
            URL url = new URL("http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/api/reports");
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
                throw new RuntimeException("Failed : HTTP error code : "
                        + http.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            Toast.makeText(getActivity(), "Report created!", Toast.LENGTH_LONG).show();
            ((MainActivity)getActivity()).displayView(new ClassifyFragment());

            //at this point we need to now send and store the picture, but first get the report id of what we just created.
            reportID = getReportId(user_id, classification, description);
            if (reportID != -1) {
                File f = new File("image", reportID + ".png");
                FileOutputStream os = new FileOutputStream(f);
                iv.getDrawingCache().compress(CompressFormat.PNG, 100, os);
                os.close();
                uploadToS3(f);
            }
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

    public void uploadToS3(File f) {
        /* code in this method taken from AWS docs with practically zero alterations */
        String bucketName = "smartcommunity";
        String keyName = f.getName();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(), // Context
                "IDENTITY_POOL_ID", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonS3 s3client = new AmazonS3Client(credentialsProvider);
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            s3client.putObject(new PutObjectRequest(
                    bucketName, keyName, f));
            Log.i("upToS3", "File successfully uploaded");
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    public int getReportId(int user_id, int classification, String description) {
        final String REPORT_URL = "http://smartcommunity-dev2.us-east-1.elasticbeanstalk.com/api/reports";
        try {
            JSONArray jsonArray = JsonReader.readJsonFromUrl(REPORT_URL);
            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jo = jsonArray.getJSONObject(x);
                if (jo.getString("description").equals(description)
                        && jo.getInt("issue_id") == classification
                        && jo.getInt("user_id") == user_id) {
                    return jo.getInt("id");
                }
            }

        } catch (Exception e) {
            Log.i("getReportId", "Stack trace:");
            e.printStackTrace();
        }
        return -1;
    }

    private int parseClassification(String classification){
        int classNumber = 0;
        if(classification.contains("Broken/Missing Water Cover")){classNumber = 1;}
        if(classification.contains("Water Main Break")){classNumber = 2;}
        if(classification.contains("Natural Flooding")){classNumber = 3;}
        if(classification.contains("Freshwater")){classNumber = 4;}
        if(classification.contains("Saltwater Intrusion")){classNumber = 5;}
        if(classification.contains("Disease")){classNumber = 6;}
        if(classification.contains("Standing Water")){classNumber = 7;}
        if(classification.contains("Waste Water/General")){classNumber = 8;}
        if(classification.contains("Spills/Overflows")){classNumber = 9;}
        if(classification.contains("Trim Trees on Bank")){classNumber = 10;}
        if(classification.contains("Blocked Canal")){classNumber = 11;}
        if(classification.contains("Canal Culvert Blocked")){classNumber = 12;}
        if(classification.contains("Canal Bank Needs Mowing")){classNumber = 13;}
        if(classification.contains("Canal Needs Cleaning")){classNumber = 14;}
        if(classification.contains("Traffic Signs")){classNumber = 15;}
        if(classification.contains("Street Sign Name Issue")){classNumber = 16;}
        if(classification.contains("Stop Sign Issue")){classNumber = 17;}
        if(classification.contains("Pothole")){classNumber = 18;}
        if(classification.contains("Damaged Sidewalk")){classNumber = 19;}

        // Quality?
        if(classification.contains("Quality")){classNumber = 29;}

        if(classification.contains("Palm Tree Frond Removal")){classNumber = 23;}
        if(classification.contains("Traffic Lights")){classNumber = 24;}
        if(classification.contains("Streetlights")){classNumber = 25;}
        if(classification.contains("Power Lines Down")){classNumber = 26;}
        if(classification.contains("Leaning Pole")){classNumber = 27;}
        if(classification.contains("Deteriorated Pole")){classNumber = 28;}
        if(classification.contains("Lost Animal")){classNumber = 30;}
        if(classification.contains("On US-1")){classNumber = 31;}
        if(classification.contains("State Road North")){classNumber = 32;}
        if(classification.contains("State Road South")){classNumber = 33;}
        if(classification.contains("On Florida Turnpike")){classNumber = 34;}
        if(classification.contains("Public Waters")){classNumber = 35;}
        if(classification.contains("Large Scale Fish/Duck Kills")){classNumber = 36;}

        if(classification.contains("Animal Bite")){classNumber = 37;}
        if(classification.contains("Crocodile/Alligator")){classNumber = 38;}
        if(classification.contains("Mosquitoes")){classNumber = 39;}
        if(classification.contains("Tegus")){classNumber = 40;}
        return classNumber;
    }

    public void chooseAPicture() {
        // Opens picture gallery for user to select a picture, opens for result
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE);
    }
}