package com.example.gabriel.ribbit.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gabriel.ribbit.R;
import com.example.gabriel.ribbit.RibbitApplication;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.user_name_field)EditText mUserName;
    @Bind(R.id.password_field)EditText mPassword;
    @Bind(R.id.sign_label)TextView mToSignUp;
    @Bind(R.id.progressBar)ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();*/

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        /*mToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/
    }
    public void logIn(View view){

        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        if (userName.isEmpty() || password.isEmpty() ){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.login_up_error_message);
            builder.setTitle(R.string.login_error_title);
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else{
            //Login

            mProgressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(userName, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    //setProgressBarIndeterminateVisibility(false);
                    mProgressBar.setVisibility(View.INVISIBLE);

                    if (e == null) {
                        //Succesfull
                        RibbitApplication.updateParseInstalation(parseUser);
                        Intent intent = new  Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(e.getMessage());
                        builder.setTitle(R.string.login_error_title);
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
            });

        }
    }

    public void signUp(View view){
        Intent intent = new  Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

}
