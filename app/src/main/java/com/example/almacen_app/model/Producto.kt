package com.tuapp.almacenvirtual.model
import com.google.gson.annotations.SerializedName


data class Producto(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val categoria: String,
    @SerializedName("ImagenUrl")
    val imagenUrl: String?,
    val usuarioId: Int
)
