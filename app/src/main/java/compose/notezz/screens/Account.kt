package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ScrollState.Companion.Saver
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.AccountDetails
import compose.notezz.model.UserEmail
import compose.notezz.model.UserPreference
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "WrongConstant","CoroutineCreationDuringComposition")
@Composable
fun Account(token: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val UpdateAccountStatus = remember { mutableStateOf(false) }
    val DeleteAccountStatus = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStore = UserPreference(context)
    val stateOfAlertBox = remember{mutableStateOf(true)}
    val deleteAlertBox = remember{mutableStateOf(false)}
    val scope = rememberCoroutineScope()


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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 15.dp, top = 59.dp)
        )
        Divider()
        Text(
            text = "Update your account settings below.",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontSize = 16.sp,

            // color = Color(R.color.textColor)
        )
        Text(
            text = "Email",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontWeight = FontWeight.Bold,

            )

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.Black)

        )
        Text(
            text = stringResource(R.string.Updateacount), modifier = Modifier.padding(bottom = 5.dp)

        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))

        Text(
            text = "Username",
            modifier = Modifier.padding(bottom = 5.dp, top = 8.dp),
            fontWeight = FontWeight.Bold,

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


        Button(
            onClick = { UpdateAccountStatus.value = true },

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

        if (UpdateAccountStatus.value == true) {
            if (username.value.isEmpty() || email.value.isEmpty() || username.value.length < 4) {
                val toast = Toast.makeText(LocalContext.current,"\"Please fill email and username field.Length of username must be more than 3 characters.",Toast.LENGTH_SHORT)
                toast.duration = 100
                toast.show()
                UpdateAccountStatus.value = false
            } else {
                val updateResponseData = produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                    initialValue = DataOrException(
                        loading = true
                    )
                ) {
                    value =  authViewModel.updateAccount(
                        "Bearer" + " " + token, AccountDetails(username.value, email.value)
                    )
                }.value

                if(updateResponseData.loading == true){
                    CircularProgressIndicator()
                } else if(updateResponseData.data!!.code() == 201){
                    val toast = Toast.makeText(LocalContext.current,"Account updated.",Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()
                    UpdateAccountStatus.value = false
                    navController.navigate("listofNotes/$token")
                }else{
                    val toast = Toast.makeText(LocalContext.current,"Something went wrong.",Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()
                    UpdateAccountStatus.value = false
                }

            }
        }

        Button(
            onClick = {DeleteAccountStatus.value = true
                        stateOfAlertBox.value = true},

            ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.ic_baseline_login_24),
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

        if(DeleteAccountStatus.value == true) {
            if (stateOfAlertBox.value == true) {

                AlertDialog(
                    onDismissRequest = { stateOfAlertBox.value = false
                                        DeleteAccountStatus.value = false},
                    confirmButton = {
                        TextButton(
                            onClick = {

                                deleteAlertBox.value = true
                            },

                            ) {
                            Text("Yes")

                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { stateOfAlertBox.value = false
                            DeleteAccountStatus.value = false}) {
                            Text("No")
                        }
                    },
                    title = { Text("Are you sure? This is irreversible!") },
                )

            }
            if (deleteAlertBox.value == true)
            {
            val responseData = produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                    initialValue = DataOrException(
                        loading = true
                    )
                ) {
                    value = authViewModel.deleteAccount(
                        "Bearer" + " " + token)
                }.value

                if(responseData.loading == true){
                    CircularProgressIndicator()
                }else if (responseData.data!!.code() == 200)
                {
                    val toast = Toast.makeText(LocalContext.current, "Account deleted", Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()
                    scope.launch{
                        async {
                            dataStore.saveLoginStatus("loggedOut")
                            delay(200)
                            navController.navigate("login")
                        }
                    }
                    DeleteAccountStatus.value = false

                }
                else{
                    val toast = Toast.makeText(LocalContext.current, "something went wrong", Toast.LENGTH_SHORT)
                    toast.duration = 100
                    toast.show()


                }
            }

        }



    }
}
