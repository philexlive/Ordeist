package com.philexliveprojects.ordeist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philexliveprojects.ordeist.data.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(orderRepository: OrderRepository) : ViewModel() {
    val orders = orderRepository.getOrdersList().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        listOf()
    )
}
