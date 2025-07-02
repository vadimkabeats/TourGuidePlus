package com.example.tourguideplus.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tourguideplus.data.dao.PlaceDao
import com.example.tourguideplus.data.dao.RouteDao
import com.example.tourguideplus.data.model.Place

@Database(
    entities = [Place::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun routeDao(): RouteDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tourguide_db"
                ).build()
                INSTANCE = inst
                inst
            }
    }
}