package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.PactEntity
import com.example.data.model.SummonedJinnEntity

@Database(
    entities = [SummonedJinnEntity::class, PactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JinnDatabase : RoomDatabase() {
    abstract fun jinnDao(): JinnDao

    companion object {
        @Volatile
        private var INSTANCE: JinnDatabase? = null

        fun getDatabase(context: Context): JinnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JinnDatabase::class.java,
                    "jinn_grimoire_database"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
