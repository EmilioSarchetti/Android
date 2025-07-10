package com.tuapp.almacenvirtual.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.almacenvirtual.model.Usuario
import com.tuapp.almacenvirtual.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    val usuarioResponse = MutableLiveData<Usuario?>()
    val errorMessage = MutableLiveData<String?>()

    fun login(nameUsuario: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val response: Response<Usuario> = repository.login(nameUsuario, contrasena)
                if (response.isSuccessful && response.body() != null) {
                    usuarioResponse.postValue(response.body())
                } else {
                    errorMessage.postValue("Credenciales incorrectas")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun limpiarEstado() {
        usuarioResponse.postValue(null)
        errorMessage.postValue(null)
    }
}
