package com.atharvafulay.civilliberties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.os.Handler;
import android.os.SystemClock;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.*;
import androidx.core.app.ActivityCompat;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //recording vars
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;
    private int recordState = 1;
    private MediaRecorder recorder = null;
    private boolean playing = false;
    private MediaPlayer   player = null;
    //timer vars
    int Seconds, Minutes, MilliSeconds ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler() ;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        //home button code
        final Button copButton = findViewById(R.id.copButton);
        copButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPulledOverInfo();
            }
        });
        //profile button code
        final Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    player.release();
                    player = null;
                    playing = false;
                }
                else {
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(fileName);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    playing = true;
                }
            }
        });
        //record button code
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/pullover.3gp";
        final Button recordButton = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordState % 2 == 0){
                    recordButton.setBackgroundColor(getResources().getColor(R.color.button_default));
                    MillisecondTime = 0L ;
                    StartTime = 0L ;
                    TimeBuff = 0L ;
                    UpdateTime = 0L ;
                    Seconds = 0 ;
                    Minutes = 0 ;
                    MilliSeconds = 0 ;
                    handler.removeCallbacks(recordRunnable);
                    recordButton.setText(R.string.record_button);
                    stopRecording();
                }
                else{
                    recordButton.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(recordRunnable, 0);
                    startRecording();
                }
                recordState++;
            }
        });
    }
    //Page changing methods
    public void openPulledOverInfo(){
        Intent intent = new Intent(this, pulledOverInfo.class);
        startActivity(intent);
    }

    //recording methods
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    //Timer runnable
    public Runnable recordRunnable = new Runnable() {

        public void run() {
            final Button recordButton = findViewById(R.id.recordButton);
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            recordButton.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

}
