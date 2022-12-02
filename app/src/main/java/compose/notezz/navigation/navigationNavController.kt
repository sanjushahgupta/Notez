package compose.notezz.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.notezz.screens.*

@Composable

fun navigationNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
            BackHandler() {
                //
            }
        }

        composable("signUp") {
            SignUpScreen(navController = navController)
            BackHandler() {
                //
            }
        }

        composable("logIn") {
            LogInScreen(navController = navController)
            BackHandler() {
                //
            }
        }

        composable("listofNotes/{token}") {
            HomeScreenListOfNotes(
                it.arguments?.getString("token").toString(),
                navController = navController
            )
            BackHandler() {
                //
            }
        }

        composable("addNotes/{token}/{title}/{body}/{id}/{status}/{created}/{updated}/{userId}") {
            AddandEditScreen(
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


    }

}
