package com.philexliveprojects.ordeist.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.philexliveprojects.ordeist.ui.home.HomeScreen
import com.philexliveprojects.ordeist.ui.newcategory.NewCategoryScreen
import com.philexliveprojects.ordeist.ui.neworder.NewOrderScreen
import com.philexliveprojects.ordeist.ui.order.OrderScreen
import com.philexliveprojects.ordeist.ui.signin.SignInScreen
import com.philexliveprojects.ordeist.ui.signup.SignUpScreen
import com.philexliveprojects.ordeist.ui.userprofile.UserProfileScreen
import kotlinx.serialization.Serializable

interface Routes {
    @Serializable
    object SignIn

    @Serializable
    object SignUp

    @Serializable
    object Profile

    @Serializable
    object Home

    @Serializable
    data class Order(val id: Int)

    @Serializable
    object NewOrder

    @Serializable
    object NewCategory

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdeistApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController,
            startDestination = Routes.SignIn,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Routes.SignIn> {
                SignInScreen(
                    onSignIn = {
                        navController.navigate(
                            route = Routes.Home,
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(Routes.SignIn, true)
                                .build()
                        )
                    },
                    onSignUp = { navController.navigate(Routes.SignUp) }
                )
            }

            composable<Routes.SignUp> {
                SignUpScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable<Routes.Home> {
                HomeScreen(
                    onAddOrder = { navController.navigate(Routes.NewOrder) },
                    onOrder = { navController.navigate(Routes.Order(it)) },
                    onProfile = { navController.navigate(Routes.Profile) },
                    onAddCategory =  { navController.navigate(Routes.NewCategory) }
                )
            }

            composable<Routes.Profile> {
                UserProfileScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onLogOut = {
                        navController.navigate(Routes.SignIn) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Routes.Order> {
                OrderScreen(onNavigateBack = { navController.navigateUp() })
            }

            composable<Routes.NewOrder> {
                NewOrderScreen(onNavigateBack = { navController.navigateUp() })
            }

            composable<Routes.NewCategory> {
                NewCategoryScreen()
            }
        }
    }
}

