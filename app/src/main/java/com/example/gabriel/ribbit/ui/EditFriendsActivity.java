package com.example.gabriel.ribbit.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gabriel.ribbit.ParseConstants;
import com.example.gabriel.ribbit.R;
import com.example.gabriel.ribbit.adapters.UserAdapater;
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

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.friends_grid)
    GridView mGridView;
    @Bind(R.id.empty)TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        mProgressBar.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        mGridView.setOnItemClickListener(mOnItemClickListener);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        //Este query busca los usuarios en parse

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                mProgressBar.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.VISIBLE);

                if (e == null) {
                    //Success
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mParseUsers = users;
                    String[] usernames = new String[mParseUsers.size()];
                    int i = 0;
                    for (ParseUser user : mParseUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    if(mGridView.getAdapter() == null) {

                        UserAdapater adapter = new UserAdapater(EditFriendsActivity.this, mParseUsers);
                        mGridView.setAdapter(adapter);
                    }
                    else{
                        //refill the the list
                        ((UserAdapater)mGridView.getAdapter()).refill(mParseUsers);
                    }
                    if(usernames.length == 0){
                        mTextView.setVisibility(View.VISIBLE);
                    }
                    addFriendsCheckmark();
                   /* mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (mListView.isItemChecked(position)) {
                                mFriendsRelation.add(mParseUsers.get(position));
                            } else {
                                mFriendsRelation.remove(mParseUsers.get(position));
                            }


                            mCurrentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    //
                                    if (e != null) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }
                            });

                        }
                    });*/
                } else {
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

    private void addFriendsCheckmark() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    //List returned - look for a match
                    for (int i = 0; i < mParseUsers.size(); i++) {
                        ParseUser user = mParseUsers.get(i);
                        for (ParseUser friend : friends) {
                            if (friend.getObjectId().equals(user.getObjectId())) {
                                mGridView.setItemChecked(i, true);

                            }

                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView checkImageView = (ImageView)view.findViewById(R.id.check_image_view);

            if (mGridView.isItemChecked(position)) {
                mFriendsRelation.add(mParseUsers.get(position));
                checkImageView.setVisibility(View.VISIBLE);
            } else {
                mFriendsRelation.remove(mParseUsers.get(position));
                checkImageView.setVisibility(View.INVISIBLE);
            }


            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    };



}
