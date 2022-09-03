package com.example.asd.getCode


import com.google.gson.annotations.SerializedName

data class get_Code(
    @SerializedName("schoolInfo")
    var schoolInfo: List<SchoolInfo?>?
)