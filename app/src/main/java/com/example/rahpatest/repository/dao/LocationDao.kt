package com.example.rahpatest.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.rahpatest.repository.table.LocationTable

@Dao
interface LocationDao {
    @Insert
    fun insertAll(vararg tableDataHoliday: LocationTable)

    @Update
    fun updateAll(vararg tableDataHoliday: LocationTable)


    @Query("Select *FROM locationtable ORDER BY id DESC")
    fun getAll(): LiveData<List<LocationTable>>

}