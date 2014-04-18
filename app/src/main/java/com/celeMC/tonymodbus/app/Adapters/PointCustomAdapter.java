package com.celeMC.tonymodbus.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;

import java.util.ArrayList;

import static android.widget.Toast.*;

/**
 * Created by celestinbasura on 18/04/14.
 */
public class PointCustomAdapter extends ArrayAdapter<ModBusPoint> {
    Context context;
    int layoutResourceId;
    ArrayList<ModBusPoint> data = new ArrayList<ModBusPoint>();

    public PointCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<ModBusPoint> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.txt_pointname);
            holder.textAddress = (TextView) row.findViewById(R.id.txt_pointaddress);
            holder.textTimer = (TextView) row.findViewById(R.id.txt_timervalue);
            holder.btnActivate = (Button) row.findViewById(R.id.btn_activate);
            holder.btnEditTimer = (Button) row.findViewById(R.id.btn_timer);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        ModBusPoint point = data.get(position);
        holder.textName.setText(point.getName());
        holder.textAddress.setText(point.getAddress() + "");
        holder.textTimer.setText(point.getTimerValue() + "");


        holder.btnActivate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Edit Button Clicked", "**********");
                makeText(context, "Edit button Clicked",
                        LENGTH_LONG).show();
            }
        });
        holder.btnEditTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");
                makeText(context, "Delete button Clicked",
                        LENGTH_LONG).show();
            }
        });
        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textAddress;
        TextView textTimer;
        Button btnActivate;
        Button btnEditTimer;
    }
}


