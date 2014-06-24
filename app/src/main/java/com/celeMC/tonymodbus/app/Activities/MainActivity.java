package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Constants;
import com.celeMC.tonymodbus.app.R;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainActivity extends Activity {


    TextView txtStatus;
    Button btnInteroir;
    Button btnExteroir;
    Button btnConnect;
    Button btnGroups;
    ImageButton btnInfo;
    boolean isConnecting = false;
    boolean isConnected;
    boolean isHome = true;
    Long time;
    long lastResponseTime;
    Handler handler = new Handler();
    Timer tm;
    TimerTask readRegs;
    SharedPreferences sharedPreferences;
    volatile ModbusTCPTransaction trans = null; //the transaction
    ReadMultipleRegistersRequest regRequest = null;
    private ProgressDialog pd;
    String versionName;
    String simpleDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.PREF, 0); // 0 - for private mode
        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Connecting...");
        pd.setMessage("Please wait.");
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);

        try {
            versionName = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try{
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            simpleDate = SimpleDateFormat.getInstance().format(new java.util.Date(time));
            zf.close();

        }catch(Exception e){
        }


        createConn();
        btnInteroir = (Button) findViewById(R.id.btn_interior);
        btnExteroir = (Button) findViewById(R.id.btn_exterior);
        btnConnect = (Button) findViewById(R.id.btn_connect_command);
        btnGroups = (Button) findViewById(R.id.btn_groups);
        txtStatus = (TextView) findViewById(R.id.txt_connstatus);
        btnInfo = (ImageButton) findViewById(R.id.btn_info);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!Connection.conn.isConnected()) {

                    Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            connect();

                        }
                    }).start();

                } else {

                    closeConnection();
                    Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();

                }


            }
        });


        btnExteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("cele", "clickled");

                if (Connection.conn.isConnected()) {
                    Intent interiorIntent = new Intent(MainActivity.this, ExteriorPointActivity.class);//InteriorPointActivity.class
                    startActivity(interiorIntent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnInteroir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cele", "clickled");

                if (Connection.conn.isConnected()) {
                    Intent exteriorIntent = new Intent(MainActivity.this, InteriorPointActivity.class);
                    startActivity(exteriorIntent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connection.conn.isConnected()) {
                    Intent groupIntent = new Intent(MainActivity.this, GroupPointActivity.class);
                    startActivity(groupIntent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();

                }

            }
        });


        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MainActivity.this).setTitle("Info")
                        .setMessage("Developed by: Celestin Basura\nFor: Tony Maher\nVersion: " + versionName +
                        "\nBuild date: " + simpleDate)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }

            }
        );


    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("cele", "Onresume");

        tm = new Timer();
        readRegs = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(Connection.conn.isConnected()){
                            readRegs();
                        }

                        refreshScreen();
                    }
                }).start();


            }
        };
        tm.scheduleAtFixedRate(readRegs, (long) 500, (long) 2000);



    }


    @Override
    protected void onPause() {
        Log.d("cele", "Pause disconnect.");
        tm.cancel();
        readRegs.cancel();

        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("cele", "Stop disconnect.");
        tm.cancel();
        readRegs.cancel();

        super.onStop();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent iinent = new Intent(MainActivity.this, Settings.class);
            startActivity(iinent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    void createConn() {

        final String currentIP;
        final int currentPort;

        if (isHome) {

            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);
            currentPort = sharedPreferences.getInt(Settings.internalPort, Constants.DEFAULT_PORT);

        } else {
            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);
            currentPort = sharedPreferences.getInt(Settings.externalPort, Constants.EXT_DEFAULT_PORT);

        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                InetAddress address = null;
                try {
                    address = InetAddress.getByName(currentIP);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                Connection.createConn(address, currentPort);
                Connection.conn.setPort(currentPort);

            }
        }).start();

    }

    void connect() {


        handler.post(new Runnable() {
            @Override
            public void run() {
                pd.show();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    isConnecting = true;

                    if (!Connection.conn.isConnected()) {
                        Log.d("cele", "Connecting...");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pd.show();
                            }
                        });
                        Connection.conn.connect();
                        Connection.conn.setTimeout(40000);

                    } else {

                        Log.d("cele", "Already connected");
                    }
                } catch (UnknownHostException e) {


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Unknown host", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("cele", "No host");
                    Log.d("cele", e.getMessage());

                } catch (Exception e) {


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                    Log.d("cele", "failed to Connect");
                    Log.d("cele", " " + e.getLocalizedMessage());
                }


                if (Connection.conn.isConnected()) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        }
                    });


                    Log.d("cele", "Connected");
                    isConnecting = false;

                    tm.cancel();
                    readRegs.cancel();
                    tm = new Timer();
                    readRegs = new TimerTask() {
                        @Override
                        public void run() {

                            readRegs();
                            refreshScreen();

                        }
                    };

                    tm.scheduleAtFixedRate(readRegs, (long) 500, (long) 2000);


                }

                isConnecting = false;

            }
        }).start();


    }


    void closeConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (Connection.conn != null && isConnected) {
                    Connection.conn.close();
                    Log.d("cele", "Connection closed to " + Connection.conn.getAddress());

                } else {
                    Log.d("cele", "Not connected");
                    return;

                }
            }
        }).start();

    }

    boolean isHome() {


        return true;
    }


    void readRegs() {

        time = System.currentTimeMillis();


        if (!Connection.conn.isConnected()) {
            Log.d("cele", " not Connected in readRegs");
            return;
        } else {
            Log.d("cele", "Connected in readRegs");


            regRequest = new ReadMultipleRegistersRequest(0, 100);
            trans = new ModbusTCPTransaction(Connection.conn);
            trans.setRequest(regRequest);

            try {

                trans.execute();


            } catch (ModbusIOException e) {
                Log.d("cele", "IO error");

                e.printStackTrace();
            } catch (ModbusSlaveException e) {

                Log.d("cele", "Slave returned exception");
                e.printStackTrace();
            } catch (ModbusException e) {
                Log.d("cele", "Failed to execute request");

                e.printStackTrace();
            } catch (NullPointerException e) {

                e.printStackTrace();
            }


        }
        lastResponseTime = System.currentTimeMillis() - time;

    }

    void refreshScreen() {


        isConnected = Connection.conn.isConnected();
        Log.d("cele", " read");

        if (isConnected && trans.getResponse() != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {


                    btnConnect.setText("Disconnect");
                    txtStatus.setText(" Connected to Server\n Response time: " + (System.currentTimeMillis() - time) + "ms");


                }
            });

        } else {


            handler.post(new Runnable() {
                @Override
                public void run() {

                    btnConnect.setText("Connect");
                    txtStatus.setText(" Not Connected to Server");

                }
            });


        }


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit Application")
                .setMessage("Connection will be dropped if active, \nare you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tm.cancel();
                        readRegs.cancel();
                        closeConnection();
                        tm.cancel();
                        readRegs.cancel();
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

}




