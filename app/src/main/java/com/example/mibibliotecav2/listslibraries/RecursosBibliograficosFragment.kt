package com.example.mibibliotecav2.listslibraries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import com.example.mibibliotecav2.model.remote.BibliotecasRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_recursos_bibliograficos.*


class RecursosBibliograficosFragment : Fragment(), BibliotecasRVAdapter.OnItemClickListener {

    private val bibliotecasList: MutableList<BibliotecasRemote> = mutableListOf()
    private lateinit var bibliotecasAdapter: BibliotecasRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recursos_bibliograficos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarBibliotecas()

        RV_bibliotecas.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        RV_bibliotecas.setHasFixedSize(true)
        bibliotecasList.clear()

        bibliotecasAdapter =
            BibliotecasRVAdapter(bibliotecasList as ArrayList<BibliotecasRemote>, this)
        RV_bibliotecas.adapter = bibliotecasAdapter
    }

    private fun cargarBibliotecas() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("bibliotecas")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val biblioteca = datasnapshot.getValue(BibliotecasRemote::class.java)
                    bibliotecasList.add(biblioteca!!)
                }
                bibliotecasAdapter.notifyDataSetChanged()
            }

        }
        myRef.addValueEventListener(postListener)
    }

    override fun onItemClickInfo(biblioteca: BibliotecasRemote) {
        val action =
            RecursosBibliograficosFragmentDirections.actionRecursosBibliograficosFragmentToInfoBibliotecaFragment(
                biblioteca
            )
        findNavController().navigate(action)

    }

    override fun onItemClickMapa(biblioteca: BibliotecasRemote) {
        val action =
            RecursosBibliograficosFragmentDirections.actionRecursosBibliograficosFragmentToMapsFragment(
                biblioteca
            )
        findNavController().navigate(action)
    }


}