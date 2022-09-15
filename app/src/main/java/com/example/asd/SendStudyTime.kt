package com.example.asd

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

data class Message(
    val message: String,
)

interface startStudymode {
    @FormUrlEncoded
    @POST("/studymode/start/")
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    fun startStudymode(
    ): Call<Message>
}
interface stopStudymode {
    @FormUrlEncoded
    @POST("/studymode/stop/")
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    fun stopStudymode(
    ): Call<Message>
}
