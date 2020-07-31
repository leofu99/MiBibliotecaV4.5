package com.example.mibibliotecav2.model.remote

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mibibliotecav2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_foto.*

class FotoFragment : Fragment() {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid
    var idlibro = ""

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
            idlibro = safeArgs.id

            if (!libro.isNullOrEmpty())
                Picasso.get().load(libro).into(iv_fotosola)

            bt_eliminarfoto.setOnClickListener {
                eliminarFirebase(libro, idlibro)
            }
        }


    }

    private fun eliminarFirebase(foto: String, libro: String) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("libros")
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val imagen = datasnapshot.value
                    if (imagen == foto) {
                        val alertDialog: AlertDialog.Builder? = activity?.let {
                            val builder = AlertDialog.Builder(it)
                            builder.apply {
                                setMessage("Desea eliminar la foto?")
                                setPositiveButton("aceptar") { dialog, id ->
                                    myRef.child(usrid!!).child(idlibro).child("galeria")
                                        .child(datasnapshot.key!!).removeValue()
                                    Toast.makeText(
                                        context,
                                        "La imagen fue eliminada",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    requireActivity().onBackPressed()
                                }
                                setNegativeButton("cancelar") { dialog, id ->
                                }
                            }
                        }
                        alertDialog?.show()
                    }
                }
            }
        }
        myRef.child(usrid!!).child(idlibro).child("galeria")
            .addListenerForSingleValueEvent(postListener)


    }
}