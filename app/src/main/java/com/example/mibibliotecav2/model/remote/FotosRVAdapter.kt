package com.example.mibibliotecav2.model.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_foto.view.*

class FotosRVAdapter(
    private var librosList: ArrayList<String>,
    private val onItemClickListener: LibroFragment


) : RecyclerView.Adapter<FotosRVAdapter.FotosViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FotosViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_foto, parent, false)
        return FotosViewHolder(itemView, onItemClickListener)

    }

    override fun getItemCount(): Int = librosList.size

    override fun onBindViewHolder(
        holder: FotosViewHolder,
        position: Int
    ) {
        val libro: String = librosList[position]
        holder.bindDeudor(libro)


    }

    inner class FotosViewHolder(
        itemView: View,
        private var onItemClickListener: LibroFragment
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        private lateinit var libro: String

        fun bindDeudor(libro: String) {
            this.libro = libro
            itemView.setOnClickListener(this)
            itemView.iv_itemfoto.setBackgroundColor(0)
            Picasso.get().load(libro).into(itemView.iv_itemfoto)

        }

        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(libro)
        }
    }


    interface OnItemClickListener {


        fun onItemClick(libro: String)

    }


}