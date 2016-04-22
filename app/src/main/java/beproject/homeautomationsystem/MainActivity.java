package beproject.homeautomationsystem;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    BluetoothDevice device;
    ImageView tubeIcon, cflIcon, fanIcon;
    RelativeLayout tube, cfl, fan;
    TextView tubeText, cflText, fanText;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String newAddress = null;
    SharedPreferences sharedPreferences;
    int i;
    boolean tubeState, cflState, fanState;
    RatingBar ratingBar, ratingBar1, ratingBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        tubeIcon = (ImageView) findViewById(R.id.tube_icon);
        tubeText = (TextView) findViewById(R.id.tube_text);
        tube = (RelativeLayout) findViewById(R.id.tube);
        cflIcon = (ImageView) findViewById(R.id.cfl_icon);
        cflText = (TextView) findViewById(R.id.cfl_text);
        cfl = (RelativeLayout) findViewById(R.id.cfl);
        fanIcon = (ImageView) findViewById(R.id.fan_icon);
        fanText = (TextView) findViewById(R.id.fan_text);
        fan = (RelativeLayout) findViewById(R.id.fan);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);

        ratingBar.setEnabled(false);
        ratingBar1.setEnabled(false);
        ratingBar2.setEnabled(true);


        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BluetoothConnect.class));
            }
        });

        tubeState = sharedPreferences.getBoolean("tubeState", false);
        if (!tubeState) {
            tubeIcon.setImageResource(R.mipmap.tubeoff);
            tubeText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tubeIcon.setImageResource(R.mipmap.tubeon);
            tubeText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        cflState = sharedPreferences.getBoolean("cflState", false);
        if (!cflState) {
            cflIcon.setImageResource(R.mipmap.lightoff);
            cflText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            cflIcon.setImageResource(R.mipmap.lighton);
            cflText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        fanState = sharedPreferences.getBoolean("fanState", false);
        if (!fanState) {
            fanIcon.setImageResource(R.mipmap.fanoff);
            fanText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            fanIcon.setImageResource(R.mipmap.fanon);
            fanText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                i=(int)rating;
                if(i==0)
                {
                    i=8;
                }
                i=sendData(i+"");

                if(i!=1)
                {
                    Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                i=(int)rating;
//                if(i==0)
//                {
//                    i=8;
//                }
//                i=sendData(i+"");
//
//                if(i!=1)
//                {
//                    Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });


        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tubeState) {
                    i = sendData("0");
                    if (i == 1) {
                        tubeIcon.setImageResource(R.mipmap.tubeon);
                        tubeText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("tubeState", true);
                        tubeState = true;
//                        ratingBar.setEnabled(true);
//                        ratingBar.setProgress(3);
                        sendData("11");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    i = sendData("0");
                    if (i == 1) {
                        tubeIcon.setImageResource(R.mipmap.tubeoff);
                        tubeText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("tubeState", false);
                        tubeState = false;
//                        ratingBar.setProgress(0);
//                        ratingBar.setEnabled(false);
                        sendData("12");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cflState) {
                    i = sendData("0");
                    if (i == 1) {
                        cflIcon.setImageResource(R.mipmap.lighton);
                        cflText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("cflState", true);
                        cflState = true;
                        sendData("9");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    i = sendData("0");
                    if (i == 1) {
                        cflIcon.setImageResource(R.mipmap.lightoff);
                        cflText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("cflState", false);
                        cflState = false;
                        sendData("10");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fanState) {
                    i = sendData("0");
                    if (i == 1) {
                        fanIcon.setImageResource(R.mipmap.fanon);
                        fanText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("fanState", true);
                        fanState = true;
                        //sendData("7");
                        ratingBar2.setEnabled(true);
                        ratingBar2.setProgress(3);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    i = sendData("0");
                    if (i == 1) {
                        fanIcon.setImageResource(R.mipmap.fanoff);
                        fanText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("fanState", false);
                        fanState = false;
                        //sendData("8");
                        ratingBar2.setProgress(0);
                        ratingBar2.setEnabled(false);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        newAddress = intent.getStringExtra(BluetoothConnect.EXTRA_DEVICE_ADDRESS);

        if (newAddress != null) {
            device = btAdapter.getRemoteDevice(newAddress);

            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e1) {
                Toast.makeText(getBaseContext(), "ERROR - Could not create Bluetooth socket", Toast.LENGTH_SHORT).show();
            }
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Toast.makeText(getBaseContext(), "ERROR - Could not close Bluetooth socket", Toast.LENGTH_SHORT).show();
                }
            }
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "ERROR - Could not create bluetooth outstream", Toast.LENGTH_SHORT).show();
            }
            sendData("0");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (newAddress != null)
                btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "ERROR - Failed to close Bluetooth socket", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBTState() {
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "ERROR - Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private int sendData(String message) {
        byte[] msgBuffer = message.getBytes();
        try {
            if (newAddress != null) {
                outStream.write(msgBuffer);
                return 1;
            }
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
            finish();
            return 0;
        }
        return 0;
    }
}
