package com.koflox.bot

import com.koflox.bot.data.SubredditPhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ImgurApiService {

    @Headers("Authorization: Bearer a068ce3d570f4ddf0d6b020376f43e445a5d0f82")
//    @Headers("Authorization: Clien-ID 2fc6285caa0ee75")
    @GET("gallery/r/{subredditName}")
    fun getSubredditPhotos(@Path("subredditName") subredditName: String): Call<SubredditPhotosResponse>

}