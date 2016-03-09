package com.example.keith.android_raspberrypitvcontroller;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private Switch bluetoothSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothlayout);
        setupUI();

    }

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
        //actionbar with back button
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //register ui elements
        bluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);

        //check whether the bluetooth is on or not
        checkBluetoothState();
        onSwitchBluetoothState();

    }


    public void onSwitchBluetoothState() {
        //attach a listener to check for changes in state
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBluetoothState() == true) {
                    //set the switch to O
                    scanBluetooth();
                } else {
                    Toast.makeText(BluetoothActivity.this, "Turning Bluetooth on...", Toast.LENGTH_SHORT).show();
                    turnOnBluetooth();
                    onSwitchBluetoothState();
                }
            }
        });

    }


    public boolean checkBluetoothState() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(BluetoothActivity.this, "Bluetooth is on", Toast.LENGTH_SHORT).show();
            bluetoothSwitch.setChecked(true);
            return true;
        } else {
            Toast.makeText(BluetoothActivity.this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
            bluetoothSwitch.setChecked(false);
            return false;
        }

    }

    public void turnOnBluetooth() {
        String bluetoothStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
        String bluetoothRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        IntentFilter filter = new IntentFilter(bluetoothStateChanged);
        startActivityForResult(new Intent(bluetoothRequestEnable), 0);

    }

    public void scanBluetooth() {


    }

}

