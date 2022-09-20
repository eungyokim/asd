package com.example.asd

import retrofit2.Call
import retrofit2.http.*


interface getStudyTime {
    @GET("/@me/studyinfo")
    fun GetStudyTime(
        @Header("SN") SN: String,
    ): Call<getTime>
}