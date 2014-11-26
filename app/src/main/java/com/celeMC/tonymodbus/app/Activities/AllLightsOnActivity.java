package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AllLightsOnActivity extends Activity {


    final int registerOffset = 0;
    final int registerOffsetExt = 128;
    Timer tm;
    TimerTask readRegs;
    Handler handler = new Handler();
    volatile ModbusTCPTransaction trans = null; //the transaction
    ReadMultipleRegistersRequest regRequest = null;
    volatile ReadMultipleRegistersResponse regResponse = null;

    ReadMultipleRegistersRequest regRequestExt = null;
    volatile ReadMultipleRegistersResponse regResponseExt = null;
    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    ArrayList<ModBusPoint> pointListHelperExt = new ArrayList<ModBusPoint>();
    ArrayList<ModBusPoint> pointListHelperOn = new ArrayList<ModBusPoint>();
    TextView headline;
    Boolean isConnectedToSlave = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pointlist);

        headline = (TextView) findViewById(R.id.txt_listname);
        headline.setText("Lights On");



        pointListHelper.add(new ModBusPoint("Study", 0, registerOffset)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("Kitchen 1", 4, registerOffset));
        pointListHelper.add(new ModBusPoint("Kitchen 2", 8, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed 1", 12, registerOffset));
        pointListHelper.add(new ModBusPoint("Ensuite", 16, registerOffset));
        pointListHelper.add(new ModBusPoint("WC1", 20, registerOffset));
        pointListHelper.add(new ModBusPoint("WIR", 24, registerOffset));
        pointListHelper.add(new ModBusPoint("Dinning", 28, registerOffset));
        pointListHelper.add(new ModBusPoint("Family 1", 32, registerOffset));
        pointListHelper.add(new ModBusPoint("Family 2", 36, registerOffset));
        pointListHelper.add(new ModBusPoint("Laundry", 40, registerOffset));//
        pointListHelper.add(new ModBusPoint("Passage Rear", 44, registerOffset));
        pointListHelper.add(new ModBusPoint("Bath 2", 48, registerOffset));
        pointListHelper.add(new ModBusPoint("WC 2", 52, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed 2", 56, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed 3", 60, registerOffset));
        pointListHelper.add(new ModBusPoint("Entrance Foyer", 64, registerOffset));
        pointListHelper.add(new ModBusPoint("Landing Top", 68, registerOffset));//
        pointListHelper.add(new ModBusPoint("Landing Stairs", 72, registerOffset));

      /*  pointListHelper.add(new ModBusPoint("Top Landing", 0, registerOffset)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("Foyer", 4, registerOffset));
        pointListHelper.add(new ModBusPoint("Top Landing2", 8, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed", 12, registerOffset));
        pointListHelper.add(new ModBusPoint("WIR", 16, registerOffset));
        pointListHelper.add(new ModBusPoint("Ensuite", 20, registerOffset));
        pointListHelper.add(new ModBusPoint("Kitchen1", 24, registerOffset));
        pointListHelper.add(new ModBusPoint("Kitchen2", 28, registerOffset));
        pointListHelper.add(new ModBusPoint("Dining1", 32, registerOffset));
        pointListHelper.add(new ModBusPoint("Dining2", 36, registerOffset));//
        pointListHelper.add(new ModBusPoint("Family1", 40, registerOffset));
        pointListHelper.add(new ModBusPoint("Family2", 44, registerOffset));
        pointListHelper.add(new ModBusPoint("Balcony1", 48, registerOffset));
        pointListHelper.add(new ModBusPoint("Balcony2", 52, registerOffset));
        pointListHelper.add(new ModBusPoint("Alfresco1", 56, registerOffset));
        pointListHelper.add(new ModBusPoint("Alfresco2", 60, registerOffset));
        pointListHelper.add(new ModBusPoint("Study1", 64, registerOffset));//
        pointListHelper.add(new ModBusPoint("Study2", 68, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed1", 72, registerOffset));
        pointListHelper.add(new ModBusPoint("Bed2", 76, registerOffset));
        pointListHelper.add(new ModBusPoint("Rear Passage", 80, registerOffset));
        pointListHelper.add(new ModBusPoint("WC2", 84, registerOffset));//
        pointListHelper.add(new ModBusPoint("Bath2", 88, registerOffset));
        pointListHelper.add(new ModBusPoint("Laundry", 92, registerOffset));

*/

        pointListHelperExt.add(new ModBusPoint("Alfresco 1", 0, registerOffsetExt)); //first int is the status reg value, second is the timer address
        pointListHelperExt.add(new ModBusPoint("Alfresco 2", 4, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Alfresco Garden 1", 8, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Alfresco Garden 2", 12, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Balcony 1", 16, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Balcony 2", 20, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Garage 1", 24, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Garage 2", 28, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Front Feature Wall Left", 32, registerOffsetExt));//
        pointListHelperExt.add(new ModBusPoint("Front Feature Wall Right", 36, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Portfico Sofit 1", 40, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Portfico Sofit 2", 44, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Portfico Sofit 3", 48, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Front Garden Left", 52, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Front Garden Right", 56, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Rear Outside Left", 60, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Rear Outside Back", 64, registerOffsetExt));
        pointListHelperExt.add(new ModBusPoint("Rear Outside Right", 68, registerOffsetExt));

        userAdapter = new PointCustomAdapter(AllLightsOnActivity.this, R.layout.list_item, pointListHelperOn);
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {

                connectToDevice();

            }
        }).start();


    }


    @Override
    protected void onPause() {
        Log.d("cele", "Pause disconnect.");
        tm.cancel();
        readRegs.cancel();
        closeConnection();
        super.onPause();
    }


    @Override
    protected void onStop() {
        tm.cancel();
        readRegs.cancel();
        closeConnection();
        super.onStop();

    }


    void connectToDevice() {

        try {


            if (!Connection.conn.isConnected()) {
                Log.d("cele", "Connecting...");
                Connection.conn.connect();

            } else {
                Log.d("cele", "Already connected");
            }


            if (Connection.conn.isConnected()) {
                Log.d("cele", "Connected");

                tm = new Timer();
                readRegs = new TimerTask() {
                    @Override
                    public void run() {
                        readSentronRegisters();
                    }
                };

                tm.scheduleAtFixedRate(readRegs, (long) 10, (long) 1000);



                isConnectedToSlave = true;
            }

        } catch (UnknownHostException e) {
            Log.d("cele", "No host");
            Log.d("cele", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("cele", "failed to Connect");
            Log.d("cele", " l" + e.getLocalizedMessage());
        }


    }

    void closeConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (Connection.conn != null && Connection.conn.isConnected()) {
                    tm.cancel();
                    readRegs.cancel();
                    //Connection.conn.close();

                } else {
                    Log.d("cele", "Not connected");
                    return;

                }


            }
        }).start();


    }


    void readSentronRegisters() {


        regRequest = new ReadMultipleRegistersRequest(registerOffset, 100);
        regRequestExt = new ReadMultipleRegistersRequest(registerOffsetExt, 100);

        trans = new ModbusTCPTransaction(Connection.conn);
        trans.setRequest(regRequest);

        try {

            trans.execute();
            Log.d("cele", trans.getTransactionID() + "");


            if (trans.getResponse() instanceof WriteMultipleRegistersResponse) {

                Log.d("cele", " response is write");
            }
            regResponse = (ReadMultipleRegistersResponse) trans.getResponse();
            if (regResponse == null) {
                Log.d("cele", " response is NULL");
            } else {
                Log.d("cele", " response is NOT null");

            }



            trans.setRequest(regRequestExt);
            trans.execute();
            Log.d("cele", trans.getTransactionID() + "");


            if (trans.getResponse() instanceof WriteMultipleRegistersResponse) {

                Log.d("cele", " response is write");
            }
            regResponseExt = (ReadMultipleRegistersResponse) trans.getResponse();
            if (regResponseExt == null) {
                Log.d("cele", " response is NULL");
            } else {
                Log.d("cele", " response is NOT null");

            }
            
            
            
            
            

        } catch (ModbusIOException e) {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Server IO error", Toast.LENGTH_SHORT).show();
                }
            });
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
                       
        } catch (ClassCastException e) {
            trans.setRequest(regRequest);
            e.printStackTrace();
        }


        handler.post(new Runnable() {
            @Override
            public void run() {

                refreshGUI();
            }
        });

    }

    private void refreshGUI() {

        //pointListHelperOn.clear();

        userAdapter.clear();
        Log.d("cele", "refreshed");

        if (regResponse == null) {

            for (int i = 0; i < pointListHelper.size(); i++) {
                pointListHelper.get(i).setTimerValue(9999);
                pointListHelper.get(i).setValue(999);
            }

            for (int i = 0; i < pointListHelperExt.size(); i++) {
                pointListHelperExt.get(i).setTimerValue(9999);
                pointListHelperExt.get(i).setValue(999);
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    userAdapter.notifyDataSetChanged();

                }
            });

            Log.d("cele", "response null");
            return;
        }


        for (int i = 0; i < pointListHelper.size(); i++) {

            pointListHelper.get(i).setTimerValue(regResponse.getRegisterValue((i * 4) + 2));
            pointListHelper.get(i).setValue(regResponse.getRegisterValue(i * 4));


            if(pointListHelper.get(i).getStatusValue() > 0){

               // pointListHelperOn.add(pointListHelper.get(i));
                userAdapter.add(pointListHelper.get(i));
            }

        }

        for (int i = 0; i < pointListHelperExt.size(); i++) {

            pointListHelperExt.get(i).setTimerValue(regResponseExt.getRegisterValue((i * 4) + 2));
            pointListHelperExt.get(i).setValue(regResponseExt.getRegisterValue(i * 4));

            if(pointListHelperExt.get(i).getStatusValue() > 0){

                pointListHelperOn.add(pointListHelperExt.get(i));
            }

        }



        Log.d("cele", "On List is " + pointListHelperOn.size());


        if(pointListHelperOn.size() == 0){

            headline.setText("No lights ON");

        }else{
            headline.setText("Lights ON");


        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                userAdapter.notifyDataSetChanged();

            }
        });

    }

}
