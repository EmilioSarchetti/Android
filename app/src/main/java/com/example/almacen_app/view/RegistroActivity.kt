package com.tuapp.almacenvirtual.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tuapp.almacenvirtual.databinding.ActivityRegistroBinding
import com.tuapp.almacenvirtual.model.Usuario
import com.tuapp.almacenvirtual.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val repository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarse.setOnClickListener {
            val usuario = binding.etUsername.text.toString()
            val contrasena = binding.etPassword.text.toString()

            if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val nuevoUsuario = Usuario(null, usuario, contrasena)
                        val response = repository.register(nuevoUsuario)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@RegistroActivity, "Registrado con éxito", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@RegistroActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistroActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
