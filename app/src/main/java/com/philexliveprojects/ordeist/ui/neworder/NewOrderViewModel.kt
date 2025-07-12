package com.philexliveprojects.ordeist.ui.neworder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.CategoryRepository
import com.philexliveprojects.ordeist.data.Order
import com.philexliveprojects.ordeist.data.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewOrderViewModel(
    private val orderRepository: OrderRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _newOrderState = MutableStateFlow(NewOrder())
    val newOrderState = _newOrderState.asStateFlow()

    val categories = categoryRepository.getCategoriesList().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onClientNameChange(value: String) = _newOrderState.update {
        newOrderState.value.copy(clientName = value)
    }

    fun onPhoneNumberChange(value: String) = _newOrderState.update {
        newOrderState.value.copy(phoneNumber = value)
    }

    fun onEmailChange(value: String) = _newOrderState.update {
        newOrderState.value.copy(email = value)
    }

    fun allRequiredDone() = checkClientNameEmpty()
            && checkCategoryIsEmpty()
            && checkPhoneNumberOrEmailEmpty()

    fun setCategory(value: String) {
        _newOrderState.update {
            newOrderState.value.copy(category = value)
        }
    }

    private fun checkClientNameEmpty() = _newOrderState.value.clientName.isNotEmpty()

    private fun checkCategoryIsEmpty() = _newOrderState.value.category.isNotEmpty()

    private fun checkPhoneNumberOrEmailEmpty() =
        _newOrderState.value.phoneNumber.isNotEmpty() || _newOrderState.value.email.isNotEmpty()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrder() = viewModelScope.launch(Dispatchers.IO) {
        orderRepository.addOrder(
            newOrderState.map {
                Order(
                    clientName = it.clientName,
                    category = it.category,
                    phoneNumber = it.phoneNumber,
                    email = it.email,
                    date = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                )
            }.first()
        )
    }
}

data class NewOrder(
    val clientName: String = "",
    val category: String = "",
    val phoneNumber: String = "",
    val email: String = ""
)
