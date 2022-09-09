package com.example.asd

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

data class Message(
    val msg: String,
)

interface startStudymode {
    @FormUrlEncoded
    @POST("/studymode/start/")
    @Headers("SN: stacasd")
    fun startStudymode(
    ): Call<Message>
}
interface stopStudymode {
    @FormUrlEncoded
    @POST("/studymode/stop/")
    @Headers("SN: stacasd")
    fun stopStudymode(
    ): Call<Message>
}
