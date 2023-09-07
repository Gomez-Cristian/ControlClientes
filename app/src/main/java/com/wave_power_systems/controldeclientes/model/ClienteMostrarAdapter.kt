package com.wave_power_systems.controldeclientes.model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wave_power_systems.controldeclientes.R
import com.wave_power_systems.controldeclientes.controller.MostrarClientesActivity

class ClienteMostrarAdapter(
    private var clienteLiest: List<Cliente>,
    private val listener: MostrarClientesActivity
) : RecyclerView.Adapter<ClienteMostrarAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre : TextView = itemView.findViewById(R.id.nombreClienteTextView)
        val celular : TextView = itemView.findViewById(R.id.celularClienteTextView)
        val cedula : TextView = itemView.findViewById(R.id.cedulaClienteTextView)
        val correo : TextView = itemView.findViewById(R.id.correoClienteTextView)
        val direccionPrincipal : TextView = itemView.findViewById(R.id.direccionPrincipalClienteTextView)
        val direccionSecundaria : TextView = itemView.findViewById(R.id.direccionSecundariaClienteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_clientes_guardados, parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val clienteActual: Cliente = clienteLiest[position]
        holder.nombre.text = clienteActual.nombre
        holder.celular.text = clienteActual.celular
        holder.cedula.text = clienteActual.cedula
        holder.correo.text = clienteActual.correo
        holder.direccionPrincipal.text = clienteActual.direccionPrincipal
        holder.direccionSecundaria.text = clienteActual.direccionesSecundarias

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount() = clienteLiest.size

    interface ClienteAdapterListener {
        fun ruletaSelected(idBanca: String, grupo: String, nombreBanca: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListCliente(list: MutableList<Cliente>){
        clienteLiest = list
        notifyDataSetChanged()
    }
}


