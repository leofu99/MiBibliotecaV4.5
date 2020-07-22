package com.example.mibibliotecav2.model.remote

class UsersRemote(
    val id: String? = "",
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val residencia: String = "",
    val notificacion: Boolean = false,
    val urlPhoto: String = ""
)

class LibrosRemote(
    val id: String? = "",
    val titulo: String = "",
    val autor: String = "",
    val nedicion: String = "",
    val lpublicacion: String = "",
    val aedicion: String = "",
    val npag: String = "",
    val genero: String = "",
    val notas: String = "",
    val isbn: String = "",
    val portada:String = "",
    val galeria: String = "")

class PrestamosRemote(
    val  id: String? = "",
    val titulo: String = "",
    val paginas: String = "",
    val genero: String = "",
    val fprestamo: String = "",
    val fdevolucion: String = "",
    val urlportada: String = "",
    val idprestamo: String = ""
)

