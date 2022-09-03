package com.example.asd.getCode


import com.google.gson.annotations.SerializedName

data class SchoolInfo(
    @SerializedName("head")
    var head: List<Head?>?,
    @SerializedName("row")
    var row: List<Row?>?
)