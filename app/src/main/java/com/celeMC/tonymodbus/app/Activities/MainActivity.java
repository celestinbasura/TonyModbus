package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import static android.text.format.Formatter.formatIpAddress;

public class MainActivity extends Activity {


    TextView txtStatus;
    TextView txtPhoneIP;
    Button btnInteroir;
    Button btnExteroir;
    Button btnConnect;
    Button btnGroups;
    Button btnAlarms;
    Button btnActivated;
    Button btnReserved;

    ImageButton btnCloudStatus;
    ImageButton btnInfo;
    ImageButton btnCamera;
    boolean isConnecting = false;
    boolean isConnected;
    boolean isHome = true;
    private boolean doubleBackToExitPressedOnce;
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

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    android.net.NetworkInfo mobile = null;
    android.net.NetworkInfo wifi = null;
    WifiManager wifiManager = null;
    ConnectivityManager connMgr = null;

    boolean isExceptionActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        clearNotification();
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
        txtPhoneIP = (TextView) findViewById(R.id.txt_phone_ip);
        btnInfo = (ImageButton) findViewById(R.id.btn_info);
        btnCloudStatus = (ImageButton) findViewById(R.id.btn_status);
        btnCamera = (ImageButton) findViewById(R.id.btn_camera);
        btnAlarms = (Button) findViewById(R.id.btn_alarms);
        btnActivated = (Button) findViewById(R.id.btn_all_active);
        btnReserved = (Button) findViewById(R.id.btn_reserved);


        btnConnect.setClickable(false);

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        connMgr = (ConnectivityManager) this.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);




        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!Connection.conn.isConnected()) {

                  //  Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                    txtStatus.setText(" Connecting...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            createConn();
                            connect();

                        }
                    }).start();

                } else {

                    closeConnection();
                   // Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();

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

        btnAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cele", "clickled");

                if (Connection.conn.isConnected()) {
                    Intent AlarmsIntent = new Intent(MainActivity.this, AlarmsActivity.class);
                    startActivity(AlarmsIntent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnActivated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cele", "clickled");

                if (Connection.conn.isConnected()) {
                    Intent AllOnIntent = new Intent(MainActivity.this, AllLightsOnActivity.class);
                    startActivity(AllOnIntent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnReserved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("cele", "clickled");

                if (Connection.conn.isConnected()) {
                    Intent intent = new Intent(MainActivity.this, Garden.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }


            }
       });


        if(isHome() && checkAvailableConnection() == 1){

            while(!btnConnect.isClickable()){

               txtStatus.setText("Waiting to Auto Connect");
            }
            connect();
            Toast.makeText(getApplicationContext(), "Connecting automatically to Home network", Toast.LENGTH_SHORT).show();
        }


        btnCloudStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Server status");

                WebView wv = new WebView(MainActivity.this);

                String currentIP;


                if (isHome()) {

                    currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);


                } else {
                    currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);


                }


                wv.loadUrl( "http://www." + currentIP + ":8081");
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();


            }
        });





        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setCancelable(true);




                dialog.setContentView(R.layout.cam_dialog);
                dialog.show();

                Button cam1 = (Button) dialog.findViewById(R.id.btn_dialog_cam1);
                Button cam2 = (Button) dialog.findViewById(R.id.btn_dialog_cam2);
                Button cam3 = (Button) dialog.findViewById(R.id.btn_dialog_cam3);
                Button cam4 = (Button) dialog.findViewById(R.id.btn_dialog_cam4);

                cam1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();
                        Dialog dialogCam1 = new Dialog(MainActivity.this);
                        dialogCam1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        WindowManager.LayoutParams a = dialogCam1.getWindow().getAttributes();
                        a.dimAmount = 0;
                        dialogCam1.getWindow().setAttributes(a);
                        dialogCam1.setCancelable(true);
                        dialogCam1.getWindow().setLayout(3000, 10000);
                        WebView wv = new WebView(MainActivity.this);
                        dialogCam1.setContentView(wv);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialogCam1.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = (height / 2 + 10);
                        String currentIP;
                        if (isHome()) {

                            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);

                        } else {
                            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);

                        }

                        Log.d("tag", "showing");
                        dialogCam1.show();
                        dialogCam1.getWindow().setAttributes(lp);

                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.setInitialScale( (int)(width / 6.85));

                        wv.getSettings().setLoadWithOverviewMode(true);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.getSettings().setUseWideViewPort(true);
                        wv.setWebViewClient(new MyWebViewClient());

                        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                        Log.d("tag", "url load");


                        wv.loadUrl( "http://www." + currentIP + ":8082/mobile.htm");
                    }
                });


                cam2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();
                        Dialog dialogCam2 = new Dialog(MainActivity.this);
                        dialogCam2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        WindowManager.LayoutParams a = dialogCam2.getWindow().getAttributes();
                        a.dimAmount = 0;
                        dialogCam2.getWindow().setAttributes(a);
                        dialogCam2.setCancelable(true);
                        dialogCam2.getWindow().setLayout(3000, 10000);
                        WebView wv = new WebView(MainActivity.this);
                        dialogCam2.setContentView(wv);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialogCam2.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = (height / 2 + 10);
                        String currentIP;
                        if (isHome()) {

                            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);

                        } else {
                            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);

                        }

                        Log.d("tag", "showing");
                        dialogCam2.show();
                        dialogCam2.getWindow().setAttributes(lp);

                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.setInitialScale( (int)(width / 6.85));

                        wv.getSettings().setLoadWithOverviewMode(true);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.getSettings().setUseWideViewPort(true);
                        wv.setWebViewClient(new MyWebViewClient());

                        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                        Log.d("tag", "url load");


                        wv.loadUrl( "http://www." + currentIP + ":8083/mobile.htm");


                    }
                });




                cam3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();
                        Dialog dialogCam3 = new Dialog(MainActivity.this);
                        dialogCam3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        WindowManager.LayoutParams a = dialogCam3.getWindow().getAttributes();
                        a.dimAmount = 0;
                        dialogCam3.getWindow().setAttributes(a);
                        dialogCam3.setCancelable(true);
                        dialogCam3.getWindow().setLayout(3000, 10000);
                        WebView wv = new WebView(MainActivity.this);
                        dialogCam3.setContentView(wv);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialogCam3.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = (height / 2 + 10);
                        String currentIP;
                        if (isHome()) {

                            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);

                        } else {
                            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);

                        }

                        Log.d("tag", "showing");
                        dialogCam3.show();
                        dialogCam3.getWindow().setAttributes(lp);

                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.setInitialScale( (int)(width / 6.85));

                        wv.getSettings().setLoadWithOverviewMode(true);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.getSettings().setUseWideViewPort(true);
                        wv.setWebViewClient(new MyWebViewClient());

                        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                        Log.d("tag", "url load");


                        wv.loadUrl( "http://www." + currentIP + ":8084/mobile.htm");


                    }
                });



                cam4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();
                        Dialog dialogCam4 = new Dialog(MainActivity.this);
                        dialogCam4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        WindowManager.LayoutParams a = dialogCam4.getWindow().getAttributes();
                        a.dimAmount = 0;
                        dialogCam4.getWindow().setAttributes(a);
                        dialogCam4.setCancelable(true);
                        dialogCam4.getWindow().setLayout(3000, 10000);
                        WebView wv = new WebView(MainActivity.this);
                        dialogCam4.setContentView(wv);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialogCam4.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = (height / 2 + 10);
                        String currentIP;
                        if (isHome()) {

                            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);

                        } else {
                            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);

                        }

                        Log.d("tag", "showing");
                        dialogCam4.show();
                        dialogCam4.getWindow().setAttributes(lp);

                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.setInitialScale( (int)(width / 6.85));

                        wv.getSettings().setLoadWithOverviewMode(true);
                        wv.getSettings().setBuiltInZoomControls(true);
                        wv.getSettings().setUseWideViewPort(true);
                        wv.setWebViewClient(new MyWebViewClient());

                        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                        Log.d("tag", "url load");


                        wv.loadUrl( "http://www." + currentIP + ":8085/mobile.htm");


                    }
                });




            }
        });



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

                        if(Connection.conn != null){

                        if(Connection.conn.isConnected()){
                            readRegs();
                        }

                        refreshScreen();
                    }else{
                        return;
                        }
                    }
                }).start();

            }
        };
        tm.scheduleAtFixedRate(readRegs, (long) 500, (long) 2000);

    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onPause() {
        Log.d("cele", "Pause disconnect.");
        tm.cancel();
        readRegs.cancel();

        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (handler != null) { handler.removeCallbacks(mRunnable); }
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



        new Thread(new Runnable() {
            @Override
            public void run() {


                String currentIP;
                int currentPort;

                if (isHome()) {

                    currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);
                    currentPort = sharedPreferences.getInt(Settings.internalPort, Constants.DEFAULT_PORT);

                } else {
                    currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);
                    currentPort = sharedPreferences.getInt(Settings.externalPort, Constants.EXT_DEFAULT_PORT);

                }

                InetAddress address = null;
                try {

                    address = InetAddress.getByName(currentIP);
                    //address = InetAddress.getByName("AFMaher.duckdns.org");
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

                        String currentIP;
                        int currentPort;

                        if (isHome()) {

                            currentIP = sharedPreferences.getString(Settings.internalIP, Constants.DEFAULT_IP);
                            currentPort = sharedPreferences.getInt(Settings.internalPort, Constants.DEFAULT_PORT);

                        } else {
                            currentIP = sharedPreferences.getString(Settings.externalIP, Constants.EXT_DEFAULT_IP);
                            currentPort = sharedPreferences.getInt(Settings.externalPort, Constants.EXT_DEFAULT_PORT);

                        }

                        InetAddress address = null;
                       try {

                            address = InetAddress.getByName(currentIP);
                            //address = InetAddress.getByName("AFMaher.duckdns.org");
                       } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }


                        Connection.conn.setAddress(address);
                        Connection.conn.setPort(currentPort);



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
                           // Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
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



    //returns 0 if no connection, 1 if Wifi and 2 if 3G is connected

    private int checkAvailableConnection() {
        connMgr = (ConnectivityManager) this.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (wifi.isAvailable() && wifi.isConnectedOrConnecting()) {
            return 1;

        }if (mobile.isAvailable() && mobile.isConnectedOrConnecting()) {
            return 2;
        }

        return 0;
    }

    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(getApplicationContext().WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddr = wifiInfo.getIpAddress();
        return formatIpAddress(ipAddr);
    }


    private String getWifiName() {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (true//WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED
                ) {
            ssid = wifiInfo.getSSID();
        }
        Log.d("cele", " SSID " + ssid);
        //Toast.makeText(getApplicationContext(), "SSID is " + ssid, Toast.LENGTH_SHORT).show();
        return ssid;
    }



    boolean isHome() {

        if (checkAvailableConnection() == 1) {
            String name = sharedPreferences.getString(Settings.externalSSID, Constants.EXT_DEFAULT_SSID);
            String wiFiName = getWifiName();
            if (name.equals(wiFiName.substring(1, wiFiName.length()-1))) {

                return true;
            } else {


            }

        } else {


            return false;
        }

        return false;
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
                isExceptionActive = false;


            } catch (ModbusIOException e) {
                Log.d("cele", "IO error");
                isExceptionActive = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        txtStatus.setText(" Server IO error\nPlease reconnect");

                    }
                });

                closeConnection();
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

        if (isConnected) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(checkAvailableConnection() == 1){
                        txtPhoneIP.setText("Phone IP is " + getLocalIpAddress()  + " on " + getWifiName() + "\n Home: " + isHome());
                    }else{

                        if(checkAvailableConnection() == 2){
                            txtPhoneIP.setText("Phone connected to mobile network");
                        }else{
                            txtPhoneIP.setText("Phone not connected");


                        }

                    }

                    btnConnect.setText("Disconnect");
                    btnConnect.setBackgroundResource(R.drawable.button_red_sel);

                  if(!isExceptionActive){

                      txtStatus.setText(" Connected to Server\n Response time: " + (System.currentTimeMillis() - time) + "ms" + " Tr ID: " + trans.getTransactionID() );
                  }




                }
            });

        } else {


            handler.post(new Runnable() {
                @Override
                public void run() {


                    if(checkAvailableConnection() == 1){
                        txtPhoneIP.setText("Phone IP is " + getLocalIpAddress() + " on " + getWifiName() + "\n Home: " + isHome());
                    }else{

                        if(checkAvailableConnection() == 2){
                            txtPhoneIP.setText("Phone connected to mobile network");
                        }else{
                            txtPhoneIP.setText("Phone not connected");


                        }


                    }


                    btnConnect.setText("Connect");
                    btnConnect.setClickable(true);
                    btnConnect.setBackgroundResource(R.drawable.button_green_sel);
                    txtStatus.setText(" Not Connected to Server");

                }
            });


        }



    }



    private void clearNotification()
    {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }


    @Override
    public void onBackPressed() {


        if(Connection.conn.isConnected()){

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit and disconnect", Toast.LENGTH_SHORT).show();

            handler.postDelayed(mRunnable, 2000);

        }else{
            super.onBackPressed();
            return;
        }





    }


  public  class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler, String host, String realm) {

            String user = sharedPreferences.getString(Settings.cameraUser, Constants.CAM_DEFAULT_USER);

            String pass = sharedPreferences.getString(Settings.cameraPass, Constants.CAM_DEFAULT_PASS);
            Log.d("Tag", "user: " + user + " pass " + pass);
            handler.proceed(user, pass);



        }
    }


}




