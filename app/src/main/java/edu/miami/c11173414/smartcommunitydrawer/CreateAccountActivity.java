package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void createAccountClickHandler(View view){
        switch (view.getId()){
            case R.id.create_account_button:
                String firstname, lastname, username, email, password, passwordConfirm;
                firstname = ((EditText)(findViewById(R.id.create_firstname))).getText().toString();
                Log.i("Create account: ", "firstname = " + firstname);
                lastname = ((EditText)(findViewById(R.id.create_lastname))).getText().toString();
                Log.i("Create account: ", "lastname = " + lastname);
                username = ((EditText)(findViewById(R.id.create_username))).getText().toString();
                Log.i("Create account: ", "username = " + username);
                email = ((EditText)(findViewById(R.id.create_email))).getText().toString();
                Log.i("Create account: ", "email = " + email);
                password = ((EditText)(findViewById(R.id.create_password))).getText().toString();
                Log.i("Create account:", "pass = " + password);
                passwordConfirm = ((EditText)(findViewById(R.id.create_password_confirm))).getText().toString();
                Log.i("Create account:", "passconf = " + passwordConfirm);
                createAccount(firstname, lastname, username, email, password, passwordConfirm);
                break;
            default:
                break;
        }
    }

    public boolean createAccount(String firstname, String lastname, String username, String email, String password, String passConfirm) {


        // TODO: create the account programmatically
        boolean createSuccess = (password.equals(passConfirm));

        if (createSuccess) {
            Intent returnIntent = new Intent();
            // TODO: find out why returnIntent is sending null user name
            returnIntent.putExtra(getPackageName(), getPackageName() + ".username");
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

}
