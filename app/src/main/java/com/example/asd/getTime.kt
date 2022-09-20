package com.example.asd


import com.google.gson.annotations.SerializedName

data class getTime(
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: List<Result?>?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("thisMonth")
    var thisMonth: List<ThisMonth?>?,
    @SerializedName("total")
    var total: Int?
)