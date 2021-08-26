package com.softwarica.wheelchairapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softwarica.wheelchairapp.BluetoothFragment
import com.softwarica.wheelchairapp.R

class PairedDeviceListAdapter(private val dataSet: ArrayList<String>,private val addressSet: ArrayList<String>, private val context : Context) :
    RecyclerView.Adapter<PairedDeviceListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val lay : LinearLayout
        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.deviceTxt)
            lay = view.findViewById(R.id.devlay)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.device_list, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d("TAG", "onBindViewHolder: " + dataSet[position])
        viewHolder.textView.text = dataSet[position]
        viewHolder.lay.setOnClickListener {
            BluetoothFragment.Companion.CreateConnectionThread(context, addressSet[position])
                .start()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}