package compose.notezz.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.model.UserPreference
import compose.notezz.model.UsernameandPassword
import compose.notezz.util.Dimension
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class, ExperimentalComposeUiApi::class)
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
    val stateOfLoginButton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStore = UserPreference(context)
    val focus = LocalFocusManager.current
    val passwordTxtVisibilityState = remember { mutableStateOf(false) }

    ScaffoldTopAppBar()


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

        LoginScreenIntro()
        TextView("Username")
        UsernameOutlinedTextField(username)
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        TextView("Password")
        PasswordTextField(password, passwordTxtVisibilityState)
        Spacer(modifier = Modifier.padding(top = Dimension.height(value = 1.5f).dp))
        LoginButton(stateOfLoginButton, focus)
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        NavigateToForgotPasswordScreen(navController)
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 3f).dp))
        TextWithStyle("Don't have an account?")
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        Divider()
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        NavigateToSignUpScreen(navController, focus)


        if (stateOfLoginButton.value) {
            val usernameandPassword = UsernameandPassword(username.value, password.value)
            if (password.value.isEmpty() || username.value.isEmpty()) {
                ComposableToastMessage("Please fill out fields.")
                stateOfLoginButton.value = false
            } else if (username.value.length < 4) {
                ComposableToastMessage("Length of a username must be of 4 characters or more.")
                stateOfLoginButton.value = false
            } else if (password.value.length < 6) {
                ComposableToastMessage("Length of a password must be of 6 characters or more")
                stateOfLoginButton.value = false
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val logInResponseData = authViewModel.logIn(usernameandPassword)
                        when (val responseCode = logInResponseData.code().toString()) {
                            "401" -> {
                                toastMessage(context, "Invalid Credentials.")
                                stateOfLoginButton.value = false
                            }
                            "201" -> {

                                val toKen = logInResponseData.body()?.token
                                if (toKen != null) {
                                    dataStore.saveLoginStatus(toKen)
                                }
                                navController.navigate("listofNotes/$toKen")
                                stateOfLoginButton.value = false

                            }
                            else -> {
                                toastMessage(context, responseCode)
                                stateOfLoginButton.value = false
                            }
                        }

                    } catch (e: java.net.UnknownHostException) {
                        toastMessage(context, "Please check your internet connection.")
                        stateOfLoginButton.value = false
                    } catch (e: Exception) {
                        toastMessage(context, "Unknown exception ")
                        stateOfLoginButton.value = false
                    }
                }


            }
        }
    }
}

@Composable
fun TextWithStyle(msg: String) {
    Text(
        msg,
        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6,
    )
}

@Composable
private fun LoginScreenIntro() {
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
}


@Composable
fun TextView(msg: String) {
    Text(
        text = msg,
        modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
        fontSize = 16.sp
    )
}


@SuppressLint("WrongConstant")
@Composable
fun ComposableToastMessage(msg: String) {
    val toast =
        Toast.makeText(
            LocalContext.current,
            msg,
            Toast.LENGTH_SHORT
        )
    toast.duration = 100
    toast.show()
}

@SuppressLint("WrongConstant")
fun toastMessage(context: Context, msg: String) {
    val toast =
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        )
    toast.duration = 100
    toast.show()
}


@Composable
fun NavigateToForgotPasswordScreen(navController: NavController) {
    Text(
        "Forgot Password?",
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.blueish),
        modifier = Modifier.clickable { navController.navigate("forgotpassword/email") }
    )
}

@Composable
fun NavigateToSignUpScreen(
    navController: NavController,
    focus: FocusManager
) {
    Button(
        onClick = {
            navController.navigate("signUp")
            focus.clearFocus()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
        //  colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
    ) {
        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "",

            )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            "Register", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White
        )
    }
}

@Composable
fun LoginButton(
    stateOfLoginButton: MutableState<Boolean>,
    focus: FocusManager
) {
    Button(
        onClick = {
            stateOfLoginButton.value = true
            focus.clearFocus()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
        // colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
    ) {
        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_baseline_login_24),
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
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun PasswordTextField(
    password: MutableState<String>,
    passwordTxtVisibilityState: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .autofill(
                autofillTypes = listOf(AutofillType.Password),
                onFill = { password.value = it },
            ),
        value = password.value,
        onValueChange = { password.value = it },
        placeholder = { Text("Enter password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordTxtVisibilityState.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordTxtVisibilityState.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            Icon(
                image,
                "hide/show",
                modifier = Modifier.clickable {
                    passwordTxtVisibilityState.value = !passwordTxtVisibilityState.value
                })
        },

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        )
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun UsernameOutlinedTextField(username: MutableState<String>) {
    OutlinedTextField(
        value = username.value,
        onValueChange = { username.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .autofill(listOf(AutofillType.Username),
                { username.value = it }),
        placeholder = { Text("Enter username") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        )
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldTopAppBar() {
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
        }
    }) {}
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this
        .onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}