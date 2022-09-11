package com.example.asd

import com.example.asd.getCode.get_Code
import retrofit2.Call
import retrofit2.http.*


interface SendSchoolName {
    // 학교 CODE 받아오는 외부 API 사용
    @GET("/hub/schoolInfo?Type=json&pIndex=1&pSize=100%2F/")
    fun SendUserInfo(
        @Query("LCTN_SC_NM") LCTN_SC_NM: String,
        @Query("SCHUL_NM") SCHUL_NM: String,
        @Query("SCHUL_KND_SC_NM") SCHUL_KND_SC_NM: String,
    ): Call<get_Code>
}