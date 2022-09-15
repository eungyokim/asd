package com.example.asd

import retrofit2.Call
import retrofit2.http.*

data class Message(
    val message: String,
)

interface startStudymode {
    @GET("/studymode/start")
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    fun startStudymode(

    ): Call<Message>
}
interface stopStudymode {
    @GET("/studymode/stop")
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    fun stopStudymode(
    ): Call<Message>
}
