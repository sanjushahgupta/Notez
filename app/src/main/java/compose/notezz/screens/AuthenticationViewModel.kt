package compose.notezz.screens

import androidx.lifecycle.ViewModel
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.*
import compose.notezz.repository.NotezzRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@SuppressWarnings("unchecked")
@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val noteRepo: NotezzRepo) : ViewModel() {

    suspend fun signUp(usernameandPassword: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        return noteRepo.signUp(usernameandPassword)
    }
    suspend fun logIn(usernameandPasswordL: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        return noteRepo.logIn(usernameandPasswordL)
    }

    suspend fun getNotes(token: String): DataOrException<Response<ArrayList<Note>>, Boolean, Exception> {
        return noteRepo.getNotes(token)
    }

    suspend fun addNote(
        token: String,
        noteInfo: NoteInfo
    ): Response<Note> {
        return noteRepo.addNote(token, noteInfo)
    }

   suspend fun deleteNote(token: String, id: Int): DataOrException<Response<Unit>, Boolean, Exception> {

        return noteRepo.deleteNote("Bearer $token", id)

    }

   suspend fun updateNote(token: String, id: Int, updateNoteRequest: updateNoteRequest): Response<Note>{
       return noteRepo.updateNote(token,id,updateNoteRequest)
   }



    suspend fun forgotPassword(userEmail:UserEmail):Response<Unit>{
        return noteRepo.forgotPassword(userEmail)
    }

    suspend fun updateAccount(token: String, accountDetails: AccountDetails):DataOrException<Response<Unit>, Boolean, Exception>{
        return noteRepo.updateAccount(token, accountDetails)
    }

    suspend fun deleteAccount(token: String): DataOrException<Response<Unit>, Boolean, Exception>{
        return noteRepo.deleteAccount(token)
    }

}
