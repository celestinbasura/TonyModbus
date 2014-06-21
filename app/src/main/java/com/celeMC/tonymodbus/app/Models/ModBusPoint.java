package com.celeMC.tonymodbus.app.Models;

/**
 * Created by celestinbasura on 15/04/14.
 */
public class ModBusPoint {


    String name;

    int statusAdr;

    int controlAdr = 9999;

    int timerAdr = 9999;

    int statusValue = 999;

    int timerValue = 9999;

    int offset = 0;






    public String getName() {
        return name;
    }



    public ModBusPoint(String name, int statusAdr, int offset) {

        this.name = name;
        this.statusAdr = statusAdr;
        this.controlAdr = statusAdr + 1;
        this.timerAdr = statusAdr + 2;
        this.offset = offset;

    }

    public int getTimerAdr(){

        return timerAdr + offset;
    }

    public int getControlAdr(){


            return controlAdr + offset;



    }

    public void setTimerValue(int timerValue) {
        this.timerValue = timerValue;
    }

    public int getTimerValue(){

        return this.timerValue;
    }

    public void setValue( int value){
        this.statusValue = value;

    }
    public int getStatusValue(){

        return this.statusValue;
    }



}
