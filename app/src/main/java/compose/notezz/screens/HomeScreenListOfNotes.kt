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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.room.Update
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note

@SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreenListOfNotes(tokenfromRoomDB: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()

    val notesResult = produceState<DataOrException<ArrayList<Note>, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = authViewModel.getNotes("Bearer" + " " + tokenfromRoomDB)
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
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}


    Column {
        if (notesResult.loading == true) {
            CircularProgressIndicator()

        } else if (notesResult.data != null) {

            ListItem(authViewModel, tokenfromRoomDB,navController, notesResult.data!!)

        }
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addNotes/$tokenfromRoomDB") },
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
                            modifier = Modifier.padding(end = 20.dp)
                                .clickable{navController.navigate("addNotes/$token")}
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


/*@Composable
fun ListItem(data:ArrayList<Note>){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(bottom = 50.dp)){
        items(data) { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp), shape = RoundedCornerShape(20), elevation = 20.dp) {
                Row(){
                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = item.title, fontWeight = FontWeight.Bold)
                        Text(item.body)
                    }
                    val mutablestatetodelete = remember{ mutableStateOf(false) }
                    Icon(imageVector = Icons.Default.Delete, contentDescription ="Delete",modifier = Modifier.clickable { mutablestatetodelete })
                }
            }

        }
    }
}*/
