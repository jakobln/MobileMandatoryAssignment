package com.example.mandatoryassignment.models

data class ResaleItem(val id: Int, val title: String, val description: String, val price: Int, val seller: String, val date: Int, val pictureUrl: String) {
    constructor(title: String, description: String, price: Int, seller: String, date: Int, pictureUrl: String) : this(-1, title, description, price, seller, date, pictureUrl)

    override fun toString(): String {
        return "$id $title $description $price $seller $date $pictureUrl"
    }
}