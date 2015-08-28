package com.example.gabriel.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    TextView mSignUpTextView;
    @Bind(R.id.user_name_field)EditText mUserName;
    @Bind(R.id.password_field)EditText mPassword;
    @Bind(R.id.sign_label)TextView mToSignUp;
    @Bind(R.id.progressBar)ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
