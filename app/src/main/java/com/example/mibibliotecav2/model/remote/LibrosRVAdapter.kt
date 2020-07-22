package com.example.sesionroom.model.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import com.example.mibibliotecav2.model.remote.LibrosRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_grid_movie.view.*

class LibrosRVAdapter(
    var librosList: ArrayList<LibrosRemote>
): RecyclerView.Adapter<LibrosRVAdapter.LibrosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibrosViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_grid_movie, parent,false)
        return LibrosViewHolder(itemView)

    }

    override fun getItemCount(): Int = librosList.size

    override fun onBindViewHolder(holder: LibrosViewHolder, position: Int)
    {
        val libro : LibrosRemote = librosList[position]
        holder.bindDeudor(libro)
    }
    class LibrosViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindDeudor(libro: LibrosRemote){
            itemView.tv_titulo.text = libro.titulo
            itemView.tv_autor.text = libro.autor
            if(!libro.portada.isNullOrEmpty())
                Picasso.get().load(libro.portada).into(itemView.iv_foto)

        }
    }


}