package com.tuapp.almacenvirtual.repository

import com.tuapp.almacenvirtual.model.Usuario
import com.tuapp.almacenvirtual.network.RetrofitInstance
import retrofit2.Response

class AuthRepository {
    suspend fun login(name_usuario: String, contrasena: String): Response<Usuario> {
        val credentials = mapOf(
            "name_usuario" to name_usuario,
            "contrasena" to contrasena
        )
        return RetrofitInstance.api.login(credentials)
    }

    suspend fun register(usuario: Usuario): Response<Unit> {
        return RetrofitInstance.api.register(usuario)
    }
}
