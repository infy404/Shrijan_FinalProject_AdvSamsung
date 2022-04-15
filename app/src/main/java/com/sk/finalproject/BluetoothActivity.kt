package com.sk.finalproject

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class BluetoothActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT:Int = 1;
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2;

    //Bluetooth Adapter
    lateinit var bAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        //Assigning the adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()

        //Updating the status of the bluetooth
        if(bAdapter == null){
            findViewById<TextView>(R.id.btStatusTV).text = "Bluetooth is not available"
        }
        else{
            findViewById<TextView>(R.id.btStatusTV).text = "Bluetooth is available"
        }

        //setting image according to the status of the bluetooth.
        if(bAdapter.isEnabled){
            //Bluetooth is
            findViewById<ImageView>(R.id.btImageView).setImageResource(R.drawable.ic_bt_on)

        }
        else{
            //Bluetooth is off
            findViewById<ImageView>(R.id.btImageView).setImageResource(R.drawable.ic_bt_off)
        }

        //Turning on the bluetooth

        findViewById<Button>(R.id.btn_on).setOnClickListener {
            if (bAdapter.isEnabled) {
                //already enabled
                Toast.makeText(this, "Bluetooth is already On", Toast.LENGTH_LONG).show()
            } else {
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)

            }
        }

        findViewById<Button>(R.id.btn_off).setOnClickListener {
            if (!bAdapter.isEnabled) {
                //already enabled
                Toast.makeText(this, "Bluetooth is already turned Off", Toast.LENGTH_LONG).show()
            } else {
                bAdapter.disable()
                findViewById<ImageView>(R.id.btImageView).setImageResource(R.drawable.ic_bt_off)
                Toast.makeText(this, "Bluetooth turned Off", Toast.LENGTH_LONG).show()
            }
        }


        findViewById<Button>(R.id.btn_discover).setOnClickListener{
            if(!bAdapter.isDiscovering){
                Toast.makeText(this, "Making your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
            }
        }

        findViewById<Button>(R.id.btn_paired).setOnClickListener{
            if (bAdapter.isEnabled){
                val pairedTv = findViewById<TextView>(R.id.pairedTv)
                 pairedTv.text = "Paired Devices"
                val devices = bAdapter.bondedDevices
                for (device in devices){
                    val deviceName = device.name
                    val deviceAddress = device
                    pairedTv.append("\n Device: $deviceName, $device")
                }
            }
            else {
                Toast.makeText(this, "Turn on Bluetooth On First", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_ENABLE_BT ->
                if(resultCode == Activity.RESULT_OK){
                    findViewById<ImageView>(R.id.btImageView).setImageResource(R.drawable.ic_bt_on)
                    Toast.makeText(this, "Bluetooth is On", Toast.LENGTH_LONG).show()
                }
            else{
                    Toast.makeText(this, "Could not turn on Bluetooth", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

}