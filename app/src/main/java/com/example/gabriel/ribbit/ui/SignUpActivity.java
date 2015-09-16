package com.example.gabriel.ribbit.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.gabriel.ribbit.R;
import com.example.gabriel.ribbit.RibbitApplication;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.user_name_field)EditText mUserName;
    @Bind(R.id.password_field)EditText mPassword;
    @Bind(R.id.email_field)EditText mEmail;
    @Bind(R.id.progressBar)ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();*/

        mProgressBar.setVisibility(View.INVISIBLE);

    }

    public void signUp(View view){

        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String email = mEmail.getText().toString().trim();

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.sign_up_error_message);
            builder.setTitle(R.string.sign_error_title);
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else{
            //create the new user
            mProgressBar.setVisibility(View.VISIBLE);
            final ParseUser newUser = new ParseUser();
            newUser.setUsername(userName);
            newUser.setPassword(password);
            newUser.setEmail(email);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        //Succesfull
                        RibbitApplication.updateParseInstalation(ParseUser.getCurrentUser());

                        mProgressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new  Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(e.getMessage());
                        builder.setTitle(R.string.sign_error_title);
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }
    public void cancel (View view){
        finish();
    }


}
