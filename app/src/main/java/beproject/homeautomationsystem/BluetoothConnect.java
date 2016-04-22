package beproject.homeautomationsystem;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothConnect extends AppCompatActivity {

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    ListView pairedListView;
    public static String EXTRA_DEVICE_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        // Find and set up the ListView for paired devices
        pairedListView = (ListView) findViewById(R.id.list);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                // Make an intent to start next activity while taking an extra which is the MAC address.
                Intent i = new Intent(BluetoothConnect.this, MainActivity.class);
                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBTState();

        mPairedDevicesArrayAdapter.clear();// clears the array so items aren't duplicated when resuming from onPause


        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        mPairedDevicesArrayAdapter.notifyDataSetChanged();
    }

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if (mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!mBtAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

}
