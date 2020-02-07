package com.example.rahpatest.repository.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "LocationTable")
data class LocationTable(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "location_address") var location: String,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double
)