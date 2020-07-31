package com.example.mibibliotecav2.model.remote


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import kotlinx.android.synthetic.main.item_nota.view.*

class FrasesRVAdapter(
    private var librosList: ArrayList<String>,
    private val onItemClickListener: LibroFragment


) : RecyclerView.Adapter<FrasesRVAdapter.FrasesViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FrasesViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return FrasesViewHolder(itemView, onItemClickListener)

    }

    override fun getItemCount(): Int = librosList.size

    override fun onBindViewHolder(
        holder: FrasesViewHolder,
        position: Int
    ) {
        val libro: String = librosList[position]
        holder.bindDeudor(libro)


    }

    inner class FrasesViewHolder(
        itemView: View,
        private var onItemClickListener: LibroFragment
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        private lateinit var libro: String

        fun bindDeudor(libro: String) {
            this.libro = libro
            itemView.setOnClickListener(this)
            itemView.tv_frase?.text = libro


        }

        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(libro)
        }
    }


    interface OnItemClickListener {


        fun onItemClick1(libro: String)

    }


}