package edu.miami.c11173414.smartcommunitydrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    }

}
