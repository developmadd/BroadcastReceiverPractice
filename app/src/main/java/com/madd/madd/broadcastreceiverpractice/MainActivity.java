package com.madd.madd.broadcastreceiverpractice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewCharge;
    TextView textViewInternet;
    TextView textViewBluetooth;

    IntentFilter intentFilter;
    GenericBroadcastReceiver broadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCharge = findViewById(R.id.TV_Charge);
        textViewInternet = findViewById(R.id.TV_Internet);
        textViewBluetooth = findViewById(R.id.TV_Bluetooth);


        // Create intent filter for dynamic broadcast receiver
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // Create Broadcast Receiver using a customized class, this class executes the correct method when an specific event
        // has happened.
        broadCastReceiver = new GenericBroadcastReceiver( new GenericBroadcastReceiver.SignalAdministrator() {

            @Override
            public void onChangeChargingStatus(boolean chargeStatus) {
                showChargingStatus(chargeStatus);
            }

            @Override
            public void onChangeConnectivityStatus(boolean connectivityStatus, String type) {
                showConnectivityStatus(connectivityStatus,type);
            }

            @Override
            public void onChangeBluetoothStatus(boolean bluetoothStatus) {
                showBluetoothStatus(bluetoothStatus);
            }

        });

        // Initialize bluetooth and charging status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            showChargingStatus(batteryManager.isCharging());
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean bluetoothStatus = mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
        showBluetoothStatus(bluetoothStatus);







    }






    private void showChargingStatus( boolean chargeStatus){
        if(chargeStatus) {
            textViewCharge.setText("Energia: Cargando");
            textViewCharge.setTextColor(Color.parseColor("#00AA00"));
        } else {
            textViewCharge.setText("Energia: Desconectado");
            textViewCharge.setTextColor(Color.parseColor("#AA0000"));
        }
    }

    private void showConnectivityStatus( boolean connectivityStatus, String type ){
        if(connectivityStatus) {
            textViewInternet.setText("Internet: Conectado" + "(" + type +")");
            textViewInternet.setTextColor(Color.parseColor("#00AA00"));
        } else {
            textViewInternet.setText("Internet: Desconectado");
            textViewInternet.setTextColor(Color.parseColor("#AA0000"));
        }
    }

    private void showBluetoothStatus( boolean bluetoothStatus ){
        if(bluetoothStatus) {
            textViewBluetooth.setText("Bluetooth: Encendido");
            textViewBluetooth.setTextColor(Color.parseColor("#00AA00"));
        } else {
            textViewBluetooth.setText("Bluetooth: Apagado");
            textViewBluetooth.setTextColor(Color.parseColor("#AA0000"));
        }
    }



    // Register broadcast receiver when the app is on foreground
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadCastReceiver,intentFilter);
    }


    // Unregister broadcast receiver when the app is on background
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCastReceiver);
    }



}


// PENDIENTES
//
//  intent filter ¿Como activar actividad, servicio o broadcast receiver a traves de intent?
//  intent action edit, data image
//  jobscheduler
//  onBind
//  sendbroadcast
//  parseable serializable
//
//
//



