package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UserPreference
import compose.notezz.model.UsernameandPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter",
    "CoroutineCreationDuringComposition"
)
@Composable
fun LogInScreen(navController: NavController) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("ramniwash") }
    val password = remember { mutableStateOf("2rxbjjbd") }
    var stateOfLoginButton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)
    var loginStatus by remember { mutableStateOf("") }
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.h5,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 15.dp, top = 59.dp)
        )
        Divider()
        Text(
            text = "Login below to see your old notes.",
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
            fontSize = 18.sp
            // color = Color(R.color.textColor)
        )

        Text(
            text = "Username",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontWeight = FontWeight.Bold,
            //  color = Color(R.color.textColor)
        )
        OutlinedTextField(
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
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.padding(top = 15.dp))


        Button(
            onClick = { stateOfLoginButton.value = true },
            // colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
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
        Spacer(modifier = Modifier.padding(bottom = 12.dp))
        Text(
            "Forgot Password?",
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.blue)
        )




        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            "Don't have an account?",
            modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            // color = Color(R.color.textColor)
        )

        Button(
            onClick = {navController.navigate("signUp")},
            //  colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
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

                var Token = logInResponseData.data!!.token

                scope.launch {
                    dataStore.saveLoginStatus(Token)
                }

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
