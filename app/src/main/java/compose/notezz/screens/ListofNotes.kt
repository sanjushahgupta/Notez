package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch
import retrofit2.Response


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreenListOfNotes(Token: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    var token = Token
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)
    var loginStatus by remember { mutableStateOf("") }


    val notesResult = produceState<DataOrException<Response<ArrayList<Note>>, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = authViewModel.getNotes("Bearer" + " " + token)
    }.value

    if (notesResult.loading == true) {
        CircularProgressIndicator()

    } else if (notesResult.data!!.code() == 401) {
        token = "0"
        navController.navigate("logIn")
        loginStatus = "0"
        scope.launch {
            dataStore.saveLoginStatus("0")
        }

    } else {
        Scaffold(topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray

            ) {
                // Text("The text is "+token)

                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    tint = Color.Cyan,
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo"
                )
                Spacer(Modifier.weight(1f))

                Icon(tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_baseline_logout_24),
                    contentDescription = "logout",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            token = "0"
                            navController.navigate("logIn")
                            loginStatus = "0"
                            scope.launch {
                                dataStore.saveLoginStatus("0")
                            }
                        })
            }

        }) {}


        Column {
            var title = " "
            var body = " "
            if (notesResult.loading == true) {
                CircularProgressIndicator()

            } else if (notesResult.data!!.code() == 200) {

                if (notesResult.data!!.body()!!.isEmpty()) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 80.dp, start = 15.dp)
                    ) {
                        Text(text = "No notes", fontWeight = FontWeight.Bold)
                    }

                } else if (!notesResult.data!!.body()!!.isEmpty()) {

                   ListItem(authViewModel, token, navController, notesResult!!.data!!.body()!!)
                }

            }

            Scaffold(floatingActionButton = {

                FloatingActionButton(
                    onClick = {

                        navController.navigate("addNotes/$token/${title}/${body}/idis0/status/created/updated/userId")
                    }, backgroundColor = Color.Cyan
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "To add Notes")
                }
            }) {}
        }
    }
}
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
            .width(Dimension.width(value = 82f).dp)
            .height(Dimension.height(value = 82f).dp)
            .background(Color.LightGray)
    ) {
        items(data) { item ->

            var mutablestatetodelete = remember{mutableStateOf(false)}
            Card(
                modifier = Modifier
                    .width(Dimension.width(value = 100f).dp)
                    .height(Dimension.height(value = 12f).dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(20),
                elevation = 20.dp
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
                      //

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .clickable{ mutablestatetodelete.value = true}
                                .padding(end = 6.dp)
                                .wrapContentSize(),
                            tint = Color.Red,
                        )
                        if (mutablestatetodelete.value == true) {
                           Delete(authenticationViewModel, token, item.id, navController)

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


@SuppressLint("WrongConstant")
@Composable
fun Delete(
    authenticationViewModel: AuthenticationViewModel,
    token: String,
    id: Int,
    navController: NavController
) {

    val stateOfAlertBox = remember { mutableStateOf(true) }
    val deleteResponse = remember { mutableStateOf(false) }


    if (stateOfAlertBox.value == true) {

        AlertDialog(
            onDismissRequest = { stateOfAlertBox.value = false },
            confirmButton = {
                TextButton(
                    onClick = {

                        deleteResponse.value = true
                    },

                    ) {
                    Text("Yes")

                }
            },
            dismissButton = {
                TextButton(onClick = { stateOfAlertBox.value = false }) {
                    Text("No")
                }
            },
            title = { Text("Are you sure you want to delete this item?") },
        )

    }

    if (deleteResponse.value == true) {
        val deleteResponseData = produceState<DataOrException<Response<Unit>, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = authenticationViewModel.deleteNote(token, id)
        }.value

        if (deleteResponseData.loading == true) {
            CircularProgressIndicator()
        } else if (deleteResponseData.data!!.code() == 200) {
            navController.navigate("listofNotes/$token")
            deleteResponse.value = false
        } else {
            val toast =
                Toast.makeText(LocalContext.current, "Something went wrong", Toast.LENGTH_LONG)
            toast.duration = 100
            toast.show()
            deleteResponse.value = false
        }
    }


}
