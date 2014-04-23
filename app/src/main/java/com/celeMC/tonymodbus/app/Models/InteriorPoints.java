package com.celeMC.tonymodbus.app.Models;

import com.celeMC.tonymodbus.app.Interfaces.PointGroupInterface;

import java.util.ArrayList;

/**
 * Created by Celestin on 10.4.2014..
 */
public class InteriorPoints implements PointGroupInterface {

    ArrayList<ModBusPoint> interiorPointList = new ArrayList<ModBusPoint>();

    int offset; // value used to enter the offset of the address from zero

//Created two methods to add points, one handles a creation a new ModBusPoint while the second method can just add an existing point.

    @Override
    public void addPointToList(String name, int address, int timerAddress, int defaultValue) {

        interiorPointList.add(new ModBusPoint(name, address,timerAddress, defaultValue));

    }

    public void setOffset(int offset){

        this.offset = offset;
    }

    public int getOffset(){

        return this.offset;

    }


    @Override
    public void addPointToList(ModBusPoint pnt) {

        interiorPointList.add(pnt);
    }


    @Override
    public ArrayList<ModBusPoint> getAllPoints() {

        return interiorPointList;
    }


    @Override
    public ModBusPoint getPoint(int index) {

        return interiorPointList.get(index);
    }

    @Override
    public void setPoint(int address, int value) {

        interiorPointList.get(this.indexFromAddress(address)).setValue(value);

    }

    private int indexFromAddress(int index){

        return this.getOffset() + index;
    }
}
