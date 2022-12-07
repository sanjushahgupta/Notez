package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
fun SignUpScreen(navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val signUpButtton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray
        ) {

            Icon(
                modifier = Modifier.padding(start = 10.dp),
                tint = Color.Cyan,
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}

    var focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .clickable(MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Register",
            style = MaterialTheme.typography.h5,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 15.dp, top = 59.dp)
        )
        Divider()
        Text(
            text = "Register below and start taking notes in seconds.",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp,

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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.Black)

        )

        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            text = "Password",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.Black)

        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            text = "Password confirmation",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontWeight = FontWeight.Bold,
            //  color = Color(R.color.textColor)
        )
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.padding(top = 15.dp))

        Text(
            "By registering, you agree to our Terms and Conditions and Privacy Policy",
            fontWeight = FontWeight.Bold,
            // color = colorResource(id = R.color.blue)
        )

        Spacer(modifier = Modifier.padding(bottom = 12.dp))

        Button(
            onClick = { signUpButtton.value = true },

            ) {


            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Text(
            "Already have an account?",
            modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            // color = Color(R.color.textColor)
        )

        Button(
            onClick = { navController.navigate("login") },

            ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.ic_baseline_login_24),
                contentDescription = "",

                )

            Text(
                "Login", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }


    if (signUpButtton.value) {
        if (password.value.isEmpty() || confirmPassword.value.isEmpty() || username.value.isEmpty()) {
            val toast =
                Toast.makeText(LocalContext.current, "Please fill out fields.", Toast.LENGTH_SHORT)
            toast.duration = 100
            toast.show()
            signUpButtton.value = false
        } else if (username.value.length < 4) {
            val toast = Toast.makeText(
                context, "Length of a username must be of 4 characters or more.", Toast.LENGTH_SHORT
            )
            toast.duration = 100
            toast.show()
            signUpButtton.value = false
        } else if (password.value.length < 6) {
            val toast = Toast.makeText(
                context, "Length of a password must be of 6 characters or more.", Toast.LENGTH_SHORT
            )
            toast.duration = 100
            toast.show()
            signUpButtton.value = false

        } else if (!password.value.equals(confirmPassword.value)) {
            val toast = Toast.makeText(
                context, "Passwords do not match.", Toast.LENGTH_SHORT
            )
            toast.duration = 100
            toast.show()
            signUpButtton.value = false

        } else {
            val NotezzData =
                produceState<DataOrException<Response<ResponseofSignUpAndLogIn>, Boolean, Exception>>(
                    initialValue = DataOrException(loading = true)
                ) {
                    val usernameandPassword = UsernameandPassword(username.value, password.value)
                    value = authViewModel.signUp(usernameandPassword)
                }.value

            if (NotezzData.loading == true) {

                CircularProgressIndicator()

            } else if (NotezzData.data!!.code() == 201) {

                var Token = NotezzData.data!!.body()!!.token

                scope.launch {
                    dataStore.saveLoginStatus(Token)
                }

                LaunchedEffect(Unit) {
                    delay(200)
                    navController.navigate("listofNotes/$Token")
                }

            } else {
                val toast = Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT)
                toast.duration = 100
                toast.show()
                signUpButtton.value = false

            }
        }
    }
}
