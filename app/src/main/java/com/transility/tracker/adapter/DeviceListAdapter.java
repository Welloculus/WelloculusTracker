package com.transility.tracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transility.tracker.R;
import com.transility.tracker.app.receiver.OnDeviceConnectListener;
import com.transility.tracker.beans.DeviceInfoBean;
import com.transility.tracker.bluetooth.BluetoothHandler;
import com.transility.tracker.utils.Constants;

import java.util.List;

/**
 * Adapter to show the list of devices associated with the user.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ListItemViewHolder> {

    private List<DeviceInfoBean> deviceInfoList;
    private OnDeviceConnectListener listener = null;
    private BluetoothHandler mBluetoothHandler;
    private Context mContext;
    private Boolean allDevice = false;

    /**
     * The type My view holder.
     */
    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDeviceMacAddress;
        private TextView txtDeviceName;
        private TextView txtConnectionType;
        protected TextView txtHeartRateLabel;
        protected TextView txtHeartRateValue;
        protected TextView txtDeviceAttr;
        private Button btnDeviceConnect;
        private Button btnStartStop;


        /**
         * Instantiates a new My view holder.
         *
         * @param view the view
         */
        public ListItemViewHolder(View view) {
            super(view);
            txtDeviceMacAddress = (TextView) view.findViewById(R.id.txtdeviceId);
            txtDeviceName = (TextView) view.findViewById(R.id.txtDeviceName);
            txtConnectionType = (TextView) view.findViewById(R.id.txtConnectionType);
            txtHeartRateLabel = (TextView) view.findViewById(R.id.text_heart_rate_label);
            txtHeartRateValue = (TextView) view.findViewById(R.id.text_heart_rate_value);
            btnDeviceConnect = (Button) view.findViewById(R.id.btnDeviceConnect);
            btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
            //  txtDeviceAttr = (TextView)view.findViewById(R.id.txtDeviceAttr);
        }
    }

    /**
     * Instantiates a new Device list adapter.
     *
     * @param context        the context
     * @param deviceInfoList the device info list
     * @param listener       the listener
     */
    public DeviceListAdapter(Context context, List<DeviceInfoBean> deviceInfoList, OnDeviceConnectListener listener) {
        this.mContext = context;
        this.deviceInfoList = deviceInfoList;
        this.listener = listener;
        mBluetoothHandler = BluetoothHandler.getInstance();
    }

    public DeviceListAdapter(Context context, List<DeviceInfoBean> deviceInfoList, OnDeviceConnectListener listener, Boolean all) {
        this.mContext = context;
        this.deviceInfoList = deviceInfoList;
        this.listener = listener;
        mBluetoothHandler = BluetoothHandler.getInstance();
        this.allDevice = all;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_row, parent, false);

        return new ListItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {
        final DeviceInfoBean deviceInfo = deviceInfoList.get(position);

        holder.txtDeviceMacAddress.setText(deviceInfo.getDevice_udi());
        holder.txtDeviceName.setText(deviceInfo.getDevice_name());
        holder.txtConnectionType.setText(deviceInfo.getDevice_type());
        holder.txtHeartRateValue.setText(deviceInfo.getCurrentValue()+"");
        holder.btnDeviceConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onConnectClick(deviceInfo);
            }

        });

       /* holder.btnStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onConnectClick(deviceInfo, v);

            }

        });*/

        if (deviceInfo.getDevice_type() != null && Constants.BLUETOOTH.equalsIgnoreCase(deviceInfo.getDevice_type()) && !allDevice) {
            //holder.txtDeviceAttr.setText("Heart Rate");
            holder.btnDeviceConnect.setVisibility(View.VISIBLE);
        } else {
            //holder.txtDeviceAttr.setText("Room Tempreture");
            holder.btnDeviceConnect.setVisibility(View.GONE);
        }
        if (allDevice) {
            holder.btnStartStop.setVisibility(View.VISIBLE);
            if (allDevice && (!deviceInfo.getIn_use())) {
                holder.btnStartStop.setText("Start Using");
            } else {
                holder.btnStartStop.setText("Stop Using");
            }
        } else {
            holder.btnStartStop.setVisibility(View.GONE);
        }


        if (mBluetoothHandler.isConnected()) {
            DeviceInfoBean connectedDevice = mBluetoothHandler.getConnectedDevice();
            if (connectedDevice != null && connectedDevice.getDevice_udi().equalsIgnoreCase(deviceInfo.getDevice_udi())) {
                holder.btnDeviceConnect.setText(mContext.getString(R.string.disconnect));
                holder.txtHeartRateLabel.setVisibility(View.VISIBLE);
                holder.txtHeartRateValue.setVisibility(View.VISIBLE);
            } else {
                holder.txtHeartRateLabel.setVisibility(View.GONE);
                holder.txtHeartRateValue.setVisibility(View.GONE);
                holder.btnDeviceConnect.setText(mContext.getString(R.string.connect));
            }
        } else {
            holder.txtHeartRateLabel.setVisibility(View.GONE);
            holder.txtHeartRateValue.setVisibility(View.GONE);
            holder.btnDeviceConnect.setText(mContext.getString(R.string.connect));
        }

    }

    @Override
    public int getItemCount() {
        return deviceInfoList != null ? deviceInfoList.size() : 0;
    }


}
