package com.philexliveprojects.ordeist.data

import android.content.Context

class AppContainer(private val context: Context) {
    val accountRepository: AccountRepository by lazy {
        AccountRepositoryImpl(OrderDatabase.getDatabase(context).accountDao())
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(OrderDatabase.getDatabase(context).orderDao())
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(context)
    }
}