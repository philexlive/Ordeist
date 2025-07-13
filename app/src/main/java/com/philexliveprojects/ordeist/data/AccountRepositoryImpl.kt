package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun loginAccount(login: String, password: String): Flow<Int?>

    fun getAccount(id: Int): Flow<Account>

    suspend fun createAccount(value: Account)

    suspend fun deleteAccount(id: Int)

    suspend fun updateAccount(value: Account)
}

class AccountRepositoryImpl(private val dao: AccountDao) : AccountRepository {
    override fun loginAccount(login: String, password: String): Flow<Int?> = dao.loginAccount(login, password)

    override fun getAccount(id: Int): Flow<Account> = dao.getAccount(id)

    override suspend fun createAccount(value: Account) = dao.createAccount(value)

    override suspend fun deleteAccount(id: Int) = dao.deleteAccount(id)

    override suspend fun updateAccount(value: Account) = dao.updateAccount(value)
}