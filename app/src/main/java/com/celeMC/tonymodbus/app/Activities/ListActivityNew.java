package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;
import com.celeMC.tonymodbus.app.Threads.ConnecterThread;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;

import java.util.ArrayList;


/**
 * Created by Celestin on 11.02.14..
 */
public class ListActivityNew extends Activity {

    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    ReadMultipleRegistersResponse rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pointlist);

        /**
         * add item in arraylist
         */
        pointListHelper.add(new ModBusPoint("p1", 0, 999));
        pointListHelper.add(new ModBusPoint("p2", 1, 999));
        pointListHelper.add(new ModBusPoint("p3", 2, 999));
        pointListHelper.add(new ModBusPoint("p4", 3, 999));
        pointListHelper.add(new ModBusPoint("p5", 4, 999));
        pointListHelper.add(new ModBusPoint("p6", 5, 999));
        pointListHelper.add(new ModBusPoint("p7", 6, 999));

        rep = ConnecterThread.getRegResponse();
        if(rep == null){
            Toast.makeText(this.getBaseContext(), "No response recieved", Toast.LENGTH_SHORT).show();
        }else {
            pointListHelper.get(0).setValue(rep.getRegisterValue(0));
            pointListHelper.get(1).setValue(rep.getRegisterValue(1));
            pointListHelper.get(2).setValue(rep.getRegisterValue(2));
            pointListHelper.get(3).setValue(rep.getRegisterValue(3));
            pointListHelper.get(4).setValue(rep.getRegisterValue(4));
            pointListHelper.get(5).setValue(rep.getRegisterValue(5));
            pointListHelper.get(6).setValue(rep.getRegisterValue(6));
        }
        /**
         * set item into adapter
         */
        userAdapter = new PointCustomAdapter(ListActivityNew.this, R.layout.list_item, pointListHelper);
        userList = (ListView) findViewById(R.id.listView);
        userList.setItemsCanFocus(false);
        userList.setAdapter(userAdapter);
        /**
         * get on item click listener
         */
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
                Toast.makeText(ListActivityNew.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

}

