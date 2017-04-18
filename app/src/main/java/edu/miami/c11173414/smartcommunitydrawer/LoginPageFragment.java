package edu.miami.c11173414.smartcommunitydrawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginPageFragment extends Fragment implements View.OnClickListener {
    View fragmentLayout;


    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = inflater.inflate(R.layout.fragment_login_page, container,false);

        Button loginSubmit = (Button)fragmentLayout.findViewById(R.id.login_submit_button);
        if (loginSubmit == null){
            Toast.makeText(getActivity(), "button is null", Toast.LENGTH_SHORT).show();
        }else {
            loginSubmit.setOnClickListener(this);
        }
        return(fragmentLayout);
    }

    private boolean sendLoginRequest(String username, String password){
        try {
            // Figure out what this needs
            String urlParameters = "username=" + username + "&password=" + password;

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            String requestURL = "http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/login";
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
        }catch (Exception e){
            Log.i("sendLoginRequest: ", "Something went wrong");
        }
        return false;
    }

    /**
    private boolean sendLoginRequestApache(String username, String password){
        HttpClient httpClient;
        HttpPost httppost;

        httpClient = new DefaultHttpClient();
        httppost = new HttpPost("http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/users");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    return true;
                } finally {
                    instream.close();
                }
            } else {
                Log.i("sendLoginRequest:", "HTTP Response Null");
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), "Something went wrong with the login request!", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
     **/

    @Override
    public void onClick(View view) {
        EditText usernameField, passwordField;
        usernameField = (EditText)fragmentLayout.findViewById(R.id.username_login_field);
        passwordField = (EditText)fragmentLayout.findViewById(R.id.password_login_field);
        String userName, password;
        userName = usernameField.getText().toString();
        password = passwordField.getText().toString();
        Log.i("onClick:", "U: " + userName + " P: " + password);
        Toast.makeText(getActivity(), "U: " + userName + " P: " + password +"", Toast.LENGTH_SHORT).show();
    }

}
