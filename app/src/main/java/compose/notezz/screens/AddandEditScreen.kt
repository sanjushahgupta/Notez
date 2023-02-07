package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.model.NoteInfo
import compose.notezz.model.updateNoteRequest
import compose.notezz.util.Dimension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "WrongConstant", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition"
)
@Composable
fun AddEditScreen(
    token: String,
    title: String,
    body: String,
    noteId: String,
    status: String,
    updated: String,
    created: String,
    userId: String,
    navController: NavController
) {

    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val noteTitle = remember { mutableStateOf(title) }
    val noteBody = remember { mutableStateOf(body) }
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
        }
    }) {}
    val focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .clickable(MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .padding(
                top = Dimension.height(value = 1f).dp,
                start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.Center
    ) {

        NoteTitleTextField(noteTitle)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        NoteDetailsOutlinedTextField(noteBody)
        Spacer(modifier = Modifier.padding(top = 8.dp))

         if (noteId == "idis0") {
            AddButton(addState, focus)

        } else {
            if (noteId != "idis0") {
                UpdateButton(updateState, focus)
            }
        }
    }


    val noteInfo = NoteInfo(noteTitle.value, noteBody.value, status = "active")
    if (addState.value) {
        if (noteInfo.title.isEmpty() && noteInfo.body.isEmpty() || noteInfo.title == " " && noteInfo.body == " "
        ) {
            ComposableToastMessage(
                msg = "Title or description must be provided.",
                btnState = addState
            )
        } else {
            val context = LocalContext.current
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = authViewModel.addNote("Bearer $token", noteInfo)

                    when (val responseCode = response.code().toString()) {
                        "401" -> {
                            toastMessage(context, "Invalid Credentials.", addState)
                        }
                        "201" -> {
                            navController.navigate("listOfNotes/$token")
                            addState.value = false

                        }
                        else -> {
                            toastMessage(context, responseCode, addState)
                        }
                    }
                } catch (e: java.net.UnknownHostException) {
                    toastMessage(context, "Please check your internet connection.", addState)
                } catch (e: Exception) {
                    toastMessage(context, e.message.toString(), addState)
                }

            }

        }
    }


    //update

    if (updateState.value) {
        val noteId1 = noteId.toInt()
        val userId1 = userId.toInt()
        val updateNote =
            updateNoteRequest(noteTitle.value, noteBody.value, status, noteId1, userId1, created, updated)

        if (updateNote.title == " " && updateNote.body == " " || updateNote.title.isEmpty() && updateNote.body.isEmpty()) {
            ComposableToastMessage(
                msg = "Title or description must be provided.",
                btnState = updateState
            )
        } else {
            val context = LocalContext.current
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = authViewModel.updateNote("Bearer $token", noteId1, updateNote)
                    when ( val responseCode = response.code().toString()) {
                        "401" -> {
                            toastMessage(context, "Invalid Credentials.", updateState)
                        }
                        "200" -> {
                            navController.navigate("listOfNotes/$token")
                            updateState.value = false

                        }
                        else -> {
                            toastMessage(context, responseCode, updateState)
                        }
                    }

                } catch (e: java.net.UnknownHostException) {
                    toastMessage(context, "Please check your internet connection.", updateState)
                } catch (e: Exception) {
                    toastMessage(context, e.message.toString(), updateState)
                }

            }
        }

    }
}

@Composable
private fun UpdateButton(
    updateState: MutableState<Boolean>,
    focus: FocusManager
) {
    Button(
        onClick = {
            updateState.value = true
            focus.clearFocus()
        }, modifier = Modifier
            .wrapContentSize(),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_save_24),
            contentDescription = "update",
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text("Update Note", color = Color.White)
    }
}

@Composable
private fun AddButton(
    addState: MutableState<Boolean>,
    focus: FocusManager
) {

        Button(
            onClick = {
                addState.value = true
                focus.clearFocus()
            }, modifier = Modifier
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gray))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_save_24),
                contentDescription = "save",
                tint = Color.White
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text("Add Note", color = Color.White)

        }
    }



@Composable
private fun NoteDetailsOutlinedTextField(noteBody: MutableState<String>) {
    Card(
        modifier = Modifier
            .verticalScroll(ScrollState(1), true)
            .fillMaxWidth()
            .height(Dimension.height(value = 35f).dp),
        elevation = 20.dp,
    ) {

        OutlinedTextField(
            value = noteBody.value,
            onValueChange = { noteBody.value = it },
            label = { Text("Description", color = Color.Black) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            )
        )
    }
}

@Composable
private fun NoteTitleTextField(noteTitle: MutableState<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 59.dp)
            .height(Dimension.height(value = 10f).dp), elevation = 20.dp
    ) {
        OutlinedTextField(
            value = noteTitle.value,
            onValueChange = { noteTitle.value = it },
            label = { Text("Title", color = Color.Black) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            )
        )
    }
}
