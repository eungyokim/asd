package com.example.asd


import retrofit2.Call
import retrofit2.http.*

data class getMsg(
    val message: String? = null
)

interface SendUserInfo {
    @FormUrlEncoded
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    @POST("/send_user_info")
    fun SendUserInfo(
        @Field("name") name: String?,
        @Field("age") age: String?,
        @Field("gender") gender: String?,
        @Field("uuid") uuid: String?,
        @Field("school") school: String?,
    ): Call<getMsg>

}


