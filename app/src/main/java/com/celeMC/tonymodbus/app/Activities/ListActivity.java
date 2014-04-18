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

import java.util.ArrayList;


/**
 * Created by Celestin on 11.02.14..
 */
public class ListActivity extends Activity {

    ListView userList;
    PointCustomAdapter userAdapter;
    ArrayList<ModBusPoint> pointListHelper = new ArrayList<ModBusPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * add item in arraylist
         */
        pointListHelper.add(new ModBusPoint("Mumer", 0, 54));
        pointListHelper.add(new ModBusPoint("Jon"  , 1, 654));
        pointListHelper.add(new ModBusPoint("Broom", 2, 87));
        pointListHelper.add(new ModBusPoint("Lee"  , 3, 98));
        pointListHelper.add(new ModBusPoint("Jon"  , 4, 7));
        pointListHelper.add(new ModBusPoint("Broom", 5, 9));
        pointListHelper.add(new ModBusPoint("Lee"  , 6, 9));
        /**
         * set item into adapter
         */
        userAdapter = new PointCustomAdapter(ListActivity.this, R.layout.list_item, pointListHelper);
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
                Toast.makeText(ListActivity.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

}

