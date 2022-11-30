package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WelcomeScreen(navController: NavController) {

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
        ) {
            Text(text = "notezz!", color = Color.White)
            // Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more", )
        }
    }) {}

    Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("signUp") }) {
                Text(text = "Sign Up")

            }
            Button(onClick = { navController.navigate("logIn") }) {
                Text(text = "Log In")

            }


        }


    }
}
