package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
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
import compose.notezz.model.UserEmail
import compose.notezz.util.Dimension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint(
    "SuspiciousIndentation",
    "UnusedMaterialScaffoldPaddingParameter",
    "CoroutineCreationDuringComposition",
    "WrongConstant"
)
@OptIn(DelicateCoroutinesApi::class)

@Composable
fun ForgotPassword(navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val forgotButton = remember { mutableStateOf(false) }
    val emailID = remember { mutableStateOf("") }
    val focus = LocalFocusManager.current
    val context = LocalContext.current

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
                top = Dimension.height(value = 1f).dp
            )
            .padding(start = 10.dp, end = 5.dp), verticalArrangement = Arrangement.Center
    ) {

        RequestLoginLinkIntro()
        EmailTextField(emailID)
        Row() {
            SendLoginLinkBtn(forgotButton, focus)
            NavigateToLoginScreenTxt(navController)
        }
        RequestLinkImage()

    }

    when {
        forgotButton.value -> {

            if (emailID.value.isEmpty()) {
                ComposableToastMessage("Please check your input.", forgotButton)
            } else {
                val emailId = emailID.value
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val response = authViewModel.forgotPassword(UserEmail(emailId))
                        when (val responseCode = response.code()) {
                            201 -> {
                                toastMessage(
                                    context,
                                    "One time login link is sent to your registered email.",
                                    forgotButton
                                )
                            }
                            400 -> {
                                toastMessage(context, "Please check your input.", forgotButton)
                            }
                            else -> {
                                toastMessage(context, responseCode.toString(), forgotButton)
                            }
                        }
                    } catch (e: java.net.UnknownHostException) {
                        toastMessage(
                            context,
                            "Please check your internet connection.",
                            forgotButton
                        )
                    } catch (e: Exception) {
                        toastMessage(context, "Something went wrong.", forgotButton)
                    }
                }
            }
        }
    }
}


@Composable
private fun NavigateToLoginScreenTxt(navController: NavController) {
    Text("Back to login",
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.blueish),

        modifier = Modifier
            .padding(top = 17.dp, start = 15.dp)
            .clickable { navController.navigate("logIn") })
}

@Composable
private fun SendLoginLinkBtn(
    forgotButton: MutableState<Boolean>,
    focus: FocusManager
) {
    Button(colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray)),
        onClick = {
            forgotButton.value = true
            focus.clearFocus()
        }) {

        Icon(
            painter = painterResource(id = R.drawable.link),
            contentDescription = "link",
            tint = Color.White
        )
        Text("Send login link", color = Color.White)
    }
}

@Composable
private fun RequestLinkImage() {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.requestloginlink),
            contentDescription = "requestlinkimage",
            modifier = Modifier.padding(top = 20.dp, start = Dimension.height(value = 8f).dp)
        )
    }
}

@Composable
private fun RequestLoginLinkIntro() {
    Text(
        stringResource(R.string.RequestLoginLink),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 14.dp, top = 59.dp)

    )
    Divider()
    Text(
        text = stringResource(R.string.ForgotPasswordDescription),
        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),

        )
}

@Composable
private fun EmailTextField(emailID: MutableState<String>) {
    Text(
        "Email",
        // color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    OutlinedTextField(
        value = emailID.value,
        onValueChange = { emailID.value = it },
        placeholder = {
            Text(
                text = "Enter email"

            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        )
    )
    Spacer(modifier = Modifier.padding(bottom = 10.dp))

}

