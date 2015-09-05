package com.example.gabriel.ribbit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gabriel.ribbit.adapters.MiFragmentPagerAdapter;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int TAKE_PICTURE_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PICTURE_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int FILE_SIZE_LIMIT = 1024*2014*10;//10mb

    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0://Take picture
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            if (mMediaUri == null) {
                                Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
                            }
                            break;
                        case 1://Take Video
                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                            if (mMediaUri == null) {
                                Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                //Lowest quality

                                startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                            }
                            break;
                        case 2://Choose Picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, CHOOSE_PICTURE_REQUEST);
                            break;
                        case 3://Choose video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");
                            Toast.makeText(MainActivity.this, R.string.video_file_size_warning,
                                    Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);
                            break;

                    }
                }

                private Uri getOutputMediaFileUri(int mediaType) {
                    // To be safe, you should check that the SDCard is mounted
                    // using Environment.getExternalStorageState() before doing this.
                    if (isExternalStorageAvailable()) {
                        //get the uri

                        //1.get the external storage directory
                        String appName = MainActivity.this.getString(R.string.app_name);
                        File mediaStorageDir = new File(
                                Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), appName);
                        //2.create subdirectory
                        if (!mediaStorageDir.exists()) {

                            if (!mediaStorageDir.mkdirs()) {
                                Log.e(TAG, "Failed to create directory");
                                return null;
                            }
                        }
                        //3.create filename
                        //4.create the file

                        File mediaFile;
                        Date now = new Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.US).format(now);

                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == MEDIA_TYPE_IMAGE) {
                            mediaFile = new File(path + "IMG" + timeStamp + ".jpg");
                        } else if (mediaType == MEDIA_TYPE_VIDEO) {
                            mediaFile = new File(path + "VID" + timeStamp + ".mp4");
                        } else {
                            return null;
                        }
                        Log.d(TAG, "File" + Uri.fromFile(mediaFile));
                        //5.return the file
                        return Uri.fromFile(mediaFile);
                    } else {
                        return null;
                    }

                }

                private boolean isExternalStorageAvailable() {
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                    }
                    return false;
                }
            };


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiFragmentPagerAdapter(MainActivity.this,
                getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);


        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            startLogInActivity();
        } else {
            Log.i(TAG, currentUser.toString());
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Succesfull add it to the gallery

            if (requestCode == CHOOSE_PICTURE_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
                }
                else {
                    mMediaUri = data.getData();
                }
                if(requestCode == CHOOSE_VIDEO_REQUEST){
                    int filesize = 0;
                    InputStream inputStream = null;
                    try{
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        filesize = inputStream.available();
                    }
                    catch (FileNotFoundException e){
                        Toast.makeText(this, R.string.file_not_found_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e){
                        Toast.makeText(this, R.string.file_not_found_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally{
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            //this is intentionally blank
                        }
                    }
                    if (filesize >= FILE_SIZE_LIMIT){
                        Toast.makeText(this, R.string.error_file_size_to_large, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);

                sendBroadcast(mediaScanIntent);
            }
            Intent recipientsIntent = new Intent(this, RecipientActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if (requestCode == TAKE_PICTURE_REQUEST || requestCode == CHOOSE_PICTURE_REQUEST){
                fileType = ParseConstants.FILE_TYPE_IMAGE;
            }
            else{
                fileType = ParseConstants.FILE_TYPE_VIDEO;
            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE,fileType);
            startActivity(recipientsIntent);
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    private void startLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Nueva tarea
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Cierra la tarea anterior
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                ParseUser.logOut();

                startLogInActivity();
                break;
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_take_a_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();

        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}
