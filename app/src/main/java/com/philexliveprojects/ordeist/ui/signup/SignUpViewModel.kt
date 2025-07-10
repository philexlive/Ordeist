package com.philexliveprojects.ordeist.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.Account
import com.philexliveprojects.ordeist.data.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    fun onSecondNameChange(value: String) =
        _signUpUiState.update { signUpUiState.value.copy(secondName = value) }

    fun onFirstNameChange(value: String) =
        _signUpUiState.update { signUpUiState.value.copy(firstName = value) }

    fun onPhoneNumberChange(value: String) =
        _signUpUiState.update { signUpUiState.value.copy(phoneNumber = value) }

    fun onEmailChange(value: String) {
        _signUpUiState.update { signUpUiState.value.copy(email = value) }
    }

    fun onPasswordChange(value: String) =
        _signUpUiState.update { signUpUiState.value.copy(password = value) }

    fun onPassRepeatChange(value: String) =
        _signUpUiState.update { signUpUiState.value.copy(repeat = value) }

    fun allRequiredDone() = checkPhoneNumber()
            && checkPassword()
            && checkPassRepeat()

    fun signUp() = viewModelScope.launch(Dispatchers.IO) {
        accountRepository.createAccount(
            Account(
                secondName = signUpUiState.value.secondName,
                firstName = signUpUiState.value.firstName,
                phoneNumber = signUpUiState.value.phoneNumber,
                email = signUpUiState.value.email,
                password = signUpUiState.value.password
            )
        )
    }

    private fun checkPhoneNumber() =
        with(signUpUiState.value) {
            with(phoneNumber) {
                matches(Regex("^[+](?:[0-9\\-()/.]\\s?){6,15}[0-9]$"))
            }

        }

    private fun checkPassword() = signUpUiState.value.password.length >= 6
    private fun checkPassRepeat() = signUpUiState.value.let { it.repeat == it.password }
}

data class SignUpUiState(
    val secondName: String = "",
    val firstName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val repeat: String = "",
    val filled: Boolean = false
)