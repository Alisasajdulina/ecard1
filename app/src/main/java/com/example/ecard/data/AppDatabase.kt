package com.example.ecard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecardnarwhal.data.CardDao
import com.example.ecardnarwhal.data.CardEntity

@Database(entities = [CardEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cardDao(): CardDao
    companion object {
        @Volatile private var INST: AppDatabase? = null
        fun get(ctx: Context): AppDatabase {
            return INST ?: synchronized(this) {
                val db = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "ecard.db").build()
                INST = db; db
            }
        }
    }
}
