package compose.notezz.navigation

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
        }

        composable("signUp") {
            SignUpScreen(navController = navController)
        }

        composable("logIn") {
            LogInScreen(navController = navController)
        }

        composable("listofNotes/{token}") {
            HomeScreenListOfNotes(
                it.arguments?.getString("token").toString(),
                navController = navController
            )
        }

        composable("addNotes/{token}/{title}/{body}/{id}/{status}/{created}/{updated}/{userId}") {
            AddandEditScreen(
                it.arguments?.getString("token").toString(),
                it.arguments?.getString("title").toString(),
                it.arguments?.getString("body").toString(),
                it.arguments?.getInt("id")!!.toInt(),
                it.arguments?.getString("status").toString(),
                it.arguments?.getString("created").toString(),
                it.arguments?.getString("updated").toString(),
                it.arguments?.getInt("userId")!!.toInt(),
                navController = navController
            )
        }


    }

}
