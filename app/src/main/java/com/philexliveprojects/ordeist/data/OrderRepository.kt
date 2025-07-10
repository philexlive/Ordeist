package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersList(): Flow<List<Order>>

    fun getOrder(id: Int): Flow<Order>

    suspend fun addOrder(order: Order)

    suspend fun deleteOrder(order: Order)
}

class OrderRepositoryImpl(private val orderDao: OrderDao) : OrderRepository {
    override fun getOrdersList(): Flow<List<Order>> = orderDao.getOrdersList()

    override fun getOrder(id: Int): Flow<Order> = orderDao.getOrder(id)

    override suspend fun addOrder(order: Order) = orderDao.addOrder(order)

    override suspend fun deleteOrder(order: Order) = orderDao.deleteOrder(order)
}