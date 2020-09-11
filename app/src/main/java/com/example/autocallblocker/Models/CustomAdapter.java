package com.example.autocallblocker.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.autocallblocker.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    //
    // This would hold the database objects i.e. Blacklist
    private ArrayList<BlackList> records;
    Context context;

    public CustomAdapter(ArrayList<BlackList> records, Context context) {
        this.records = records;
        this.context = context;

    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null)
            view = inflater.inflate(R.layout.list, viewGroup, false);

        // Fetch phone number from the database object
        ((TextView) view.findViewById(R.id.serial_tv)).setText("" + (i + 1));
        ((TextView) view.findViewById(R.id.phone_number_tv)).setText((records.get(i).getPhoneNumber()));
        return view;
    }

}
