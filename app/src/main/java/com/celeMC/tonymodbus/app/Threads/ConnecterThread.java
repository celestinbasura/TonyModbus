package com.celeMC.tonymodbus.app.Threads;

import android.content.Context;
import android.util.Log;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Celestin on 9.4.2014..
 */
public class ConnecterThread implements  Runnable{

    Context ctx;
    Socket socket;
    String hostIP = "192.168.197.1";
    int modbusPort = 5020;
    TCPMasterConnection con = null;
    ModbusTCPTransaction trans = null;
    ReadInputDiscretesRequest disRequest = null;
    ReadInputDiscretesResponse disResponse = null;
    ReadInputRegistersResponse regResponse = null;
    ReadInputRegistersRequest regRequest = null;

    int startReference = 0;
    int count = 5;
    int repeat = 100;

   public ConnecterThread(Context ctx, String IP, int port){

    this.ctx = ctx;
    this.hostIP = IP;
    this.modbusPort = port;

}


    @Override
    public void run() {
        System.out.println("initiated run");
        InetAddress modbusServerIP = null;
        try {
            modbusServerIP = InetAddress.getByName(hostIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        con = new TCPMasterConnection(modbusServerIP);

        con.setPort(modbusPort);
        con.setTimeout(03720);

        Log.d("cele", "starting conn");


        try {
            Log.d("cele", "trying");
            con.connect();
            Log.d("cele", "tried");

        } catch (Exception e1) {
            e1.printStackTrace();
        }



        if (con.isConnected()) {

            Log.d("cele", "commected");

        } else {

            Log.d("cele", "not connected");
        }



        Log.d("cele", "closed");





    };
    }






