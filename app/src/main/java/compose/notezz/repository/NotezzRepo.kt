package compose.notezz.repository

import android.provider.ContactsContract
import android.provider.ContactsContract.Data
import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.*
import compose.notezz.network.NotezzApi
import retrofit2.Response
import javax.inject.Inject





class NotezzRepo @Inject constructor(val api: NotezzApi) {

    suspend fun signUp(usernameandPassword: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        val response = api.signUp(usernameandPassword)
        return response
    }

    suspend fun logIn(usernameandPassword: UsernameandPassword): Response<ResponseofSignUpAndLogIn>{
        val response = api.LogIn(usernameandPassword)
        return response
    }

    suspend fun getNotes(token: String): DataOrException<Response<ArrayList<Note>>, Boolean, Exception> {
        val response = try {
            api.getListOfNotes(token)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(response)
    }

    suspend fun addNote(token: String, noteInfo: NoteInfo):Response<Note>{
        val response = api.addNotes(token, noteInfo)
        return response

    }

    //repo
    suspend fun deleteNote(token: String, id: Int): DataOrException<Response<Unit>, Boolean, Exception> {

        val response = try{
            api.deleteNote(token, id)
        }catch(e: Exception){
            return DataOrException(e= e)
        }
        return DataOrException(response)

    }

    suspend fun updateNote(token: String, id:Int, updateNoteRequest: updateNoteRequest): Response<Note>{
        val response = api.updateNote(token, id,updateNoteRequest)
        return response

    }



    suspend fun forgotPassword(userEmail:  UserEmail):Response<Unit>{

        val response = api.forgotPassword(userEmail)

        return response

    }

suspend fun updateAccount(token: String, accountDetails: AccountDetails): DataOrException<Response<Unit>, Boolean, Exception>{

    val response = try{
        api.updateAccount(token, accountDetails)
    }catch (e: Exception){
        return DataOrException(e = e)
    }
    return DataOrException(response)

}

    suspend fun deleteAccount(token:String): DataOrException<Response<Unit>, Boolean, Exception>{

        val response = try{
            api.deleteAccount(token)
        }catch(e: Exception){
            return DataOrException(e =e)
        }
        return DataOrException(response)

    }

}
