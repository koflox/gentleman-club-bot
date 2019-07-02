package data

data class SubredditPhotosResponse(
    val data: List<Data> = listOf(),
    val status: Int = 0,
    val success: Boolean = false
)