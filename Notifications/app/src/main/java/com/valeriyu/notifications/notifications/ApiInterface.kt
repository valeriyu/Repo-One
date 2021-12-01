package com.valeriyu.notifications.notifications

import com.valeriyu.notifications.models.RootModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("fcm/send")
    fun sendNotification(
    @Header("Authorization") serverKey:String,
    @Body root: RootModel?): Call<ResponseBody?>?
}