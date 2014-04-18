package com.celeMC.tonymodbus.app.Models;

/**
 * Created by celestinbasura on 15/04/14.
 */
public class ModBusPoint {


    String name;

    public int getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    int address;
    int value;
    boolean interpretedVaule; //Value represented in boolean from the recieved int value (value == 0 > false, else > true


//a constructor for a point, adds all the required info. Default value is used to add more control if the default value changes from 0
// in any later modifications of the program

    public ModBusPoint(String name, int address, int defaultValue) {

        this.name = name;
        this.address = address;
        this.value = defaultValue;

    }

    public int getIntPointValue() {

        return this.value;
    }

    public boolean getBooleanPointValue() {

        if (this.value == 0) {

            return false;

        } else {
            return true;
        }
    }

    public void setValue(int value){

        this.value = value;
    }
    public int getTimerValue(){

        return 0;
    }
}
