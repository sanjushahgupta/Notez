package compose.notezz.model

import com.google.gson.annotations.SerializedName

class updateNoteRequest(
    @SerializedName("title")
    val title:String,
    val body: String,
    val status: String,
    val id : Int,
    val created: String = "",
    val updated: String="",
    val userid: Int = 0,
    )
