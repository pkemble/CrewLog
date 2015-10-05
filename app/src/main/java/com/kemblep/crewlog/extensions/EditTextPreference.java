package com.kemblep.crewlog.extensions;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Pete on 10/5/2015.
 */
public class EditTextPreference extends android.preference.EditTextPreference { //TODO: get this to be implemented
    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary(){
        return this.getText();
    }
}
