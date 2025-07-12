package com.philexliveprojects.ordeist.ui.newcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.Category
import com.philexliveprojects.ordeist.data.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IncorrectCategoryException(message: String) : Exception(message)

class NewCategoryViewModel(val repository: CategoryRepository) : ViewModel() {
    private var _categoryState = MutableStateFlow(Category(name = ""))

    val category = _categoryState.asStateFlow()

    fun changeCategoryName(value: String) {
        var tempValue = value

        if (value.length >= 50)
            tempValue = value.substring(0, 50)

        _categoryState.value = _categoryState.value.copy(name = tempValue)
    }

    fun addNewCategory() {
        if (category.value.name.isEmpty())
            throw IllegalArgumentException("Category name cannot be empty")

        val newCategory = category.value.copy()

        viewModelScope.launch {

            repository.addCategory(newCategory)
        }
    }
}

