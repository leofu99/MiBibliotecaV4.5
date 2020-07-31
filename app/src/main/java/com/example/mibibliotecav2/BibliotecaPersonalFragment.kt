package com.example.mibibliotecav2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mibibliotecav2.model.remote.LibrosRemote
import com.example.sesionroom.model.local.LibrosRVAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_biblioteca_personal.*


class BibliotecaPersonalFragment : Fragment(),LibrosRVAdapter.OnItemClickListener {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid

    private var  librosList: MutableList<LibrosRemote> = mutableListOf()
    lateinit var librosAdapter: LibrosRVAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biblioteca_personal, container, false)
        return view



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarDeudores()

        rv_libros?.layoutManager = GridLayoutManager(requireContext(),  2)
        rv_libros?.setHasFixedSize(true)
        librosList.clear()
        librosAdapter = LibrosRVAdapter(librosList as ArrayList<LibrosRemote>,this)
        rv_libros?.adapter = librosAdapter
        BF_agregarlibro.setOnClickListener {

        findNavController().navigate(R.id.action_bibliotecaPersonalFragment_to_nuevolibroFragment)
        }

    }


    private fun cargarDeudores() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("libros").child(usrid!!)

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val libro = datasnapshot.getValue(LibrosRemote::class.java)
                    librosList.add(libro!!)

                }
                librosAdapter.notifyDataSetChanged()

            }


        }
        myRef.addValueEventListener(postListener)


    }

    override fun onItemClick(libro: LibrosRemote) {
        val action = BibliotecaPersonalFragmentDirections.actionBibliotecaPersonalFragmentToLibroFragment(libro)


        Toast.makeText(requireContext(), "selección: ${libro.titulo}", Toast.LENGTH_SHORT).show()

        findNavController().navigate(action)

    }
}