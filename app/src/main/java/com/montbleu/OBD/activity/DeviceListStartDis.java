package com.montbleu.OBD.activity;

import static com.montbleu.OBD.activity.MBTActivity.mBluetoothAdapter;
import static com.montbleu.OBD.activity.MBTActivity.openDeviceConnection;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.montbleu.OBD.adapter.DeviceListAdapter;
import com.montbleu.Utils.Constants;
import com.montbleu.roadsafety.R;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class DeviceListStartDis extends Activity implements DeviceListAdapter.onConnectButtonClickListener {
    private ListView mListView;
    public static  DeviceListAdapter mAdapter;
    public static ArrayList<BluetoothDevice> mRawDeviceList;
    public static ArrayList<BluetoothDevice> mDeviceList;

    //private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805f9b34fb";
    //private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805f9b34fb";
    private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothSocket mSocket = null;
    private BufferedReader mBufferedReader = null;
    static Boolean visibleStatus = false;
    HashSet uniqueDevList;
    static BluetoothDevice device = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paired_devices);
        mRawDeviceList = new ArrayList<>();
        mDeviceList = new ArrayList<>();
        mRawDeviceList.clear();
        mDeviceList.clear();
        mRawDeviceList		= getIntent().getExtras().getParcelableArrayList("device.list");

        uniqueDevList = new HashSet();
        uniqueDevList.clear();
        uniqueDevList.addAll(mRawDeviceList);
        mDeviceList.addAll(uniqueDevList);

        mListView		= (ListView) findViewById(R.id.lv_paired);

        mAdapter		= new DeviceListAdapter(this, DeviceListStartDis.this);

        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = mDeviceList.get(position);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);
                } else {
                    showToast("Pairing...");
                    pairDevice(device);
                }
            }
        });

	/*	mAdapter.setListener(new DeviceListAdapter.onConnectButtonClickListener() {
			@Override
			public void onConnectButtonClick(int position) {
				BluetoothDevice device = mDeviceList.get(position);
				try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        openDeviceConnection(device);
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
				}
			}
		});*/


        mListView.setAdapter(mAdapter);

        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onDestroy();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    };


/*
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void openDeviceConnection(BluetoothDevice aDevice)
			throws IOException {
		InputStream aStream = null;
		InputStreamReader aReader = null;
		try {
			//Toast.makeText(getApplicationContext(), "uuid len"+aDevice.getUuids().length, Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Befre Socket :" , Toast.LENGTH_SHORT).show();
                mSocket = aDevice.createRfcommSocketToServiceRecord(getSerialPortUUID());
				//Toast.makeText(getApplicationContext(), "Socket :" + mSocket, Toast.LENGTH_SHORT).show();
				mSocket.connect();
				if (mSocket.isConnected()) {
					Toast.makeText(getApplicationContext(), "Socket cnnected" + mSocket.isConnected(), Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(), "Socket not cnnected" , Toast.LENGTH_SHORT).show();
				}
				aStream = mSocket.getInputStream();
			    aStream.read();
			    Toast.makeText(getApplicationContext(), "Stream Read :" + aStream.read(), Toast.LENGTH_LONG).show();
			    aReader = new InputStreamReader(aStream);
				mBufferedReader = new BufferedReader(aReader);
				Toast.makeText(getApplicationContext(), "Data Reading :" + mBufferedReader.readLine(), Toast.LENGTH_LONG).show();

			*/
/*	Gson g = new Gson();
			RespResult respResult = g.fromJson(mBufferedReader.readLine(), RespResult.class);
			Toast.makeText(getApplicationContext(), "data "+respResult.getBucketEndDate(), Toast.LENGTH_SHORT).show();
*//*


     */
/*	close( mBufferedReader);
			close( aReader );
			close( aStream );
			close( mSocket );*//*

     */
/*if (!mBufferedReader.readLine().isEmpty()) {
				Toast.makeText(getApplicationContext(),"Data Reading :"+mBufferedReader.readLine(),Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),"Data Empty",Toast.LENGTH_SHORT).show();
			}*//*


		}
		catch ( IOException e ) {
			Log.e( TAG, "Could not connect to device", e );

			Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
			close( mBufferedReader);
			close( aReader );
			close( aStream );
			close( mSocket );
			throw e;
		}
	}
*/


    private UUID getSerialPortUUID() {
        //return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        return UUID.fromString(UUID_SERIAL_PORT_PROFILE);
    }


    private void close(Closeable aConnectedObject) {
        if ( aConnectedObject == null ) return;
        try {
            aConnectedObject.close();
        } catch ( IOException e ) {
        }
        aConnectedObject = null;
    }

    @Override
    public void onConnectButtonClick(int position) {
        device = mDeviceList.get(position);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Boolean result = openDeviceConnection(device);
                if (result) {
                    Constants.BluetoothName=device.getName();
                    finish();
                    visibleStatus = true;
                } else {
                    finish();
                    visibleStatus = false;
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
