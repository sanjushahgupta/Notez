package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.UserEmail
import compose.notezz.util.Dimension
import retrofit2.Response

@SuppressLint("WrongConstant", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPassword(navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val forgotButton = remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current
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
    Column(
        modifier = Modifier
            .clickable(MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .padding(
                top = Dimension.height(value = 1f).dp,
                start = Dimension.height(value = 0.5f).dp
            )
            .padding(start = 10.dp, end = 5.dp),
        verticalArrangement = Arrangement.Center
    ) {

        val emailID = remember { mutableStateOf("") }

        Text(
            stringResource(R.string.RequestLoginLink),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp, top = 59.dp)

        )
        Divider()
        Text(
            text = stringResource(R.string.ForgotPasswordDescription),
            //style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            "Email",
            // color = Color.Black,
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
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray)),
                onClick = {
                    forgotButton.value = true
                }) {

                Icon(
                    painter = painterResource(id = R.drawable.link),
                    contentDescription = "link",
                    tint = Color.White
                )
                Text("Send login link", color = Color.White)
            }
            Text("Back to login",
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blueish),

                modifier = Modifier
                    .padding(10.dp)
                    .clickable { navController.navigate("logIn") })
        }

        if (forgotButton.value == true) {

            if (emailID.value.isEmpty()) {
                val toast = Toast.makeText(
                    LocalContext.current,
                    "Please check your input.",
                    Toast.LENGTH_SHORT
                )
                toast.duration = 100
                toast.show()

            } else {
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

    Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.requestloginlink),
            contentDescription = "requestlinkimage",
            modifier = Modifier.padding(top = 20.dp,  start = Dimension.height(value = 8f).dp)
        )
    }
}
}
