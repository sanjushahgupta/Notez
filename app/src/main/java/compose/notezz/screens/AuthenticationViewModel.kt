package compose.notezz.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.Note
import compose.notezz.model.NoteInfo
import compose.notezz.model.ResponseofSignUpAndLogIn
import compose.notezz.model.UsernameandPassword
import compose.notezz.repository.NotezzRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(val notezzRepo: NotezzRepo) : ViewModel() {
    suspend fun signUp(usernameandPassword: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception> {
        return notezzRepo.signUpp(usernameandPassword)

    }

    /* var userToken = MutableLiveData<String>()
     fun UserTokenFromLogin(usernameandPasswordL: UsernameandPassword){
         viewModelScope.launch {
             val token = notezzRepo.logIn(usernameandPasswordL).data?.token.toString()
             if(token != null){
                 userToken.postValue(token)
             }
         }
     }*/

    suspend fun logIn(usernameandPasswordL: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception>{
        return notezzRepo.logIn(usernameandPasswordL)
    }

    suspend fun getNotes(token:String):DataOrException<ArrayList<Note>, Boolean, Exception>{
        return notezzRepo.getNotes(token)
    }

    suspend fun addNote(token:String, noteInfo: NoteInfo):DataOrException<ArrayList<Note>, Boolean, Exception>{
        return notezzRepo.addNote(token, noteInfo)
    }
    fun deleteNote(token:String, id:Int){
        viewModelScope.launch {
            notezzRepo.deleteNote("Bearer"+" "+ token, id)
        }


    }
}
