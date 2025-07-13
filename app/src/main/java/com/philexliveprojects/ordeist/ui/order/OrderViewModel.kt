package com.philexliveprojects.ordeist.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.philexliveprojects.ordeist.data.Order
import com.philexliveprojects.ordeist.data.OrderRepository
import com.philexliveprojects.ordeist.ui.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val order = repository.getOrder(savedStateHandle.toRoute<Routes.Order>().id)
        .stateIn(
            scope = viewModelScope,
            initialValue = Order(
                category = "",
                clientName = "",
                phoneNumber = "",
                email = "",
                date = ""
            ),
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun removeOrder() = viewModelScope.launch(Dispatchers.IO) {
        if (order.value == null) return@launch

        repository.deleteOrder(order.value!!)
    }
}
