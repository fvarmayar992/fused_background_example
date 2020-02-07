package com.example.rahpatest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rahpatest.R
import com.example.rahpatest.repository.table.LocationTable
import kotlinx.android.synthetic.main.recycler_list_location.view.*

class LocationAdapter(
    var items: ArrayList<LocationTable> ,
    val context: Context
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_list_location, parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.locationLatitude?.text = items.get(position).latitude.toString()
        holder?.locationLongitude?.text = items.get(position).longitude.toString()
        holder?.locationTimes?.text = items.get(position).location.toString()

    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val locationLatitude = view.txtV_rcv_location_latitude
    val locationLongitude = view.txtV_rcv_location_longitude
    val locationTimes = view.txtV_rcv_location_time

}