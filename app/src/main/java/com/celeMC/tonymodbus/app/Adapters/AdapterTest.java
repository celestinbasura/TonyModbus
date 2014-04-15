package com.celeMC.tonymodbus.app.Adapters;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;


public class AdapterTest extends ArrayAdapter<ModBusPoint> {
    Context context;
    int layoutResourceId;
    ArrayList<ModBusPoint> data = new ArrayList<ModBusPoint>();

    public AdapterTest(Context context, int layoutResourceId,
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
            holder.textName = (TextView) row.findViewById(R.id.txtpointName);
            holder.textAddress = (TextView) row.findViewById(R.id.txtPointAddress);
            holder.textLocation = (TextView) row.findViewById(R.id.txtCurrentState);
            holder.btnEdit = (Button) row.findViewById(R.id.btnTurnOn);
            holder.btnDelete = (Button) row.findViewById(R.id.btnTurnOff);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        ModBusPoint point = data.get(position);
        holder.textName.setText(point.getName());
        holder.textAddress.setText(point.getAddress());
        holder.textLocation.setText(point.getIntPointValue());
        holder.btnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Edit Button Clicked", "**********");
                Toast.makeText(context, "Edit button Clicked",
                        Toast.LENGTH_LONG).show();
            }
        });
        holder.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");
                Toast.makeText(context, "Delete button Clicked",
                        Toast.LENGTH_LONG).show();
            }
        });
        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textAddress;
        TextView textLocation;
        Button btnEdit;
        Button btnDelete;
    }
}
