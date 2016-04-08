package com.example.keith.android_raspberrypitvcontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        addDrawerItems();
        volumeControls();
        channelControls();
        sourceControl();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.bluetoothLayout) {
            Intent bluetoothIntent = new Intent(MainTVRemoteActivity.this, BluetoothActivity.class);
            startActivity(bluetoothIntent);
        }
        return true;
    }

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

    }

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

    public void checkTVOnOffState() {
        //attach a listener to check for changes in state
        tvOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    switchStatus.setText("TV is currently ON");
                } else {
                    switchStatus.setText("TV is currently OFF");
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

    public void sourceControl() {
        //volume up listener
        View.OnClickListener oclSourceBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainTVRemoteActivity.this, "Source change", 10).show();
            }
        };

        // assign click listener to buttons
        tvSourceButton.setOnClickListener(oclSourceBtn);
    }


}



