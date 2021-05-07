package com.example.ledmatrix

//import android.R
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

class DeviceListAdapter(context: Context, tvResourceId: Int, devices: ArrayList<BluetoothDevice>) :
    ArrayAdapter<BluetoothDevice?>(context, tvResourceId, devices as List<BluetoothDevice?>) {
    private val mLayoutInflater: LayoutInflater
    private val mDevices: ArrayList<BluetoothDevice>
    private val mViewResourceId: Int

    init {
        mDevices = devices
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mViewResourceId = tvResourceId
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var convertView = convertView
        var convertView = mLayoutInflater.inflate(mViewResourceId, null)
        val device = mDevices[position]
        if (device != null) {
            val deviceName = convertView.findViewById<TextView>(R.id.tv_device_name)
            val deviceAdress = convertView.findViewById<TextView>(R.id.tv_device_address)
            if (deviceName != null) {
                deviceName.text = device.name
            }
            if (deviceAdress != null) {
                deviceAdress.text = device.address
            }
        }
        return convertView
    }
}
