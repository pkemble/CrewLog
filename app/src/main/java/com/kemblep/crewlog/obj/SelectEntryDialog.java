package com.kemblep.crewlog.obj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.kemblep.crewlog.R;

import java.util.ArrayList;

/**
 * Created by Pete on 6/4/2015.
 */
public class SelectEntryDialog extends AlertDialog {

    public LogbookEntry SelectedEntry;
    private ArrayList<LogbookEntry> mLogEntries;
    private Context mContext;
    public SelectEntryDialog(ArrayList<LogbookEntry> logEntires, Context context){
        super(context);
        mContext = context;
        mLogEntries = logEntires;

    }
    public SelectEntryDialog init(){

        final AlertDialog.Builder bSingle = new AlertDialog.Builder(mContext);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.select_dialog_singlechoice);

        for (LogbookEntry e : mLogEntries) {
            String lineItem = e.EntryDate;
            if (e.TailNumber != null) {
                lineItem += " : " + e.TailNumber;
            }
            arrayAdapter.add(lineItem);
        }
        bSingle.setTitle(R.string.select_multiple_title);
        bSingle.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SelectedEntry = mLogEntries.get(which);
                dialog.dismiss();
            }
        });

        bSingle.create();
        return this;
    }
}
