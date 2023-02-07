package compose.notezz.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.notezz.screens.*
import kotlin.system.exitProcess

@Composable
fun NavigationNavController() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
            BackHandler() {

            }
        }

        composable("signUp") {
            SignUpScreen(navController = navController)
        }

        composable("logIn") {
            LogInScreen(navController = navController)
            BackHandler() {
                exitProcess(1)
            }
        }

        composable("listOfNotes/{token}") {
            HomeScreenListOfNotes(
                it.arguments?.getString("token").toString(),
                navController = navController
            )
            BackHandler() {
            exitProcess(1)
            }
        }

        composable("addNotes/{token}/{title}/{body}/{id}/{status}/{created}/{updated}/{userId}") {
            AddEditScreen(
                it.arguments?.getString("token").toString(),
                it.arguments?.getString("title").toString(),
                it.arguments?.getString("body").toString(),
                it.arguments?.getString("id").toString(),
                it.arguments?.getString("status").toString(),
                it.arguments?.getString("created").toString(),
                it.arguments?.getString("updated").toString(),
                it.arguments?.getString("userId")!!.toString(),
                navController = navController
            )

        }

        composable("forgotPassword/{email}") {
            ForgotPassword(navController = navController)
        }

        composable("updateAccount/{token}") {
            Account(
                it.arguments?.getString("token").toString(),
                navController = navController
            )

        }
    }

}
