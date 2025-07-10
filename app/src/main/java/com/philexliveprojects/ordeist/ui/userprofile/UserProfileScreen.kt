package com.philexliveprojects.ordeist.ui.userprofile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    onNavigateBack: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: UserProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val userProfile by viewModel.userProfile.collectAsState()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            Modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .padding(innerPadding)
        ) {
            UserCard(
                userName = userProfile.name,
                userEmail = userProfile.email,
                userPhoneNumber = userProfile.phoneNumber,
                onPasswordChange = { showDialog = true },
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center)
                    .fillMaxWidth()
                    .weight(1f)
            )

            Button(
                onClick = { onLogOut() },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }

    if (showDialog) {
        val newPass by viewModel.newPass.collectAsState()
        val snackbarText = stringResource(R.string.password_changed)
        BasicAlertDialog(
            onDismissRequest = { showDialog = false },
            content = {
                Surface(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_small))
                ) {
                    Column(
                        Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                        verticalArrangement = Arrangement.spacedBy(
                            dimensionResource(R.dimen.padding_small)
                        )
                    ) {
                        TextField(
                            value = newPass.password,
                            onValueChange = viewModel::onPassChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.new_password)) },
                            visualTransformation = PasswordVisualTransformation()
                        )

                        TextField(
                            value = newPass.repeat,
                            onValueChange = viewModel::onRepeatChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.repeat)) },
                            keyboardActions = KeyboardActions { defaultKeyboardAction(ImeAction.Done) },
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Button(
                            onClick = {
                                viewModel.changePassword()
                                showDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(snackbarText)
                                }
                            },
                            enabled = viewModel.checkPassword(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.change_password))
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun UserCard(
    userName: String,
    userEmail: String,
    userPhoneNumber: String,
    onPasswordChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_large))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = userName,
                fontSize = 24.sp,
            )

            ContactBox(
                text = userEmail,
                label = R.string.email,
                modifier = Modifier.fillMaxWidth()
            )

            ContactBox(
                text = userPhoneNumber,
                label = R.string.phone_number,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            Button(
                onClick = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(stringResource(R.string.change_password))
            }
        }
    }
}


@Composable
private fun ContactBox(text: String, @StringRes label: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_small)))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(label),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            fontSize = 8.sp
        )

        Text(
            text = text,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        )
    }
}
