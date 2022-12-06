package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.UserEmail
import retrofit2.Response

@SuppressLint("WrongConstant")
@Composable
fun ForgotPassword(email: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val forgotButton = remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { focus.clearFocus() }
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {

        val emailID = remember { mutableStateOf("") }

        Text(
            stringResource(R.string.RequestLoginLink),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 15.dp)
        )
        Divider()
        Text(
            text = stringResource(R.string.ForgotPasswordDescription),
            //style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            "Email",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(value = emailID.value,
            onValueChange = { emailID.value = it },
            placeholder = {
                Text(
                    text = "Enter email"

                )
            })
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = {
                forgotButton.value = true
            }) {

                Icon(painter = painterResource(id = R.drawable.link), contentDescription = "link")
                Text("Send login link")
            }
            Text("Back to login", fontWeight = FontWeight.Bold, color = Color.Blue,

                modifier = Modifier
                    .padding(10.dp)
                    .clickable { navController.navigate("logIn") })

        }
        Image(
            painter = painterResource(id = R.drawable.requestloginlink),
            contentDescription = "requestlinkimage",
            modifier = Modifier.padding(top = 20.dp, start = 15.dp)
        )





        if (forgotButton.value == true) {

            val response = produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                initialValue = DataOrException(
                    loading = true
                )
            ) {
                value = authViewModel.forgotPassword(UserEmail(emailID.value))
            }.value

            if (response.loading == true) {
                CircularProgressIndicator()

            } else if (response.data!!.code() == 201) {
                val toast = Toast.makeText(
                    LocalContext.current,
                    "One time login link is sent to your registered email.",
                    Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()
                forgotButton.value = false

            } else if (response.data!!.code() == 400) {

                val toast = Toast.makeText(
                    LocalContext.current, "Please check your input.", Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()
                forgotButton.value = false
            } else {
                val toast = Toast.makeText(
                    LocalContext.current, "Something went wrong.", Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()
                forgotButton.value = false
            }
        }
    }
}
