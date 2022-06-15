package com.example.mandatoryassignment.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mandatoryassignment.models.ResaleItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResaleRepository {
    private val url = "https://anbo-restresale.azurewebsites.net/api/"

    private val resaleService: ResaleService
    val itemLiveData: MutableLiveData<List<ResaleItem>> = MutableLiveData<List<ResaleItem>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        resaleService = build.create(ResaleService::class.java)
        getPosts()
    }

    fun getPosts() {
        resaleService.getAllItems().enqueue(object : Callback<List<ResaleItem>> {
            override fun onResponse(
                call: Call<List<ResaleItem>>,
                response: Response<List<ResaleItem>>
            ) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    itemLiveData.postValue(response.body())
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<ResaleItem>>, t: Throwable) {
                //itemLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun getPostsSortedPrice() {
        resaleService.getAllItems().enqueue(object : Callback<List<ResaleItem>> {
            override fun onResponse(
                call: Call<List<ResaleItem>>,
                response: Response<List<ResaleItem>>
            ) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val sortedByPrice = response.body()?.sortedBy { it.price }
                    itemLiveData.postValue(sortedByPrice)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<ResaleItem>>, t: Throwable) {
                //itemLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun getPostsSortedDate() {
        resaleService.getAllItems().enqueue(object : Callback<List<ResaleItem>> {
            override fun onResponse(
                call: Call<List<ResaleItem>>,
                response: Response<List<ResaleItem>>
            ) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val sortedByDate = response.body()?.sortedBy { it.date }
                    itemLiveData.postValue(sortedByDate)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<ResaleItem>>, t: Throwable) {
                //itemLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun getPostsFilteredByPrice(minPrice: Int, maxPrice: Int) {
        resaleService.getAllItems().enqueue(object : Callback<List<ResaleItem>> {
            override fun onResponse(
                call: Call<List<ResaleItem>>,
                response: Response<List<ResaleItem>>
            ) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val filteredByPrice = response.body()?.filter { it.price > minPrice && it.price < maxPrice }
                    itemLiveData.postValue(filteredByPrice)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<ResaleItem>>, t: Throwable) {
                //itemLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(resaleItem: ResaleItem) {
        resaleService.saveItem(resaleItem).enqueue(object : Callback<ResaleItem> {
            override fun onResponse(call: Call<ResaleItem>, response: Response<ResaleItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<ResaleItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        resaleService.deleteItem(id).enqueue(object : Callback<ResaleItem> {
            override fun onResponse(call: Call<ResaleItem>, response: Response<ResaleItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<ResaleItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(resaleItem: ResaleItem) {
        resaleService.updateItem(resaleItem.id, resaleItem).enqueue(object : Callback<ResaleItem> {
            override fun onResponse(call: Call<ResaleItem>, response: Response<ResaleItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<ResaleItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
}