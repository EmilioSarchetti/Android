package com.tuapp.almacenvirtual.utils

import android.content.Context
import android.content.SharedPreferences
import com.tuapp.almacenvirtual.model.Usuario
import com.google.gson.Gson

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)

    fun saveUsuario(usuario: Usuario) {
        val json = Gson().toJson(usuario)
        prefs.edit().putString("usuario", json).apply()
    }

    fun getUsuario(): Usuario? {
        val json = prefs.getString("usuario", null)
        return if (json != null) Gson().fromJson(json, Usuario::class.java) else null
    }

    fun logout() {
        prefs.edit().remove("usuario").apply()
    }
}
