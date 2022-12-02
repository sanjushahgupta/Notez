package compose.notezz.model

import com.google.gson.annotations.SerializedName

class updateNoteRequest(
    val title:String,
    val body: String,
    val status: String ="ACTIVE",
    val id : Int,
    val userid: Int,
    val created: String = "",
    val updated: String="",
    )
