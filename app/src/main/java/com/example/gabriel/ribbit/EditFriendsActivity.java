package com.example.gabriel.ribbit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class EditFriendsActivity extends AppCompatActivity {

    protected static final String TAG = EditFriendsActivity.class.getSimpleName();
    protected List<ParseUser> mParseUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    @Bind(R.id.progressBar)ProgressBar mProgressBar;
    @Bind(R.id.listview)ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.bind(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);






    }
    @Override
    protected void onResume()
    {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                mProgressBar.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);

                if (e == null){
                    //Success
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mParseUsers = users;
                    String[] usernames = new String[mParseUsers.size()];
                    int i = 0;
                    for(ParseUser user: mParseUsers){
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames
                    );
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            mFriendsRelation.add(mParseUsers.get(position));
                            mCurrentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    //
                                    if(e != null){
                                        Log.e(TAG, e.getMessage());
                                    }
                                }
                            });

                        }
                    });
                }
                else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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
