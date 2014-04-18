package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.celeMC.tonymodbus.app.R;
import com.celeMC.tonymodbus.app.Threads.ConnecterThread;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView txtStatus;
        Button btnInteroir;
        Button btnExteroir;
        Button btnGroups;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStatus = (TextView)findViewById(R.id.txt_connstatus);

        btnInteroir = (Button) findViewById(R.id.btn_interior);
        btnExteroir = (Button) findViewById(R.id.btn_exterior);
        btnGroups = (Button) findViewById(R.id.btn_groups);
        txtStatus.setText("smeme");
        final ConnecterThread cnn = new ConnecterThread(this.getBaseContext() ,"192.168.1.37" ,5020);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cnn.run();
            }
       }).start();


        btnExteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interiorIntent = new Intent(MainActivity.this, ListActivityNew.class);//InteriorPointActivity.class
                startActivity(interiorIntent);
            }
        });


        btnInteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exteriorIntent = new Intent(MainActivity.this, ListActivityNew.class);
                startActivity(exteriorIntent);
            }
        });


        btnGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupIntent = new Intent (MainActivity.this, ListActivityNew.class);
                startActivity(groupIntent);
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
