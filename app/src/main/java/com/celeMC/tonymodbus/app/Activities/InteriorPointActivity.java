package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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


public class InteriorPointActivity extends Activity {

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
        headline.setText("Interior");


        pointListHelper.add(new ModBusPoint("Top Landing", 0, 2, 999)); //first int is the status reg value, second is the timer address
        pointListHelper.add(new ModBusPoint("Foyer", 4, 6, 999));
        pointListHelper.add(new ModBusPoint("Top Landing2", 8, 10, 999));
        pointListHelper.add(new ModBusPoint("Bed", 12, 14, 999));
        pointListHelper.add(new ModBusPoint("WIR", 16, 18, 999));
        pointListHelper.add(new ModBusPoint("Ensuite", 20, 22, 999));
        pointListHelper.add(new ModBusPoint("Kitchen1", 24, 26, 999));
        pointListHelper.add(new ModBusPoint("Kitchen2", 28, 30, 999));
        pointListHelper.add(new ModBusPoint("Dining1", 32, 34, 999));
        pointListHelper.add(new ModBusPoint("Dining2", 36, 38, 999));//
        pointListHelper.add(new ModBusPoint("Family1", 40, 42, 999));
        pointListHelper.add(new ModBusPoint("Family2", 44, 46, 999));
        pointListHelper.add(new ModBusPoint("Balcony1", 48, 50, 999));
        pointListHelper.add(new ModBusPoint("Balcony2", 52, 54, 999));
        pointListHelper.add(new ModBusPoint("Alfresco1", 56, 58, 999));
        pointListHelper.add(new ModBusPoint("Alfresco2", 60, 62, 999));
        pointListHelper.add(new ModBusPoint("Study1", 64, 66, 999));//
        pointListHelper.add(new ModBusPoint("Study2", 68, 70, 999));
        pointListHelper.add(new ModBusPoint("Bed1", 72, 74, 999));
        pointListHelper.add(new ModBusPoint("Bed2", 76, 78, 999));
        pointListHelper.add(new ModBusPoint("Rear Passage", 80, 82, 999));
        pointListHelper.add(new ModBusPoint("WC2", 84, 86, 999));//
        pointListHelper.add(new ModBusPoint("Bath2", 88, 90, 999));
        pointListHelper.add(new ModBusPoint("Laundry", 92, 94, 999));
        pointListHelper.add(new ModBusPoint("WC2", 84, 86, 999));



        rep = ConnecterThread.getRegResponse();

        if(rep == null){
            Toast.makeText(this.getBaseContext(), "No response recieved", Toast.LENGTH_SHORT).show();
        }else {

            pointListHelper.get(0).setValue(rep.getRegisterValue(0));
            pointListHelper.get(0).setTimerValue(rep.getRegisterValue(2));

            pointListHelper.get(1).setValue(rep.getRegisterValue(4));
            pointListHelper.get(1).setTimerValue(rep.getRegisterValue(6));




        }

        userAdapter = new PointCustomAdapter(InteriorPointActivity.this, R.layout.list_item, pointListHelper);
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
                Toast.makeText(InteriorPointActivity.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }




}
