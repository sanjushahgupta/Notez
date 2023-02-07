package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
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
fun SignUpScreen(navController: NavController) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val signUpBtnState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)

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
                top = Dimension.height(value = 1f).dp, start = Dimension.height(value = 0.5f).dp
            )
            .padding(start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Center
    ) {

        SignUpIntro()
        UsernameTextField(username)
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        PasswordTextField(password)
        Spacer(modifier = Modifier.padding(top = Dimension.height(value = 1f).dp))
        PasswordConfirmationTxtField(confirmPassword)
        Spacer(modifier = Modifier.padding(top = 15.dp))
        LinkToTermsAndPolicy()
        Spacer(modifier = Modifier.padding(bottom = 12.dp))
        RegisterUserButton(signUpBtnState, focus)
        TextWithStyle("Already have an account?")
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        Divider()
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        NavigateToLoginScreen(navController, focus)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }


    when {
        signUpBtnState.value -> {
            when {
                password.value.isEmpty() || confirmPassword.value.isEmpty() || username.value.isEmpty() -> {
                    ComposableToastMessage("Please fill out fields.", signUpBtnState)
                }
                username.value.length < 4 -> {
                    ComposableToastMessage(
                        "Length of a username must be of 4 characters or more.",
                        signUpBtnState
                    )
                }
                password.value.length < 6 -> {
                    ComposableToastMessage(
                        "Length of a password must be of 6 characters or more",
                        signUpBtnState
                    )
                }
                password.value != confirmPassword.value -> {
                    ComposableToastMessage("Passwords do not match.", signUpBtnState)

                }
                else -> {
                    val usernameAndPassword = UsernameandPassword(username.value, password.value)

                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            val signUpResponse = authViewModel.signUp(usernameAndPassword)
                            when (val responseCode = signUpResponse.code().toString()) {
                                "201" -> {
                                    val tokEn = signUpResponse.body()!!.token
                                    dataStore.saveLoginStatus(tokEn)
                                    navController.navigate("listOfNotes/$tokEn")
                                    signUpBtnState.value = false
                                }
                                "409" -> {
                                    toastMessage(
                                        context,
                                        "Username already exists.",
                                        signUpBtnState
                                    )

                                }
                                else -> {
                                    toastMessage(context, responseCode, signUpBtnState)

                                }
                            }
                        } catch (e: java.net.UnknownHostException) {
                            toastMessage(
                                context,
                                "Please check your internet connection.",
                                signUpBtnState
                            )

                        } catch (e: Exception) {
                            toastMessage(context, e.message.toString(), signUpBtnState)

                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun LinkToTermsAndPolicy() {
    HyperlinkText(
        fullText = "By registering, you agree to our " + "Terms and Conditions and Privacy Policy.",

        hyperLinks = mutableMapOf(
            "Terms and Conditions" to "https://notezz.com/terms",
            "Privacy Policy" to "https://notezz.com/privacy"
        ), textStyle = TextStyle(
            //  textAlign = TextAlign.Start,
            color = colorResource(id = R.color.lightGray)
        ), linkTextColor = colorResource(id = R.color.blueish), fontSize = 15.sp
    )
}

@Composable
private fun NavigateToLoginScreen(
    navController: NavController,
    focus: FocusManager
) {
    Button(
        onClick = {
            navController.navigate("login")
            focus.clearFocus()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))

    ) {
        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_baseline_login_24),
            contentDescription = "",

            )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            "Login", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White
        )
    }
}

@Composable
private fun RegisterUserButton(
    signUpButtonState: MutableState<Boolean>,
    focus: FocusManager
) {
    Button(
        onClick = {
            signUpButtonState.value = true
            focus.clearFocus()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
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
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun PasswordConfirmationTxtField(confirmPassword: MutableState<String>) {
    Text(
        fontSize = 16.sp,
        text = "Password confirmation",
        modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
        //  color = Color(R.color.textColor)
    )
    val passwordConfirmVisual = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = confirmPassword.value,
        onValueChange = { confirmPassword.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .autofill(listOf(AutofillType.Password),
                onFill = { confirmPassword.value = it }),
        visualTransformation = if (passwordConfirmVisual.value) VisualTransformation.None else PasswordVisualTransformation(),
        placeholder = { Text("Confirm password") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        ),
        trailingIcon = {

            val imageForVisibility =
                if (passwordConfirmVisual.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            Icon(imageForVisibility, "showOrHide", modifier = Modifier.clickable {

                passwordConfirmVisual.value = !passwordConfirmVisual.value

            })
        }
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun PasswordTextField(password: MutableState<String>) {
    TextView("Password")
    val passwordVisual = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .autofill(listOf(AutofillType.Password),
                onFill = { password.value = it }),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisual.value) VisualTransformation.None else PasswordVisualTransformation(),
        placeholder = { Text("Enter password") },
        trailingIcon = {

            val imageForVisibility =
                if (passwordVisual.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            Icon(imageForVisibility, "showOrHide", modifier = Modifier.clickable {

                passwordVisual.value = !passwordVisual.value

            })
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        ),

        )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun UsernameTextField(username: MutableState<String>) {
    Text(
        text = "Username",
        modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
        fontSize = 16.sp
    )

    OutlinedTextField(

        value = username.value,
        onValueChange = { username.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .autofill(listOf(AutofillType.Username),
                onFill = { username.value = it }),
        placeholder = { Text("Enter username") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Black,
            backgroundColor = Color.White
        )
    )
}

@Composable
private fun SignUpIntro() {
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
        modifier = Modifier.padding(bottom = 5.dp, top = Dimension.height(value = 0.8f).dp),
        fontSize = 16.sp,
    )
}

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    hyperLinks: Map<String, String>,
    textStyle: TextStyle = TextStyle.Default,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)

        for ((key, value) in hyperLinks) {

            val startIndex = fullText.indexOf(key)
            val endIndex = startIndex + key.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,

                    ), start = startIndex, end = endIndex
            )
            addStringAnnotation(
                tag = "URL", annotation = value, start = startIndex, end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ), start = 0, end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(modifier = modifier, text = annotatedString, style = textStyle, onClick = {
        annotatedString.getStringAnnotations("URL", it, it).firstOrNull()?.let { stringAnnotation ->
            uriHandler.openUri(stringAnnotation.item)
        }
    })
}
