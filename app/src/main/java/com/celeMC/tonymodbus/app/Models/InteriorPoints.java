package com.celeMC.tonymodbus.app.Models;

import java.util.ArrayList;

/**
 * Created by Celestin on 10.4.2014..
 */
public class InteriorPoints implements PointGroupInterface {

    ArrayList<ModBusPoint> interiorPointList = new ArrayList<ModBusPoint>();


//Created two methods to add points, one handles a creation a new ModBusPoint while the second method can just add an existing point.

    @Override
    public void addPointToList(String name, int address, int defaultValue) {

        interiorPointList.add(new ModBusPoint(name, address, defaultValue));

    }

    @Override
    public void addPointToList(ModBusPoint pnt) {

        interiorPointList.add(pnt);
    }



    @Override
    public ArrayList<ModBusPoint> getAllPoints() {

        return null;
    }

    @Override
    public ModBusPoint getPoint() {
        return null;
    }

    @Override
    public void setPoint(int address, int value) {

    }
}
