package compose.notezz.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.UserPreference
import compose.notezz.util.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreenListOfNotes(Token: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)


    if (!isInternetAvailable(context)) {
        ShowNoInternetMessage(context, navController,Token)

    } else {
        val notesResult = getNoteResult(authViewModel, Token)

        if (notesResult.loading == true) {
            DisplayCircularProgress()
        } else if (notesResult.data!!.code() == 200) {
            NotesListTopBar(navController, Token, scope, dataStore)
            DisplayNotesListBody(navController, Token, notesResult, authViewModel)
        } else {
            CircularProgressIndicator()
            CheckUserPreference(scope, dataStore, navController)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun DisplayNotesListBody(
    navController: NavController,
    token: String,
    notesResult: DataOrException<Response<ArrayList<Note>>, Boolean, Exception>,
    authViewModel: AuthenticationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Dimension.height(value = 8f).dp,
                start = Dimension.height(value = 0.5f).dp
            ), verticalArrangement = Arrangement.Center
    ) {
        Scaffold(floatingActionButton = {
            FloatingAddActionButton(navController, token)
        }) {
            DisplayNotes(notesResult, authViewModel, token, navController)
        }

    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun NotesListTopBar(
    navController: NavController,
    token: String,
    scope: CoroutineScope,
    dataStore: UserPreference
) {
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray

        ) {
            TopBarLogo()
            Spacer(Modifier.weight(1f))
            AccountSetting(navController, token)
            Spacer(Modifier.padding(end = 15.dp))
            LogoutIcon(scope, dataStore, navController)

        }

    }) {}
}

@Composable
private fun DisplayCircularProgress() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = colorResource(id = R.color.LogiTint))
    }
}

@Composable
private fun AccountSetting(navController: NavController, token: String) {
    Icon(
        Icons.Default.Settings,
        "",
        modifier = Modifier
            .padding(start = 10.dp, end = 8.dp)
            .clickable { navController.navigate("updateAccount/$token") },
        tint = Color.Gray
    )
}

@Composable
private fun FloatingAddActionButton(
    navController: NavController,
    token: String
) {
    val title = " "
    val body = " "
    FloatingActionButton(
        onClick = {

            navController.navigate("addNotes/$token/${title}/${body}/idis0/status/created/updated/userId")
        }, backgroundColor = colorResource(id = R.color.LogiTint)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "To add Notes",
            tint = Color.White
        )
    }
}

@Composable
private fun DisplayNotes(
    notesResult: DataOrException<Response<ArrayList<Note>>, Boolean, Exception>,
    authViewModel: AuthenticationViewModel,
    token: String,
    navController: NavController
) {
    if (notesResult.loading == true) {
        CircularProgressIndicator()

    } else if (notesResult.data!!.code() == 200) {

        if (notesResult.data!!.body()!!.isEmpty()) {


            Text(text = "No notes", fontWeight = FontWeight.Bold)


        } else if (!notesResult.data!!.body()!!.isEmpty()) {

            ListItem(
                authViewModel,
                token,
                navController,
                notesResult.data!!.body()!!
            )

        }
    }
}

@Composable
private fun LogoutIcon(
    scope: CoroutineScope,
    dataStore: UserPreference,
    navController: NavController
) {
    Icon(tint = Color.Gray,
        painter = painterResource(id = R.drawable.ic_baseline_logout_24),
        contentDescription = "logout",
        modifier = Modifier
            .padding(end = 6.dp)
            .clickable {
                scope.launch {
                    dataStore.saveLoginStatus("loggedOut")
                }
                navController.navigate("logIn")
            })
}

@Composable
private fun TopBarLogo() {
    Icon(
        modifier = Modifier.padding(start = 10.dp),
        tint = colorResource(id = R.color.LogiTint),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "logo"
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun CheckUserPreference(
    scope: CoroutineScope,
    dataStore: UserPreference,
    navController: NavController
) {
    scope.launch {
        dataStore.saveLoginStatus("loggedOut")
        async {
            delay(200)
            navController.navigate("logIn")
        }.await()
    }
}

@Composable
private fun getNoteResult(
    authViewModel: AuthenticationViewModel,
    token: String
): DataOrException<Response<ArrayList<Note>>, Boolean, Exception> {
    val notesResult =
        produceState<DataOrException<Response<ArrayList<Note>>, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = authViewModel.getNotes("Bearer" + " " + token)
        }.value
    return notesResult
}

@Composable
private fun ShowNoInternetMessage(context: Context, navController: NavController, Token: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, 
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
        Text("OOPS! NO INTERNET CONNECTION.",color = Color.Gray)
        Button(onClick = { navController.navigate("listofNotes/$Token")},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
          Text(text = "Try AGAIN" )
        }
        
    }
   
}

@SuppressLint("UnrememberedMutableState", "WrongConstant")
@Composable
fun ListItem(
    authenticationViewModel: AuthenticationViewModel,
    token: String,
    navController: NavController,
    data: ArrayList<Note>
) {
    Spacer(modifier = Modifier.padding(top = 55.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        items(data) { item ->

            val mutablestatetodelete = remember { mutableStateOf(false) }
            val stateOfAlertBox = remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .width(Dimension.width(value = 100f).dp)
                    .height(Dimension.height(value = 11f).dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(8),
                elevation = 20.dp,
                backgroundColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(true,
                            null,
                            null,
                            onClick = { navController.navigate("addNotes/$token/${item.title}/${item.body}/${item.id}/${item.status}/${item.created}/${item.created}/${item.userId}") })
                ) {
                    var trimtitle = item.title
                    var trimbody = item.body

                    Row {
                        if (trimtitle.length > 30) {
                            trimtitle = item.title.substring(0, 28) + ".."
                        }
                        Text(
                            text = trimtitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(start = 6.dp, top = 8.dp)
                        )

                        Spacer(Modifier.weight(1f))

                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .clickable { stateOfAlertBox.value = true }
                                .padding(end = 6.dp)
                                .wrapContentSize(),
                            tint = Color(0xFFFFC107)

                        )
                        if (stateOfAlertBox.value == true) {
                            AlertDialog(
                                onDismissRequest = { stateOfAlertBox.value = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {

                                            mutablestatetodelete.value = true
                                            stateOfAlertBox.value = false
                                        },

                                        ) {
                                        Text("Yes", color = Color.Black)

                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { stateOfAlertBox.value = false }) {
                                        Text("No", color = Color.Black)
                                    }
                                },
                                title = { Text("Are you sure you want to delete this item?") },
                            )
                        }

                        if (mutablestatetodelete.value) {
                            val context = LocalContext.current
                            if (!isInternetAvailable(context)) {

                                val toast = Toast.makeText(
                                    context, "Please check your internet.",
                                    Toast.LENGTH_SHORT
                                )
                                toast.duration = 100
                                toast.show()
                                mutablestatetodelete.value = false

                            } else {

                                val deleteResponseData =
                                    produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
                                        initialValue = DataOrException(loading = true)
                                    ) {
                                        value = authenticationViewModel.deleteNote(token, item.id)
                                    }.value

                                if (deleteResponseData.loading == true) {
                                    CircularProgressIndicator()
                                } else if (deleteResponseData.data!!.code() == 200) {
                                    navController.navigate("listofNotes/$token")
                                    mutablestatetodelete.value = false

                                } else {
                                    val toast = Toast.makeText(
                                        LocalContext.current,
                                        "Something went wrong",
                                        Toast.LENGTH_LONG
                                    )
                                    toast.duration = 100
                                    toast.show()
                                    navController.navigate("listofNotes/$token")
                                    mutablestatetodelete.value = false

                                }
                            }

                        }
                    }
                    if (trimbody.length > 40) {
                        trimbody = item.body.substring(0, 40) + ".."
                    }
                    Text(
                        trimbody,
                        modifier = Modifier.padding(bottom = 5.dp, start = 6.dp, top = 7.dp)
                    )
                }

            }
        }

    }
}

