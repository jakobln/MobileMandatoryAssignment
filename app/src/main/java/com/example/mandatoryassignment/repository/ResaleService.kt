package com.example.mandatoryassignment.repository

import com.example.mandatoryassignment.models.ResaleItem
import retrofit2.Call
import retrofit2.http.*

interface ResaleService {
    @GET("resaleitems")
    fun getAllItems():Call<List<ResaleItem>>

    @GET("resaleitems/{itemId}")
    fun getItemById(@Path("itemId") itemId: Int): Call <ResaleItem>

    @POST("resaleitems")
    fun saveItem(@Body resaleItem: ResaleItem): Call<ResaleItem>

    @DELETE("resaleitems/{id}")
    fun deleteItem(@Path("id") id: Int): Call<ResaleItem>

    @PUT("resaleitems/{id}")
    fun updateItem(@Path("id") id: Int, @Body resaleItem: ResaleItem): Call<ResaleItem>
}