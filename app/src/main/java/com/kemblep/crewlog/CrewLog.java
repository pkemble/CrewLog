package com.kemblep.crewlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kemblep.crewlog.database.DbUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CrewLog extends ActionBarActivity {

    private Date mDate;
    private SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd/yyyy");
    private final String TAG = CrewLog.class.getName();
    private Context mContext;
    public DbUtilities DbUtilities;

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

//    private void setupDebug(){
//        final DbUtilities dbUtilities = new DbUtilities(mContext);
//
//        Button btnDelete = (Button) findViewById(R.id.btn_delete_logbook);
//        btnDelete.setVisibility(View.VISIBLE);
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbUtilities.dropTables();
//            }
//        });
//
//        Button btnExport = (Button) findViewById(R.id.btn_export_logbook);
//        btnExport.setVisibility(View.VISIBLE);
//        btnExport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbUtilities.backupDatabase();
//            }
//        });
//
//        Button btnPopulate = (Button) findViewById(R.id.btn_populate_logbook);
//        btnPopulate.setVisibility(View.VISIBLE);
//        btnPopulate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbUtilities.populateLogbook();
//            }
//        });
//    }
}
