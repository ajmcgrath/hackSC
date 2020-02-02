package com.atharvafulay.civilliberties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class police_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_info);

        final Button interact = findViewById(R.id.interactButton);
        interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSendMessage();
            }
        });
    }
    public void openSendMessage(){
        Intent intent = new Intent(this, sendMessage.class);
        startActivity(intent);
    }
}
