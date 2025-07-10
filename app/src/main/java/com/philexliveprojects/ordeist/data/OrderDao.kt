package com.philexliveprojects.ordeist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun getOrdersList(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id")
    fun getOrder(id: Int): Flow<Order>

    @Delete
    suspend fun deleteOrder(order: Order)

    @Insert
    suspend fun addOrder(order: Order)
}