package com.atharvafulay.civilliberties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.os.SystemClock;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import java.io.IOException;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button driverButton = findViewById(R.id.driverButton);
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDriverHome();
            }
        });
    }

    public void openDriverHome(){
        Intent intent = new Intent(this, driverHome.class);
        startActivity(intent);
    }
}
