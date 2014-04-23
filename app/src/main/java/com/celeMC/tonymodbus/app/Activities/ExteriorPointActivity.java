package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Models.InteriorPoints;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;
import com.celeMC.tonymodbus.app.Threads.ConnecterThread;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;

import java.util.ArrayList;


public class ExteriorPointActivity extends Activity {

    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    ReadMultipleRegistersResponse rep;
    TextView headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pointlist);

        headline = (TextView) findViewById(R.id.txt_listname);
        headline.setText("Exterior");


        pointListHelper.add(new ModBusPoint("Garage1", 0, 2, 999)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("Garage2", 4, 6, 999));
        pointListHelper.add(new ModBusPoint("Portfico Sofit1", 8, 10, 999));
        pointListHelper.add(new ModBusPoint("Portfico Sofit2", 12, 14, 999));
        pointListHelper.add(new ModBusPoint("Portfico Sofit3", 16, 18, 999));
        pointListHelper.add(new ModBusPoint("Front Feature Wall Left", 20, 22, 999));
        pointListHelper.add(new ModBusPoint("Front Feature Wall Right", 24, 26, 999));
        pointListHelper.add(new ModBusPoint("Front Garden Left", 28, 30, 999));
        pointListHelper.add(new ModBusPoint("Front Garden Right", 32, 34, 999));
        pointListHelper.add(new ModBusPoint("Rear Garden 1", 36, 38, 999));//
        pointListHelper.add(new ModBusPoint("Rear Garden 2", 40, 42, 999));
        pointListHelper.add(new ModBusPoint("Rear Outside Left", 44, 46, 999));
        pointListHelper.add(new ModBusPoint("Rear Outside Back", 48, 50, 999));
        pointListHelper.add(new ModBusPoint("Rear Outside Right", 52, 54, 999));


        rep = ConnecterThread.getRegResponse();

        if(rep == null){
            Toast.makeText(this.getBaseContext(), "No response recieved", Toast.LENGTH_SHORT).show();
        }else {

            pointListHelper.get(0).setValue(rep.getRegisterValue(0));
            pointListHelper.get(0).setTimerValue(rep.getRegisterValue(2));

            pointListHelper.get(1).setValue(rep.getRegisterValue(4));
            pointListHelper.get(1).setTimerValue(rep.getRegisterValue(6));




        }

        userAdapter = new PointCustomAdapter(ExteriorPointActivity.this, R.layout.list_item, pointListHelper);
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
                Toast.makeText(ExteriorPointActivity.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }




}
