package com.doomonafireball.kingofstuff.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.doomonafireball.kingofstuff.android.dagger.AppModule;
import com.doomonafireball.kingofstuff.android.dagger.IObjectGraph;

import javax.inject.Inject;

import dagger.ObjectGraph;
import oak.util.PRNGFixes;

public class MainApp extends Application {

    public static String TAG = "kingofstuff-android";
    private static ObjectGraph applicationGraph;
    private static Context sContext;

    @Inject
    Datastore mDataStore;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        /**
         * Ensure the keys generated for the CryptoSharedPreferences are strong and sufficiently
         * random.
         *
         * See http://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
         */
        PRNGFixes.apply();

        sContext = this;
        applicationGraph = ObjectGraph.create(getAppModule());
        applicationGraph.inject(this);
        try {
            int newVersionCode = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionCode;
            int oldVersionCode = mDataStore.getVersion();
            if (oldVersionCode != 0 && oldVersionCode != newVersionCode) {
                onVersionUpdate(oldVersionCode, newVersionCode);
            }
            mDataStore.persistVersion(newVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onVersionUpdate(int oldVersionCode, int newVersionCode) {
        //this method is called when the version code changes, use comparison operators to control migration
    }

    public static Context getContext() {
        return sContext;
    }

    protected Object getAppModule() {
        return new AppModule(this);
    }

    public static ObjectGraph getObjectGraph() {
        return applicationGraph;
    }
}

