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


class LibroFragment : Fragment(), FotosRVAdapter.OnItemClickListener {
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

            var fotosList: MutableList<String> = libro.galeria

            lateinit var fotosAdapter: FotosRVAdapter
            rv_fotos?.layoutManager = GridLayoutManager(requireContext(), 2)
            rv_fotos?.setHasFixedSize(true)
            fotosAdapter =
                FotosRVAdapter(
                    fotosList as ArrayList<String>,
                    this
                )
            rv_fotos?.adapter = fotosAdapter


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

    override fun onItemClick(libro: String) {
        val action = LibroFragmentDirections.actionLibroFragmentToFotoFragment(libro)
        findNavController().navigate(action)


    }


}