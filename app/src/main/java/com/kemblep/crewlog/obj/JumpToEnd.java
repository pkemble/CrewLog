package com.kemblep.crewlog.obj;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Pete on 6/1/2015.
 */
public class JumpToEnd implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        EditText et = (EditText) v;
        Integer i = et.getText().length();
        if(et.hasFocus() && i > 0) {
            et.clearFocus();
            et.setSelection(i);
            et.requestFocus();
        }
    }
}
