package com.philexliveprojects.ordeist.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.AccountRepository
import com.philexliveprojects.ordeist.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val accountRepository: AccountRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    fun onLoginEnter(value: String) {
        clearError()
        _uiState.update {
            uiState.value.copy(login = value)
        }
    }

    fun onPasswordEnter(value: String) {
        clearError()
        _uiState.update {
            uiState.value.copy(password = value)
        }
    }

    private fun clearError() {
        _uiState.update {
            uiState.value.copy(isError = false)
        }
    }

    fun signIn(signInLambda: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val id: Int? = accountRepository.loginAccount(
            login = uiState.value.login,
            password = uiState.value.password
        ).firstOrNull()

        if (id == null) {
            _uiState.update {
                uiState.value.copy(isError = true)
            }
            return@launch
        }

        preferences.addAccountId(id)

        withContext(Dispatchers.Main) {
            signInLambda()
        }
    }
}

data class SignInState(
    val login: String = "",
    val password: String = "",
    val isError: Boolean = false
)

