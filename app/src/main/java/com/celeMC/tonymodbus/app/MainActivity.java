package com.celeMC.tonymodbus.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.celeMC.tonymodbus.app.ModBusConnect.ConnecterThread;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtStatus;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStatus = (TextView)findViewById(R.id.txt_connstatus);
        txtStatus.setText("smeme");
        final ConnecterThread cnn = new ConnecterThread(this.getBaseContext() ,"192.168.197.1" ,5020);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cnn.run();
            }
        }).start();



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
