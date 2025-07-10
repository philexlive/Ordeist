package com.philexliveprojects.ordeist.ui.neworder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.SLIDING_SPEED
import com.philexliveprojects.ordeist.ui.AppViewModelProvider
import com.philexliveprojects.ordeist.ui.Categories
import com.philexliveprojects.ordeist.ui.utils.CategoryBox
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewOrderScreen(
    onNavigateBack: () -> Unit,
    viewModel: NewOrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var isCategorySelectionOpened by remember { mutableStateOf(false) }

    val state by viewModel.newOrderState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val snackbarText = stringResource(R.string.new_order_added)

    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.new_order),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
                fontSize = 24.sp
            )
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                CategoryBox(
                    text = state.category.ifEmpty {
                        stringResource(R.string.choose_category)
                    },
                    onClick = { isCategorySelectionOpened = true },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.spacer)))

                TextField(
                    value = state.clientName,
                    onValueChange = viewModel::onClientNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.client_name))
                    }
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.spacer)))

                Text(
                    text = stringResource(R.string.contacts)
                )

                TextField(
                    value = state.phoneNumber,
                    onValueChange = viewModel::onPhoneNumberChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.client_phone_number))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )

                TextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.client_email))
                    }
                )

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel_order))
                    }

                    Button(
                        onClick = {
                            viewModel.addOrder()
                            onNavigateBack()
                            scope.launch {
                                snackbarHostState.showSnackbar(snackbarText)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = viewModel.allRequiredDone()
                    ) {
                        Text(stringResource(R.string.add_order))
                    }
                }
            }
        }
    }
    AnimatedVisibility(
        visible = isCategorySelectionOpened,
        enter = slideInHorizontally { SLIDING_SPEED } + fadeIn(),
        exit = slideOutHorizontally { SLIDING_SPEED } + fadeOut()
    ) {
        CategorySelection(
            onCategory = {
                viewModel.setCategory(it)
                isCategorySelectionOpened = false
            },
            onNavigateBack = {
                isCategorySelectionOpened = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelection(
    onCategory: (String) -> Unit = { },
    onNavigateBack: () -> Unit = { }
) {
    Surface(Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
            TopAppBar(
                title = { Text(stringResource(R.string.choose_category)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                }
            )

            Categories.entries.forEach {
                val label = stringResource(it.label)
                CategoryBox(
                    modifier = Modifier.fillMaxWidth(),
                    text = label,
                    onClick = { onCategory(label) }
                )
            }
        }
    }
}