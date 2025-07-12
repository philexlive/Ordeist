package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow

class AccountRepository(private val dao: AccountDao) {
    fun loginAccount(login: String, password: String): Flow<Int?> = dao.loginAccount(login, password)

    fun getAccount(id: Int): Flow<Account> = dao.getAccount(id)

    suspend fun createAccount(value: Account) = dao.createAccount(value)

    suspend fun deleteAccount(id: Int) = dao.deleteAccount(id)

    suspend fun updateAccount(value: Account) = dao.updateAccount(value)
}