package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Constants;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class InteriorPointActivity extends Activity {


    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    TextView headline;
    int registerOffset = 0;
    Boolean isWriting = false;
    Boolean isConnectedToSlave = false;
    SharedPreferences sharedPreferences;
    Timer tm;
    TimerTask readRegs;
    Handler handler = new Handler();
    volatile ModbusTCPTransaction trans = null; //the transaction
    ReadMultipleRegistersRequest regRequest = null;
    volatile ReadMultipleRegistersResponse regResponse = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pointlist);

        headline = (TextView) findViewById(R.id.txt_listname);
        headline.setText("Interior");
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.PREF, 0); // 0 - for private mode

        pointListHelper.add(new ModBusPoint("Top Landing", 0, registerOffset)); //first int is the status reg value, second is the timer address
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


        userAdapter = new PointCustomAdapter(InteriorPointActivity.this, R.layout.list_item, pointListHelper);
        userList = (ListView) findViewById(R.id.listView);
        userList.setItemsCanFocus(false);
        userList.setAdapter(userAdapter);


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
        closeConnection();
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("cele", "Stop disconnect.");
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

                //tm.purge();

                if (Connection.conn != null && Connection.conn.isConnected()) {
                    tm.cancel();
                    readRegs.cancel();
                    //Connection.conn.close();
                    //Log.d("cele", "Connection closed to " + Connection.conn.getAddress());

                } else {
                    Log.d("cele", "Not connected");
                    return;

                }


            }
        }).start();


    }


    void readSentronRegisters() {

        if (isWriting || userAdapter.isWriting) {
            return;
        }

        regRequest = new ReadMultipleRegistersRequest(registerOffset, 100);

        trans = new ModbusTCPTransaction(Connection.conn);
        trans.setRequest(regRequest);

        try {

            trans.execute();
            Log.d("cele", trans.getTransactionID() + "");

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
        }

        try {
            if (trans.getResponse() instanceof WriteMultipleRegistersResponse) {

                Log.d("cele", " response is write");
            }
            regResponse = (ReadMultipleRegistersResponse) trans.getResponse();
            if (regResponse == null) {
                Log.d("cele", " response is NULL");
            } else {
                Log.d("cele", " response is NOT null");

            }

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

        Log.d("cele", "External refreshed");

        if (regResponse == null) {


                for (int i = 0; i < pointListHelper.size(); i++) {
                    pointListHelper.get(i).setTimerValue(9999);
                    pointListHelper.get(i).setValue(999);
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

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    userAdapter.notifyDataSetChanged();

                }
            });

        }


    }






