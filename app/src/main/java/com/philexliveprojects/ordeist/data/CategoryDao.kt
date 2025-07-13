package com.philexliveprojects.ordeist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Insert
    suspend fun add(category: Category)

    @Query("SELECT * FROM categories")
    fun getList(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE label = :label")
    fun get(label: String): Flow<Category?>

    @Delete
    suspend fun delete(value: Category)
}
