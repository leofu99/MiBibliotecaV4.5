package com.example.mibibliotecav2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mibibliotecav2.model.remote.PrestamosRVAdapter
import com.example.mibibliotecav2.model.remote.PrestamosRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_prestamos.*

class PrestamosFragment : Fragment() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid

    private val prestamosList: MutableList<PrestamosRemote> = mutableListOf()
    lateinit var prestamosAdapter: PrestamosRVAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prestamos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDeudores()
        rv_prestamos.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_prestamos.setHasFixedSize(true)
        prestamosList.clear()
        prestamosAdapter = PrestamosRVAdapter(prestamosList as ArrayList<PrestamosRemote>)
        rv_prestamos.adapter = prestamosAdapter


        BF_agregardeudor.setOnClickListener {
            findNavController().navigate(R.id.action_prestamosFragment_to_agregarprestamoFragment)
        }
    }


    private fun cargarDeudores() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Prestamos").child(usrid!!)

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val prestamo = datasnapshot.getValue(PrestamosRemote::class.java)
                    prestamosList.add(prestamo!!)

                }
                prestamosAdapter.notifyDataSetChanged()

            }


        }
        myRef.addValueEventListener(postListener)


    }
}