package com.celeMC.tonymodbus.app.Activities;

import android.content.Context;

import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

/**
 * Created by celestinbasura on 15/06/14.
 */
public class ConnectionManager {

    Context ctx;
   static  Connection tcpConnection;
    private  static ConnectionManager instance;


    public static ConnectionManager getInstance(Context ctx){
        if(instance == null) {
            instance = new ConnectionManager(ctx);

        }
        return instance;


    }

   private ConnectionManager(Context ctx){

        this.ctx = ctx;
        tcpConnection = new Connection(this.ctx);

    }

    void connectToServer(){

        tcpConnection.connectToDevice();

    }

    void disconnectFromServer(){

        if(tcpConnection.isConnectedToSlave){
            tcpConnection.closeConnection();
        }



    }

    public boolean isConnected(){


        return tcpConnection.isConnectionEstablished();
    }


    public synchronized TCPMasterConnection getTCPconnection(){

        return tcpConnection.getConnection();
    }





}
