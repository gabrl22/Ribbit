package com.example.gabriel.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Gabriel on 8/26/15.
 */
public class RibbitApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "xASTd4yuL6FSRkmprkGYUg95D6Xgmpim2kvDsyWA"
                , "X4sekSCvA9CKWgjkr1pNO83qxtMGtN9e9WFbZPS5");

    }
}
