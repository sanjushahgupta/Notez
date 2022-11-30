package compose.notezz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.notezz.screens.*

@Composable

fun navigationNavController(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination ="welcome" ){
        composable("welcome"){
            WelcomeScreen(navController = navController)
        }

        composable("signUp"){
            SignUpScreen(navController = navController)
        }

        composable("logIn"){
            LogInScreen(navController = navController)
        }

        composable("listofNotes/{token}"){
            HomeScreenListOfNotes(it.arguments?.getString("token").toString(), navController= navController)
        }

        composable("addNotes/{token}"){
            AddandEditScreen(it.arguments?.getString("token").toString(), navController = navController)
        }


    }

}
