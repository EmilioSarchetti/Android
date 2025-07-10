package com.tuapp.almacenvirtual.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tuapp.almacenvirtual.R
import com.tuapp.almacenvirtual.utils.SessionManager
import com.tuapp.almacenvirtual.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var sessionManager: SessionManager
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        sessionManager = SessionManager(this)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.usuarioResponse.observe(this, Observer { usuario ->
            usuario?.let {
                sessionManager.saveUsuario(it)
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProductoActivity::class.java))
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
