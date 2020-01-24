package com.madd.madd.broadcastreceiverpractice;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        broadCastReceiver = new GenericBroadcastReceiver(new GenericBroadcastReceiver.SignalAdministrator() {

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


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadCastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCastReceiver);
    }













}
