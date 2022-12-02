package compose.notezz.model

import com.google.gson.annotations.SerializedName

class Note(
        @SerializedName("title")
        val title:String,
        val body: String,
        val status: String= "ACTIVE",
        val userId: Int,
        val id: Int,
        val created: String,
        val updated : String = ""
    )
