package compose.notezz.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.R
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.UserEmail

@Composable
fun ForgotPassword(email: String, navController: NavController) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
   // val forgotButton = remember { mutableStateOf(false) }



  /*  Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        val email = remember { mutableStateOf("") }

        Text(
            stringResource(R.string.RequestLoginLink),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 15.dp)
        )
        Divider()
        Text(
            text = stringResource(R.string.ForgotPasswordDescription),
            //style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            "Email",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(value = email.value, onValueChange = { email.value = it }, placeholder = {
            Text(
                text = "Enter email"

            )
        })
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = { forgotButton.value = true }) {

                Icon(painter = painterResource(id = R.drawable.link), contentDescription = "link")
                Text("Send login link")
            }
            Text(
                "Back to login",
                fontWeight = FontWeight.Bold,
                color = Color.Blue,

                modifier = Modifier
                    .padding(10.dp)
                    .clickable { navController.navigate("logIn") })

        }
        Image(
            painter = painterResource(id = R.drawable.requestloginlink),
            contentDescription = "requestlinkimage",
            modifier = Modifier.padding(top = 20.dp, start = 15.dp)
        )
    }
*/

val email = "sanjushahgupta@gmail.com"

   // if(forgotButton.value == true){

        val response = produceState<DataOrException<Any, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            )
        ) {
            val email = "sanjushahgupta@gmail.com"
            value = authViewModel.forgotPassword(UserEmail(email))
        }.value

        if (response.loading == true) {
            CircularProgressIndicator()

        } else if (response.data != null) {
            Log.d("tag-data", response.data.toString())
            Toast.makeText(LocalContext.current, response.data.toString(), Toast.LENGTH_SHORT).show()
        } else {
           Toast.makeText(LocalContext.current, response.e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("tag", response.e.toString())
        }
  //  }

}
