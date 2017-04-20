package edu.miami.c11173414.smartcommunitydrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

public class CreateAccountActivity extends Activity {

    private final String USERS_LINK = "http://smartcommunity-dev.us-east-1.elasticbeanstalk.com/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //TODO this better. As of now it is only workaround for NetworkOnMainThread Exception
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void createAccountClickHandler(View view) {
        switch (view.getId()) {
            case R.id.create_account_button:
                String firstname, lastname, username, email, password, passwordConfirm;
                firstname = ((EditText) (findViewById(R.id.create_firstname))).getText().toString();
                Log.i("Create account: ", "firstname = " + firstname);
                lastname = ((EditText) (findViewById(R.id.create_lastname))).getText().toString();
                Log.i("Create account: ", "lastname = " + lastname);
                username = ((EditText) (findViewById(R.id.create_username))).getText().toString();
                Log.i("Create account: ", "username = " + username);
                email = ((EditText) (findViewById(R.id.create_email))).getText().toString();
                Log.i("Create account: ", "email = " + email);
                password = ((EditText) (findViewById(R.id.create_password))).getText().toString();
                Log.i("Create account:", "pass = " + password);
                passwordConfirm = ((EditText) (findViewById(R.id.create_password_confirm))).getText().toString();
                Log.i("Create account:", "passconf = " + passwordConfirm);
                createAccount(firstname, lastname, username, email, password, passwordConfirm);
                break;
            default:
                break;
        }
    }

    public boolean createAccount(String firstname, String lastname, String username, String email, String password, String passConfirm) {


        boolean equalPasswordCheck = (password.equals(passConfirm));

        if (equalPasswordCheck) {
            if (createAccountByPost(username, password, email)) {
                Intent returnIntent = new Intent();
                // TODO: find out why returnIntent is sending null user name
                // You were placing getPackageName() + ".username" as the string with key getPackageName(). lol
                returnIntent.putExtra(getPackageName() + ".username", username);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private boolean createAccountByPost(String username, String password, String email) {
        HttpURLConnection conn = null;
        try {
            // Figure out what this needs
            JSONObject user = new JSONObject();
            JSONObject details = new JSONObject();
            details.put("username", username);
            details.put("email", email);
            details.put("password", password);
            details.put("password_confirmation", password); //no need to confirm here, checked prior to this method
            user.put("user", details);
            String urlParameters = user.toString();
            String requestURL = USERS_LINK;
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
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
            Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.i("sendCreateAcc: ", "Something went wrong"); //saying something is wrong isnt
            e.printStackTrace();                                 // helpful if you don't print stack trace doofus ;)
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

}
