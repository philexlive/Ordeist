package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow


interface CategoryRepository {
    fun getCategoriesList(): Flow<List<Category>>

    fun getCategory(value: String): Flow<Category?>

    suspend fun categoryExists(value: String): Boolean

    suspend fun addCategory(value: Category)

    suspend fun deleteCategory(value: Category)
}

// TODO: Bug coroutines leak in tests
class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override fun getCategoriesList(): Flow<List<Category>> = dao.getList()

    override fun getCategory(value: String): Flow<Category?> = dao.get(value)

    override suspend fun categoryExists(value: String): Boolean = dao.exists(value)

    override suspend fun addCategory(value: Category) = dao.add(value)

    override suspend fun deleteCategory(value: Category) = dao.delete(value)
}