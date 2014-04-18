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

import java.util.ArrayList;


/**
 * Created by Celestin on 11.02.14..
 */
public class ListActivityNew extends Activity {

    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pointlist);

        /**
         * add item in arraylist
         */
        pointListHelper.add(new ModBusPoint("p1", 0, 54));
        pointListHelper.add(new ModBusPoint("p2", 1, 654));
        pointListHelper.add(new ModBusPoint("p3", 2, 87));
        pointListHelper.add(new ModBusPoint("p4", 3, 98));
        pointListHelper.add(new ModBusPoint("p5", 4, 7));
        pointListHelper.add(new ModBusPoint("p6", 5, 9));
        pointListHelper.add(new ModBusPoint("p7", 6, 9));

        pointListHelper.get(0).setValue(ConnecterThread.getRegResponse().getRegisterValue(0));
        pointListHelper.get(1).setValue(ConnecterThread.getRegResponse().getRegisterValue(1));
        pointListHelper.get(2).setValue(ConnecterThread.getRegResponse().getRegisterValue(2));
        pointListHelper.get(3).setValue(ConnecterThread.getRegResponse().getRegisterValue(3));
        pointListHelper.get(4).setValue(ConnecterThread.getRegResponse().getRegisterValue(4));
        pointListHelper.get(5).setValue(ConnecterThread.getRegResponse().getRegisterValue(5));
        pointListHelper.get(6).setValue(ConnecterThread.getRegResponse().getRegisterValue(6));
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

