package com.example.tourguideplus.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tourguideplus.data.dao.NoteDao
import com.example.tourguideplus.data.dao.PlaceDao
import com.example.tourguideplus.data.dao.RouteDao
import com.example.tourguideplus.data.dao.VisitLogDao
import com.example.tourguideplus.data.model.Note
import com.example.tourguideplus.data.model.Place
import com.example.tourguideplus.data.model.RouteEntity
import com.example.tourguideplus.data.model.RoutePlaceCrossRef
import com.example.tourguideplus.data.model.VisitLog

@Database(
    entities = [
        Place::class,
        RouteEntity::class,
        RoutePlaceCrossRef::class,
        Note::class,
        VisitLog::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun routeDao(): RouteDao
    abstract fun noteDao(): NoteDao
    abstract fun visitLogDao(): VisitLogDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tourguide_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}