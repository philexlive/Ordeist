package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    fun getOrdersList(): Flow<List<Order>> = orderDao.getList()

    fun getOrder(id: Int): Flow<Order> = orderDao.getById(id)

    suspend fun addOrder(value: Order) = orderDao.add(value)

    suspend fun deleteOrder(value: Order) = orderDao.delete(value)
}