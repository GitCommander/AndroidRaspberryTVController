package com.example.keith.android_raspberrypitvcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainTVRemoteActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch tvOnOffSwitch;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private Button channelUpButton;
    private Button channelDownButton;
    private Button tvSourceButton;
    private ListView fDrawerList;
    private ArrayAdapter<String> fAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;
    BluetoothAdapter mBluetoothAdapter;
    Handler handler;
    final byte delimiter = 33;
    int readBufferPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tvremote);
        setUpUI();
        //serverInteractions();

    }

    //creates action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow, menu);
        return true;
    }

    //creates bluetooth option in overflow menu on actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.bluetoothLayout) {
            Intent bluetoothIntent = new Intent(MainTVRemoteActivity.this, BluetoothActivity.class);
            startActivity(bluetoothIntent);
        }
        return true;
    }

    //setup UI
    public void setUpUI() {
        //register ui elements
        fDrawerList = (ListView)findViewById(R.id.navList);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        tvOnOffSwitch = (Switch) findViewById(R.id.tvOnOffSwitch);
        volumeUpButton = (Button) findViewById(R.id.volumeUpButton);
        volumeDownButton = (Button) findViewById(R.id.volumeDownButton);
        channelUpButton = (Button) findViewById(R.id.channelUpButton);
        channelDownButton = (Button) findViewById(R.id.channelDownButton);
        tvSourceButton = (Button) findViewById(R.id.tvSourceButton);
        //volume and channel controls
        addDrawerItems();
        volumeControls();
        channelControls();
        sourceControl();

        //set the switch to ON
        tvOnOffSwitch.setChecked(true);

        //check tv on off state
        checkTVOnOffState();

    }

    //navigation drawer
    private void addDrawerItems() {
        String[] featureArray = { "Voice Dictation", "TV Guide"};
        fAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, featureArray);
        fDrawerList.setAdapter(fAdapter);

        fDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    Intent voiceIntent = new Intent(MainTVRemoteActivity.this, VoiceDictationActivity.class);
                    startActivity(voiceIntent);
                }
                if(position == 1){
                    Intent tvGuideIntent = new Intent(MainTVRemoteActivity.this, TVGuideActivity.class);
                    startActivity(tvGuideIntent);
                }
            }
        });

    }


    public void serverInteractions(){
        handler = new Handler();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final class workerThread implements Runnable {

            private String btMsg;
            public workerThread(String msg) {
                btMsg = msg;
            }
            public void run()
            {   //send bluetooth instruction to raspberry pi
                sendBtMsg(btMsg);
                while(!Thread.currentThread().isInterrupted())
                {
                    int bytesAvailable;
                    boolean workDone = false;

                    try {
                        final InputStream mmInputStream;
                        mmInputStream = mmSocket.getInputStream();
                        bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {

                            byte[] packetBytes = new byte[bytesAvailable];
                            Log.e("TV Remote bt","bytes available");
                            byte[] readBuffer = new byte[1024];
                            mmInputStream.read(packetBytes);

                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    //The variable data now contains result
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {   //toast to tell user of result
                                            Toast.makeText(MainTVRemoteActivity.this, data, 10).show();

                                        }
                                    });
                                    workDone = true;
                                    break;
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                            //when work is done bluetooth active session is closed
                            if (workDone == true){
                                mmSocket.close();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        };

    }

    //function to pass a command string to python server on raspberry pi
    public void sendBtMsg(String msg2send){
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()){
                mmSocket.connect();
            }

            String msg = msg2send;
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkTVOnOffState() {
        //attach a listener to check for changes in state
        tvOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    switchStatus.setText("TV is currently ON");
                    //(new Thread(new workerThread("tvOn"))).start();
                } else {
                    switchStatus.setText("TV is currently OFF");
                    //(new Thread(new workerThread("tvOff"))).start();
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
                //(new Thread(new workerThread("volumeUp"))).start();
            }
        };
        //volume down
        View.OnClickListener oclVolumeDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Volume Down", 10).show();
                //(new Thread(new workerThread("volumeDown"))).start();
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
                //(new Thread(new workerThread("channelUp"))).start();
            }
        };

        View.OnClickListener oclChannelDownBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Channel Down", 10).show();
                //(new Thread(new workerThread("channelDown"))).start();
            }
        };
        // assign click listener to the OK button (btnOK)
        channelUpButton.setOnClickListener(oclChannelUpBtn);
        channelDownButton.setOnClickListener(oclChannelDownBtn);
    }

    public void sourceControl() {
        //volume up listener
        View.OnClickListener oclSourceBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Source change", 10).show();
                //(new Thread(new workerThread("source"))).start();
            }
        };
        // assign click listener to buttons
        tvSourceButton.setOnClickListener(oclSourceBtn);
    }


}



