package com.kemblep.crewlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kemblep.crewlog.database.DbUtilities;

public class CrewLog extends ActionBarActivity {

    private final String TAG = CrewLog.class.getName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_log);

        mContext = getApplicationContext();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_main, new LogbookFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(preferences.getString("pref_pilot_name", null) == null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, new SettingsFragment())
                    .addToBackStack("Flight Entry")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crewlog_main, menu);
        if(BuildConfig.DEBUG){
            menu.setGroupVisible(R.id.debug_menu, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DbUtilities dbUtilities = new DbUtilities(mContext);

        switch (id){
            case R.id.action_settings:
                getFragmentManager().beginTransaction()
                        .replace(R.id.fl_main, new SettingsFragment())
                        .addToBackStack("Flight Entry")
                        .commit();
                break;
            case R.id.db_delete:
                dbUtilities.dropTables();
                break;
            case R.id.db_export:
                dbUtilities.backupDatabase();
                break;
            case R.id.db_populate:
                dbUtilities.populateLogbook();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
