package com.celeMC.tonymodbus.app.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Activities.ConnectionManager;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteSingleRegisterRequest;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;

import java.util.ArrayList;

import static android.widget.Toast.*;

/**
 * Created by celestinbasura on 18/04/14.
 */
public class PointCustomAdapter extends ArrayAdapter<ModBusPoint> {
    Context context;
    int layoutResourceId;
    Handler handler = new Handler();
    TCPMasterConnection listConnection;
    ModbusTCPTransaction trans = null;
    ArrayList<ModBusPoint> data = new ArrayList<ModBusPoint>();

    public PointCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<ModBusPoint> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        listConnection = ConnectionManager.getInstance(context.getApplicationContext()).getTCPconnection();
        trans = new ModbusTCPTransaction(listConnection);
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

            holder.textTimer = (TextView) row.findViewById(R.id.txt_timervalue);
            holder.btnEditTimer = (ImageButton) row.findViewById(R.id.btn_timer);
            holder.imgStatus = (ImageButton) row.findViewById(R.id.img_state);
            row.setTag(holder);



        } else {
            holder = (UserHolder) row.getTag();
        }

        final ModBusPoint point = data.get(position);
        holder.textName.setText(point.getName());
       // holder.textAddress.setText(point.getStatusValue() + "");
        holder.textTimer.setText(point.getTimerValue() + "");



        if(point.getStatusValue() == 0){

            holder.imgStatus.setBackgroundResource(R.drawable.light_bulb_off);
        }else
            if(point.getStatusValue() == 999){

                holder.imgStatus.setBackgroundResource(R.drawable.light_bulb_error);

            }else{

            holder.imgStatus.setBackgroundResource(R.drawable.light_bulb);
        }



        if(point.getTimerValue() == 0){

            holder.btnEditTimer.setBackgroundResource(R.drawable.timer_icon_off);
        }else
        if(point.getTimerValue() == 9999){

            holder.btnEditTimer.setBackgroundResource(R.drawable.timer_icon_error);

        }else{

            holder.btnEditTimer.setBackgroundResource(R.drawable.timer_icon);
        }


        holder.imgStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                writeToServer(1, point.getControlAdr());


            }
        });
        holder.btnEditTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Set Timer Value");
                alert.setMessage("Enter the timer value");

// Set an EditText view to get user input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int temp = 0;
                        String value = input.getText().toString();


                        try{

                            temp = Integer.valueOf(value);
                        }catch (NumberFormatException e){
                            Toast.makeText(context, " Not a number", LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                        writeToServer(temp, point.getTimerAdr());
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();


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
        //TextView textAddress;
        TextView textTimer;
        ImageButton btnEditTimer;
        ImageButton imgStatus;
    }

    private void writeToServer(final int value, final int address){






        new Thread(new Runnable() {
            @Override
            public void run() {




                SimpleRegister sr = new SimpleRegister();
                sr = new SimpleRegister(value);
                Log.d("cele", "reg created");

                WriteSingleRegisterRequest singleRequest = new WriteSingleRegisterRequest(address, sr);


                //WriteMultipleRegistersRequest writeRequest = new WriteMultipleRegistersRequest(sr);
                Log.d("cele", "request set");
                if (!(listConnection != null && listConnection.isConnected())) {
                    return;


                }
                trans.setRequest(singleRequest);
                try {

                    trans.execute();
                    trans.getResponse();
                    Log.d("cele", "executed to " + address  + " with " + value);
                } catch (ModbusException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {

                   // e.printStackTrace();
                }

            }


        }
        ).start();




    }

}




