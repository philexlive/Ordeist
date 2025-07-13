package com.philexliveprojects.ordeist.data

import android.content.Context

class AppContainer(private val context: Context) {
    val accountRepositoryImpl: AccountRepository by lazy {
        AccountRepositoryImpl(OrdeistDatabase.getDatabase(context).accountDao())
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(OrdeistDatabase.getDatabase(context).orderDao())
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl(OrdeistDatabase.getDatabase(context).categoryDao())
    }
}