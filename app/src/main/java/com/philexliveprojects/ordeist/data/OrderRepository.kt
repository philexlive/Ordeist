package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersList(): Flow<List<Order>>

    fun getOrder(id: Int): Flow<Order>

    suspend fun addOrder(value: Order)

    suspend fun deleteOrder(value: Order)
}

class OrderRepositoryImpl(private val orderDao: OrderDao) : OrderRepository {
    override fun getOrdersList(): Flow<List<Order>> = orderDao.getList()

    override fun getOrder(id: Int): Flow<Order> = orderDao.getById(id)

    override suspend fun addOrder(value: Order) = orderDao.add(value)

    override suspend fun deleteOrder(value: Order) = orderDao.delete(value)
}