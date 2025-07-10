package com.tuapp.almacenvirtual.view

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tuapp.almacenvirtual.databinding.ActivityProductoBinding
import com.tuapp.almacenvirtual.model.Producto
import com.tuapp.almacenvirtual.network.RetrofitInstance
import com.tuapp.almacenvirtual.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoBinding
    private lateinit var sessionManager: SessionManager
    private var imagenUri: Uri? = null
    private var productos: List<Producto> = listOf()

    private val launcherImagen = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imagenUri = uri
        uri?.let {
            Glide.with(this).load(it).into(binding.ivImagenPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val usuario = sessionManager.getUsuario()

        if (usuario == null) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnSeleccionarImagen.setOnClickListener {
            launcherImagen.launch("image/*")
        }

        binding.btnCargarProducto.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val cantidad = binding.etCantidad.text.toString().trim()
            val categoria = binding.spCategoria.selectedItem.toString()

            if (nombre.isEmpty() || cantidad.isEmpty() || categoria == "Seleccionar") {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val nombreRB = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
                    val cantidadRB = cantidad.toRequestBody("text/plain".toMediaTypeOrNull())
                    val categoriaRB = categoria.toRequestBody("text/plain".toMediaTypeOrNull())
                    val usuarioRB = usuario.usuario_id!!.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val imagenPart = imagenUri?.let { uri ->
                        val file = createTempFileFromUri(uri)
                        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("imagen", file.name, reqFile)
                    }

                    val response = RetrofitInstance.api.createProducto(
                        nombreRB, cantidadRB, categoriaRB, usuarioRB, imagenPart
                    )

                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@ProductoActivity, "Producto creado", Toast.LENGTH_SHORT).show()
                            limpiarCampos()
                            cargarProductos()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ProductoActivity, "Error al guardar producto", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@ProductoActivity, "Error de red", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        cargarProductos()
    }

    private fun cargarProductos() {
        val usuario = sessionManager.getUsuario() ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getProductos(usuario.usuario_id!!)
                if (response.isSuccessful) {
                    productos = response.body() ?: emptyList()
                    runOnUiThread {
                        mostrarProductos()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ProductoActivity, "No se pudieron obtener los productos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@ProductoActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarProductos() {
        val layout = binding.layoutProductos
        layout.removeAllViews()
        for (p in productos) {
            val tv = TextView(this)
            tv.text = "${p.nombre} (${p.cantidad}) - ${p.categoria}"
            layout.addView(tv)
        }
    }

    private fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etCantidad.setText("")
        binding.spCategoria.setSelection(0)
        binding.ivImagenPreview.setImageDrawable(null)
        imagenUri = null
    }

    private fun createTempFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, getFileName(uri))
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return result ?: uri.lastPathSegment ?: "imagen_temp.jpg"
    }
}
