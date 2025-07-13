package com.philexliveprojects.ordeist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.philexliveprojects.ordeist.ORDER_DATABASE

@Database(
    entities = [
        Order::class,
        Account::class,
        Category::class
    ],
    version = 4,
    exportSchema = false
)
abstract class OrdeistDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    companion object {
        private var Instance: OrdeistDatabase? = null

        fun getDatabase(context: Context): OrdeistDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, OrdeistDatabase::class.java, ORDER_DATABASE)
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}