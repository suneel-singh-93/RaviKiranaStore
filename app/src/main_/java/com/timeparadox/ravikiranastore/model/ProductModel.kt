package com.timeparadox.ravikiranastore.model

data class ProductModel(
    val status:String,
    val message:String,
    val productList:List<ProductListModel>,
)