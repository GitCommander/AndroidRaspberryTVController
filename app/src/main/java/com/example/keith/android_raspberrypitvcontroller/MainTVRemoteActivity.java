package com.example.keith.android_raspberrypitvcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainTVRemoteActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch tvOnOffSwitch;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private Button channelUpButton;
    private Button channelDownButton;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tvremote);
        setUpUI();

        //check tv on off state
        checkTVOnOffState();
        checkBluetoothState();

        //set the switch to ON
        tvOnOffSwitch.setChecked(true);

        //volume and channel controls
        volumeControls();
        channelControls();

    }

    public void setUpUI(){
        //find interactive components on screen
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        tvOnOffSwitch = (Switch) findViewById(R.id.tvOnOffSwitch);
        volumeUpButton = (Button) findViewById(R.id.volumeUpButton);
        volumeDownButton = (Button) findViewById(R.id.volumeDownButton);
        channelUpButton = (Button) findViewById(R.id.channelUpButton);
        channelDownButton = (Button) findViewById(R.id.channelDownButton);

    }

    public void checkTVOnOffState(){
        //attach a listener to check for changes in state
        tvOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    switchStatus.setText("Switch is currently ON");
                } else {
                    switchStatus.setText("Switch is currently OFF");
                }
            }
        });

    }

    public void volumeControls(){
        //volume up listener
        View.OnClickListener oclVolumeUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Volume Up",100).show();
            }
        };
        //volume down
        View.OnClickListener oclVolumeDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Volume Down",100).show();
            }
        };
        // assign click listener to buttons
        volumeUpButton.setOnClickListener(oclVolumeUpBtn);
        volumeDownButton.setOnClickListener(oclVolumeDownBtn);
    }


    public void channelControls(){
        View.OnClickListener oclChannelUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Channel Up",100).show();

            }
        };

        View.OnClickListener oclChannelDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Channel Down",100).show();
            }
        };
        // assign click listener to the OK button (btnOK)
        channelUpButton.setOnClickListener(oclChannelUpBtn);
        channelDownButton.setOnClickListener(oclChannelDownBtn);

    }

    public void checkBluetoothState(){

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
            Toast.makeText(MainTVRemoteActivity.this, "Bluetooth is on",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainTVRemoteActivity.this, "Bluetooth is off",Toast.LENGTH_LONG).show();
            turnOnBluetooth();
        }

    }

    public void turnOnBluetooth(){
        String bluetoothStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
        String bluetoothRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        IntentFilter filter = new IntentFilter(bluetoothStateChanged);
        startActivityForResult(new Intent(bluetoothRequestEnable), 0);


    }


}

