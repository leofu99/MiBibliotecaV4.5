package com.example.mibibliotecav2.model.remote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mibibliotecav2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_libro.*
import kotlinx.android.synthetic.main.list_item_grid_movie.view.*

class LibroFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_libro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            val safeArgs = LibroFragmentArgs.fromBundle(it)
            val libro= safeArgs.libroRecibido
            tv_titulolibro.text = libro.titulo
            if(!libro.portada.isNullOrEmpty())
                Picasso.get().load(libro.portada).into(iv_portadalibro)


        }
    }


}