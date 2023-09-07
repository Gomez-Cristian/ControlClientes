package com.wave_power_systems.controldeclientes.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.wave_power_systems.controldeclientes.R
import com.wave_power_systems.controldeclientes.model.Cliente
import com.wave_power_systems.controldeclientes.model.ClienteBrain
import com.wave_power_systems.controldeclientes.model.ClienteMostrarAdapter
import kotlinx.android.synthetic.main.activity_mostrar_clientes.*
import kotlinx.android.synthetic.main.search_header_layout.*
import java.util.*


class MostrarClientesActivity : AppCompatActivity() {

    private var clienteGlobal = ClienteBrain()
    private lateinit var clienteArrayList: ArrayList<Cliente>
    private lateinit var recyclerView: RecyclerView
    private var adapter: ClienteMostrarAdapter? = null

    enum class BusquedaScope{NOMBRE, DIRECCION }
    var busquedaTipo : BusquedaScope = BusquedaScope.NOMBRE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_clientes)

        try {

            backButtonMenu.setOnClickListener {
                backToRegister()
            }

            backTitleMenu.setOnClickListener {
                backToRegister()
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position){
                        0 -> {
                            busquedaTipo = BusquedaScope.NOMBRE
                            search_edit_text.hint = "Buscar cliente por nombre"}
                        1 -> {
                            busquedaTipo = BusquedaScope.DIRECCION
                            search_edit_text.hint = "Buscar cliente por cédula"}
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }catch ( e: Exception ) {}
    }

    override fun onResume() {
        super.onResume()
        try {
            progressBar.visibility = View.VISIBLE

            recyclerViewConstructor()
            searchConstructor()
        EvenChangeListener()
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
            progressBar.visibility = View.GONE
        }catch ( e: Exception ) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        try {

        }catch ( e: Exception ) {}
    }

    private fun EvenChangeListener() {
        try {
            clienteArrayList = ArrayList()

            clienteGlobal.db.collection("clientes").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MostrarClientesActivity, "${error.message}", Toast.LENGTH_SHORT).show()
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            clienteArrayList.add(dc.document.toObject(Cliente::class.java))
                        }
                    }

                    progressBar.visibility = View.GONE
                    attachAdapter(clienteArrayList)  // Enviar los datos al recycler
                }
            })
        }catch ( e: Exception ) {}
    }

    private fun recyclerViewConstructor() {
        try {
            clienteArrayList = ArrayList()
            adapter = ClienteMostrarAdapter(clienteArrayList, this )

            recyclerView = findViewById(R.id.clientesRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager?)!!.orientation
            )
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = adapter
        } catch (e : Exception){
            println(e)
        }
    }

    private fun searchConstructor(){
        try {
            search_edit_text.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    filterList(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            clear_search_query.setOnClickListener {
                clearFocus()
                search_edit_text.setText("")
            }
        } catch (e : Exception){
            println(e)
        }
    }

    //SEARCH
    private fun filterList(filterItem: String) {
        try {
            val filterdNames:MutableList<Cliente> = ArrayList()
            when (busquedaTipo){
                BusquedaScope.NOMBRE -> {
                    for (s in clienteArrayList) {
                        if (s.nombre!!.lowercase().contains(filterItem.lowercase())) {
                            filterdNames.add(s)
                        }
                    }
                    adapter!!.updateListCliente(filterdNames)
                }
                BusquedaScope.DIRECCION -> {
                    for (s in clienteArrayList) {
                        if (s.cedula!!.lowercase().contains(filterItem.lowercase())) {
                            filterdNames.add(s)
                        }
                    }
                    adapter!!.updateListCliente(filterdNames)
                }
            }
        } catch (e : Exception){
            println(e)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun attachAdapter(list: List<Cliente>) {
        try {
            if (clienteArrayList.isNotEmpty()){
                no_search_results_found_text.visibility = View.GONE
                adapter = ClienteMostrarAdapter(list, this)
                recyclerView.adapter = adapter
            }else{
                no_search_results_found_text.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        } catch (e : Exception){
            println(e)
        }
    }

    private fun backToRegister(){
        startActivity(Intent(this, RegistrarClienteActivity::class.java))
        finish()
    }

    private fun clearFocus() {
        val view = this.currentFocus
        if (view is EditText) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            view.clearFocus()
        }
    }

    override fun onBackPressed() {
        try {
            backToRegister()
        }catch ( e: Exception ) {}
    }


}