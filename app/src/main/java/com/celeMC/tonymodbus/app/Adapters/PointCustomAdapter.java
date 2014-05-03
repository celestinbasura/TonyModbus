package com.celeMC.tonymodbus.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        UserHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);


            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.txt_pointname);
            holder.textAddress = (TextView) row.findViewById(R.id.txt_pointaddress);
            holder.textTimer = (TextView) row.findViewById(R.id.txt_timervalue);
            holder.btnEditTimer = (ImageButton) row.findViewById(R.id.btn_timer);
            holder.imgStatus = (ImageButton) row.findViewById(R.id.img_state);
            row.setTag(holder);

           // if(position % 2 == 1){
            //    row.setBackgroundColor(Color.parseColor("#ff367f52"));//TODO FIX COLORS (Random)
           // }

        } else {
            holder = (UserHolder) row.getTag();
        }

        ModBusPoint point = data.get(position);
        holder.textName.setText(point.getName());
        holder.textAddress.setText(point.getIntPointValue() + "");
        holder.textTimer.setText(point.getTimerValue() + "");



        if(point.getIntPointValue() == 0){

            holder.imgStatus.setBackgroundResource(R.drawable.light_bulb_off);
        }else
            if(point.getIntPointValue() == 999){

                holder.imgStatus.setBackgroundResource(R.drawable.light_bulb_error);

            }else{

            holder.imgStatus.setBackgroundResource(R.drawable.light_bulb);
        }


        holder.imgStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Log.i("Edit Button Clicked", "**********");
                makeText(context, "Toggle button tapped on: " + (position + 1),
                        LENGTH_SHORT).show();
            }
        });
        holder.btnEditTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");
                makeText(context, "Timer button Clicked on: " + (position + 1),
                        LENGTH_SHORT).show();
            }
        });
        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textAddress;
        TextView textTimer;
        ImageButton btnEditTimer;
        ImageButton imgStatus;
    }
}


