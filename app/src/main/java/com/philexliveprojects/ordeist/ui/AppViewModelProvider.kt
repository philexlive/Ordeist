package com.philexliveprojects.ordeist.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.philexliveprojects.ordeist.OrdeistApplication
import com.philexliveprojects.ordeist.ui.home.HomeViewModel
import com.philexliveprojects.ordeist.ui.newcategory.NewCategoryViewModel
import com.philexliveprojects.ordeist.ui.neworder.NewOrderViewModel
import com.philexliveprojects.ordeist.ui.order.OrderViewModel
import com.philexliveprojects.ordeist.ui.signin.SignInViewModel
import com.philexliveprojects.ordeist.ui.signup.SignUpViewModel
import com.philexliveprojects.ordeist.ui.userprofile.UserProfileViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SignInViewModel(
                profPrintApplication().container.accountRepository,
                profPrintApplication().container.userPreferencesRepository
            )
        }

        initializer {
            SignUpViewModel(profPrintApplication().container.accountRepository)
        }

        initializer {
            HomeViewModel(profPrintApplication().container.orderRepository)
        }

        initializer {
            NewOrderViewModel(
                profPrintApplication().container.orderRepository,
                profPrintApplication().container.categoryRepository
            )
        }

        initializer {
            UserProfileViewModel(
                profPrintApplication().container.accountRepository,
                profPrintApplication().container.userPreferencesRepository
            )
        }
        initializer {
            OrderViewModel(
                profPrintApplication().container.orderRepository,
                createSavedStateHandle()
            )
        }
        initializer {
            NewCategoryViewModel(profPrintApplication().container.categoryRepository)
        }
    }

    fun CreationExtras.profPrintApplication(): OrdeistApplication =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OrdeistApplication)
}