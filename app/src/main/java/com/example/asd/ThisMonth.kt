package com.example.asd


import com.google.gson.annotations.SerializedName

data class ThisMonth(
    @SerializedName("end")
    var end: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("start")
    var start: String?,
    @SerializedName("time")
    var time: Int?,
    @SerializedName("userId")
    var userId: String?
)