package com.philexliveprojects.ordeist.ui.newcategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.Category
import com.philexliveprojects.ordeist.data.CategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

sealed interface NewCategoryResult {
    object Success : NewCategoryResult
    class Error(val message: String) : NewCategoryResult
}

class NewCategoryViewModel(val repository: CategoryRepository) : ViewModel() {
    private var _categoryState = MutableStateFlow(Category(label = ""))

    val category = _categoryState.asStateFlow()

    fun changeCategoryName(value: String): NewCategoryResult {
        if (value.length >= 50)
            return NewCategoryResult.Error("Category name cannot be longer than 50 characters")

        _categoryState.value = _categoryState.value.copy(label = value)

        return NewCategoryResult.Success
    }

    // TODO: Bug coroutines leak in tests
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addNewCategory(): NewCategoryResult {
        val label = category.value.label.trim()

        if (label.isEmpty()) {
            Log.e("NewCategoryViewModel", "Label cannot be empty")
        }

        val result = viewModelScope.async {
            repository.addCategory(category.value.copy(category.value.label.trim()))
        }

        return result.getCompleted()
    }
}

