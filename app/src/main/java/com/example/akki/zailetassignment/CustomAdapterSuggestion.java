package com.example.akki.zailetassignment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akki on 15-05-2017.
 */

public class CustomAdapterSuggestion extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> heading;
    private final ArrayList<String> name;

    public CustomAdapterSuggestion(Activity context, ArrayList<String> heading, ArrayList<String> name) {
        super(context, R.layout.suggestion_layout, heading);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.heading=heading;
        this.name=name;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.suggestion_layout, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.heading);
        TextView des = (TextView) rowView.findViewById(R.id.heading2);

        String data=heading.get(position);
        String data1=name.get(position);

        title.setText(data);                  //setting data
        des.setText(data1);
        return rowView;
    };

}