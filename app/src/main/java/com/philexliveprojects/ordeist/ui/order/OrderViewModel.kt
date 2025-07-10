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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class OrderViewModel(
    private val repository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val order = repository.getOrder(savedStateHandle.toRoute<Routes.Order>().id)
        .filterNotNull()
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

    suspend fun removeOrder() = withContext(Dispatchers.IO) {
        repository.deleteOrder(order.value)
    }
}
