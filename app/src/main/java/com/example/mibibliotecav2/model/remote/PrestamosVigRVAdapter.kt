package com.example.mibibliotecav2.model.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.R
import kotlinx.android.synthetic.main.alertas_item.view.*

class PrestamosVigRVAdapter(
    val prestamosvigList: ArrayList<PrestamosRemote>,
    val onItenClickListener: OnItemClickListener
) : RecyclerView.Adapter<PrestamosVigRVAdapter.PrestamosVigViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrestamosVigViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.alertas_item, parent, false)
        return PrestamosVigViewHolder(itemView, onItenClickListener)
    }

    override fun getItemCount(): Int = prestamosvigList.size

    override fun onBindViewHolder(
        holder: PrestamosVigViewHolder,
        position: Int
    ) {
        val prestamo = prestamosvigList[position]
        holder.bindprestamosvig(prestamo)
    }

    class PrestamosVigViewHolder(
        itemView: View,
        val onItenClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun bindprestamosvig(prestamo: PrestamosRemote) {
            itemView.TV_nombre_individuo.text = prestamo.deudor
            itemView.TV_fecha_prestamo_individuo.text = prestamo.fprestamo
            itemView.TV_fecha_devolucion_individuo.text = prestamo.fdevolucion

            itemView.IB_borrar_prestamo_individuo.setOnClickListener {
                onItenClickListener.onItemClickBorrar(prestamo)
            }


        }

    }

    interface OnItemClickListener {
        fun onItemClickBorrar(prestamo: PrestamosRemote)

    }


}
