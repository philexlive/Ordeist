package com.philexliveprojects.ordeist.data

import android.content.Context

class AppContainer(private val context: Context) {
    val accountRepository: AccountRepository by lazy {
        AccountRepository(OrdeistDatabase.getDatabase(context).accountDao())
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(OrdeistDatabase.getDatabase(context).orderDao())
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(OrdeistDatabase.getDatabase(context).categoryDao())
    }
}