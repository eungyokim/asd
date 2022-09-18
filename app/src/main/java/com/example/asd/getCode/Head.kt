package com.example.asd.getCode


import com.google.gson.annotations.SerializedName

data class Head(
    @SerializedName("list_total_count")
    var listTotalCount: Int?,
    @SerializedName("RESULT")
    var rESULT: RESULT?
)