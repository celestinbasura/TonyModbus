package com.celeMC.tonymodbus.app.Threads;

import android.content.Context;
import android.util.Log;

/*import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;*/

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.io.IOException;
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
    ReadMultipleRegistersRequest  regRequest= null;
   static  ReadMultipleRegistersResponse regResponse = null;
    ReadInputDiscretesResponse disResponse = null;
   // ReadInputRegistersResponse regResponse = null;
  //  ReadInputRegistersRequest regRequest = null;

    int startReference = 0;
    int count = 1;
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


        Log.d("cele", "starting conn");


        try {
            Log.d("cele", "trying");
            con.connect();
            Log.d("cele", "tried");

            regRequest = new ReadMultipleRegistersRequest(0,120);
            trans = new ModbusTCPTransaction(con);

            trans.setConnection(con);


            trans.setRequest(regRequest);
            Log.d("cele", "pre-executing");
            trans.execute();
            Log.d("cele", "executing");
            regResponse = (ReadMultipleRegistersResponse) trans.getResponse();


            for (int i = 0; i < regResponse.getWordCount();i++){

                Log.d("cele", regResponse.getRegisterValue(i) + "");
            }
           Log.d("cele", "cele" + regResponse.getRegisters().toString());



        } catch (Exception e1) {
            e1.printStackTrace();
        }



        if (con.isConnected()) {

            Log.d("cele", "commected");
            con.close();

        } else {

            Log.d("cele", "not connected");
        }


        con.close();

        Log.d("cele", "closed");





    }

    public static ReadMultipleRegistersResponse getRegResponse(){

        if(regResponse != null){
            return regResponse;
        }
        return null;
    }

    }






