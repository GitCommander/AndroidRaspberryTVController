package com.example.keith.android_raspberrypitvcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainTVRemoteActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch tvOnOffSwitch;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private Button channelUpButton;
    private Button channelDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tvremote);

        //find interactive components on screen
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        tvOnOffSwitch = (Switch) findViewById(R.id.tvOnOffSwitch);
        volumeUpButton = (Button) findViewById(R.id.volumeUpButton);
        volumeDownButton = (Button) findViewById(R.id.volumeDownButton);
        channelUpButton = (Button) findViewById(R.id.channelUpButton);
        channelDownButton = (Button) findViewById(R.id.channelDownButton);

        //set the switch to ON
        tvOnOffSwitch.setChecked(true);

        //attach a listener to check for changes in state
        tvOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("Switch is currently DOING");
                } else {
                    switchStatus.setText("Switch is currently OFF");
                }
            }
        });

        View.OnClickListener oclVolumeUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switchStatus.setText("Volume Up");

            }
        };

        View.OnClickListener oclVolumeDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switchStatus.setText("Volume Down");

            }
        };

        View.OnClickListener oclChannelUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switchStatus.setText("Channel Up");

            }
        };

        View.OnClickListener oclChannelDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switchStatus.setText("Channel Down");

            }
        };



        //check the current state before we display the screen
        if(tvOnOffSwitch.isChecked()){
            switchStatus.setText("Switch is currently ON");
        }
        else {
            switchStatus.setText("Switch is currently OFF");
        }

        // assign click listener to the OK button (btnOK)
        volumeUpButton.setOnClickListener(oclVolumeUpBtn);
        volumeDownButton.setOnClickListener(oclVolumeDownBtn);
        channelUpButton.setOnClickListener(oclChannelUpBtn);
        channelDownButton.setOnClickListener(oclChannelDownBtn);

    }
}

