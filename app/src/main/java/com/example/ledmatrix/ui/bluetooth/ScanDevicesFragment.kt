package com.example.ledmatrix.ui.bluetooth

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ledmatrix.R
import com.example.ledmatrix.utils.toast
import kotlinx.android.synthetic.main.activity_scan_devices.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ScanDevicesFragment : Fragment() {
    private var list : ArrayList<BluetoothDevice> = ArrayList()

    private val REQUEST_ENABLE_BLUETOOTH = 1
    companion object{
        val EXTRA_ADDRESS : String = "Device_address"
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        lateinit var m_progress: ProgressDialog
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_bluetoothAdapter : BluetoothAdapter
        var m_isConnected: Boolean = false
        private lateinit var nAdapter: DeviceListAdapter
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.activity_scan_devices, container, false)
        return mView
    }
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            val action : String = intent.action!!
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    if(!list.contains(device)) {
                        list.add(device)
                        nAdapter.nData = list
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null){
            context?.toast("this device doesn't support bluetooth")
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
        btn_refresh.setOnClickListener {
            pulsator.stop()
            list.clear()
            if (m_bluetoothAdapter.isDiscovering) {
                m_bluetoothAdapter.cancelDiscovery()
                // checkBTPermissions()
                m_bluetoothAdapter.startDiscovery()
                pulsator.start()
                val discoverDeviceIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
                context?.registerReceiver(mBroadcastReceiver, discoverDeviceIntent)
            }
            if(!m_bluetoothAdapter.isDiscovering){
                // checkBTPermissions()
                m_bluetoothAdapter.startDiscovery()
                pulsator.start()
                // val discoverDeviceIntent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply { BluetoothDevice.ACTION_FOUND }
                val discoverDeviceIntent = IntentFilter(BluetoothDevice.ACTION_FOUND )
                context?.registerReceiver(mBroadcastReceiver, discoverDeviceIntent)
            }
        }
        nAdapter = DeviceListAdapter()
        recyclerView.apply {
            adapter = nAdapter
        }
        nAdapter.nListener = object : DeviceListAdapter.DeviceAdapterListener{
            override fun nOnClickbtnPair(device: BluetoothDevice) {
                activity?.let { ConnectToDevice(it,device.address).execute() }
            }
        }

    }
    override fun onStart() {
        super.onStart()
        m_bluetoothAdapter.startDiscovery()
        pulsator.start()
        // val discoverDeviceIntent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply { BluetoothDevice.ACTION_FOUND }
        val discoverDeviceIntent = IntentFilter(BluetoothDevice.ACTION_FOUND )
        context?.registerReceiver(mBroadcastReceiver, discoverDeviceIntent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter!!.isEnabled){
                    context?.toast("BlueTooth has been enabled")
                }else{
                    context?.toast("Bluetooth has been disabled")
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                context?.toast("Bluetooth enabling has been cancled")
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(mBroadcastReceiver)
    }
    private class ConnectToDevice(c: Context, Address: String) : AsyncTask<Void, Void, String>(){
        private var connectSuccess:Boolean = true
        private val context: Context
        private val address : String
        init {
            this.context = c
            this.address = Address
        }
        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "Please wait")
        }

        override fun doInBackground(vararg params: Void?): String {
            try {
                if(m_bluetoothSocket == null || !m_isConnected){
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    m_bluetoothAdapter.cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            }catch (e: IOException){
                connectSuccess =false
                e.printStackTrace()
            }
            return null.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!connectSuccess){
                Log.i("data", "couldn't connect")
            }else{
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }

}