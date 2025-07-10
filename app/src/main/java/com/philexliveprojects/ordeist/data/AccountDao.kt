package com.philexliveprojects.ordeist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT id FROM accounts WHERE phone_number = :login AND password = :password")
    fun loginAccount(login: String, password: String): Flow<Int?>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccount(id: Int): Flow<Account>

    @Insert
    suspend fun createAccount(value: Account)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccount(id: Int)

    @Update
    suspend fun updateAccount(value: Account)
}
