package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Adapters.PointCustomAdapter;
import com.celeMC.tonymodbus.app.Models.ModBusPoint;
import com.celeMC.tonymodbus.app.R;

import java.util.ArrayList;

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
        pointListHelper.add(new ModBusPoint("Mumer", 100, 54));
        pointListHelper.add(new ModBusPoint("Jon", 43, 654));
        pointListHelper.add(new ModBusPoint("Broom", 65,87));
        pointListHelper.add(new ModBusPoint("Lee", 565,98));
        pointListHelper.add(new ModBusPoint("Jon", 4,7));
        pointListHelper.add(new ModBusPoint("Broom", 65,9));
        pointListHelper.add(new ModBusPoint("Lee",65,9));
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

