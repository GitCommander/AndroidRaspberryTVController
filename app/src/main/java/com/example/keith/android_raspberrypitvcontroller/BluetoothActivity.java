package com.example.keith.android_raspberrypitvcontroller;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Set;


public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private Switch bluetoothSwitch;
    private Button bluetoothScanningButton;
    protected static final int DISCOVERY_REQUEST = 1;
    protected static final int ENABLE_BLUETOOTH_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothlayout);
        setupUI();

    }

    //create back button on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupUI(){
        //set up actionbar with back button
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //register ui elements
        bluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);
        bluetoothScanningButton = (Button) findViewById(R.id.bluetoothScanButton);

        //turn on listeners
        onSwitchBluetoothState();
        onScanButton();

        //on activity load, check whether the bluetooth is on or not
        checkBluetoothState();
    }

    //bluetooth on/off switch listener
    public void onSwitchBluetoothState() {
        //attach a listener to check for changes in state
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if its not checked
                if (isChecked) {
                    //set the switch to O
                    turnOnBluetooth();
                } else {
                    turnOffBluetooth();
                }
            }
        });
    }

    //bluetooth scan button listener
    public void onScanButton() {
        //scan listener
        View.OnClickListener oclScanBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBluetooth();
            }
        };
        // assign click listener to buttons
        bluetoothScanningButton.setOnClickListener(oclScanBtn);
    }

    //broadcast receiver informs user of bluetooth state
    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            //int previousState = intent.getIntExtra(prevStateExtra, -1);
            switch(state){
                case(BluetoothAdapter.STATE_TURNING_ON) :
                {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth turning on", Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_ON):
                {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth is on", Toast.LENGTH_SHORT).show();
                    bluetoothScanningButton.setVisibility(View.VISIBLE);
                    break;
                }
                case(BluetoothAdapter.STATE_TURNING_OFF):
                {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth turning off", Toast.LENGTH_SHORT).show();
                    break;
                }
                case(BluetoothAdapter.STATE_OFF):
                {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
                    bluetoothScanningButton.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

    //Check whether the bluetooth is on or off
    public boolean checkBluetoothState() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(BluetoothActivity.this, "Bluetooth is on", Toast.LENGTH_SHORT).show();
            bluetoothSwitch.setChecked(true);
            bluetoothScanningButton.setVisibility(View.VISIBLE);
            return true;
        } else {
            Toast.makeText(BluetoothActivity.this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
            bluetoothScanningButton.setVisibility(View.GONE);
            return false;
        }
    }

    //turn on bluetooth
    public void turnOnBluetooth() {

        String bluetoothStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
        String bluetoothRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        IntentFilter filter = new IntentFilter(bluetoothStateChanged);
        registerReceiver(bluetoothState, filter);
        startActivityForResult(new Intent(bluetoothRequestEnable), ENABLE_BLUETOOTH_REQUEST);
    }

    //turn off bluetooth
    public void turnOffBluetooth(){
        bluetoothAdapter.disable();

    }

    //scan bluetooth for devices
    public void scanBluetooth() {
        String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
        String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
        IntentFilter filter = new IntentFilter(scanModeChanged);
        registerReceiver(bluetoothState, filter);
        startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);

    }

    //function handles permission denials
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            // if it was the request to enable Bluetooth:
            case (ENABLE_BLUETOOTH_REQUEST): {
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
                    bluetoothSwitch.setChecked(false);
                }break;
            }
            case (DISCOVERY_REQUEST):{
                if (resultCode != Activity.RESULT_CANCELED) {
                    Toast.makeText(BluetoothActivity.this, "Discovery Permission Granted", Toast.LENGTH_SHORT).show();
                    findDevices();
                }
                else {
                    Toast.makeText(BluetoothActivity.this, "Discovery Permission Denied", Toast.LENGTH_SHORT).show();
                }break;
            }
        }//end of switch
    }//end of onActivityResult

    //function to find devices, already paired or new
    private void findDevices(){
        String lastUsedRemoteDevice = getLastUsedRemoteBTDevice();
        if(lastUsedRemoteDevice != null){
            String toastText="Checking for known paired devices, namely: "+lastUsedRemoteDevice;
            Toast.makeText(BluetoothActivity.this, toastText, Toast.LENGTH_SHORT).show();
            //see if this device is in a list of currently visible paired devices
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for(BluetoothDevice pairedDevice : pairedDevices){
                if(pairedDevice.getAddress().equals(lastUsedRemoteDevice)){
                    toastText = "Found device: " + pairedDevice.getName() + "@" + lastUsedRemoteDevice;
                    Toast.makeText(BluetoothActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    bluetoothDevice = pairedDevice;
                }
            }

        }
        if(bluetoothDevice== null){
            String toastText;
            //start discovery
            if(bluetoothAdapter.startDiscovery()){
                toastText = "Discovery thread started ... Scanning for Devices";
                Toast.makeText(BluetoothActivity.this, toastText, Toast.LENGTH_SHORT).show();
                registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
        }
    }//end of findDevices

    BroadcastReceiver discoveryResult = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String toastText = "Discovered: " + remoteDeviceName;
            Toast.makeText(BluetoothActivity.this, toastText, Toast.LENGTH_SHORT).show();
        }
    };

    private String getLastUsedRemoteBTDevice(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS", null);
        return result;
    }

}

