package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
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
                tint = colorResource(id = R.color.LogiTint),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )
          //  Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}

    var focus = LocalFocusManager.current
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
            .padding(start = 10.dp, end = 10.dp),verticalArrangement = Arrangement.Center
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
            modifier = Modifier.padding(bottom = 5.dp, top = Dimension.height(value = 0.8f).dp),
            fontSize = 16.sp,

            // color = Color(R.color.textColor)
        )

        Text(
            text = "Username",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp
            //  color = Color(R.color.textColor)
        )
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {Text("Enter Username")})

        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))

        Text(
            text = "Password",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {Text("Enter password")}
        )

        Spacer(modifier = Modifier.padding(top = Dimension.height(value = 1f).dp))
        Text(
            fontSize = 16.sp,
            text = "Password confirmation",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            //  color = Color(R.color.textColor)
        )
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {Text("Confirm password")}

        )

        Spacer(modifier = Modifier.padding(top = 15.dp))
        HyperlinkText(
            fullText = "By registering, you agree to our "+ "Terms and Conditions and Privacy Policy.",

            hyperLinks = mutableMapOf(
                "Terms and Conditions" to "https://notezz.com/terms",
                "Privacy Policy" to "https://notezz.com/privacy"
            ),
            textStyle = TextStyle(
              //  textAlign = TextAlign.Start,
                color = colorResource(id = R.color.lightGray)
            ),
            linkTextColor = colorResource(id = R.color.blueish),
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.padding(bottom = 12.dp))

        Button(
            onClick = { signUpButtton.value = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))) {


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
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))
        Divider()
        Spacer(modifier = Modifier.padding(bottom = Dimension.height(value = 1f).dp))

        Button(
            onClick = { navController.navigate("login") },
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

        for((key, value) in hyperLinks){

            val startIndex = fullText.indexOf(key)
            val endIndex = startIndex + key.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,

                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = value,
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = textStyle,
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
