package com.example.mibibliotecav2.model.remote

import java.io.Serializable

class BibliotecasRemote(
    val contenido: String = "",
    val fachada: String = "",
    val horario: String = "",
    val id: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val municipio: String = "",
    val nombre: String = "",
    val url: String = ""
) : Serializable