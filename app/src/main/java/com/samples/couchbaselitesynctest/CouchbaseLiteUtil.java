package com.samples.couchbaselitesynctest;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CouchbaseLiteUtil {

    private final static String TAG = CouchbaseLiteUtil.class.getSimpleName();

    private final static String CBL_HOST = "http://10.0.0.72";
    private final static String CBL_PORT = "4984";
    private final static String DB_NAME = "foo_bar";
    private final static String SYNC_URL = CBL_HOST + ":" + CBL_PORT + "/" + DB_NAME;

    private static Manager manager;
    private static Database database;
    private static Replication pull;
    private static Replication push;

    private static URL syncUrl;

    public static void initialize(Context c) {
        try {
            syncUrl = new URL(SYNC_URL);
            getManagerInstance(c);
            getDatabaseInstance();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error creating URL", e);
        } catch (IOException e) {
            Log.e(TAG, "Error creating manager", e);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error getting database", e);
        }

        try {
            initalizeReplications();
        } catch (CouchbaseLiteException e) {
            Log.d(TAG, "Error starting replication", e);
        }
    }

    public static Manager getManagerInstance(Context context) throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    public static Database getDatabaseInstance() throws CouchbaseLiteException {
        if (manager != null && database == null) {
            database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    private static void initalizeReplications() throws CouchbaseLiteException {
        pull = getDatabaseInstance().createPullReplication(syncUrl);
        push = getDatabaseInstance().createPushReplication(syncUrl);
        pull.setContinuous(true);
        push.setContinuous(true);
        pull.start();
        push.start();
    }
}
