package com.example.musicplayer_servicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String nombreCanciones[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        final ArrayList<File> canciones = leerCanciones(getExternalFilesDir(null));

        Toast.makeText(this, getExternalFilesDir(null).toString() , Toast.LENGTH_SHORT).show();

        nombreCanciones = new String[canciones.size()];

        for (int i = 0; i < canciones.size(); i++){
            nombreCanciones[i] = canciones.get(i).getName().toString().replace(".mp3","");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_cancion,
                R.id.textView, nombreCanciones);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, AudioPlayer.class).putExtra("posicion", position).putExtra("lista", canciones));
            }
        });

    }

    private ArrayList<File> leerCanciones(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File files[] = root.listFiles();

        for(File file: files){
            if(file.isDirectory()){
                arrayList.addAll(leerCanciones(file));
            } else {
                if (file.getName().endsWith(".mp3")){
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }
}
