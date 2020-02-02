package com.atharvafulay.civilliberties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class sendMessage extends AppCompatActivity {


    private TextView mOne, mTwo;
    private EditText edtBox;
    long MillisecondTime, StartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        StartTime = 0L ;
        mOne = findViewById(R.id.M1);
        mTwo = findViewById(R.id.M2);
        edtBox = findViewById(R.id.edtMessage);

        final Button interact = findViewById(R.id.interactButton);
        interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String message = edtMessage.getText().toString();

                final String message = edtBox.getText().toString().trim();
                edtBox.setText("");
                mOne.setText(message);
                mOne.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTwo.setVisibility(View.VISIBLE);
                    }
                }, 5000);



            }
        });

        final Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
    }



    public void openHome(){
        Intent intent = new Intent(this, driverHome.class);
        startActivity(intent);
    }


}