package edu.miami.c11173414.smartcommunitydrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

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

    @Override
    public void onClick(View view) {
        EditText usernameField, passwordField;
        usernameField = (EditText)fragmentLayout.findViewById(R.id.username_login_field);
        passwordField = (EditText)fragmentLayout.findViewById(R.id.password_login_field);
        String userName, password;
        userName = usernameField.getText().toString();
        password = passwordField.getText().toString();
        Toast.makeText(getActivity(), "U: " + userName + " P: " + password +"", Toast.LENGTH_SHORT).show();
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/users/new");

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("param-1", "12345"));
        params.add(new BasicNameValuePair("param-2", "Hello!"));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (Exception e) {

        }

//Execute and get the response.
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }
        } catch (Exception e) {

        }
    }

}
