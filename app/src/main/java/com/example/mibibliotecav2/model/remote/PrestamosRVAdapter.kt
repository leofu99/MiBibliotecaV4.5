package com.example.mibibliotecav2.model.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_grid_movie.view.*

class PrestamosRVAdapter(
    var prestamosList: ArrayList<PrestamosRemote>
): RecyclerView.Adapter<PrestamosRVAdapter.PrestamosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestamosViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_grid_movie, parent,false)
        return PrestamosViewHolder(itemView)

    }

    override fun getItemCount(): Int = prestamosList.size

    override fun onBindViewHolder(holder: PrestamosViewHolder, position: Int)
    {
        val prestamo : PrestamosRemote = prestamosList[position]
        holder.bindDeudor(prestamo)
    }
    class PrestamosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindDeudor(prestamo: PrestamosRemote){
            itemView.tv_titulo.text = prestamo.titulo
            itemView.tv_autor.text = prestamo.deudor
            if(!prestamo.urlportada.isNullOrEmpty())
                Picasso.get().load(prestamo.urlportada).into(itemView.iv_foto)

        }
    }


}