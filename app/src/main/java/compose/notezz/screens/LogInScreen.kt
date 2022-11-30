package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UsernameandPassword
import kotlinx.coroutines.delay

@SuppressLint("SuspiciousIndentation")
@Composable
fun LogInScreen(navController: NavController) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("ramniwash") }
    val password = remember { mutableStateOf("2rxbjjbd") }
    var stateOfLoginButton = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth(), Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 15.dp, top = 15.dp)
            )
            Divider()
            Text(
                text = "Login below to see your old notes.",
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
                fontWeight = FontWeight.Bold,
                // color = Color(R.color.textColor)
            )

            Text(
                text = "Username",
                modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
                fontWeight = FontWeight.Bold,
                //  color = Color(R.color.textColor)
            )
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                modifier = Modifier
                    .fillMaxWidth()

            )

            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            Text(
                text = "Password",
                modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
                fontWeight = FontWeight.Bold,
                //  color = Color(R.color.textColor)
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier
                    .fillMaxWidth()

            )

            Spacer(modifier = Modifier.padding(top = 15.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { stateOfLoginButton.value = true },
                    shape = RectangleShape,
                    //colors = ButtonDefaults.buttonColors(contentColor = Color.White, backgroundColor = Color(
                    //   R.color.buttonColor))
                ) {
                    Icon(
                        tint = Color.White,
                        painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_login_24),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "Forgot Password?",
                    fontWeight = FontWeight.Bold,
                    // color = colorResource(id = R.color.blueColor),
                )


            }

            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            Text(
                "Don't have an account?",
                modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
                fontWeight = FontWeight.Bold,
                // color = Color(R.color.textColor)
            )

            Button(
                onClick = {},
                // colors = ButtonDefaults.buttonColors(Color(R.color.buttonColor))

            ) {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_person_24),
                    contentDescription = "",

                    )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    "Register",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }


            if (stateOfLoginButton.value == true) {
                val usernameandPassword = UsernameandPassword(username.value, password.value)
                var logInResponseData =
                    produceState<DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception>>(
                        initialValue = DataOrException(
                            loading = true
                        )
                    ) {
                        value = authViewModel.logIn(usernameandPassword)
                    }.value

                if (logInResponseData.loading == true) {
                    CircularProgressIndicator()

                } else if (logInResponseData.data != null) {

                    val Token = logInResponseData.data!!.token

                    LaunchedEffect(Unit) {
                        delay(200)
                        navController.navigate("listofNotes/$Token")
                    }

                } else {
                    Text(text = "Exception:" + logInResponseData.data)

                }

            }
        }
    }
}
