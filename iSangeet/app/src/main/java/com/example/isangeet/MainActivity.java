package com.example.isangeet;

import static java.util.Collections.addAll;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public Object myFile;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> mysongs = fetchsongs(Environment.getExternalStorageDirectory());
                        String[] items = new String[mysongs.size()];
                        for (int i = 0; i < mysongs.size(); i++) {
                            items[i] = mysongs.get(i).getName().replace(".mp3", "");
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, Playsong.class);


                                String currentsong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songlist",mysongs);
                                intent.putExtra("currentsong",currentsong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });



                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }



    public ArrayList<File> fetchsongs(File file){
    ArrayList arrayList = new ArrayList();
    File[] songs = file.listFiles();
            if(songs !=null){
           for (File myfile:songs){
                if(!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchsongs(myfile));
                }
                else{
                       if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")){
                        arrayList.add(myfile);
                }
            }
        }
    }
    return arrayList;
  }
}