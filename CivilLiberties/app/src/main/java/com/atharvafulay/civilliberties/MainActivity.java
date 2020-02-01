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

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private int recordState = 1;
    int Seconds, Minutes, MilliSeconds ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler() ;
        //record button code
        final Button copButton = findViewById(R.id.copButton);
        copButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPulledOverInfo();
            }
        });
        //record button code
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
                }
                else{
                    recordButton.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(recordRunnable, 0);
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
