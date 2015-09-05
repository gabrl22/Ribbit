package com.example.gabriel.ribbit;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.ribbit.helper_methods.FileHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipientActivity extends AppCompatActivity {

    public static final String TAG = RecipientActivity.class.getSimpleName();

    protected List<ParseUser> mParseUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected MenuItem mSendMenuItem;
    protected Uri mMediaUri;
    protected String mFileType;

    @Bind(R.id.listview)
    ListView mListView;
    @Bind(R.id.empty)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);
        ButterKnife.bind(this);

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mListView.getCheckedItemCount() > 0) {
                    mSendMenuItem.setVisible(true);
                } else {
                    mSendMenuItem.setVisible(false);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        mTextView.setVisibility(View.INVISIBLE);
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    mParseUsers = friends;
                    String[] usernames = new String[mParseUsers.size()];
                    int i = 0;
                    for (ParseUser user : mParseUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            RecipientActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames
                    );
                    if (usernames.length == 0) {
                        mTextView.setVisibility(View.VISIBLE);
                    }
                    mListView.setAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_recipient, menu);
        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {

            ParseObject message = createMessage();
            if (message == null) {
                //error
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.error_selecting_file));
                builder.setTitle(getString(R.string.sorry));
                builder.setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                send(message);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
        if (fileBytes == null) {
            return null;
        } else {
            if (mFileType.equals(ParseConstants.FILE_TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, file);
            return message;
        }


    }

    protected ArrayList<String> getRecipientIds() {
        ArrayList<String> recipentIds = new ArrayList<String>();
        for (int i = 0; i < mListView.getCount(); i++) {
            if (mListView.isItemChecked(i)) {
                recipentIds.add(mParseUsers.get(i).getObjectId());
            }
        }
        return recipentIds;
    }

    protected void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    //Succesfull
                    Toast.makeText(RecipientActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientActivity.this);
                    builder.setMessage(getString(R.string.error_sending_message));
                    builder.setTitle(getString(R.string.sorry));
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
