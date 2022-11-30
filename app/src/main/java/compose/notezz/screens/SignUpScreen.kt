package compose.notezz.screens

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UsernameandPassword

@Composable
fun SignUpScreen(navController: NavController){
    var authViewModel: AuthenticationViewModel = hiltViewModel()
    val NotezzData = produceState<DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        val usernameandPassword = UsernameandPassword("shysdfjksd", "wioefhowi3ihoe4")
        value = authViewModel.signUp(usernameandPassword)
    }.value

    if (NotezzData.loading == true) {
        CircularProgressIndicator()

    } else if(NotezzData.data != null){
        Text(text = NotezzData.data?.token.toString())
    }



}
