package compose.notezz.network

import compose.notezz.model.*
import retrofit2.http.*

interface NotezzApi {

    @POST("/auth/signup")
    suspend fun SignUp(@Body register: UsernameandPassword): ResponseofSignUpAndLogIn

    @POST("/auth/signin")
    suspend fun LogIn(@Body usernameandPassword: UsernameandPassword): ResponseofSignUpAndLogIn


    @GET("/notes")
    suspend fun getListOfNotes(@Header("Authorization") token: String): ArrayList<Note>


    // {title: "hello", body: "<p>mann</p>", status: "ACTIVE"}
    @POST("/notes")
    suspend fun addNotes(@Header("Authorization") token: String, @Body noteInfo: NoteInfo): Note


    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Header("Authorization") token: String, @Path("id") id: Int)

    //Request URL: https://api.notezz.com/notes/70342
    //Request Method: PATCH

    @PATCH("/notes/{id}")
    suspend fun updateNote(@Header("Authorization") token: String, @Path("id") id:Int, @Body updateNoteRequest: updateNoteRequest):Note
}
