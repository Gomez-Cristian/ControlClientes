package com.wave_power_systems.controldeclientes.model

data class Cliente (
    var nombre: String? = null,
    var celular: String? = null,
    var cedula: String? = null,
    var correo: String? = null,
    var direccionPrincipal: String? = null,
    var direccionesSecundarias: String? = null
)
