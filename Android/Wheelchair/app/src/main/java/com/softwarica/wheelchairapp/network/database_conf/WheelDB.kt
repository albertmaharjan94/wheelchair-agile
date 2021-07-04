package com.softwarica.wheelchairapp.network.database_conf

import android.app.Activity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.softwarica.wheelchairapp.network.dao.ActivityDao
import com.softwarica.wheelchairapp.network.model.StartActivity
import com.softwarica.wheelchairapp.network.model.StartTimeConverter

@Database(
    entities = [(StartActivity::class)],
    version = 1
)
@TypeConverters(StartTimeConverter::class)

abstract class WheelDB : RoomDatabase() {
    abstract fun getActivityDao(): ActivityDao
//    abstract fun getAuthDao(): AuthDao
//    abstract fun getTrackerDao(): TrackerDao

    companion object{
        @Volatile
        private var instance: WheelDB ?= null

        fun getinstance(context: Context): WheelDB{
            if (instance == null){
                synchronized(WheelDB::class) {
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WheelDB::class.java,
            "WheelDB"
        ).build()
    }
}