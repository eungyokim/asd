package com.example.asd

import retrofit2.Call
import retrofit2.http.*

data class Message(
    val message: String,
)

interface startStudymode {
    @GET("/studymode/start")
    fun startStudymode(
        @Header("SN") SN: String,
    ): Call<Message>
}
interface stopStudymode {
    @GET("/studymode/stop")
    fun stopStudymode(
        @Header("SN") SN: String,
    ): Call<Message>
}
