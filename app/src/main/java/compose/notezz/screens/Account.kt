package compose.notezz.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.AccountDetails
import compose.notezz.model.UserPreference
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "WrongConstant", "CoroutineCreationDuringComposition",
    "SuspiciousIndentation"
)
@Composable
fun Account(token: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val updateAccountStatus = remember { mutableStateOf(false) }
    val deleteAccountStatus = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStore = UserPreference(context)
    val stateOfAlertBox = remember { mutableStateOf(true) }
    val deleteAlertBox = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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

    val focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .clickable(
                MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Update account",
            color = Color.Black,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 15.dp, top = 59.dp)
        )
        Divider()
        Text(
            text = "Update your account settings below.",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp,
        )
        Text(
            text = "Email",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp)

        )
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .autofill(listOf(AutofillType.EmailAddress),
                    onFill = { email.value = it }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            ),
            placeholder = {Text("Enter email")}

        )
        Text(
            text = stringResource(R.string.Updateacount), modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)

        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))

        Text(
            text = "Username",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            )
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .autofill(listOf(AutofillType.Username),
                    onFill = { username.value = it })
                    ,
            placeholder = { Text("Enter username") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            )

        )

        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            text = "Password",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
        )
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
            )

        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        Text(
            text = "Password confirmation",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
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
            trailingIcon = {

                val imageForVisibility =
                    if (passwordConfirmVisual.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                Icon(imageForVisibility, "showOrHide", modifier = Modifier.clickable {

                    passwordConfirmVisual.value = !passwordConfirmVisual.value

                })
            }
            ,colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            ),
            placeholder = {(Text(text = "Confirm password"))}

        )

        Spacer(modifier = Modifier.padding(top = 15.dp))


        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray)),
            onClick = {
                updateAccountStatus.value = true
                focus.clearFocus()
            },

            ) {


            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = "Update Account",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        if (updateAccountStatus.value) {
            if (username.value.isEmpty() && password.value.isEmpty()) {
                val toast =
                    Toast.makeText(
                        LocalContext.current,
                        "username and password cannot be empty.",
                        Toast.LENGTH_SHORT
                    )
                toast.duration = 100
                toast.show()
                updateAccountStatus.value = false
            } else if(!isInternetAvailable(LocalContext.current)){
                val toast =
                    Toast.makeText(
                        LocalContext.current,
                        "Please check your internet connection.",
                        Toast.LENGTH_SHORT
                    )
                toast.duration = 100
                toast.show()
                updateAccountStatus.value = false
            }
            else {
                val updateResponseData = produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                        initialValue = DataOrException(
                            loading = true
                        )
                    ) {
                        value = authViewModel.updateAccount(
                            "Bearer $token",

                            AccountDetails(username.value, email.value, password.value)
                        )
                    }.value

                if (updateResponseData.loading == true) {
                    CircularProgressIndicator(color = Color(R.color.LogiTint))
                } else if (updateResponseData.data!!.code() == 201) {
                    val toast =
                        Toast.makeText(LocalContext.current, "Account updated.", Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()
                    navController.navigate("updateAccount/$token")
                    updateAccountStatus.value = false
                } else {
                    val toast = Toast.makeText(
                        LocalContext.current,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    )
                    toast.duration = 100
                    toast.show()

                    updateAccountStatus.value = false
                }

            }
        }

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.Red)),
            onClick = {
                deleteAccountStatus.value = true
                stateOfAlertBox.value = true
            },

            ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "",

                )

            Text(
                "Delete Account & All Notes Forever",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 8.dp))

        if (deleteAccountStatus.value) {
            if (stateOfAlertBox.value ) {

                AlertDialog(
                    onDismissRequest = {
                        stateOfAlertBox.value = false
                        deleteAccountStatus.value = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {

                                deleteAlertBox.value = true
                            },

                            ) {
                            Text("Yes", color = Color.Black)

                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            stateOfAlertBox.value = false
                            deleteAccountStatus.value = false
                        }) {
                            Text("No", color = Color.Black)
                        }
                    },
                    title = { TextView("Are you sure? This is irreversible!") },
                )

            }
            if (deleteAlertBox.value) {
                if (!isInternetAvailable(LocalContext.current)) {
                    val toast =
                        Toast.makeText(
                            LocalContext.current,
                            "Please check your internet connection.",
                            Toast.LENGTH_SHORT
                        )
                    toast.duration = 100
                    toast.show()
                    deleteAccountStatus.value = false
                    stateOfAlertBox.value = false
                } else {
                    val responseData =
                        produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                            initialValue = DataOrException(
                                loading = true
                            )
                        ) {
                            value = authViewModel.deleteAccount(
                                "Bearer $token"
                            )
                        }.value

                    when {
                        responseData.loading == true -> {
                            CircularProgressIndicator(color = Color(R.color.LogiTint))
                        }
                        responseData.data!!.code() == 200 -> {
                            val toast = Toast.makeText(
                                LocalContext.current,
                                "Account deleted",
                                Toast.LENGTH_SHORT
                            )
                            toast.duration = 100
                            toast.show()
                            scope.launch {
                                async {
                                    dataStore.saveLoginStatus("loggedOut")
                                    delay(200)
                                    navController.navigate("login")
                                }
                            }
                            deleteAccountStatus.value = false


                        }
                        else -> {
                            val toast = Toast.makeText(
                                LocalContext.current,
                                responseData.toString(),
                                Toast.LENGTH_SHORT
                            )
                            toast.duration = 100
                            toast.show()
                        }
                    }
                }

            }

        }
    }
}
