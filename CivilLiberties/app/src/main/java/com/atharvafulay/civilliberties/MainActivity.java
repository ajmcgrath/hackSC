package com.atharvafulay.civilliberties;

import androidx.appcompat.app.AppCompatActivity;

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

        //record button code
        handler = new Handler() ;

        //Add OnClickEvents to the button that responds to the user event
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
                    handler.removeCallbacks(runnable);
                    recordButton.setText(R.string.record_button);
                }
                else{
                    recordButton.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                }
                recordState++;
            }
        });
    }

    public Runnable runnable = new Runnable() {

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
