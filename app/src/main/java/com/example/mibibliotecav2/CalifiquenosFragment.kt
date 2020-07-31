package com.example.mibibliotecav2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_califiquenos.*

class CalifiquenosFragment : Fragment() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("calificaciones")
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_califiquenos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BT_calificacion.setOnClickListener {
            val statusBar = RTB_calificacion.rating.toString()
            myRef.child("calificaciones").child(mAuth.currentUser?.uid!!).setValue(statusBar)

            Toast.makeText(requireContext(), "Calificación: $statusBar", Toast.LENGTH_SHORT).show()
        }
    }
}