package com.example.mibibliotecav2.model.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mibibliotecav2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_foto.*

class FotoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs =
                FotoFragmentArgs.fromBundle(
                    it
                )
            val libro = safeArgs.foto
            if (!libro.isNullOrEmpty())
                Picasso.get().load(libro).into(iv_fotosola)
        }


    }
}