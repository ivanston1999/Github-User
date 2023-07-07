package com.submission.githubfinal.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var instance: RoomDb? = null

        @JvmStatic
        fun getDatabase(context: Context): RoomDb {
            if(instance == null) {
                synchronized(RoomDb::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDb::class.java, "User_Database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as RoomDb
        }
    }
}