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
import retrofit2.Response
import javax.inject.Inject

@SuppressWarnings("unchecked")
@HiltViewModel
class AuthenticationViewModel @Inject constructor(val notezzRepo: NotezzRepo) : ViewModel() {

    suspend fun signUp(usernameandPassword: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        return notezzRepo.signUp(usernameandPassword)
    }
    suspend fun logIn(usernameandPasswordL: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        return notezzRepo.logIn(usernameandPasswordL)
    }

    suspend fun getNotes(token: String): DataOrException<Response<ArrayList<Note>>, Boolean, Exception> {
        return notezzRepo.getNotes(token)
    }

    suspend fun addNote(
        token: String,
        noteInfo: NoteInfo
    ): Response<Note> {
        return notezzRepo.addNote(token, noteInfo)
    }

   suspend fun deleteNote(token: String, id: Int): DataOrException<Response<Unit>, Boolean, Exception> {

        return notezzRepo.deleteNote("Bearer" + " " + token, id)

    }

   suspend fun updateNote(token: String, id: Int, updateNoteRequest: updateNoteRequest): Response<Note>{
       return notezzRepo.updateNote(token,id,updateNoteRequest)
   }



    suspend fun forgotPassword(userEmail:UserEmail):Response<Unit>{
        return notezzRepo.forgotPassword(userEmail)
    }

    suspend fun updateAccount(token: String, accountDetails: AccountDetails):DataOrException<Response<Unit>, Boolean, Exception>{
        return notezzRepo.updateAccount(token, accountDetails)
    }

    suspend fun deleteAccount(token: String): DataOrException<Response<Unit>, Boolean, Exception>{
        return notezzRepo.deleteAccount(token)
    }

}
