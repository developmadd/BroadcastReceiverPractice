package com.madd.madd.broadcastreceiverpractice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;


/*
    This class implements a service that simulates a long time task
    and that task is executed in a second thread, when the task is finished
    the system throws an notification to the user, due it is executed in a
    service, the notification is thrown even if te activity is in background
    A handler is used to communicate between threads.
 */

public class CounterService extends Service {



    private CounterHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize resources to notify user
        String channelId = "ServicePractice";
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId)
                        .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                        .setContentTitle("Mensaje del Servicio")
                        .setAutoCancel(true);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }
        }


        // Create a custom handler implementation object,
        // Due this handler is created in main thread, we can update UI
        // or have interaction with user
        handler = new CounterHandler();
        handler.setBehaviour(new CounterHandler.CounterInterface() {
            @Override
            public void taskFinished(String message) {
                // Send notification
                notificationBuilder.setContentText(message);
                notificationManager.notify(0, notificationBuilder.build());
            }
        });

    }








    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Duration is defined in intent where the service is thrown
        final int duration = intent.getExtras().getInt("duration");

        // Create a new thread where the long task will be executed
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < duration ; i++) {
                    task();
                }

                // Create a message to tell handler task is over
                Bundle bundle = new Bundle();
                bundle.putString("message","Tarea finalizada");
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);

                stopSelf();

            }
        }).start();

        return START_STICKY;
    }


    void task() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }












    // Own handler implementation which handle the messages sent from work thread
    // This class only executes the correct method depending of the message, method are given by
    // a defined interface.
    static class CounterHandler extends Handler{

        CounterInterface counterInterface;

        void setBehaviour(CounterInterface counterInterface) {
            this.counterInterface = counterInterface;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = msg.getData().getString("message");
            counterInterface.taskFinished(message);
        }

        interface CounterInterface {
            void taskFinished(String message);
        }

    }






}