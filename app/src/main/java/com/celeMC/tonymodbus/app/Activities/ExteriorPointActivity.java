package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.ModbusSlaveException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ExteriorPointActivity extends Activity {


    Timer tm;
    TimerTask readRegs;
    Handler handler = new Handler();
    volatile ModbusTCPTransaction trans = null; //the transaction
    ReadMultipleRegistersRequest regRequest = null;
    volatile ReadMultipleRegistersResponse regResponse = null;
    TCPMasterConnection connectionExterior;
    ListView userList;
    boolean isWriting;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    TextView headline;
    final int registerOffset = 96;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pointlist);
        connectionExterior = ConnectionManager.getInstance(getApplicationContext()).getTCPconnection();

        headline = (TextView) findViewById(R.id.txt_listname);
        headline.setText("Exterior");



        pointListHelper.add(new ModBusPoint("Garage1", 0, registerOffset)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("Garage2", 4, registerOffset));
        pointListHelper.add(new ModBusPoint("Portfico Sofit1", 8, registerOffset));
        pointListHelper.add(new ModBusPoint("Portfico Sofit2", 12, registerOffset));
        pointListHelper.add(new ModBusPoint("Portfico Sofit3", 16, registerOffset));
        pointListHelper.add(new ModBusPoint("Front Feature Wall Left", 20, registerOffset));
        pointListHelper.add(new ModBusPoint("Front Feature Wall Right", 24, registerOffset));
        pointListHelper.add(new ModBusPoint("Front Garden Left", 28, registerOffset));
        pointListHelper.add(new ModBusPoint("Front Garden Right", 32, registerOffset));
        pointListHelper.add(new ModBusPoint("Rear Garden 1", 36, registerOffset));//
        pointListHelper.add(new ModBusPoint("Rear Garden 2", 40, registerOffset));
        pointListHelper.add(new ModBusPoint("Rear Outside Left", 44, registerOffset));
        pointListHelper.add(new ModBusPoint("Rear Outside Back", 48, registerOffset));
        pointListHelper.add(new ModBusPoint("Rear Outside Right", 52, registerOffset));


        userAdapter = new PointCustomAdapter(ExteriorPointActivity.this, R.layout.list_item, pointListHelper);
        userList = (ListView) findViewById(R.id.listView);
        userList.setItemsCanFocus(false);
        userList.setAdapter(userAdapter);

        /**
         * get on item click listener
         */


        tm = new Timer();
        readRegs = new TimerTask() {
            @Override
            public void run() {
                readSentronRegisters();
            }
        };

        //tm.scheduleAtFixedRate(readRegs, (long) 100, (long) 2000);


        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.d("List View Clicked", "**********");
                Toast.makeText(getApplicationContext(),
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });



    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cele", "Onresume");

        tm = new Timer();
        readRegs = new TimerTask() {
            @Override
            public void run() {
                readSentronRegisters();
            }
        };

        tm.scheduleAtFixedRate(readRegs, (long) 100, (long) 1000);

    }


    @Override
    protected void onPause() {
        Log.d("cele", "Pause disconnect.");
       // closeConnection();
        tm.cancel();
        Log.d("cele", "Onrpause");
        readRegs.cancel();
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("cele", "Stop disconnect.");
      //  closeConnection();
        tm.cancel();
        readRegs.cancel();
        super.onStop();

    }



    void readSentronRegisters() {



        regRequest = new ReadMultipleRegistersRequest(registerOffset, 75);

        trans = new ModbusTCPTransaction(connectionExterior);
        trans.setRequest(regRequest);

        try {

            trans.execute();

        } catch (ModbusIOException e) {
            Log.d("cele", "IO error");

            e.printStackTrace();
        } catch (ModbusSlaveException e) {

            Log.d("cele", "Slave returned exception");
            e.printStackTrace();
        } catch (ModbusException e) {
            Log.d("cele", "Failed to execute request");

            e.printStackTrace();
        } catch (NullPointerException e) {

            e.printStackTrace();
        }

        try {
            if (trans.getResponse() instanceof WriteMultipleRegistersResponse) {

                Log.d("cele", " response is write");
            }
            regResponse = (ReadMultipleRegistersResponse) trans.getResponse();

        } catch (ClassCastException e) {
            trans.setRequest(regRequest);
            e.printStackTrace();
        }


        handler.post(new Runnable() {
            @Override
            public void run() {

               refreshGUI();
                userAdapter.notifyDataSetChanged();
            }
        });
        //userAdapter.notifyDataSetChanged();
    }

    private void refreshGUI(){

        Log.d("cele", "External refreshed");

        if(regResponse == null){

            Log.d("cele", "response null");
            return;
        }
        for(int i = 0; i < pointListHelper.size(); i++){
            pointListHelper.get(i).setTimerValue(regResponse.getRegisterValue((i * 4) + 2));
            pointListHelper.get(i).setValue(regResponse.getRegisterValue(i * 4));

        }

    }

}
