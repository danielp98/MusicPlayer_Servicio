package com.example.musicplayer_servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    public AudioPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static final String ACTION_PLAY = "com.example.action.PLAY";
     public MediaPlayer mediaPlayer = null;

    private static final String CHANNEL_ID = "MyNotificationChannelID";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }


        Bundle bundle = intent.getExtras();

        ArrayList<File> canciones = (ArrayList) bundle.getParcelableArrayList("lista");
        int posicion =  bundle.getInt("posicion");

        Uri uri = Uri.parse(canciones.get(posicion).toString());


        //


        Intent notificationIntent = new Intent(this.getApplicationContext(), AudioPlayer.class);


        /*TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addParentStack(AudioPlayer.class);
        taskStackBuilder.addNextIntent(notificationIntent);


        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int)System.currentTimeMillis(), intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notificacion de Music Player")
                .setContentText("Cancion: " + canciones.get(posicion).getName().toString().replace(".mp3",""))
                .setSmallIcon(R.drawable.ic_queue_music_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification); //inicia en 2do plano la notificacion

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Media Player Service", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);





        //




        //do heavy work on a background thread
        //stopSelf();



        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync(); // prepare async to not block main thread

        //mediaPlayer.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
