package com.example.keith.android_raspberrypitvcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Button scanBluetoothBtn;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tvremote);
        setUpUI();

        //check tv on off state
        checkTVOnOffState();

        //set the switch to ON
        tvOnOffSwitch.setChecked(true);

        //volume and channel controls
        volumeControls();
        channelControls();

        //bluetooth scan button
        onClickScanBluetooth();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Toast.makeText(MainTVRemoteActivity.this, "Option selected", 10).show();
        if(item.getItemId() == R.id.bluetooth) {
            Intent bluetoothIntent = new Intent(MainTVRemoteActivity.this, BluetoothActivity.class);
            startActivity(bluetoothIntent);
        }
        return true;
    }

    public void setUpUI() {
        //find interactive components on screen
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        tvOnOffSwitch = (Switch) findViewById(R.id.tvOnOffSwitch);
        volumeUpButton = (Button) findViewById(R.id.volumeUpButton);
        volumeDownButton = (Button) findViewById(R.id.volumeDownButton);
        channelUpButton = (Button) findViewById(R.id.channelUpButton);
        channelDownButton = (Button) findViewById(R.id.channelDownButton);
        scanBluetoothBtn = (Button) findViewById(R.id.scanBluetoothBtn);

    }

    public void checkTVOnOffState() {
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

    public void volumeControls() {
        //volume up listener
        View.OnClickListener oclVolumeUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Volume Up", 10).show();
            }
        };
        //volume down
        View.OnClickListener oclVolumeDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Volume Down", 10).show();
            }
        };
        // assign click listener to buttons
        volumeUpButton.setOnClickListener(oclVolumeUpBtn);
        volumeDownButton.setOnClickListener(oclVolumeDownBtn);
    }


    public void channelControls() {
        View.OnClickListener oclChannelUpBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Channel Up", 10).show();

            }
        };

        View.OnClickListener oclChannelDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Channel Down", 10).show();
            }
        };
        // assign click listener to the OK button (btnOK)
        channelUpButton.setOnClickListener(oclChannelUpBtn);
        channelDownButton.setOnClickListener(oclChannelDownBtn);

    }

    public boolean checkBluetoothState() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(MainTVRemoteActivity.this, "Bluetooth is on", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(MainTVRemoteActivity.this, "Bluetooth is off", Toast.LENGTH_LONG).show();
            turnOnBluetooth();
            return false;
        }

    }

    public void turnOnBluetooth() {
        String bluetoothStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
        String bluetoothRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        IntentFilter filter = new IntentFilter(bluetoothStateChanged);
        startActivityForResult(new Intent(bluetoothRequestEnable), 0);

    }

    public void onClickScanBluetooth() {

        View.OnClickListener oclScanBluetoothBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainTVRemoteActivity.this, "Scan for ",100).show();
                if (checkBluetoothState() == true) {
                    Toast.makeText(MainTVRemoteActivity.this, "Scanning...", 10).show();
                    scanBluetooth();
                }

            }
        };
        // assign click listener to the OK button (btnOK)
        scanBluetoothBtn.setOnClickListener(oclScanBluetoothBtn);
    }

    public void scanBluetooth() {


    }

}



