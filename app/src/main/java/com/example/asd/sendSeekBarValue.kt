package com.example.asd

import retrofit2.Call
import retrofit2.http.*

// LED control
interface sendLedHex {
    @FormUrlEncoded
    @POST("/sendledhex")
    fun SendLedHex(
        @Header("SN") SN: String,
        @Field("Hex") Hex : String?,
    ):Call<getMsg>
}
// Sound control
interface sendSoundIndex{
    @FormUrlEncoded
    @POST("/sendsoundindex")
    fun SendSoundIndex(
        @Header("SN") SN: String,
        @Field("index") index: Int?,
    ):Call<getMsg>
}

interface sendLedSeekBarValue {
    @FormUrlEncoded
    @POST("/sendledvalue")
    fun SendLedValue(
        @Header("SN") SN: String,
        @Field("value") value: Int?,
    ):Call<getMsg>
}
// Sound control
interface sendSoundSeekBarValue{
    @FormUrlEncoded
    @POST("/sendsoundvalue")
    fun SendSoundValue(
        @Header("SN") SN: String,
        @Field("value") value: Int?,
        ):Call<getMsg>
}
