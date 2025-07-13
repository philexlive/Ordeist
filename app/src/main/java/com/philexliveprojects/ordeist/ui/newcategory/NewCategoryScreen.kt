package com.philexliveprojects.ordeist.ui.newcategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.data.Category
import com.philexliveprojects.ordeist.data.CategoryRepository
import com.philexliveprojects.ordeist.ui.AppViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun NewCategoryScreen(
    viewModel: NewCategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val categoryUiState by viewModel.category.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    NewCategoryContent(
        categoryUiState,
        viewModel::changeCategoryName,
        viewModel::addNewCategory,
        snackbarHostState
    )
}

@Composable
fun NewCategoryContent(
    categoryUiState: Category,
    changeCategoryLabel: (String) -> NewCategoryResult,
    addNewCategory: () -> NewCategoryResult,
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        val scope = rememberCoroutineScope()

        TextField(
            value = categoryUiState.label,
            onValueChange = { newLabel ->
                val result = changeCategoryLabel(newLabel)

                if (result is NewCategoryResult.Error) {
                    scope.launch {
                        snackbarHostState.showSnackbar(result.message)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.category_name)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    showSnackbar(
                        addNewCategory(),
                        scope,
                        snackbarHostState
                    )
                }
            ),
            singleLine = true
        )

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.CenterHorizontally))
            Button(
                onClick = {
                    showSnackbar(
                        addNewCategory(),
                        scope,
                        snackbarHostState
                    )

                },
                enabled = categoryUiState.label.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_category))
            }
        }
    }
}

private fun showSnackbar(
    result: NewCategoryResult,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    when (result) {
        is NewCategoryResult.Success -> {
            scope.launch {
                snackbarHostState.showSnackbar("New category just added!")
            }
        }

        is NewCategoryResult.Error -> {
            scope.launch {
                snackbarHostState.showSnackbar(result.message)
            }
        }
    }
}

@Composable
@Preview
fun NewCategoryContentPreview() {
    val database = remember {
        mutableListOf<Category>()
    }

    val viewModel: NewCategoryViewModel = viewModel {
        NewCategoryViewModel(
            object : CategoryRepository {
                override fun getCategoriesList(): Flow<List<Category>> {
                    return flowOf(database)
                }

                override fun getCategory(value: String): Flow<Category?> {

                    return flowOf(database.firstOrNull { it.label == value })
                }

                override suspend fun addCategory(value: Category): NewCategoryResult {
                    database.add(value)
                    return NewCategoryResult.Success
                }

                override suspend fun deleteCategory(value: Category) {
                    database.remove(value)
                }

            }
        )

    }

    val uiState by viewModel.category.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    NewCategoryContent(
        uiState,
        viewModel::changeCategoryName,
        viewModel::addNewCategory,
        snackbarHostState
    )
}
