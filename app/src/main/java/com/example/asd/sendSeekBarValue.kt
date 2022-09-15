package com.example.asd

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

// LED control
interface sendLedSeekBarValue {
    @FormUrlEncoded
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    @POST("/sendledvalue")
    fun SendLedValue(
        @Field("value") value: Int?,
        @Field("Hex") Hex : String?,
    ):Call<getMsg>
}
// Sound control
interface sendSoundSeekBarValue{
    @FormUrlEncoded
    @Headers("SN: 64ba59ff-98dc-4f9d-a485-8e209b9957b6")
    @POST("/sendsoundvalue")
    fun SendSoundValue(
        @Field("value") value: Int?,
        @Field("index") index: Int?,
        ):Call<getMsg>
}


