package com.example.mibibliotecav2.model.remote

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mibibliotecav2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_libro.*


class LibroFragment : Fragment() {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_libro, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = LibroFragmentArgs.fromBundle(it)
            val libro = safeArgs.libroRecibido
            tv_titulolibro.text = libro.titulo
            if (!libro.portada.isNullOrEmpty())
                Picasso.get().load(libro.portada).into(iv_portadalibro)
            tv_infolibro.text =
                "Autor del libro: ${libro.autor}\nGénero Literario: ${libro.genero}" +
                        "\nLugar de Publicación: ${libro.lpublicacion}\nEdición: ${libro.nedicion}" +
                        "\nAño: ${libro.aedicion}\nNúmero de Páginas: ${libro.npag}"

            bt_eliminar.setOnClickListener {
                eliminarFirebase(libro)
            }

            bt_galeria.setOnClickListener {
                val action = LibroFragmentDirections.actionLibroFragmentToFotosFragment(libro.id!!)
                findNavController().navigate(action)

            }
            bt_editar.setOnClickListener {
                val action = LibroFragmentDirections.actionLibroFragmentToNuevolibroFragment(
                    idedit = libro.id!!,
                    libro = libro
                )
                findNavController().navigate(action)

            }


            var frasesList: MutableList<String> = libro.notas

            lateinit var frasesAdapter: FrasesRVAdapter
            rv_frases?.layoutManager = GridLayoutManager(requireContext(), 1)
            rv_frases?.setHasFixedSize(true)
            frasesAdapter = FrasesRVAdapter(
                frasesList as ArrayList<String>,
                this
            )
            rv_frases?.adapter = frasesAdapter


        }

    }


    private fun eliminarFirebase(libro: LibrosRemote) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("libros")
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val alertDialog: AlertDialog.Builder? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setMessage("Desea eliminar el libro ${libro.titulo}?")
                        setPositiveButton("aceptar") { dialog, id ->
                            myRef.child(usrid!!).child(libro.id!!).removeValue()
                            Toast.makeText(
                                context,
                                "${libro.titulo} fue eliminado",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(R.id.action_libroFragment_to_bibliotecaPersonalFragment)
                        }
                        setNegativeButton("cancelar") { dialog, id ->
                        }
                    }
                }
                alertDialog?.show()
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)


    }

    fun onItemClick(libro: String) {

    }


}