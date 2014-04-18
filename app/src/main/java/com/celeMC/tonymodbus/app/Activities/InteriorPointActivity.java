package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import com.celeMC.tonymodbus.app.Models.InteriorPoints;
import com.celeMC.tonymodbus.app.R;


public class InteriorPointActivity extends Activity {

    private ListView pointListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_point);

        pointListView = (ListView) findViewById(R.id.listView_interior);


        InteriorPoints intPoints = new InteriorPoints();
        intPoints.addPointToList("First Test", 0, 100);
        intPoints.addPointToList("second Test", 1, 10);
        intPoints.addPointToList("third Test", 2, 43);



        //Toast.makeText(this.getBaseContext(), "TODO read to list", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interior_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
