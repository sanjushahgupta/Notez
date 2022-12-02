package compose.notezz.repository

import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.*
import compose.notezz.network.NotezzApi
import javax.inject.Inject

class NotezzRepo @Inject constructor(val api: NotezzApi) {

    suspend fun signUpp(usernameandPassword: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception> {
        val response = try {
            api.SignUp(usernameandPassword)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)

    }

    suspend fun logIn(usernameandPassword: UsernameandPassword): DataOrException<ResponseofSignUpAndLogIn, Boolean, Exception> {
        val response = try {
            api.LogIn(usernameandPassword)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }

    suspend fun getNotes(token: String): DataOrException<ArrayList<Note>, Boolean, Exception> {
        val response = try {
            api.getListOfNotes(token)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(response)
    }

    suspend fun addNote(token: String, noteInfo: NoteInfo):DataOrException<Note, Boolean, Exception>{
        val response = try{
            api.addNotes(token, noteInfo)

        }catch(e: Exception){
            return DataOrException(e= e)

        }
        return DataOrException(response)

    }

    //repo
    suspend fun deleteNote(token: String, id: Int) {
        api.deleteNote(token, id)
    }

    suspend fun updateNote(token: String, id:Int, updateNoteRequest: updateNoteRequest): DataOrException<Note, Boolean, Exception>{
        val response = try{
        api.updateNote(token, id,updateNoteRequest)

        }catch(e: Exception){
            return DataOrException(e= e)

        }
        return DataOrException(response)

    }

}
