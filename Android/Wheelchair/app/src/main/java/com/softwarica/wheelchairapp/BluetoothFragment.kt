package com.softwarica.wheelchairapp

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.adapters.DeviceListAdapter
import com.softwarica.wheelchairapp.adapters.PairedDeviceListAdapter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class BluetoothFragment : BottomSheetDialogFragment() {
    private var mDeviceList = ArrayList<String>()
    private var mPairedDeviceList = ArrayList<String>()
    private var mPairedAddressList = ArrayList<String>()

    private lateinit var listView: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var listLay: LinearLayout
    private lateinit var pairedDevices: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.bluetooth_fragment, container, false)

        listView = root.findViewById(R.id.bluetoothList)
        loading = root.findViewById(R.id.loading)
        listLay = root.findViewById(R.id.listLay)
        pairedDevices = root.findViewById(R.id.pairedDevices)

        val permission1 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH
        )
        val permission2 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH_ADMIN
        )
        val permission3 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permission4 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission1 != PackageManager.PERMISSION_GRANTED
            || permission2 != PackageManager.PERMISSION_GRANTED
            || permission3 != PackageManager.PERMISSION_GRANTED
            || permission4 != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                642
            )
        } else {
            Log.d("DISCOVERING-PERMISSIONS", "Permissions Granted")
        }


        if (!mBluetoothAdapter.isEnabled)
            mBluetoothAdapter.enable();


        mBluetoothAdapter.startDiscovery();
        var bt = mBluetoothAdapter.bondedDevices

        if (bt.size > 0) {
            bt.forEach {
                if (mPairedAddressList.indexOf(it.address) == -1) {
                    mPairedDeviceList.add(it.name)
                    mPairedAddressList.add(it.address)
                }
            }
        }

        pairedDevices.layoutManager = LinearLayoutManager(requireContext())
        pairedDevices.adapter = PairedDeviceListAdapter(
            mPairedDeviceList,
            mPairedAddressList,
            requireContext()
        )


        var filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        var pairFilter = IntentFilter()
        pairFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)




        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    LOADING_DIALOG -> {
                        val progress: ProgressDialog = ProgressDialog(requireContext())
                        progress.setTitle("Bluetooth Connection");
                        progress.setMessage("Please wait while we connect to devices...");
                        progress.show()
                    }
                }
            }
        }

        activity?.registerReceiver(bondReceiver, pairFilter)
        activity?.registerReceiver(mReceiver, filter)

        return root
    }


    var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action

            if (action == BluetoothDevice.ACTION_FOUND) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device!!.name != null) {
                    if (mDeviceList.indexOf(device?.name) == -1) {
                        mDeviceList.add(device?.name!!)
                        mBTList.add(device)
                    }

                }
            } else if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                loading.visibility = View.GONE
                listLay.visibility = View.VISIBLE
                listView.layoutManager = LinearLayoutManager(requireContext())
                listView.adapter = DeviceListAdapter(mDeviceList)
            }
        }
    }

    var bondReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action

            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val mDevice: BluetoothDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!

                when (mDevice.bondState) {
                    BluetoothDevice.BOND_BONDED -> {
                        Toast.makeText(requireContext(), "Paired", Toast.LENGTH_SHORT).show()

                    }
                    BluetoothDevice.BOND_BONDING -> {
                        loading.visibility = View.VISIBLE
                        listLay.visibility = View.GONE
                    }
                    BluetoothDevice.BOND_NONE -> {
                        loading.visibility = View.GONE
                        listLay.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Device Not Connected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(bondReceiver)
        activity?.unregisterReceiver(mReceiver)

        //mBluetoothAdapter.cancelDiscovery();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        var mHandler: Handler? = null
        private var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        private var mBTList = ArrayList<BluetoothDevice>()
        var mmSocket: BluetoothSocket? = null
        private var LOADING_DIALOG = 1

        fun pairDevice(position: Int) {

            if (position != null) {
                mBTList.get(position).createBond();
            }
        }

        class CreateConnectionThread(private val context: Context, address: String) : Thread() {
            var bluetoothDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(address)
            var tmp: BluetoothSocket? = null
            var uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


            init {
                try {
                    /*
                    Get a BluetoothSocket to connect with the given BluetoothDevice.
                    Due to Android device varieties,the method below may not work fo different devices.
                    You should try using other methods i.e. :
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                     */
                    tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

                } catch (e: IOException) {
                    print(e.printStackTrace())
                }

                mmSocket = tmp;
            }

            override fun run() {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                bluetoothAdapter.cancelDiscovery()
                try {
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.

                    mHandler?.obtainMessage(LOADING_DIALOG)?.sendToTarget()
                    mmSocket!!.connect()

                    Log.e("Status", "Device connected")

                    var intent = Intent(context, TabActivity::class.java)
                    intent.putExtra(Constants.MODE, Constants.REMOTE)
                    intent.putExtra(Constants.BT_STATUS, Constants.CONNECTED)

                    ContextCompat.startActivity(context!!, intent, null)

                } catch (connectException: IOException) {
                    // Unable to connect; close the socket and return.
                    try {
                        mmSocket!!.close()
                        Log.e("TAG", "run: " + connectException.message)
                        Log.e("Status", "Cannot connect to device")
                    } catch (closeException: IOException) {

                    }
                    return
                }
            }

            // Closes the client socket and causes the thread to finish.
            fun cancel() {
                try {
                    mmSocket!!.close()
                } catch (e: IOException) {
                    Log.e("TAG", "Could not close the client socket", e)
                }
            }

        }
    }

}
