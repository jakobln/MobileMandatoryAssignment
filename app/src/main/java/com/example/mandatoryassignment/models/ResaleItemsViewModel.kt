package com.example.mandatoryassignment.models

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandatoryassignment.repository.ResaleRepository

class ResaleItemsViewModel : ViewModel() {
    private val repository = ResaleRepository()
    val itemLiveData: MutableLiveData<List<ResaleItem>> = repository.itemLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getPosts()
    }

    fun sortByPrice() {
        repository.getPostsSortedPrice()
    }

    fun sortByDate() {
        repository.getPostsSortedDate()
    }

    fun filterByPrice(minPrice: Int, maxPrice: Int) {
        repository.getPostsFilteredByPrice(minPrice, maxPrice)
    }

    operator fun get(index: Int): ResaleItem? {
        return itemLiveData.value?.get(index)
    }

    fun add(resaleItem: ResaleItem) {
        repository.add(resaleItem)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(resaleItem: ResaleItem) {
        repository.update(resaleItem)
    }
}