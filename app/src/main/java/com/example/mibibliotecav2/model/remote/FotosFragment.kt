package com.example.mibibliotecav2.model.remote

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mibibliotecav2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_fotos.*
import java.io.ByteArrayOutputStream

class FotosFragment : Fragment(), FotosRVAdapter.OnItemClickListener {
    private val REQUEST_IMAGE_GALERIA = 7
    private val REQUEST_IMAGE_GALERIA1 = 22
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef = database.getReference("libros")
    private var idlibro = ""
    private var fotosList: MutableList<String> = mutableListOf()
    lateinit var fotosAdapter: FotosRVAdapter
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fotos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = FotosFragmentArgs.fromBundle(it)

            idlibro = safeArgs.idlibro
            recargarfotos()

            bt_agregarfoto.setOnClickListener {
                escoger(REQUEST_IMAGE_GALERIA, REQUEST_IMAGE_GALERIA1)


            }
        }
    }

    private fun recargarfotos() {
        fotosList.clear()
        cargarFotos(idlibro)





        rv_fotos?.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_fotos?.setHasFixedSize(true)
        fotosList.clear()
        fotosAdapter =
            FotosRVAdapter(
                fotosList as ArrayList<String>,
                this
            )
        rv_fotos?.adapter = fotosAdapter
    }

    override fun onItemClick(libro: String) {
        val action =
            FotosFragmentDirections.actionFotosFragmentToFotoFragment(foto = libro, id = idlibro)
        findNavController().navigate(action)

    }

    private fun escoger(requestImage: Int, requestImage1: Int) {
        val builder = AlertDialog.Builder(activity)
        val items = arrayOfNulls<CharSequence>(2)
        items[0] = "Cámara"
        items[1] = "Galería"
        builder.setTitle("Seleccione una foto")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent(requestImage)
                    1 -> cargarImagen(requestImage1)
                }
            }
        builder.create()
        builder.show()
    }

    private fun cargarImagen(requestImagePortada: Int) {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, requestImagePortada)
            }

        }

    }


    private fun dispatchTakePictureIntent(requestImage: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, requestImage)
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            var uploadTask: UploadTask
            val mStorage = FirebaseStorage.getInstance()
            val id = myRef.push().key
            val photoRef = mStorage.reference.child(idlibro).child(id!!)
            if (requestCode > 10) {
                val uri: Uri? = data?.data
                uploadTask = photoRef.putFile(uri!!)

            } else {
                var bitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                uploadTask = photoRef.putBytes(data)
            }

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    fotosList.add(task.result.toString())

                    myRef.child(usrid!!).child(idlibro).child("galeria").setValue(fotosList)
                    Toast.makeText(requireContext(), "Foto Agregada", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()


                }
            }


        }

    }

    private fun cargarFotos(idlibro: String) {
        fotosList.clear()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("libros").child(usrid!!).child(idlibro).child("galeria")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val libro: String = datasnapshot.value.toString()

                    fotosList.add(libro)

                }
                fotosAdapter.notifyDataSetChanged()

            }


        }
        myRef.addValueEventListener(postListener)


    }


}