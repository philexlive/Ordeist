package com.philexliveprojects.ordeist.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserPreferencesRepository {
    fun getAccountId(): Flow<Int>

    suspend fun addAccountId(id: Int)

    suspend fun removeAccountId()
}

class UserPreferencesRepositoryImpl(private val context: Context) : UserPreferencesRepository {
    override fun getAccountId(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[ACCOUNT_ID] ?: -1
    }

    override suspend fun addAccountId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_ID] = id
        }
    }

    override suspend fun removeAccountId() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCOUNT_ID)
        }
    }

    companion object {
        private val ACCOUNT_ID = intPreferencesKey("account_id")
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
