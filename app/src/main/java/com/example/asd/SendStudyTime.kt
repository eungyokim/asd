package com.example.asd

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class Message(
    val msg: String,
)

interface SendStudyTime {
    @FormUrlEncoded
    @POST("/sendStudyTime")
    fun SendStudyTime(
        @Field("hour") hour: Int?,
        @Field("min") min: Int?,
    ): Call<Message>
}
