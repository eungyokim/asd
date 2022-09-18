package com.example.asd.getCode


import com.google.gson.annotations.SerializedName

data class RESULT(
    @SerializedName("CODE")
    var cODE: String?,
    @SerializedName("MESSAGE")
    var mESSAGE: String?
)