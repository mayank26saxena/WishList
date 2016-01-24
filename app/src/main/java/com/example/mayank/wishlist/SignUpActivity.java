package com.example.mayank.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    Button mRegisterButton;
    EditText mNameView, mPhoneView, mPasswordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        mNameView = (EditText) findViewById(R.id.name_register);
        mPhoneView = (EditText) findViewById(R.id.phone_register);
        mPasswordView = (EditText) findViewById(R.id.password_register);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameView.getText().toString();
                String phoneNumber = mPhoneView.getText().toString();
                if(phoneNumber.length() > 10)
                    mPhoneView.setError("Phone Number should be 10 digits");
                String password = mPasswordView.getText().toString();
                if(password.length() < 4)
                    mPasswordView.setError("Password should be more than 4 characters");
                ParseUser user = new ParseUser();
                user.setUsername(phoneNumber);
                user.setPassword(mPasswordView.getText().toString());
                user.put("full_name", name);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Snackbar.make(mPasswordView, "Registration Successful", Snackbar.LENGTH_LONG).show();
                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else {
                            Snackbar.make(mPasswordView, "Registration Failed", Snackbar.LENGTH_LONG).show();
                            Log.v(this.getClass().getSimpleName(), e.getMessage());
                        }
                    }
                });
            }
        });
    }
}
