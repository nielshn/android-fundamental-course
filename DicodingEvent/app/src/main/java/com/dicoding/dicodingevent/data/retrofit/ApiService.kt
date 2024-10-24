package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvents(@Path("id") id: String): Call<DetailEventResponse>

//    @GET("events")
//    fun searchEvents(@Query("active") active: Int, @Query("q") keyword: String): Call<EventResponse>
}