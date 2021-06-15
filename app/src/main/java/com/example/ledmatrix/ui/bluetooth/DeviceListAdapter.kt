package com.example.ledmatrix.ui.bluetooth

//import android.R
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ledmatrix.R
import java.util.*

class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>()  {

    private lateinit var view: View
    var nListener: DeviceAdapterListener ?= null
    interface DeviceAdapterListener{
        fun nOnClickbtnPair(device : BluetoothDevice)
    }
    var nData: List<BluetoothDevice> = listOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListAdapter.ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        view = layoutInflater.inflate(R.layout.device_adapter_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceListAdapter.ViewHolder, position: Int) {
        if(nData[position].name == null){
            holder.blNameDevice.text = "Unknown"
        }else {
            holder.blNameDevice.text = nData[position].name
        }
        holder.blAdressDevice.text = nData[position].address
        holder.blBtnPair.setOnClickListener {
            nListener?.nOnClickbtnPair(nData[position])
        }
    }

    override fun getItemCount(): Int {
        return nData.size
    }
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val blNameDevice = itemView.findViewById<TextView>(R.id.Bl_Name_Device)
        val blAdressDevice = itemView.findViewById<TextView>(R.id.Bl_Address_Device)
        val blBtnPair = itemView.findViewById<Button>(R.id.btn_Pair)
    }
}