package compose.notezz.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "SuspiciousIndentation"
)
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
                Log.d("tokenFound", "Fetched in welcome screen token: " + token)

                if (token.equals("loggedOut")) {
                    Log.d("tokenFound", "Redirecting to login: loggedOut")
                    navController.navigate("logIn")
                }else{
                    Log.d("tokenFound", "Sending to listOfNotes with: $token")
                    navController.navigate("listofNotes/$token")
                   // navController.navigate("logIn")

                }
            }.await()

        }
    }

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(id = compose.notezz.R.drawable.spalsh), contentDescription ="")
    }



    /*  val infiniteTransition = rememberInfiniteTransition()
      val heartSize by infiniteTransition.animateFloat(
          initialValue = 300.0f,
          targetValue = 250.0f,
          animationSpec = infiniteRepeatable(animation = tween(900, 900, FastOutLinearInEasing), repeatMode = RepeatMode.Reverse)
      )
      Image(painter = painterResource(id = compose.notezz.R.drawable.logo),
          contentDescription = "logo",
          modifier = Modifier.size(heartSize.dp)
      )*/

}
