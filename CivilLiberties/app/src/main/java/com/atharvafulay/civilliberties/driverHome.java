package com.atharvafulay.civilliberties;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class driverHome extends AppCompatActivity {
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
        setContentView(R.layout.activity_driver_home);
        handler = new Handler() ;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // Text and Main items list
        TextView calmText = (TextView) findViewById(R.id.calm_text);
        calmText.setText("Remember to remain calm. \nDo not panic. \nErratic actions will only make the officer nervous.");
        calmText.setTextColor(Color.WHITE);

        final ListView listView =
                (ListView) this.findViewById(R.id.list_content);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "I have been pulled over.",
                "A police officer is knocking at my front door.",
                "I was in an accident.",
                "I was stopped by the police (not in a vehicle)."
        };

        // Define a new Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.row, values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
//                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                if (position == 0) {
                    openPulledOverInfo();
                }

                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//                        .show();

            }

        });

        //profile button code
        final Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new OnClickListener() {
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
        recordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordState % 2 == 0){
                    recordButton.setBackgroundResource(R.drawable.my_button_bg);
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
                    recordButton.setBackgroundResource(R.drawable.record_background);
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(recordRunnable, 0);
                    startRecording();
                }
                recordState++;
            }
        });

        //POPUP TEMP
        ///////////////////////////////////////////////////////////////////////////
        AlertDialog.Builder builder = new AlertDialog.Builder(driverHome.this);
        builder.setCancelable(true);
        builder.setTitle("Contact Alert");
        builder.setMessage("A policeman is trying to contact you");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openPoliceInfo();
            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void openPoliceInfo(){
        Intent intent = new Intent(this, police_info.class);
        startActivity(intent);
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