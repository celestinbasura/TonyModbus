package com.celeMC.tonymodbus.app.Activities;

import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;


/**
 * Created by celestinbasura on 15/06/14.
 */
public class Connection {

    public static TCPMasterConnection conn;

    public static synchronized void createConn(InetAddress masterAddress, int port) {

        if (conn == null) {
            conn = new TCPMasterConnection(masterAddress);
            conn.setPort(port);

        }
    }
}







