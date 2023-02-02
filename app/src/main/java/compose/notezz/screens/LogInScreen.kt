package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UserPreference
import compose.notezz.model.UsernameandPassword
import compose.notezz.util.Dimension
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

@SuppressLint(
    "SuspiciousIndentation",
    "UnusedMaterialScaffoldPaddingParameter",
    "CoroutineCreationDuringComposition",
    "WrongConstant"
)

@Composable
fun LogInScreen(navController: NavController) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var stateOfLoginButton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)
    var loginStatus by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray
        ) {

            Icon(
                modifier = Modifier.padding(start = 10.dp),
                tint = colorResource(id = R.color.LogiTint),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )
            // Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}

    var focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .clickable(
                MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .padding(
                top = Dimension.height(value = 1f).dp, start = Dimension.height(value = 0.5f).dp
            )
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.h5,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp, top = 59.dp)
        )

        Divider()

        Text(
            text = "Login below to see your old notes.",
            modifier = Modifier.padding(bottom = 8.dp, top = Dimension.height(value = 0.8f).dp),
            fontSize = 18.sp,
        )

        Text(
            text = "Username",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp
        )


        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter username") } ,
            colors = TextFieldDefaults.outlinedTextFieldColors( focusedBorderColor = Color.Gray,unfocusedBorderColor = Color.Gray, cursorColor = Color.Black, backgroundColor = Color.White)
        )

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        Text(
            text = "Password",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp
            // fontWeight = FontWeight.Bold,
            //  color = Color(R.color.textColor)
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter password") } ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            )
        )


        Spacer(modifier = Modifier.padding(top = Dimension.height(value = 1.5f).dp))


        Button(
            onClick = { stateOfLoginButton.value = true
                focus.clearFocus()},
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
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

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))

        Text(
            "Forgot Password?",
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.blueish),
            modifier = Modifier.clickable { navController.navigate("forgotpassword/email") }
        )

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 3f).dp))

        Text(
            "Don't have an account?",
            modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            // color = Color(R.color.textColor)
        )

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))

        Divider()

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))

        Button(
            onClick = { navController.navigate("signUp")
                         focus.clearFocus()},
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
            //  colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_person_24),
                contentDescription = "",

                )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                "Register", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White
            )
        }


        if (stateOfLoginButton.value == true) {

            val usernameandPassword = UsernameandPassword(username.value, password.value)

            if (password.value.isEmpty() || username.value.isEmpty()) {
                val toast =
                    Toast.makeText(
                        LocalContext.current,
                        "Please fill out fields.",
                        Toast.LENGTH_SHORT
                    )
                toast.duration = 100
                toast.show()
                stateOfLoginButton.value = false
            } else if (username.value.length < 4) {
                val toast = Toast.makeText(
                    context,
                    "Length of a username must be of 4 characters or more.",
                    Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()
                stateOfLoginButton.value = false
            } else if (password.value.length < 6) {
                val toast = Toast.makeText(
                    context,
                    "Length of a password must be of 6 characters or more.",
                    Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()
                stateOfLoginButton.value = false

            } else {
                val logInResponseData =
                    produceState<DataOrException<Response<ResponseofSignUpAndLogIn>, Boolean, Exception>>(
                        initialValue = DataOrException(
                            loading = true
                        )
                    ) {
                        value = authViewModel.logIn(usernameandPassword)
                    }.value

                if (logInResponseData.loading == true) {
                    CircularProgressIndicator()

                } else if (logInResponseData.e?.equals(null) == false) {
                    val exceptionMsg = logInResponseData.e!!.localizedMessage.toString()
                    val toast = Toast.makeText(context, exceptionMsg, Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()
                    stateOfLoginButton.value = false

            }else if (logInResponseData.data?.code() == 201) {

                    val Token = logInResponseData.data?.body()?.token

                    scope.launch {
                        if (Token != null) {
                            dataStore.saveLoginStatus(Token)
                        }
                    }

                    LaunchedEffect(Unit) {
                        delay(200)
                        navController.navigate("listofNotes/$Token")
                    }

                } else {
                    // Text(text = "Exception:" + logInResponseData.data)
                    val toast =
                        Toast.makeText(
                            LocalContext.current,
                            "Invalid credentials",
                            Toast.LENGTH_SHORT
                        )
                    toast.duration = 100
                    toast.show()
                    stateOfLoginButton.value = false

                }
            }
        }
    }
}
