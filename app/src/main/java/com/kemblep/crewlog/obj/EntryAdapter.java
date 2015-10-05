package com.kemblep.crewlog.obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kemblep.crewlog.R;

import java.util.ArrayList;

/**
 * Created by Pete on 6/5/2015.
 */
public class EntryAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<LogbookEntry> mLogEntries;

    public EntryAdapter(Context context, ArrayList<LogbookEntry> entries){
        mContext = context;
        mLogEntries = entries;
    }

    @Override
    public int getCount() {
        return mLogEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mLogEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mLogEntries.get(position).Id; //TODO check this is ok
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_log_entries, null);
        }

        TextView tvId = (TextView) convertView.findViewById(R.id.multi_select_id);
        TextView tvDate = (TextView) convertView.findViewById(R.id.multi_select_date);
        TextView tvTail = (TextView) convertView.findViewById(R.id.multi_select_tail_number);
        TextView tvCrew = (TextView) convertView.findViewById(R.id.multi_select_crew);

        tvId.setText(mLogEntries.get(position).Id.toString());
        tvDate.setText(mLogEntries.get(position).EntryDate);
        tvTail.setText(mLogEntries.get(position).TailNumber);
        tvCrew.setText(mLogEntries.get(position).CrewMember);

        return convertView;
    }
}
