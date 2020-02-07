package com.example.rahpatest.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.rahpatest.MainActivity
import com.example.rahpatest.repository.AppDatabase
import com.example.rahpatest.repository.dao.LocationDao
import com.example.rahpatest.repository.table.LocationTable
import com.google.android.gms.location.*
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.attr.name
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.attr.name




@Suppress("UNREACHABLE_CODE")
class BackgroundLocationService : Service() {
    private lateinit var db: AppDatabase
    private lateinit var calendar: Calendar
    private lateinit var time: String

    private lateinit var mFirestore: FirebaseFirestore


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        db = AppDatabase.invoke(this)
        mFirestore = FirebaseFirestore.getInstance()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        getLastLocation()

        log("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    fun getLastLocation() {
         calendar = Calendar.getInstance()
         time =
            calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
            var location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {

                val locationTable = LocationTable(
                    0,
                    time,
                    location.latitude,
                    location.longitude
                )

                db.locationDao().insertAll(
                    locationTable
                )

                val restaurants = mFirestore.collection("locations")
                restaurants.add(
                    locationTable
                )

            }
        }
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            db.locationDao().insertAll(
                LocationTable(
                    0,
                    time,
                    mLastLocation.latitude,
                    mLastLocation.longitude
                )
            )


            log("onLocationResult")

        }
    }


    private fun log(str: String) = Log.e("BacLocationService", str)


}