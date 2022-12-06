package compose.notezz.network

import android.provider.ContactsContract.CommonDataKinds.Email
import compose.notezz.dataorexception.DataOrException
import compose.notezz.model.*
import retrofit2.Response
import retrofit2.http.*

interface NotezzApi {
    @GET("/notes")
    suspend fun getListOfNotes(@Header("Authorization") token: String): ArrayList<Note>


    // {title: "hello", body: "<p>mann</p>", status: "ACTIVE"}
    @POST("/notes")
    suspend fun addNotes(@Header("Authorization") token: String, @Body noteInfo: NoteInfo): Note


    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Header("Authorization") token: String, @Path("id") id: Int):Response<Unit>

    @PATCH("/notes/{id}")
    suspend fun updateNote(@Header("Authorization") token: String, @Path("id") id:Int, @Body UpdateNoteRequest: updateNoteRequest):Note
//Authorization

    @POST("/auth/signup")
    suspend fun signUp(@Body register: UsernameandPassword): ResponseofSignUpAndLogIn

    @POST("/auth/signin")
    suspend fun LogIn(@Body usernameandPassword: UsernameandPassword): ResponseofSignUpAndLogIn

    @POST("/auth/send-login-link")
    suspend fun forgotPassword(@Body email: UserEmail):Response<Unit>

    @POST("/auth/update")
    suspend fun updateAccount(@Header("Authorization") token: String, @Body accountDetails: AccountDetails ): Response<Unit>

}
