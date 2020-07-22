package com.example.mibibliotecav2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_biblioteca_personal.*
import kotlinx.android.synthetic.main.fragment_prestamos.*

class PrestamosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prestamos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BF_agregardeudor.setOnClickListener {
            findNavController().navigate(R.id.action_prestamosFragment_to_agregarprestamoFragment)
        }
    }


}