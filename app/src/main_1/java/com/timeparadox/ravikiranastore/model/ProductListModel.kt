package com.timeparadox.ravikiranastore.model

data class ProductListModel(
    val productId:String,
    val productCode:String,// searching element
    val productName:String,// searching element
    val productPrice:String,  // searching element
    val productType:String,
    val whereIdPlace:String,
    val productQuantity: String,
    val lastUpdateDate:String,
    val newUpdateDate:String,
    val productImage:String
    ):java.io.Serializable
