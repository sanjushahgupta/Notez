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

    suspend fun logIn(usernameandPassword: UsernameandPassword): DataOrException<Response<ResponseofSignUpAndLogIn>, Boolean, Exception> {
        val response = try {
            api.LogIn(usernameandPassword)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }

    suspend fun getNotes(token: String): DataOrException<Response<ArrayList<Note>>, Boolean, Exception> {
        val response = try {
            api.getListOfNotes(token)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(response)
    }

    suspend fun addNote(token: String, noteInfo: NoteInfo):DataOrException<Response<Note>, Boolean, Exception>{
        val response = try{
            api.addNotes(token, noteInfo)

        }catch(e: Exception){
            return DataOrException(e= e)

        }
        return DataOrException(response)

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

    suspend fun updateNote(token: String, id:Int, updateNoteRequest: updateNoteRequest): DataOrException<Response<Note>, Boolean, Exception>{
        val response = try{
        api.updateNote(token, id,updateNoteRequest)

        }catch(e: Exception){
            return DataOrException(e= e)

        }
        return DataOrException(response)

    }

    suspend fun signUp(usernameandPassword: UsernameandPassword): DataOrException<Response<ResponseofSignUpAndLogIn>, Boolean, Exception>{

        val response = try{
            api.signUp(usernameandPassword)
        }catch (e: Exception){
            return DataOrException(e = e)
        }
        return DataOrException(response)
    }

    suspend fun forgotPassword(userEmail:  UserEmail):DataOrException<Response<Unit>, Boolean, Exception>{

        val response = try{
            api.forgotPassword(userEmail)
        }catch (e: Exception){
            return DataOrException(e = e)
        }
        return DataOrException(response)

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
