package com.celeMC.tonymodbus.app.ModBusConnect;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
   public ConnecterThread(){
        super();
    }

    @Override
    public void run() {
        System.out.println("inititared run");
        InetAddress modbusServerIP = null;
        try {
            modbusServerIP = InetAddress.getByName(hostIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        con = new TCPMasterConnection(modbusServerIP);
        con.setPort(modbusPort);
        Log.d("cele", "starting conn");
        try {
            con.connect();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (con.isConnected()) {
            Looper.prepare();
            Toast.makeText(ctx, "Connected to " + hostIP, Toast.LENGTH_LONG).show();
            Looper.loop();
            Log.d("cele", "commected");
        } else {
            Toast.makeText(ctx, "Failed to connect to " + hostIP, Toast.LENGTH_LONG).show();
        }

        // socket = new Socket(modbusServerIP, modbusPort);






    };
    }




              /*  System.out.println("inititared run");
                InetAddress modbusServerIP = null;
                try {
                    modbusServerIP = InetAddress.getByName(hostIP);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                con = new TCPMasterConnection(modbusServerIP);
                con.setPort(modbusPort);
                Log.d("cele", "starting conn");
                try {
                    con.connect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (con.isConnected()) {

                    Toast.makeText(ctx, "Connected to " + hostIP, Toast.LENGTH_LONG).show();

                    Log.d("cele", "commected");
                } else {
                    Toast.makeText(ctx, "Failed to connect to " + hostIP, Toast.LENGTH_LONG).show();
                }

                // socket = new Socket(modbusServerIP, modbusPort);






        };*/





