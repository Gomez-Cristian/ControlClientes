package com.wave_power_systems.controldeclientes.controller

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wave_power_systems.controldeclientes.R
import com.wave_power_systems.controldeclientes.model.Cliente
import com.wave_power_systems.controldeclientes.model.ClienteBrain
import kotlinx.android.synthetic.main.activity_registrar_cliente.*
import kotlinx.coroutines.*

class RegistrarClienteActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var linearLayout: LinearLayout
    private var contadorEditTexts = 0
    private var botonActual: Button? = null
    private var contadorBotonAtras = 0
    private var clienteGlobal = ClienteBrain()
    enum class TextEdit{NOMBRE, CELULAR, CEDULA,CORREO, DIRECCION}
    var errorTextField = clienteGlobal.errorIndicacionTextEdit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            installSplashScreen()
            delay(800)
        }

        setContentView(R.layout.activity_registrar_cliente)

        try {
            scrollView = findViewById(R.id.scrollView)
            linearLayout = findViewById(R.id.container)

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.inicio_bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.item_menu_home
            bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { MenuItem ->
                when (MenuItem.itemId) {
                    R.id.item_menu_home -> {
                        try {

                        } catch (e: Exception) {
                        }
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.item_menu_vista_clientes -> {
                        try {
                            startActivity(Intent(this, MostrarClientesActivity::class.java))
                            finish()
                        } catch (e: Exception) {
                        }
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })

            // QUITAR SEÑAL DE ERRORES
            nombreClienteTextField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (errorTextField && nombreClienteTextField.text?.isNotEmpty() == true) {
                        errorTextField = false
                        nombreClienteTextInputLayout.isErrorEnabled = false
                    }
                }
            }

            celularClienteTextField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (errorTextField && celularClienteTextField.text?.isNotEmpty() == true) {
                        errorTextField = false
                        celularClienteTextInputLayout.isErrorEnabled = false
                    }
                }
            }

            cedulaClienteTextField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (errorTextField && cedulaClienteTextField.text?.isNotEmpty() == true) {
                        errorTextField = false
                        cedulaClienteTextInputLayout.isErrorEnabled = false
                    }
                }
            }

            correoTextInputLayout.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (errorTextField && correoTextField.text?.isNotEmpty() == true) {
                        errorTextField = false
                        nombreClienteTextInputLayout.isErrorEnabled = false
                    }
                }
            }

            direccionClienteTextField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (errorTextField && direccionClienteTextField.text?.isNotEmpty() == true) {
                        errorTextField = false
                        direccionClienteTextInputLayout.isErrorEnabled = false
                    }
                }
            }

            // BOTONES ACTION
            agregarDireccionButton.setOnClickListener {
                agregarDireccionButton.visibility = View.GONE
                agregarParEditTextBoton()
            }

            guardarButton.setOnClickListener {
                guardarDatos()
            }

            limpiarButton.setOnClickListener {
                limpiarEditTexts()
            }
        }catch ( e: Exception ) {}
    }

    override fun onResume() {
        super.onResume()
        try {

        }catch ( e: Exception ) {}
    }

    override fun onPause() {
        super.onPause()
        try {

        }catch ( e: Exception ) {}
    }

    override fun onStop() {
        super.onStop()
        try {
            limpiarEditTexts()
        }catch ( e: Exception ) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        try {

        }catch ( e: Exception ) {}
    }


    private fun agregarParEditTextBoton() {
        try {
            contadorEditTexts++

            // Nuevo LinearLayout para el boton y el edit
            val nuevoLayout = LinearLayout(this)
            nuevoLayout.orientation = LinearLayout.HORIZONTAL
            nuevoLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Nuevo edit
            val nuevoEditText = EditText(this)
            nuevoEditText.hint = "Ingrese texto $contadorEditTexts"
            nuevoEditText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Nuevo boton
            val nuevoBoton = Button(this)
            nuevoBoton.text = "(+)"
            nuevoBoton.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            nuevoBoton.setOnClickListener {
                agregarParEditTextBoton()
                nuevoEditText.requestFocus()
                nuevoBoton.visibility = Button.GONE
            }

            // Agregar nuevos objetos al Layout
            nuevoLayout.addView(nuevoEditText)
            nuevoLayout.addView(nuevoBoton)

            // Agregar Layout al principal
            linearLayout.addView(nuevoLayout)
            botonActual?.text = " "
            botonActual = nuevoBoton
            botonActual?.text = "(+)"

            GlobalScope.launch(context = Dispatchers.Main) {
                nuevoEditText.requestFocus()
            }
        }catch ( e: Exception ) {}
    }

    private fun guardarDatos() {
        try {
            clearFocus()
            val nombre = nombreClienteTextField.text?.toString()?.trim() ?: ""
            val celular = celularClienteTextField.text?.toString()?.trim() ?: ""
            val cedula = cedulaClienteTextField.text?.toString()?.trim() ?: ""
            val correo = correoTextField.text?.toString()?.trim() ?: ""
            val resultValidateEmail =  clienteGlobal.isValidEmail(correoTextField.text.toString())
            val direccionPrincipal = direccionClienteTextField.text?.toString()?.trim() ?: ""

            if (
                nombre != ""
                && celular.count() > 9
                && cedula.count() == 11
                && correo != ""
                && resultValidateEmail
                && direccionPrincipal != ""
            ) {
                progressBar.visibility = View.VISIBLE

                val datosGuardados = mutableListOf<String>()
                for (i in 0 until linearLayout.childCount) {
                    val child = linearLayout.getChildAt(i)
                    if (child is LinearLayout) {
                        val editText = child.getChildAt(0) as EditText
                        val texto = editText.text.toString()
                        datosGuardados.add(texto)
                    }
                }

                val nuevoCliente = Cliente(nombre, celular, cedula, correo, direccionPrincipal, datosGuardados.joinToString(","))

                clienteGlobal.clientesRef.document(correo).set(nuevoCliente)
                    .addOnSuccessListener {
                        // Guardado con exito
                        progressBar.visibility = View.GONE
                        clienteGlobal.alertMessage(this,"Cliente guardado",
                            "Cliente guardado correctamente!", "Aceptar",null,{
                                limpiarEditTexts()
                            },null)
                    }
                    .addOnFailureListener { error ->
                        // Error al guardar
                        progressBar.visibility = View.GONE
                        error.message?.let {
                            clienteGlobal.alertMessage(this,"Advertencia",
                                it, "Aceptar",null,null,null)
                        }
                    }
            }else{
                if (nombre.isEmpty()) {
                    errorRegister("Nombre en blanco.", TextEdit.NOMBRE)
                }else if (celular.count() < 9) {
                    errorRegister("Número de teléfono inválido!", TextEdit.CELULAR)
                }else if (cedula.count() != 11) {
                    errorRegister("Cédula inválida!", TextEdit.CEDULA)
                }else if (correo.isEmpty()) {
                    errorRegister("Correo en blanco.", TextEdit.CORREO)
                }else if (!resultValidateEmail) {
                    errorRegister("Correo inválido!", TextEdit.CORREO)
                }else if (direccionPrincipal.isEmpty()) {
                    errorRegister("Dirección en blanco.", TextEdit.DIRECCION)
                }else{
                    errorRegister("Error desconocido", TextEdit.NOMBRE)
                }
            }
        }catch ( e: Exception ) {}
    }

    private fun errorRegister(message: String, textField: TextEdit) {
        try {
            errorTextField = true
            when (textField) {
                TextEdit.NOMBRE -> {
                    nombreClienteTextInputLayout.error = message
                    nombreClienteTextInputLayout.isErrorEnabled = true
                    nombreClienteTextField?.requestFocus()
                    nombreClienteTextField?.setSelectAllOnFocus(true)
                }
                TextEdit.CELULAR -> {
                    celularClienteTextInputLayout.error = message
                    celularClienteTextInputLayout.isErrorEnabled = true
                    celularClienteTextField?.requestFocus()
                    celularClienteTextField?.setSelectAllOnFocus(true)
                }
                TextEdit.CEDULA -> {
                    cedulaClienteTextInputLayout.error = message
                    cedulaClienteTextInputLayout.isErrorEnabled = true
                    cedulaClienteTextField?.requestFocus()
                    cedulaClienteTextField?.setSelectAllOnFocus(true)
                }
                TextEdit.CORREO -> {
                    correoTextInputLayout.error = message
                    correoTextInputLayout.isErrorEnabled = true
                    correoTextField?.requestFocus()
                    correoTextField?.setSelectAllOnFocus(true)
                }
                TextEdit.DIRECCION -> {
                    direccionClienteTextInputLayout.error = message
                    direccionClienteTextInputLayout.isErrorEnabled = true
                    direccionClienteTextField?.requestFocus()
                    direccionClienteTextField?.setSelectAllOnFocus(true)
                }
            }
        } catch (e : Exception) {}
    }

    private fun limpiarEditTexts() {
        try {
            nombreClienteTextField.setText("")
            celularClienteTextField.setText("")
            cedulaClienteTextField.setText("")
            correoTextField.setText("")
            direccionClienteTextField.setText("")
            linearLayout.removeAllViews()
            contadorEditTexts = 0
            botonActual = null
            agregarDireccionButton.visibility = View.VISIBLE
            clearFocus()
        }catch ( e: Exception ) {}
    }

    private fun clearFocus() {
        try {
            val view = this.currentFocus
            if (view is EditText) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                view.clearFocus()
            }
        }catch ( e: Exception ) {}
    }

    override fun onBackPressed() {
        try {
            if (contadorBotonAtras == 0){
                Toast.makeText(this,"Presione de nuevo para salir", Toast.LENGTH_SHORT).show()
                contadorBotonAtras++
            }else{
                finishAffinity()
                super.onBackPressed()
            }
            object: CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished:Long) {}
                override fun onFinish() {contadorBotonAtras = 0}
            }.start() }catch ( e: Exception ) {}
    }
}