package compose.notezz.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.NoteInfo

@Composable
fun AddandEditScreen(token: String, navController: NavController) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()

    val title = remember { mutableStateOf("") }
    val body = remember { mutableStateOf("") }
    val addState = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(), Alignment.Center){ Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
            , elevation = 20.dp
        ) {
            TextField(value = title.value,
                onValueChange = { title.value = it },
                modifier = Modifier.padding(5.dp),
                placeholder = { Text("title") })
        }
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp),
            elevation = 20.dp,
        ) {
            TextField(value = body.value,
                onValueChange = { body.value = it },
                modifier = Modifier.padding(5.dp),
                placeholder = { Text(text = "description") })
        }
        Button(onClick = { addState.value = true }, modifier = Modifier.wrapContentSize()) {
            Text("Add note")
        }
    }
    }
    var noteInfo = NoteInfo(title.value, body.value, status = "active")

    if (addState.value == true) {
        val context = LocalContext.current
        val response =
            produceState<DataOrException<ArrayList<Note>, Boolean, Exception>>(
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
            Toast.makeText(context, "Note Added", Toast.LENGTH_LONG).show()
            addState.value = false
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            addState.value = false
        }

    }
}
