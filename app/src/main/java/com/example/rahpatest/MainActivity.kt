package com.example.rahpatest

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rahpatest.adapter.LocationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.lifecycle.Observer
import com.example.rahpatest.repository.AppDatabase
import com.example.rahpatest.repository.table.LocationTable
import com.example.rahpatest.service.BackgroundLocationService

import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val locationModel: ArrayList<LocationTable> = arrayListOf()
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = AppDatabase.invoke(this)

        listLocation()
        initLocationService()
    }

    fun initLocationService() {
        if (checkPermissions()) {

            if (!isLocationEnabled()) {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

            startToGetLocation()

        } else {
            requestPermissions()
        }

    }

    private fun startToGetLocation() {

        val cur_cal = Calendar.getInstance()
        cur_cal.setTimeInMillis(System.currentTimeMillis())

        val intent = Intent(this, BackgroundLocationService::class.java)
        val pintent = PendingIntent.getService(
            this,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cur_cal.getTimeInMillis(),
            (60 * 1000).toLong(),
            pintent
        )
    }


    private fun createRecyclerview() {
        rcv_location.layoutManager = LinearLayoutManager(this)
        rcv_location.adapter = LocationAdapter(locationModel, this)

    }

    private fun listLocation() {
        var itemLocation = db.locationDao().getAll()
        itemLocation.observe(this, object : Observer<List<LocationTable>> {
            override fun onChanged(t: List<LocationTable>?) {
                locationModel.clear()
                if (!t.isNullOrEmpty()) {
                    locationModel.addAll(t)
                    createRecyclerview()
                }
            }

        })

    }

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            42
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 42) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                initLocationService()

            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    private fun log(str: String) = Log.e("MainActivity", str)

}
