package com.example.mibibliotecav2

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mibibliotecav2.model.remote.PrestamosRemote
import com.example.mibibliotecav2.model.remote.PrestamosVigRVAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_notificaciones_bibliograficas.*

class NotificacionesBibliograficasFragment : Fragment(), PrestamosVigRVAdapter.OnItemClickListener {


    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Prestamos").child(usrid!!)

    private val prestamosList: MutableList<PrestamosRemote> = mutableListOf()
    lateinit var prestamosAdapter: PrestamosVigRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notificaciones_bibliograficas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarPrestamosVigentes()

        RV_alertas.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        RV_alertas.setHasFixedSize(true)
        prestamosList.clear()

        prestamosAdapter = PrestamosVigRVAdapter(prestamosList as ArrayList<PrestamosRemote>, this)
        RV_alertas.adapter = prestamosAdapter
    }

    private fun cargarPrestamosVigentes() {


        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val prestamo = datasnapshot.getValue(PrestamosRemote::class.java)
                    prestamosList.add(prestamo!!)

                }
                prestamosAdapter.notifyDataSetChanged()

            }
        }
        myRef.addValueEventListener(postListener)
    }

    override fun onItemClickBorrar(prestamo: PrestamosRemote) {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val alertDialog: AlertDialog.Builder? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setMessage("Desea eliminar el libro ${prestamo.titulo}?")
                        setPositiveButton("aceptar") { dialog, id ->
                            myRef.child(prestamo.id!!).removeValue()
                            Toast.makeText(
                                context,
                                "${prestamo.titulo} fue eliminado",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(R.id.notificacionesBibliograficasFragment)
                        }
                        setNegativeButton("cancelar") { dialog, id ->
                        }
                    }
                }
                alertDialog?.show()
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)


    }
}
