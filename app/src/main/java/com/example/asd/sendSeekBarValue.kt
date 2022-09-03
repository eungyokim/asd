package com.example.asd

import com.example.discoding.get_message
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class get_message(
    val msg: String
)


interface sendLedSeekBarValue {
    @FormUrlEncoded
    @POST("/sendLedValue")
    fun SendLedValue(
        @Field("value") value: Int?,
    ):Call<get_message>
}
interface sendSoundSeekBarValue{
    @FormUrlEncoded
    @POST("/sendSoundValue")
    fun SendSoundValue(
        @Field("value") value: Int?,
    ):Call<get_message>
}


