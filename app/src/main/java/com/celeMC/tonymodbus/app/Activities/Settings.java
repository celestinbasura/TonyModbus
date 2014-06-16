package com.celeMC.tonymodbus.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.celeMC.tonymodbus.app.Constants;
import com.celeMC.tonymodbus.app.R;

public class Settings extends Activity {


    Button btnSave;
    EditText txtinternalIP;
    EditText txtintenalPort;
    EditText txtexternalIp;
    EditText txtexternalPort;


    // User name (make variable public to access from outside)
    public static final String internalIP = "intIP";

    public static final String internalPort = "intPort";

    // Email address (make variable public to access from outside)
    public static final String externalIP = "extIP";

    public static final String externalPort = "extPort";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREF, 0); // 0 - for private mode


        txtinternalIP = (EditText) findViewById(R.id.edt_internal_ip);
        txtintenalPort = (EditText) findViewById(R.id.edt_internal_port);


        txtexternalIp = (EditText) findViewById(R.id.edt_external_ip);
        txtexternalPort = (EditText) findViewById(R.id.edt_external_port);

        btnSave = (Button) findViewById(R.id.btn_save_settings);

        txtinternalIP.setText(pref.getString(internalIP, Constants.DEFAULT_IP));
        txtintenalPort.setText(pref.getInt(internalPort, Constants.DEFAULT_PORT) + "");

        txtexternalIp.setText(pref.getString(externalIP, Constants.EXT_DEFAULT_IP));
        txtexternalPort.setText(pref.getInt(externalPort, Constants.EXT_DEFAULT_PORT) + "");


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = pref.edit();


                editor.putString(internalIP, txtinternalIP.getText().toString());
                editor.putInt(internalPort, Integer.valueOf(txtintenalPort.getText().toString()));

                editor.putString(externalIP, txtexternalIp.getText().toString());
                editor.putInt(externalPort, Integer.valueOf(txtexternalPort.getText().toString()));

                editor.commit();
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Settings.this, MainActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                // Staring Login Activity
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();


    }


}
