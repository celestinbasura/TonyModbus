package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {




    TextView txtStatus;
    Button btnInteroir;
    Button btnExteroir;
    Button btnConnect;
    Button btnGroups;
    Handler handler = new Handler();
    Timer tm;
    TimerTask refreshScreen;
    ConnectionManager connUsed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_main);

        btnInteroir = (Button) findViewById(R.id.btn_interior);
        btnExteroir = (Button) findViewById(R.id.btn_exterior);
        btnConnect = (Button) findViewById(R.id.btn_connect_command);
        btnGroups = (Button) findViewById(R.id.btn_groups);
        txtStatus = (TextView) findViewById(R.id.txt_connstatus);

        connUsed = ConnectionManager.getInstance(getApplicationContext());




        Log.d("cele", "created");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!connUsed.isConnected()){

                    connUsed.connectToServer();
                }else{

                    connUsed.disconnectFromServer();

                }


            }});



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



        tm = new Timer();


    }



    @Override
    protected void onResume() {
        super.onResume();
       // connUsed.connectToServer();
        Log.d("cele", "Onresume");

        tm = new Timer();
        refreshScreen = new TimerTask() {
            @Override
            public void run() {

                Log.d("cele", "value checked" + connUsed.isConnected());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(connUsed.isConnected()){

                            btnConnect.setText("Disconnect");
                            txtStatus.setText(" Connected to Server");
                        }else{

                            btnConnect.setText("Connect");
                            txtStatus.setText(" Not Connected to Server");

                        }

                    }
                });

            }
        };

        tm.scheduleAtFixedRate(refreshScreen, (long) 500, (long) 1000);

    }


    @Override
    protected void onPause() {
        Log.d("cele", "Pause disconnect.");
        // closeConnection();
        tm.cancel();
        Log.d("cele", "Onrpause");
        refreshScreen.cancel();
        //connUsed.disconnectFromServer();
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("cele", "Stop disconnect.");
        //  closeConnection();
        tm.cancel();
        refreshScreen.cancel();
        //connUsed.disconnectFromServer();

        super.onStop();

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

            Intent iinent= new Intent(MainActivity.this, Settings.class);
            startActivity(iinent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }



}
