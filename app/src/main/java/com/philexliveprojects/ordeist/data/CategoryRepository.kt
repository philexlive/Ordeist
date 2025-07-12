package com.philexliveprojects.ordeist.data

import kotlinx.coroutines.flow.Flow


class CategoryRepository(private val dao: CategoryDao) {
    fun getCategoriesList(): Flow<List<Category>> = dao.getList()

    fun getCategory(id: Int): Flow<Category> = dao.getById(id)

    suspend fun addCategory(value: Category) = dao.add(value)

    suspend fun deleteCategory(value: Category) = dao.delete(value)
}