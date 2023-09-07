package com.wave_power_systems.controldeclientes.model

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.wave_power_systems.controldeclientes.R
import java.util.regex.Pattern

class ClienteBrain {

    // Conexion firebase
    val db = FirebaseFirestore.getInstance()
    val clientesRef = db.collection("clientes")

    var statusMessageBox = false // Para no activar el mensaje 2 veces a la vez
    var errorIndicacionTextEdit = false // Marcar los editText

    // Validar los email
    fun isValidEmail(email: String): Boolean {
        val emailRegEx = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
        )
        return emailRegEx.matcher(email).matches()
    }

    // Message Box
    fun alertMessage(
        context: Context,
        title: String, message: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        try {
            if (!statusMessageBox){
                statusMessageBox = true
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.view_dialog_box)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false)
                val text = dialog.findViewById<TextView>(R.id.txtDiaTitle)
                val descrip = dialog.findViewById<TextView>(R.id.txtDiaMsg)
                text.text = title
                descrip.text = message

                val dialogPositiveButton = dialog.findViewById(R.id.btnOk) as Button
                val dialogNegativeButton = dialog.findViewById(R.id.btnCancel) as Button

                titleOfPositiveButton?.let { dialogPositiveButton.text = it } ?: run {
                    dialogPositiveButton.visibility = View.GONE
                }

                titleOfNegativeButton?.let { dialogNegativeButton.text = it } ?: run {
                    dialogNegativeButton.visibility = View.GONE
                }

                dialogPositiveButton.setOnClickListener {
                    statusMessageBox = false
                    positiveButtonFunction?.invoke()
                    dialog.dismiss()

                }

                dialogNegativeButton.setOnClickListener {
                    statusMessageBox = false
                    negativeButtonFunction?.invoke()
                    dialog.dismiss()
                }
                dialog.show()
            }

        } catch (e: Exception) {
            println("alertMessage $e")
        }
    }
}