package com.kemblep.crewlog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;


/**
 * Created by Pete on 6/4/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(preferences.getString(getResources().getString(R.string.pref_pilot_name), null) == null){
            Toast.makeText(getActivity().getApplicationContext(), "no name", Toast.LENGTH_LONG).show();

        }

    }
}
