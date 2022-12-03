package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.NoteInfo
import compose.notezz.model.updateNoteRequest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddandEditScreen(
    token: String,
    titlee: String,
    bodyy: String,
    noteId: String,
    status: String,
    updated: String,
    created: String,
    userId: String,
    navController: NavController
) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val title = remember { mutableStateOf(titlee) }
    val body = remember { mutableStateOf(bodyy) }

    val addState = remember { mutableStateOf(false) }
    val updateState = remember { mutableStateOf(false) }


    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray
        ) {

            Icon(
                modifier = Modifier.padding(start = 10.dp),
                tint = Color.Cyan,
                painter = painterResource(id = compose.notezz.R.drawable.logo),
                contentDescription = "logo"
            )
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}

    Box(modifier = Modifier.fillMaxWidth(), Alignment.Center) {
        Column(
            modifier = Modifier.padding(top = 55.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                elevation = 20.dp
            ) {
                OutlinedTextField(value = title.value,
                    onValueChange = { title.value = it },
                    modifier = Modifier.padding(5.dp),
                    placeholder = { Text("Note title") })
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Card(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                elevation = 20.dp,
            ) {
                OutlinedTextField(value = body.value,
                    onValueChange = { body.value = it },
                    modifier = Modifier.padding(5.dp),
                    placeholder = { Text(text = "Note description") })
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            if (noteId.equals("idis0")) {
                Button(onClick = { addState.value = true }, modifier = Modifier.wrapContentSize()) {
                    Icon(
                        painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_save_24),
                        contentDescription = "save"
                    )

                    // Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                    Text("Add Note")
                }

            } else if (!noteId.equals("idis0")) {
                Button(
                    onClick = { updateState.value = true },
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_save_24),
                        contentDescription = "update"
                    )
                    // Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                    Text("Update Note")
                }
            }
        }
    }


    val noteInfo = NoteInfo(title.value, body.value, status = "active")
    if (addState.value == true) {
        val context = LocalContext.current
        val response = produceState<DataOrException<Note, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            )
        ) {
            value = authViewModel.addNote("Bearer" + " " + token, noteInfo)
        }.value

        if (response.loading == true) {
            CircularProgressIndicator()
        } else if (response.data != null) {

            navController.navigate("listofNotes/$token")
            addState.value = false
        } else {
            Toast.makeText(context, response.e?.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    //update


    if (updateState.value == true) {
        val context = LocalContext.current
        val note_id = noteId.toInt()
        val user_id = userId.toInt()
        val updateNote =
            updateNoteRequest(title.value, body.value, status, note_id, user_id, created, updated)

        val response = produceState<DataOrException<Note, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            )
        ) {
            value = authViewModel.updateNote("Bearer" + " " + token, note_id, updateNote)
        }.value

        if (response.loading == true) {
            CircularProgressIndicator()

        } else if (response.data != null) {
            navController.navigate("listofNotes/$token")

            updateState.value = false
        } else {
            Toast.makeText(context, "Something went wrong, Try again", Toast.LENGTH_LONG).show()
        }
    }

}
