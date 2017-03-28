package com.example.aharon.testapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 28/03/2017.
 */

public class ExpenceArrayAdapter extends ArrayAdapter<Expence> {

    Activity activity;
    List<Expence> expences;

    public ExpenceArrayAdapter(Activity activity, List<Expence> expences) {
        super(activity, R.layout.expence_row, expences);
        this.activity = activity;
        this.expences = expences;
    }

    static class ViewContainer{
        TextView txtName;
        TextView txtDesc;
        TextView txtPrice;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewContainer viewContainer = null;
        if (rowView == null){
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.expence_row,null);
            viewContainer = new ViewContainer();
            viewContainer.txtName = (TextView)rowView.findViewById(R.id.txtName);
            viewContainer.txtDesc = (TextView)rowView.findViewById(R.id.txtDesc);
            viewContainer.txtPrice = (TextView)rowView.findViewById(R.id.txtPrice);
            rowView.setTag(viewContainer);
        }else {
            viewContainer = (ViewContainer) rowView.getTag();
        }

        viewContainer.txtName.setText(expences.get(position).getName());
        viewContainer.txtDesc.setText(expences.get(position).getDesc());
        viewContainer.txtPrice.setText(String.valueOf(expences.get(position).getPrice()));

        return rowView;
    }
}
