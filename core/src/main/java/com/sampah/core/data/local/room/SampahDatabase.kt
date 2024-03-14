package com.sampah.core.data.local.room

import android.content.Context
import androidx.room.*
import com.sampah.core.data.local.entity.SampahEntity


@Database(
    entities = [SampahEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SampahDatabase: RoomDatabase() {
    abstract fun sampahDao(): SampahDao

    companion object {

        @Volatile
        private var INSTANCE: SampahDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SampahDatabase {
            if (INSTANCE == null) {
                synchronized(SampahDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SampahDatabase::class.java, "samaph_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as SampahDatabase
        }
    }
}