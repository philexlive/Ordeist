package com.philexliveprojects.ordeist.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.AccountRepository
import com.philexliveprojects.ordeist.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val accountRepository: AccountRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {
    val userProfile = preferences.getAccountId().map {
        val account = accountRepository.getAccount(it).first()
        UserProfile(
            name = "${account.firstName} ${account.secondName}",
            phoneNumber = account.phoneNumber,
            email = account.email.ifEmpty { "N/A" }
        )

    }.stateIn(
        scope = viewModelScope,
        initialValue = UserProfile(),
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000)
    )

    private val _newPass = MutableStateFlow(NewPass())
    val newPass = _newPass.asStateFlow()

    fun onPassChange(value: String) = _newPass.update {
        newPass.value.copy(password = value)
    }

    fun onRepeatChange(value: String) = _newPass.update {
        newPass.value.copy(repeat = value)
    }

    fun changePassword() = viewModelScope.launch(Dispatchers.IO) {
        if (!checkPassword()) {
            return@launch
        }

        val account = accountRepository.getAccount(preferences.getAccountId().first()).first()

        accountRepository.updateAccount(
            account.copy(
                password = newPass.value.password
            )
        )
    }

    fun checkPassword() = checkPasswordLength() && checkPassRepeat()

    private fun checkPasswordLength() = newPass.value.password.length >= 6

    private fun checkPassRepeat() = newPass.value.let { it.repeat == it.password }
}

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

data class NewPass(
    val password: String = "",
    val repeat: String = ""
)