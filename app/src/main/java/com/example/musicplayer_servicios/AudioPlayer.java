package com.example.musicplayer_servicios;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
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
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AudioPlayer extends AppCompatActivity implements View.OnClickListener {

    private static MediaPlayer mediaPlayer;
    private TextView textView;
    private Button btnStart;
    private Button btnPause;
    private ArrayList<File> canciones;
    private int posicion;
    private Uri uri;
    private Bundle bundle;
    private static final String CHANNEL_ID = "MyNotificationChannelID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

            bundle = getIntent().getExtras();

            canciones = (ArrayList) bundle.getParcelableArrayList("lista");
            posicion = bundle.getInt("posicion");

            uri = Uri.parse(canciones.get(posicion).toString());

            btnStart = findViewById(R.id.btnStart);
            btnStart.setOnClickListener(this);

            btnPause = findViewById(R.id.btnPause);
            btnPause.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:


                Intent serviceIntent = new Intent(this, AudioPlayerService.class);
                serviceIntent.putExtra("inputExtra", uri).putExtra("posicion", posicion).putExtra("lista", canciones);

                ContextCompat.startForegroundService(this, serviceIntent);



                /*Intent notificationIntent = new Intent(this.getApplicationContext(), this.getClass());


                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                taskStackBuilder.addParentStack(this);
                taskStackBuilder.addNextIntent(notificationIntent);


                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Notificacion de Music Player")
                        .setContentText("Cancion: " + canciones.get(posicion).getName().toString().replace(".mp3",""))
                        .setSmallIcon(R.drawable.ic_queue_music_black_24dp)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(01, builder.build());*/


                break;
            case R.id.btnPause:
                Intent serviceIntentStop = new Intent(this, AudioPlayerService.class);
                stopService(serviceIntentStop);
                this.finish();
                break;
        }
    }
}
