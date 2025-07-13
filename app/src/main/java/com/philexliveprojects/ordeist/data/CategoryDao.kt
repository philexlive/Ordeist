package com.philexliveprojects.ordeist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getList(): Flow<List<Category>>

    @Query("SELECT EXISTS(SELECT * FROM categories WHERE label = :value)")
    suspend fun exists(value: String): Boolean

    @Query("SELECT * FROM categories WHERE label = :label")
    fun get(label: String): Flow<Category?>

    @Insert
    suspend fun add(category: Category)
    @Delete
    suspend fun delete(value: Category)
}
