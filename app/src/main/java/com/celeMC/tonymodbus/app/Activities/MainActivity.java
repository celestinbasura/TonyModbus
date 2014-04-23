package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Interfaces.ConnectionCallback;
import com.celeMC.tonymodbus.app.R;
import com.celeMC.tonymodbus.app.Threads.ConnecterThread;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final TextView txtStatus;
        Button btnInteroir;
        Button btnExteroir;
        final Button btnConnect;
        Button btnGroups;
        String defaultIP;
        int defaultPort;
        final EditText addressIP;
        final EditText addressPort;



        super.onCreate(savedInstanceState);


        defaultIP = "192.168.197.1";
        defaultPort = 5020;

        setContentView(R.layout.activity_main);
        txtStatus = (TextView)findViewById(R.id.txt_connstatus);


        btnInteroir = (Button) findViewById(R.id.btn_interior);
        btnExteroir = (Button) findViewById(R.id.btn_exterior);
        btnConnect = (Button) findViewById(R.id.btn_connect_command);
        btnGroups = (Button) findViewById(R.id.btn_groups);
        addressIP = (EditText) findViewById(R.id.edit_text_ip);
        addressPort = (EditText) findViewById(R.id.edit_text_port);


        addressIP.setText(defaultIP);
        addressPort.setText(defaultPort + "");


        txtStatus.setText("Connection status");



        Log.d("cele", "created");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnConnect.setText("Connecting....");
               btnConnect.setClickable(false);

    try {
        final ConnecterThread cnn = new ConnecterThread(getBaseContext(), addressIP.getText().toString(),
                Integer.valueOf(addressPort.getText().toString()), new ConnectionCallback() {
            @Override
            public void onSuccess() {

                Log.d("cele", "connected");

                txtStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        btnConnect.setText("Connected");
                        txtStatus.setText("connected");

                    }
                });

            }

            @Override
            public void onFailure() {


                txtStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        btnConnect.setClickable(true);
                        btnConnect.setText("Failed to connect");
                        txtStatus.setText("not connected");

                    }
                });

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                cnn.run();
            }
        }).start();
        Toast.makeText(getApplicationContext(), "Connecting to " + addressIP.getText(), Toast.LENGTH_SHORT).show();
    }catch (NumberFormatException e){
        Log.d("cele", "Port not correct");
        Toast.makeText(getApplicationContext(), "The port is incorrect", Toast.LENGTH_SHORT).show();
    }

            }
        });




        btnExteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interiorIntent = new Intent(MainActivity.this, ExteriorPointActivity.class);//InteriorPointActivity.class
                startActivity(interiorIntent);
            }
        });


        btnInteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exteriorIntent = new Intent(MainActivity.this, InteriorPointActivity.class);
                startActivity(exteriorIntent);
            }
        });


        btnGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupIntent = new Intent (MainActivity.this, GroupPointActivity.class);
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
