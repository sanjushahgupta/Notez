package compose.notezz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.NoteInfo
import compose.notezz.model.updateNoteRequest
import compose.notezz.util.Dimension
import retrofit2.Response


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "WrongConstant", "SuspiciousIndentation")
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
    var title by remember { mutableStateOf(titlee) }
    var body by remember { mutableStateOf(bodyy) }
    val addState = remember { mutableStateOf(false) }
    val updateState = remember { mutableStateOf(false) }


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
    //   Box(modifier = Modifier.fillMaxWidth(), Alignment.Center) {
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
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                // .verticalScroll(ScrollState(1),true,)
                .fillMaxWidth()
                .padding(top = 59.dp)
                .height(Dimension.height(value = 10f).dp), elevation = 20.dp
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = Color.Black) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black,
                    backgroundColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
        Card(
            modifier = Modifier
                .verticalScroll(ScrollState(1), true)
                .fillMaxWidth()
                .height(Dimension.height(value = 35f).dp),
            elevation = 20.dp,
        ) {

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Description", color = Color.Black) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black,
                    backgroundColor = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))


        if (noteId.equals("idis0")) {

            Button(
                onClick = {
                    addState.value = true
                    focus.clearFocus()
                }, modifier = Modifier
                    .wrapContentSize()
                    .padding(start = Dimension.height(value = 15f).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
            ) {
                Icon(
                    painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_save_24),
                    contentDescription = "save",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Text("Add Note", color = Color.White)

            }

        } else if (!noteId.equals("idis0")) {
            Button(
                onClick = {
                    updateState.value = true
                    focus.clearFocus()
                }, modifier = Modifier
                    .wrapContentSize()
                    .padding(start = Dimension.height(value = 15f).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
            ) {
                Icon(
                    painter = painterResource(id = compose.notezz.R.drawable.ic_baseline_save_24),
                    contentDescription = "update",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Update Note", color = Color.White)
            }
        }

    }


    val noteInfo = NoteInfo(title, body, status = "active")

    if (addState.value == true) {
        if (noteInfo.title.isEmpty() && noteInfo.body.isEmpty() || noteInfo.title.equals(" ") && noteInfo.body.equals(
                " "
            )
        ) {
            val toast = Toast.makeText(
                LocalContext.current,
                "Title or description must be provided.",
                Toast.LENGTH_LONG
            )
            toast.duration = 100
            toast.show()
            addState.value = false
        } else {
            val context = LocalContext.current
            val response = produceState<DataOrException<Response<Note>, Boolean, Exception>>(
                initialValue = DataOrException(
                    loading = true
                )
            ) {
                value = authViewModel.addNote("Bearer" + " " + token, noteInfo)
            }.value
            if (response.loading == true) {
                CircularProgressIndicator()
            } else if (response.data?.code() == 201) {
                navController.navigate("listofNotes/$token")
                addState.value = false
            } else {
                val toast =
                    Toast.makeText(context, response.e?.message.toString(), Toast.LENGTH_LONG)
                toast.duration = 100
                toast.show()
                addState.value = false
            }
        }
    }


    //update

    if (updateState.value == true) {
        val context = LocalContext.current
        val note_id = noteId.toInt()
        val user_id = userId.toInt()
        val updateNote =
            updateNoteRequest(title, body, status, note_id, user_id, created, updated)

        if (updateNote.title.equals(" ") && updateNote.body.equals(" ") || updateNote.title.isEmpty() && updateNote.body.isEmpty()) {
            val toast = Toast.makeText(
                LocalContext.current,
                "Title or description must be provided.",
                Toast.LENGTH_LONG
            )
            toast.duration = 100
            toast.show()
            updateState.value = false
        }

        val response = produceState<DataOrException<Response<Note>, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            )
        ) {
            value = authViewModel.updateNote("Bearer" + " " + token, note_id, updateNote)
        }.value

        if (response.loading == true) {
            CircularProgressIndicator()

        } else if (response.data!!.code() == 200) {
            navController.navigate("listofNotes/$token")
            updateState.value = false

        } else {
            val toast =
                Toast.makeText(context, response.e!!.message.toString(), Toast.LENGTH_SHORT)
            toast.duration = 100
            toast.show()
            updateState.value = false
        }
    }
}
