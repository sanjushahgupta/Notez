package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = Color.DarkGray


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

    Box(modifier = Modifier.padding(top = 50.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 10.dp, start = 10.dp),

            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Simple & free note taking " +
                        "tool",
                textAlign = TextAlign.Center,
                style = typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Take notes online without worrying about installing, customizing, learning, getting-used-to" +
                        "paying, security, privacy or anything else basically. ",
                modifier = Modifier.padding(bottom = 12.dp)

            )
            Spacer(Modifier.padding(bottom = 20.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = { navController.navigate("logIn") },
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
    }

}
