package com.example.android.moviesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "Movies";
    private JSONObject mainObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Support the fragment to the main activity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesFragment())
                    .commit();
        }
        ActionBar g = getSupportActionBar();
        g.setDisplayShowHomeEnabled(true);
        g.setIcon(R.drawable.camara);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);
        this.invalidateOptionsMenu();


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingactivity = new Intent(this, SettingsActivity.class);
                startActivity(settingactivity);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
