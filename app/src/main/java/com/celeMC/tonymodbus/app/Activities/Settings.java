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

import java.lang.reflect.Field;

public class Settings extends Activity {


    public static final String internalIP = "intIP";
    public static final String internalPort = "intPort";


    public static final String externalIP = "extIP";
    public static final String externalPort = "extPort";
    public static final String externalSSID = "extSSID";
    public static final String cameraUser = "camUser";
    public static final String cameraPass = "camPass";
    Button btnSave;
    EditText txtinternalIP1;
    EditText txtinternalIP2;
    EditText txtinternalIP3;
    EditText txtinternalIP4;
    EditText txtintenalPort;
    EditText txtexternalIp;
    EditText txtexternalPort;
    EditText txtexternalSSID;
    EditText txtCameraUser;
    EditText txtCameraPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREF, 0); // 0 - for private mode


        txtinternalIP1 = (EditText) findViewById(R.id.edt_internal_ip1);
        txtinternalIP2 = (EditText) findViewById(R.id.edt_internal_ip2);
        txtinternalIP3 = (EditText) findViewById(R.id.edt_internal_ip3);
        txtinternalIP4 = (EditText) findViewById(R.id.edt_internal_ip4);


        txtintenalPort = (EditText) findViewById(R.id.edt_internal_port);


        txtexternalIp = (EditText) findViewById(R.id.edt_external_ip);
        txtexternalPort = (EditText) findViewById(R.id.edt_external_port);
        txtexternalSSID = (EditText) findViewById(R.id.edt_external_ssid_name);

        txtCameraUser = (EditText) findViewById(R.id.edt_camera_login);
        txtCameraPass = (EditText) findViewById(R.id.edt_camera_pass);

        btnSave = (Button) findViewById(R.id.btn_save_settings);


        String intIP = pref.getString(internalIP, Constants.DEFAULT_IP);

        String[] parsed = intIP.split("\\.");
        txtinternalIP1.setText(parsed[0]);
        txtinternalIP2.setText(parsed[1]);
        txtinternalIP3.setText(parsed[2]);
        txtinternalIP4.setText(parsed[3]);
        // txtinternalIP1.setText(pref.getString(internalIP, Constants.DEFAULT_IP));
        txtintenalPort.setText(pref.getInt(internalPort, Constants.DEFAULT_PORT) + "");

        txtexternalIp.setText(pref.getString(externalIP, Constants.EXT_DEFAULT_IP));
        txtexternalPort.setText(pref.getInt(externalPort, Constants.EXT_DEFAULT_PORT) + "");
        txtexternalSSID.setText(pref.getString(externalSSID, Constants.EXT_DEFAULT_SSID));

        txtCameraUser.setText(pref.getString(cameraUser, Constants.CAM_DEFAULT_USER));
        txtCameraPass.setText(pref.getString(cameraPass, Constants.CAM_DEFAULT_PASS));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = pref.edit();


                editor.putString(internalIP, (txtinternalIP1.getText().toString() + "." +
                        txtinternalIP2.getText().toString() + "." +
                        txtinternalIP3.getText().toString() + "." +
                        txtinternalIP4.getText().toString()));


                editor.putInt(internalPort, Integer.valueOf(txtintenalPort.getText().toString()));

                editor.putString(externalIP, txtexternalIp.getText().toString());
                editor.putInt(externalPort, Integer.valueOf(txtexternalPort.getText().toString()));
                editor.putString(externalSSID, txtexternalSSID.getText().toString());
                editor.putString(cameraUser, txtCameraUser.getText().toString());
                editor.putString(cameraPass, txtCameraPass.getText().toString());

                editor.commit();
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Settings.this, MainActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();


    }


}
