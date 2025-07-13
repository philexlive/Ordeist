package com.philexliveprojects.ordeist.data

import com.philexliveprojects.ordeist.ui.newcategory.NewCategoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


interface CategoryRepository {
    fun getCategoriesList(): Flow<List<Category>>

    fun getCategory(value: String): Flow<Category?>

    suspend fun addCategory(value: Category): NewCategoryResult

    suspend fun deleteCategory(value: Category)
}

// TODO: Bug coroutines leak in tests
class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override fun getCategoriesList(): Flow<List<Category>> = dao.getList()

    override fun getCategory(value: String): Flow<Category?> = dao.get(value)

    override suspend fun addCategory(value: Category): NewCategoryResult {
        if (dao.get(value.label).first() != null) {
            return NewCategoryResult.Error("Category with label ${value.label} already exists")
        }

        dao.add(value)

        return NewCategoryResult.Success
    }

    override suspend fun deleteCategory(value: Category) = dao.delete(value)
}