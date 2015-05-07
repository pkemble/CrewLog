package com.kemble.crewlog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kemble.crewlog.R;
import com.kemble.crewlog.obj.Leg;


/**
 * Created by Pete on 5/5/2015.
 */
public class LogEntryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View vEntry = inflater.inflate(R.layout.fragment_log_entry, container, false);
        return vEntry;
    }

    protected void populateEntry(Leg leg){

    }

}
