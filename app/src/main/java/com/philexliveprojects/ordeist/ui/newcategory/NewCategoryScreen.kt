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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.philexliveprojects.ordeist.ui.AppViewModelProvider
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
    changeCategoryName: (String) -> Unit,
    addNewCategory: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        val scope = rememberCoroutineScope()

        TextField(
            value = categoryUiState.name,
            onValueChange = { newName ->
                changeCategoryName(newName)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.category_name)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    scope.launch {
                        try {
                            addNewCategory()
                            changeCategoryName("")
                        } catch (e: IncorrectCategoryException) {
                            snackbarHostState.showSnackbar(e.message ?: "Unexpected error")
                        }
                    }
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
                    scope.launch {
                        try {
                            addNewCategory()
                        } catch (e: IncorrectCategoryException) {
                            snackbarHostState.showSnackbar(e.message ?: "Unexpected error")
                        }
                    }
                },
                enabled = categoryUiState.name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_category))
            }
        }
    }
}

@Composable
@Preview
fun NewCategoryContentPreview() {
    var uiState by remember {
        mutableStateOf(Category(name = ""))
    }
    NewCategoryContent(
        categoryUiState = uiState,
        changeCategoryName = { uiState = uiState.copy(name = it) },
        addNewCategory = {},
        snackbarHostState = SnackbarHostState()
    )
}
