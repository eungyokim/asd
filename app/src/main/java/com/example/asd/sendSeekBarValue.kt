package com.example.asd

import com.example.discoding.get_message
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

data class get_message(
    val msg: String
)

// LED control
interface sendLedSeekBarValue {
    @FormUrlEncoded
    @POST("/sendLedValue")
    @Headers("SN: stacasd")
    fun SendLedValue(
        @Field("value") value: Int?,
    ):Call<get_message>
}
// Sound control
interface sendSoundSeekBarValue{
    @FormUrlEncoded
    @Headers("SN: stacasd")
        @POST("/sendSoundValue")
    fun SendSoundValue(
        @Field("value") value: Int?,
    ):Call<get_message>
}


