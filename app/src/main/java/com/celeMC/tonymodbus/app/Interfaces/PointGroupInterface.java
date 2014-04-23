package com.celeMC.tonymodbus.app.Interfaces;

import com.celeMC.tonymodbus.app.Models.ModBusPoint;

import java.util.ArrayList;

/**
 * Created by celestinbasura on 15/04/14.
 */
public interface PointGroupInterface {

    //  String listName = "";
    // int startaddress = 0;
    // int addressLength = 0;


    public ArrayList<ModBusPoint> pointList = new ArrayList<ModBusPoint>();//Point holder for the groupList


    public void addPointToList(String name, int address, int timerValue, int value);

    public void addPointToList(ModBusPoint pnt);

    public ArrayList<ModBusPoint> getAllPoints();

    public ModBusPoint getPoint(int index);

    public void setPoint(int address, int value);


}
