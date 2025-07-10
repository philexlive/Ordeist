package com.philexliveprojects.ordeist.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.PASSWORD_MIN_SIZE
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.ui.AppViewModelProvider

private val CommonModifier = Modifier.fillMaxWidth()

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSignUp: () -> Unit = {},
    onSignIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.app_name)) }
        )

        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .weight(1f)
                .wrapContentSize(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {

            TextField(
                value = uiState.login,
                onValueChange = {
                    viewModel.onLoginEnter(it)
                },
                isError = uiState.isError,
                modifier = CommonModifier,
                keyboardActions = KeyboardActions { defaultKeyboardAction(ImeAction.Next) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                placeholder = { Text(stringResource(R.string.phone_number)) },
                singleLine = true,
                colors = TextFieldDefaults.colors().copy(
                    errorTextColor = MaterialTheme.colorScheme.error
                )
            )

            TextField(
                value = uiState.password,
                onValueChange = {
                    viewModel.onPasswordEnter(it)
                },
                modifier = CommonModifier,
                isError = uiState.isError,
                keyboardActions = KeyboardActions { defaultKeyboardAction(ImeAction.Done) },
                placeholder = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = TextFieldDefaults.colors().copy(
                    errorTextColor = MaterialTheme.colorScheme.error
                )
            )

            if (uiState.isError) {
                Text(
                    stringResource(R.string.incorrect_login),
                    Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(56.dp))

            OutlinedButton(
                onClick = onSignUp,
                modifier = CommonModifier,
            ) { Text(stringResource(R.string.sign_up)) }

            Button(
                onClick = {
                    viewModel.signIn {
                        onSignIn()
                    }
                },
                modifier = CommonModifier,
                enabled = checkTheBlankFilled(uiState.login, uiState.password)
            ) { Text(stringResource(R.string.sign_in)) }
        }
    }
}

private fun checkTheBlankFilled(login: String, password: String): Boolean =
    password.length >= PASSWORD_MIN_SIZE && login.isNotEmpty()
