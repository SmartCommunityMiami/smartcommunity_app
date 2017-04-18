package edu.miami.c11173414.smartcommunitydrawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final int ACTIVITY_MAIN_APP = 1;
    private static final int ACTIVITY_CREATE_ACCOUNT = 2;
    private EditText userField, passField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userField = (EditText)findViewById(R.id.username_login_field);
        passField = (EditText)findViewById(R.id.password_login_field);
    }

    public void loginClickHandler(View view){
        Intent nextActivity;
        switch (view.getId()){
            case R.id.login_submit_button:
                String username, password, fullname;
                username = userField.getText().toString(); // Username string
                password = passField.getText().toString(); // Password string
                // TODO: CONFIRM LEGIT LOGIN CREDENTIALS

                // TODO: Get e-mail or username string (i.e. username blakem2018 -> "Blake Maune")
                fullname = username + " Smith";

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
                    Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show();
                    // TODO: Fix not auto-entering username!
                    String username = data.getStringExtra(getPackageName() + ".username");
                    // Username returning null
                    Log.i("onActRes: ", "returned username " + username);
                    userField.setText(username);
                }
            default:
                break;
        }
    }
}
