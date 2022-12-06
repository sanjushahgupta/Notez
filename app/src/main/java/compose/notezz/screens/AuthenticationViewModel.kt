package compose.notezz.screens

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.*
import compose.notezz.repository.NotezzRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(val notezzRepo: NotezzRepo) : ViewModel() {


    /* var userToken = MutableLiveData<String>()
     fun UserTokenFromLogin(usernameandPasswordL: UsernameandPassword){
         viewModelScope.launch {
             val token = notezzRepo.logIn(usernameandPasswordL).data?.token.toString()
             if(token != null){
                 userToken.postValue(token)
             }
         }
     }*/

    suspend fun logIn(usernameandPasswordL: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception> {
        return notezzRepo.logIn(usernameandPasswordL)
    }

    suspend fun getNotes(token: String): DataOrException<ArrayList<Note>, Boolean, Exception> {
        return notezzRepo.getNotes(token)
    }

    suspend fun addNote(
        token: String,
        noteInfo: NoteInfo
    ): DataOrException<Note, Boolean, Exception> {
        return notezzRepo.addNote(token, noteInfo)
    }

    fun deleteNote(token: String, id: Int) {
        viewModelScope.launch {
            notezzRepo.deleteNote("Bearer" + " " + token, id)
        }
    }

   suspend fun updateNote(token: String, id: Int, updateNoteRequest: updateNoteRequest): DataOrException<Note, Boolean, Exception>{
       return notezzRepo.updateNote(token,id,updateNoteRequest)
   }
    suspend fun signUp(usernameandPassword: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception>{
        return notezzRepo.signUp(usernameandPassword)
    }

    suspend fun forgotPassword(userEmail:UserEmail):DataOrException<Any, Boolean, Exception>{


            return notezzRepo.forgotPassword(userEmail)


    }

    fun updateAccount(token: String, accountDetails: AccountDetails){
        viewModelScope.launch {
            notezzRepo.updateAccount(token, accountDetails)
        }

    }


}
