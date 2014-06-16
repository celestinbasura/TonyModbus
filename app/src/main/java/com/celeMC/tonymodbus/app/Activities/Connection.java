package com.celeMC.tonymodbus.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.celeMC.tonymodbus.app.Constants;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by celestinbasura on 15/06/14.
 */
public class Connection {


    Boolean isConnectedToSlave = false;

    Context ctx;
    TCPMasterConnection conn;
    Timer tm;
    TimerTask readRegs;
    SharedPreferences sharedPreferences;


    private boolean isHome() {


        return true;
    }

    Connection(Context ctx){


        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences(Constants.PREF, 0); // 0 - for private mode

    }

    void  connectToDevice() {

        final String currentIP;
        final int currentPort;

        if (isHome()) {
            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);
            currentPort = sharedPreferences.getInt(Settings.internalPort, Constants.DEFAULT_PORT);

        } else {
            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);
            currentPort = sharedPreferences.getInt(Settings.externalPort, Constants.EXT_DEFAULT_PORT);


        }


        new Thread(new Runnable() {
            @Override
            public void run() {


                Log.d("cele", "Connecting to " + currentIP);
                try {





                        InetAddress address = InetAddress.getByName(currentIP);


                        conn = new TCPMasterConnection(address);


                    conn.setPort(currentPort);
                    // conn.setTimeout(1000);

                    if (!conn.isConnected()) {
                        Log.d("cele", "Connecting...");
                        conn.connect();

                    } else {
                        Log.d("cele", "Already connected");
                    }


                    if (conn.isConnected()) {
                        Log.d("cele", "Connected");

                        tm = new Timer();
                        readRegs = new TimerTask() {
                            @Override
                            public void run() {
                                // readSentronRegisters();
                            }
                        };

                        tm.scheduleAtFixedRate(readRegs, (long) 100, (long) 500);


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
        }).start();


    }

    void closeConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //tm.purge();

                if (conn != null && conn.isConnected()) {
                    tm.cancel();
                    readRegs.cancel();
                    conn.close();
                    Log.d("cele", "Connection closed to " + conn.getAddress());

                } else {
                    Log.d("cele", "Not connected");
                    return;

                }


            }
        }).start();


    }


    public  boolean isConnectionEstablished() {

        if(conn != null){

            return conn.isConnected();
        }
        return false;

    }

    public synchronized TCPMasterConnection getConnection() {

        return conn;

    }

    public String getIP(){

        if (isHome()) {
            return sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);


        } else {
            return sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);



        }

    }

    public int getPort(){


        if (isHome()) {

            return sharedPreferences.getInt(Settings.internalPort, Constants.DEFAULT_PORT);

        } else {

            return sharedPreferences.getInt(Settings.externalPort, Constants.EXT_DEFAULT_PORT);


        }

    }
}
