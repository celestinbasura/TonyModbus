package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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


public class GroupPointActivity extends Activity {

    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();
    ReadMultipleRegistersResponse rep;
    TextView headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pointlist);

        headline = (TextView) findViewById(R.id.txt_listname);
        headline.setText("Group");


        pointListHelper.add(new ModBusPoint("All OFF", 0, 2, 999)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("All ON", 4, 6, 999));
        pointListHelper.add(new ModBusPoint("Inside Living", 8, 10, 999));
        pointListHelper.add(new ModBusPoint("Front Outside Living", 12, 14, 999));
        pointListHelper.add(new ModBusPoint("Rear Outside Living", 16, 18, 999));


        rep = ConnecterThread.getRegResponse();

        if(rep == null){
            Toast.makeText(this.getBaseContext(), "No response recieved", Toast.LENGTH_SHORT).show();
        }else {

            pointListHelper.get(0).setValue(rep.getRegisterValue(0));
            pointListHelper.get(0).setTimerValue(rep.getRegisterValue(2));

            pointListHelper.get(1).setValue(rep.getRegisterValue(4));
            pointListHelper.get(1).setTimerValue(rep.getRegisterValue(6));




        }

        userAdapter = new PointCustomAdapter(GroupPointActivity.this, R.layout.list_item, pointListHelper);
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
                Toast.makeText(GroupPointActivity.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }




}
