package com.tuapp.almacenvirtual.network

import com.tuapp.almacenvirtual.model.Producto
import com.tuapp.almacenvirtual.model.Usuario
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Login
    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<Usuario>

    // Registro
    @POST("auth/register")
    suspend fun register(@Body usuario: Usuario): Response<Unit>

    // Obtener productos por usuario
    @GET("producto/{usuarioId}")
    suspend fun getProductos(@Path("usuarioId") usuarioId: Int): Response<List<Producto>>

    // Crear producto (coincide con [HttpPost("crear")])
    @Multipart
    @POST("producto/crear")
    suspend fun createProducto(
        @Part("nombre") nombre: RequestBody,
        @Part("cantidad") cantidad: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("usuarioId") usuarioId: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<Producto> // devuelve el producto creado

    // Actualizar producto (coincide con [HttpPut("editar/{id}")])
    @Multipart
    @PUT("producto/editar/{id}")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Part("nombre") nombre: RequestBody,
        @Part("cantidad") cantidad: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<Producto> // devuelve el producto actualizado

    // Eliminar producto (coincide con [HttpDelete("{id}")])
    @DELETE("producto/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Unit>
}
