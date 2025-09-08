package com.example.stockcontrol


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.stockcontrol.screens.AddProduct
import com.example.stockcontrol.screens.HomeScreen
import com.example.stockcontrol.screens.LoginScreen
import com.example.stockcontrol.screens.ProductDetailScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.room.Room
import com.example.stockcontrol.data.local.AppDatabase

import com.example.stockcontrol.repository.ItemRepository
import com.example.stockcontrol.ui.theme.StockControlTheme
import com.example.stockcontrol.viewModel.AuthViewModel
import com.example.stockcontrol.viewModel.ProductViewModel
import com.example.stockcontrol.viewModel.factory.ProductViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    val GOOGLE_SIGN_IN_REQUEST = 100
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val database = AppDatabase.getDatabase(applicationContext)

            val firestore = FirebaseFirestore.getInstance()
            val productDao = database.productDao()
            val repository = ItemRepository(productDao, firestore)
            val authViewModel: AuthViewModel = viewModel()
            nav(authViewModel, repository)
        }
        /* @Composable
     fun nav(d: String) {
         val navController = rememberNavController()
         navController.navigate(d)
     }*/
    }
}

@Composable
fun nav(authViewModel: AuthViewModel, repository: ItemRepository) {
    StockControlTheme {
        val productViewModel: ProductViewModel = viewModel(
            factory = ProductViewModelFactory(repository)
        )
        val navController = rememberNavController()
        val startDestination = if (authViewModel.currentUser.value != null) {
            "home"
        } else {
            "login"
        }
        NavHost(navController = navController, startDestination = startDestination) {

            composable("login") {
                LoginScreen(navController, authViewModel)
            }
            composable("home") {
                HomeScreen(
                    onAddClick = { navController.navigate("AddProduct") },
                    onItemClick = { productId ->
                        navController.navigate("productScreen/${productId}")
                    },
                    onExit = { authViewModel.logOut() },
                    productViewModel
                )
            }
            composable("AddProduct") {
                AddProduct(navController = navController, productViewModel)
            }
            composable(
                "productScreen/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                val product =
                    productViewModel.allProducts.value?.find { it.id == productId?.toInt() }

                if (productId != null) {
                    if (product != null) {
                        ProductDetailScreen(
                            product = product,
                            onUpdateClick = {},
                            navController = navController,
                            productViewModel = productViewModel
                        )
                    } else {

                        Text("Producto no encontrado")
                    }
                } else {

                    Text("ID de producto inválido")
                }
            }

        }
    }
}