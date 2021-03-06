package com.madd.madd.broadcastreceiverpractice;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class GenericBroadcastReceiver extends BroadcastReceiver {

    SignalAdministrator signalAdministrator;
    public GenericBroadcastReceiver(SignalAdministrator signalAdministrator) {
        this.signalAdministrator = signalAdministrator;
    }


    // The method onReceive is executed when one of the actions added to intent filter occurs
    // Our implemented interface executes the correct method when an specific action happens and send the
    // correct message
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case Intent.ACTION_POWER_CONNECTED:
                signalAdministrator.onChangeChargingStatus(true);
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                signalAdministrator.onChangeChargingStatus(false);
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if(state == BluetoothAdapter.STATE_OFF){
                    signalAdministrator.onChangeBluetoothStatus(false);
                } else if (state == BluetoothAdapter.STATE_ON){
                    signalAdministrator.onChangeBluetoothStatus(true);
                }
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                boolean connectivityStatus = !intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if(connectivityStatus){
                    ConnectivityManager connectivityManager =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if( networkInfo.getType() == ConnectivityManager.TYPE_WIFI ){
                        signalAdministrator.onChangeConnectivityStatus(true,"Wifi");
                    } else if ( networkInfo.getType() == ConnectivityManager.TYPE_MOBILE ){
                        signalAdministrator.onChangeConnectivityStatus(true,"Mobile");
                    } else {
                        signalAdministrator.onChangeConnectivityStatus(true,"Desconocido");
                    }
                } else {
                    signalAdministrator.onChangeConnectivityStatus(false,"");
                }

                break;
        }
    }





    interface SignalAdministrator {
        void onChangeChargingStatus(boolean chargeStatus);
        void onChangeConnectivityStatus(boolean connectivityStatus, String type);
        void onChangeBluetoothStatus(boolean bluetoothStatus);
    }
}