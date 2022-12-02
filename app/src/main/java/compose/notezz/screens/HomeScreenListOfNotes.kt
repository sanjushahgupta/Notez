package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreenListOfNotes(Token: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    var token = Token
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = UserPreference(context)
   var loginStatus by remember { mutableStateOf("") }

    val notesResult = produceState<DataOrException<ArrayList<Note>, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = authViewModel.getNotes("Bearer" + " " + token)
    }.value
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = Color.DarkGray

        ) {


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
                        token = "Tokenis0"
                        navController.navigate("logIn")
                        loginStatus = "0"
                        scope.launch {
                            dataStore.saveLoginStatus(loginStatus)
                        }
                    })
        }

    }) {}


    Column {
        if (notesResult.loading == true) {
            CircularProgressIndicator()

        } else if (notesResult.data != null) {

            ListItem(authViewModel, token, navController, notesResult.data!!)

        }

        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    navController.navigate("addNotes/$token/title/body/idis0/status/created/updated/userId") },
                backgroundColor = Color.Cyan
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "To add Notes")
            }
        }) {}
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
            .wrapContentHeight()
            .padding(bottom = 50.dp)
    ) {
        items(data) { item ->
            val mutablestatetodelete = remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp), shape = RoundedCornerShape(20), elevation = 20.dp
            ) {

                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(item.body, modifier = Modifier.padding(bottom = 10.dp))

                    Row {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .clickable { navController.navigate("addNotes/$token/${item.title}/${item.body}/${item.id}/${item.status}/${item.created}/${item.created}/${item.userId}") }
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .clickable {
                                    mutablestatetodelete.value = true
                                }
                                .wrapContentSize(), tint = Color.Red

                        )

                        val id = item.id
                        Delete(authenticationViewModel, token, id, mutablestatetodelete.value)


                    }

                }

            }

        }
    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun Delete(
    authenticationViewModel: AuthenticationViewModel,
    token: String,
    id: Int,
    stateofdelete: Boolean
) {
    if (stateofdelete == true) {
        authenticationViewModel.deleteNote(token, id)
    }
}
