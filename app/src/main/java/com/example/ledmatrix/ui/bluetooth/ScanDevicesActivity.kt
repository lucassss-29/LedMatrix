package com.example.ledmatrix.ui.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityScanDevicesBinding


private lateinit var binding : ActivityScanDevicesBinding

class ScanDevicesActivity : AppCompatActivity() {

    lateinit var mBluetoothAdapter:BluetoothAdapter
    lateinit var mBTDevices : ArrayList<BluetoothDevice>
    lateinit var mDeviceListAdapter: DeviceListAdapter

    private val mBroadcastReceiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                val state : Int = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> showToast("onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> showToast("mBroadcastReceiver1: STATE TURNING OFF")
                    BluetoothAdapter.STATE_ON -> showToast("mBroadcastReceiver1: STATE ON")
                    BluetoothAdapter.STATE_TURNING_ON -> showToast("mBroadcastReceiver1: STATE TURNING ON")
                }
            }
        }
    }


    private val mBroadcastReceiver2: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_SCAN_MODE,
                    BluetoothAdapter.ERROR
                )
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> showToast("Discoverability Enabled.")
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> showToast("Discoverability Disabled. Able to receive connections.")
                    BluetoothAdapter.SCAN_MODE_NONE -> showToast("Discoverability Disabled. Not able to receive connections.")
                    BluetoothAdapter.STATE_CONNECTING -> showToast("Connecting....")
                    BluetoothAdapter.STATE_CONNECTED -> showToast("Connected.")
                }
            }
        }
    }

    private val mBroadcastReceiver3: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            showToast("FOUND.")
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                mBTDevices.add(device!!)
                showToast("onReceive: " + device.name + ": " + device.address)
                mDeviceListAdapter = DeviceListAdapter(
                    context!!,
                    R.layout.device_adapter_view,
                    mBTDevices
                )
                binding.lvNewDevice.adapter = mDeviceListAdapter
            }
        }
    }

    private val mBroadcastReceiver4: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val mDevice =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //3 cases:
                //case1: bonded already
                if (mDevice!!.bondState == BluetoothDevice.BOND_BONDED) {
                    showToast("BroadcastReceiver: BOND_BONDED.")
                }
                //case2: creating a bone
                if (mDevice.bondState == BluetoothDevice.BOND_BONDING) {
                    showToast("BroadcastReceiver: BOND_BONDING.")
                }
                //case3: breaking a bond
                if (mDevice.bondState == BluetoothDevice.BOND_NONE) {
                    showToast("BroadcastReceiver: BOND_NONE.")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_scan_devices)
        setContentView(R.layout.activity_scan_devices)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_devices)

        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(mBroadcastReceiver4, filter)

        //init BT adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        //turn on or off BT
        binding.btnTurnOnOff.setOnClickListener {
            showToast("enabling/disabling bluetooth")
            enableDisableBT()
        }
        }


    fun enableDisableBT(){
        if(mBluetoothAdapter == null){
            showToast("Does not have BT capabilities")
        }
        if(!mBluetoothAdapter.isEnabled){
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBTIntent)

            //use broadcast receivers
            val BTIntent = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            registerReceiver(mBroadcastReceiver1, BTIntent)
        }
        if(mBluetoothAdapter.isEnabled){
            mBluetoothAdapter.disable()
        }
    }


    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val BTIntent = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mBroadcastReceiver1, BTIntent)
    }

    fun btnEnableDisable_Discoverable(view: View) {
        showToast("Making device discoverable for 300 seconds")
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)

        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(mBroadcastReceiver2, intentFilter)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun btnDiscover(view: View) {
        showToast("Looking for unpaired devices")

        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
            showToast("Canceling discovery")
            checkBTPermissions()

            mBluetoothAdapter.startDiscovery()
            val discoverDeviceIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver3, discoverDeviceIntent)
        }
        if(!mBluetoothAdapter.isDiscovering){
            checkBTPermissions()
            mBluetoothAdapter.startDiscovery()
            val discoverDeviceIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver3, discoverDeviceIntent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            }
        } else {
            showToast("checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.")
        }
    }


}


