package com.philexliveprojects.ordeist.ui.newcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.Category
import com.philexliveprojects.ordeist.data.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

sealed interface NewCategoryResult {
    object Success : NewCategoryResult
    class Error(val message: String) : NewCategoryResult
}

class NewCategoryViewModel(val repository: CategoryRepository) : ViewModel() {
    private var _category = MutableStateFlow(Category(label = ""))

    val category = _category.asStateFlow()

    val categoriesList = repository.getCategoriesList().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun changeCategoryName(value: String): NewCategoryResult {
        if (value.length >= 50)
            return NewCategoryResult.Error("Category name cannot be longer than 50 characters")

        _category.value = _category.value.copy(label = value)

        return NewCategoryResult.Success
    }

    // TODO: Bug coroutines leak in tests
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addNewCategory(): NewCategoryResult = runBlocking(Dispatchers.IO) {
        if (category.value.label.isEmpty()) {
            return@runBlocking NewCategoryResult.Error("Category name cannot be empty")
        }
        if (repository.categoryExists(category.value.label.trim())) {
            return@runBlocking NewCategoryResult.Error("Category already exists")
        }

        repository.addCategory(category.value)

        return@runBlocking NewCategoryResult.Success
    }
}

