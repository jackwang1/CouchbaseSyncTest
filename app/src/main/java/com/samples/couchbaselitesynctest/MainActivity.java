package com.samples.couchbaselitesynctest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Document note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CouchbaseLiteUtil.initialize(this);

        InputStream is;

        try {
            is = getResources().getAssets().open("couchbase_logo.png");
            note = Note.createNote(CouchbaseLiteUtil.getDatabaseInstance(), "Foo Bar");
            Note.addAttachment(note, "image/png", is);
        } catch (IOException e) {
            Log.e("Foo", "Error reading image", e);
        } catch (CouchbaseLiteException e) {
            Log.e("Foo", "Error accessing CBL", e);
        }
    }

    public void onButtonClick(View view) {
        try {
            // Exception here
            Note.toggleBookmark(note);
        } catch (CouchbaseLiteException e) {
            Log.e("Foo", "Error accessing CBL", e);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
