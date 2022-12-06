package compose.notezz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.notezz.R

@Composable
fun ForgotPassword(email: String, navController: NavController) {
Column(modifier = Modifier
    .padding(8.dp)
    .fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {

    val email = remember { mutableStateOf("") }

    Text(
        stringResource(R.string.RequestLoginLink),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp,end = 8.dp, bottom = 8.dp, top = 15.dp)
    )
    Divider()
    Text(
        text = stringResource(R.string.ForgotPasswordDescription),
        //style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(8.dp)
    )
    Text("Email", color = Color.Black,fontWeight = FontWeight.Bold,  modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = email.value, onValueChange = { email.value = it }, placeholder = {
        Text(
            text = "Enter email"

        )
    })
    Row(  modifier = Modifier.padding(8.dp)) {
        Button(onClick = { }) {

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
    Image(painter = painterResource(id = R.drawable.requestloginlink), contentDescription = "requestlinkimage",
    modifier = Modifier.padding(top = 20.dp, start = 15.dp))
}
}
