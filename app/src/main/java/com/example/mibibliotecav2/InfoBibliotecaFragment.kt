package com.example.mibibliotecav2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mibibliotecav2.model.remote.BibliotecasRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info_biblioteca.*


class InfoBibliotecaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_biblioteca, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val biblio = extraerArgumentos()
        TV_recibe_nombre.text = biblio.nombre
        TV_contenido_biblioteca.text = biblio.contenido
        TV_dias_biblioteca.text = biblio.horario
        TV_url_biblioteca.text = biblio.url
        Picasso.get().load(biblio.fachada).into(IV_fachada_biblioteca)


    }

    private fun extraerArgumentos(): BibliotecasRemote {
        arguments.let {
            val safeArgs = InfoBibliotecaFragmentArgs.fromBundle(it!!)
            val biblioteca = safeArgs.Bibliotecas
            return biblioteca
        }
    }


}