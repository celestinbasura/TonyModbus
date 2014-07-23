package com.celeMC.tonymodbus.app.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.celeMC.tonymodbus.app.Activities.Connection;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by celestinbasura on 23/07/14.
 */
public class PointAdapterAlarmGarden extends ArrayAdapter<ModBusPoint> {
    Context context;
    int layoutResourceId;
    ModbusTCPTransaction trans = null;
    ArrayList<ModBusPoint> data = new ArrayList<ModBusPoint>();
    private Vibrator myVib;
    public boolean isWriting = false;

    public PointAdapterAlarmGarden(Context context, int layoutResourceId,
                              ArrayList<ModBusPoint> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        myVib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View row = convertView;
        UserHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);


            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.txt_pointname);

            holder.textTimer = (TextView) row.findViewById(R.id.txt_timervalue);
            holder.btnEditTimer = (ImageButton) row.findViewById(R.id.btn_timer);
            holder.imgStatus = (ToggleButton) row.findViewById(R.id.txt_state);
            row.setTag(holder);



        } else {
            holder = (UserHolder) row.getTag();
        }

        final ModBusPoint point = data.get(position);
        holder.textName.setText(point.getName());
        holder.textTimer.setText(point.getTimerValue() + "");



        if(point.getStatusValue() == 0){

            holder.imgStatus.setText("OFF");
            holder.imgStatus.setChecked(false);
           // holder.imgStatus.

            holder.btnEditTimer.setVisibility(View.INVISIBLE);
        }else
        if(point.getStatusValue() == 999){

            holder.imgStatus.setText("ERR");
            holder.imgStatus.setChecked(false);
            holder.btnEditTimer.setVisibility(View.VISIBLE);


        }else{

            holder.btnEditTimer.setVisibility(View.VISIBLE);
            holder.imgStatus.setText("ON");
            holder.imgStatus.setChecked(true);
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


                myVib.vibrate(50);
                writeToServer(1, point.getControlAdr());



            }
        });


        holder.btnEditTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Set Timer Value");
                alert.setMessage("Enter the timer value in seconds");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);
                final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

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

                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                        writeToServer(temp, point.getTimerAdr());

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                    }
                });


                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                alert.show();



            }
        });
        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textTimer;
        ImageButton btnEditTimer;
        ToggleButton imgStatus;
    }

    private void writeToServer(final int value, final int address){

        trans = new ModbusTCPTransaction(Connection.conn);
        new Thread(new Runnable() {
            @Override
            public void run() {

                isWriting = true;

                SimpleRegister sr = new SimpleRegister();
                sr = new SimpleRegister(value);

                WriteSingleRegisterRequest singleRequest = new WriteSingleRegisterRequest(address, sr);
                Log.d("cele", "request set");

                trans.setRequest(singleRequest);
                try {

                    trans.execute();
                    trans.getResponse();
                    trans = null;
                    Log.d("cele", "executed to " + address  + " with " + value);
                } catch (ModbusException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }

                isWriting = false;
            }



        }
        ).start();




    }

}




