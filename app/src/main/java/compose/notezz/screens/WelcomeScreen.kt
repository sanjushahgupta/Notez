package compose.notezz.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import compose.notezz.model.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun WelcomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)




        scope.launch {
            dataStore.loginStatus.collect {
               val token = it.toString()
                async {
                    delay(2000)
                    if (token.equals("0")) {

                        navController.navigate("login")
                    }else{
                        navController.navigate("listofNotes/$token")
                    }
                }.await()

            }
        }



        Scaffold(topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray


            ) {

                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    tint = Color.Cyan,
                    painter = painterResource(id = compose.notezz.R.drawable.logo),
                    contentDescription = "logo"
                )
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
            }
        }) {}
        Box(
            modifier = Modifier
                .aspectRatio(1.0f)
                .clip(CircleShape)
        ) {

            // Vinyl background
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(0.5f),
                painter = painterResource(id = compose.notezz.R.drawable.landingp),
                contentDescription = ""
            )

            // Vinyl lights effect
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id =  compose.notezz.R.drawable.landingp),
                contentDescription = "",
            )

            // Vinyl 'album' cover
            // For using with Coil or Glide, wrap into surface with shape
            Image(
                modifier = Modifier
                    .fillMaxSize(0.3f)
                    .align(Alignment.Center)
                    .rotate(0.5f)
                    .aspectRatio(1.0f)
                    .clip(CircleShape),
                painter = painterResource(compose.notezz.R.drawable.landingp),
                contentDescription = "")

        }
}

      /*  Box(modifier = Modifier.padding(top = 50.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 10.dp, start = 10.dp),

                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Simple & free note taking " + "tool",
                    textAlign = TextAlign.Center,
                    style = typography.h4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Take notes online without worrying about installing, customizing, learning, getting-used-to" + "paying, security, privacy or anything else basically. ",
                    modifier = Modifier.padding(bottom = 12.dp)

                )
                Spacer(Modifier.padding(bottom = 20.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { navController.navigate("logIn") },
                        // colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_login_24),
                            contentDescription = "login"
                        )
                        Text(text = "Log In", color = Color.White)

                    }

                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                    Button(
                        onClick = { navController.navigate("signUp") },
                        // colors = ButtonDefaults.buttonColors(Color.Blue),
                        modifier = Modifier.wrapContentSize(),

                        ) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_person_24),
                            contentDescription = ""
                        )

                        Text(text = "Sign Up", color = Color.White)

                    }


                }
                Spacer(Modifier.padding(bottom = 28.dp))

                Image(
                    painter = painterResource(id = compose.notezz.R.drawable.landingp),
                    contentDescription = ""
                )
            }
        }*/
