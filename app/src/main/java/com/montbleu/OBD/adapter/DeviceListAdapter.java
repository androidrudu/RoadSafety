package com.montbleu.OBD.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.montbleu.OBD.activity.MBTActivity;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.RidesDetailPageActivity;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


public class DeviceListAdapter extends BaseAdapter{
	Boolean connect_blu=true;
	private LayoutInflater mInflater;	
	private List<BluetoothDevice> mData;
	private OnPairButtonClickListener mListener;
	private onConnectButtonClickListener mConnlistener;
	Context context;


	public DeviceListAdapter(Context context, onConnectButtonClickListener listener) {
		context=context;
        mInflater = LayoutInflater.from(context);
		mConnlistener = listener;
    }
    public DeviceListAdapter(Context context){
		context=context;

	}
	
	public void setData(List<BluetoothDevice> data) {
		mData = data;
	}
	
	public void setListener(OnPairButtonClickListener listener) {
		mListener = listener;
	}

	public void setListener(onConnectButtonClickListener listener) {
		mConnlistener = listener;
	}
	
	public int getCount() {
		return (mData == null) ? 0 : mData.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {			
			convertView			=  mInflater.inflate(R.layout.list_item_device, null);
			
			holder 				= new ViewHolder();
			
			holder.nameTv		= (TextView) convertView.findViewById(R.id.tv_name);
			holder.addressTv 	= (TextView) convertView.findViewById(R.id.tv_address);
			holder.pairBtn		= (Button) convertView.findViewById(R.id.btn_pair);
			holder.connectBtn = (Button) convertView.findViewById(R.id.btn_connect);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BluetoothDevice device	= mData.get(position);
		holder.nameTv.setText(device.getName());
		holder.addressTv.setText(device.getAddress());
		holder.pairBtn.setText((device.getBondState() == BluetoothDevice.BOND_BONDED) ? "Unpair" : "Pair");

		holder.pairBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onPairButtonClick(position);
				}
			}
		});

		if ((device.getBondState() == BluetoothDevice.BOND_BONDED)) {
			/*if (connect_blu==true){
				if (device.getName().equals("OBD_MBT_CLS")){
					if (mConnlistener != null) {
						mConnlistener.onConnectButtonClick(position);
					}
					*//*Intent intent = new Intent(context, MBTActivity.class);
					context.startActivity(intent);*//*
					//holder.connectBtn.performLongClick();
					connect_blu=false;
				}
			}*/

		//holder.connectBtn.setVisibility(View.VISIBLE);
		//holder.connectBtn.setText("Connect");
		} else {
			holder.connectBtn.setVisibility(View.GONE);
		}

		holder.connectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mConnlistener != null) {
					mConnlistener.onConnectButtonClick(position);
				}
			}
		});

        return convertView;
	}
	static class ViewHolder {
		TextView nameTv;
		TextView addressTv;
		TextView pairBtn;
		TextView connectBtn;
	}
	
	public interface OnPairButtonClickListener {
		public abstract void onPairButtonClick(int position);
	}

	public interface onConnectButtonClickListener {
		public abstract void onConnectButtonClick(int position);
	}


}