package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final int ACTIVITY_MAIN_APP = 1;
    private static final int ACTIVITY_CREATE_ACCOUNT = 2;
    private EditText userField, passField;
    private final String LOGIN_LINK = "http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/api/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userField = (EditText)findViewById(R.id.username_login_field);
        passField = (EditText)findViewById(R.id.password_login_field);
        //TODO this better. As of now it is only workaround for NetworkOnMainThread Exception
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void loginClickHandler(View view){
        Intent nextActivity;
        switch (view.getId()){
            case R.id.login_submit_button:
                String username, password, fullname;
                username = userField.getText().toString(); // Username string
                password = passField.getText().toString(); // Password string
                // TODO: CONFIRM LEGIT LOGIN CREDENTIALS

                if (sendLoginSessionPost(username, password)) {
                    //we are logged in.
                } else {
                    Toast.makeText(this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
                    // break so we dont start activity and they can try again.
                    // Leave break commented until andrew fixes http 500 error
                    // break;
                }

                // TODO: Get e-mail or username string (i.e. username blakem2018 -> "Blake Maune")
                fullname = username; // + " Smith";

                nextActivity = new Intent();
                nextActivity.setClassName(getPackageName(), getPackageName() + ".MainActivity");
                nextActivity.putExtra(getPackageName()+".username", username);
                nextActivity.putExtra(getPackageName()+".fullname", fullname);
                startActivityForResult(nextActivity, ACTIVITY_MAIN_APP);
                break;
            case R.id.create_account_link:
                nextActivity = new Intent();
                nextActivity.setClassName(getPackageName(), getPackageName() + ".CreateAccountActivity");
                startActivityForResult(nextActivity, ACTIVITY_CREATE_ACCOUNT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case ACTIVITY_MAIN_APP:
                userField.setText("");
                passField.setText("");
                break;
            case ACTIVITY_CREATE_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    // TODO: Fix not auto-entering username!
                    String username = data.getStringExtra(getPackageName() + ".username");
                    // Username returning null
                    Log.i("onActRes: ", "returned username " + username);
                    userField.setText(username);
                } else {
                    Toast.makeText(this, "Account creation failed.", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    private boolean sendLoginSessionPost(String username, String password) {
        HttpURLConnection conn = null;
        try {
            JSONObject session = new JSONObject();
            JSONObject details = new JSONObject();
            details.put("username", username);
            details.put("password", password);
            session.put("sessions", details);
            String urlParameters = session.toString();
            String requestURL = LOGIN_LINK;
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setUseCaches(false);

            try (OutputStream wr = conn.getOutputStream()) {
                wr.write(urlParameters.getBytes());
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            Log.i("response", "output done.");
            return true;
        } catch (Exception e) {
            Log.i("loginSesh: ", "Something went wrong");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }
}
