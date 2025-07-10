package com.philexliveprojects.ordeist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.philexliveprojects.ordeist.ORDER_DATABASE

@Database(entities = [Order::class, Account::class], version = 3, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun accountDao(): AccountDao

    companion object {
        private var Instance: OrderDatabase? = null

        fun getDatabase(context: Context): OrderDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, OrderDatabase::class.java, ORDER_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}