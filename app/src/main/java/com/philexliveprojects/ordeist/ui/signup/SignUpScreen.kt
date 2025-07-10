package com.philexliveprojects.ordeist.ui.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.ui.AppViewModelProvider

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.signUpUiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text(stringResource(R.string.sign_up_title)) },
            navigationIcon = {
                IconButton(onNavigateBack) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .weight(1f)
                .wrapContentSize(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large))
        ) {
            SignUpTextField(
                uiState.firstName, viewModel::onFirstNameChange, R.string.first_name, false
            )

            SignUpTextField(
                uiState.secondName, viewModel::onSecondNameChange, R.string.second_name, false
            )

            SignUpTextField(
                uiState.phoneNumber,
                viewModel::onPhoneNumberChange,
                R.string.phone_number,
                true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                )
            )

            SignUpTextField(
                uiState.email,
                viewModel::onEmailChange,
                R.string.email,
                false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            SignUpTextField(
                uiState.password, viewModel::onPasswordChange, R.string.password, true,
                visualTransformation = PasswordVisualTransformation()
            )

            SignUpTextField(
                uiState.repeat, viewModel::onPassRepeatChange, R.string.repeat, true,
                keyboardActions = KeyboardActions { defaultKeyboardAction(ImeAction.Done) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Button(
            onClick = {
                viewModel.signUp()
                onNavigateBack()
            },
            enabled = viewModel.allRequiredDone(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(stringResource(R.string.sign_up))
        }
    }
}


@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderText: Int,
    required: Boolean,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions { defaultKeyboardAction(ImeAction.Next) },
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        placeholder = { Text(stringResource(placeholderText) + if (required) "*" else "") }
    )
}