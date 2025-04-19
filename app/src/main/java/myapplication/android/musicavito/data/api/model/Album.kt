package myapplication.android.musicavito.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Album(
    val title: String?,
    @SerialName("cover_big") val image: String?
)