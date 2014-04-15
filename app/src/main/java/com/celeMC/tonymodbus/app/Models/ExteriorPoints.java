package com.celeMC.tonymodbus.app.Models;

import com.celeMC.tonymodbus.app.Interfaces.PointGroupInterface;

import java.util.ArrayList;

/**
 * Created by celestinbasura on 15/04/14.
 */
public class ExteriorPoints implements PointGroupInterface {


    @Override
    public void addPointToList(String name, int address, int value) {

    }

    @Override
    public void addPointToList(ModBusPoint pnt) {

    }

    @Override
    public ArrayList<ModBusPoint> getAllPoints() {
        return null;
    }

    @Override
    public ModBusPoint getPoint(int index) {
        return null;
    }


    public ModBusPoint getPoint() {
        return null;
    }

    @Override
    public void setPoint(int address, int value) {

    }
}
