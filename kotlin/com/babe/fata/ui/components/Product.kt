package com.babe.fata.ui.components

data class Product(
    val id: Int,
    val name: String,
    val reference: String,
    val currentStock: Int,
    val minStock: Int,
    val price: String
)