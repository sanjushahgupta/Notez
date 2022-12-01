package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UsernameandPassword

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(navController: NavController) {
    var authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("ramniwash") }
    val password = remember { mutableStateOf("2rxbjjbd") }

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = Color.DarkGray
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

    Column(
        modifier = Modifier
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
            fontSize = 16.sp
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

        Spacer(modifier = Modifier.padding(bottom = 15.dp))
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
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            text = "Password confirmation",
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

        Text(
            "By registering, you agree to our Terms and Conditions and Privacy Policy",
            fontWeight = FontWeight.Bold,
            // color = colorResource(id = R.color.blue)
        )

        Spacer(modifier = Modifier.padding(bottom = 12.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White, backgroundColor = Color(
                    R.color.blue
                )
            )
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
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
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
            onClick = {},
            colors = ButtonDefaults.buttonColors(Color(R.color.blue))

        ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.ic_baseline_login_24),
                contentDescription = "",

                )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                "Login",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
    val NotezzData = produceState<DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        val usernameandPassword = UsernameandPassword("shysdfjksd", "wioefhowi3ihoe4")
        value = authViewModel.signUp(usernameandPassword)
    }.value

    if (NotezzData.loading == true) {
        CircularProgressIndicator()

    } else if (NotezzData.data != null) {
        Text(text = NotezzData.data?.token.toString())
    }


}
